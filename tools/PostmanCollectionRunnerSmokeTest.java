import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import model.ApiRequest;
import model.ApiResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import service.ApiService;
import ui.ApiValidatorFxApp;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class PostmanCollectionRunnerSmokeTest {

    public static void main(String[] args) throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        Platform.startup(started::countDown);
        started.await();

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/echo", PostmanCollectionRunnerSmokeTest::handleEcho);
        server.start();
        int port = server.getAddress().getPort();

        try {
            ApiValidatorFxApp app = new ApiValidatorFxApp();
            JSONObject collection = collection(port);

            Method buildTree = ApiValidatorFxApp.class.getDeclaredMethod("buildPostmanCollectionTree", JSONObject.class);
            buildTree.setAccessible(true);
            TreeItem<?> root = (TreeItem<?>) buildTree.invoke(app, collection);
            TreeItem<?> folder = root.getChildren().get(0);

            Method collect = ApiValidatorFxApp.class.getDeclaredMethod("collectPostmanRequestItems", TreeItem.class, List.class);
            collect.setAccessible(true);
            List<TreeItem<?>> requests = new ArrayList<>();
            collect.invoke(app, folder, requests);
            if (requests.size() != 2) {
                throw new AssertionError("Expected 2 requests in folder, got " + requests.size());
            }

            Method createApiPanel = ApiValidatorFxApp.class.getDeclaredMethod("createApiPanel");
            Method createApiValidationPanel = ApiValidatorFxApp.class.getDeclaredMethod("createApiValidationPanel");
            Method renderResponse = ApiValidatorFxApp.class.getDeclaredMethod("renderResponse", ApiResponse.class);
            Method loadPostmanRequest = ApiValidatorFxApp.class.getDeclaredMethod("loadPostmanRequest",
                    Class.forName("ui.ApiValidatorFxApp$PostmanCollectionNode"));
            createApiPanel.setAccessible(true);
            createApiValidationPanel.setAccessible(true);
            renderResponse.setAccessible(true);
            loadPostmanRequest.setAccessible(true);
            runOnFxAndWait(() -> {
                createApiPanel.invoke(app);
                loadPostmanRequest.invoke(app, requests.get(0).getValue());
            });
            String loadedPreRequestScript = textAreaValue(app, "preRequestScriptArea");
            String loadedTestScript = textAreaValue(app, "testScriptArea");
            if (!loadedPreRequestScript.contains("requestLoaded")) {
                throw new AssertionError("Expected loaded Pre-request Script tab to contain request-level script.");
            }
            if (!loadedTestScript.contains("pm.response.json()") || !loadedTestScript.contains("capturedEcho")) {
                throw new AssertionError("Expected loaded Tests tab to contain request test script.");
            }
            ApiResponse renderedResponse = new ApiResponse();
            renderedResponse.statusCode = 200;
            renderedResponse.statusLine = "200 OK";
            renderedResponse.timeMs = 10;
            renderedResponse.sizeBytes = 64;
            renderedResponse.rawBody = new JSONObject()
                    .put("id", "ORDER-123")
                    .put("status", "CREATED")
                    .put("purchase_units", new JSONArray().put(new JSONObject().put("reference_id", "default")))
                    .toString();
            renderedResponse.prettyBody = renderedResponse.rawBody;
            renderedResponse.headersText = "Content-Type: application/json";
            Field lastResponseField = ApiValidatorFxApp.class.getDeclaredField("lastResponse");
            lastResponseField.setAccessible(true);
            lastResponseField.set(app, renderedResponse);
            runOnFxAndWait(() -> renderResponse.invoke(app, renderedResponse));
            waitForRows(app, "responseFieldRows");
            if (listSize(app, "responseFieldRows") == 0) {
                throw new AssertionError("Expected Capture Variables rows after rendering a response.");
            }
            runOnFxAndWait(() -> createApiValidationPanel.invoke(app));
            waitForRows(app, "fieldValidationRows");
            if (listSize(app, "fieldValidationRows") == 0) {
                throw new AssertionError("Expected Field Validation rows after opening validation panel.");
            }

            Method preRequest = ApiValidatorFxApp.class.getDeclaredMethod("runPostmanPreRequestScripts", TreeItem.class);
            Method buildRequest = ApiValidatorFxApp.class.getDeclaredMethod("buildPostmanApiRequest",
                    Class.forName("ui.ApiValidatorFxApp$PostmanCollectionNode"));
            Method captureTests = ApiValidatorFxApp.class.getDeclaredMethod("capturePostmanTestVariables", TreeItem.class, ApiResponse.class);
            Method captureRequestTests = ApiValidatorFxApp.class.getDeclaredMethod("capturePostmanTestVariables",
                    Class.forName("ui.ApiValidatorFxApp$PostmanCollectionNode"), ApiResponse.class);
            preRequest.setAccessible(true);
            buildRequest.setAccessible(true);
            captureTests.setAccessible(true);
            captureRequestTests.setAccessible(true);

            ApiService apiService = new ApiService();
            for (TreeItem<?> item : requests) {
                preRequest.invoke(app, item);
                ApiRequest request = (ApiRequest) buildRequest.invoke(app, item.getValue());
                if (request.url.contains("${folderValue}")) {
                    throw new AssertionError("Pre-request variable was not resolved in " + request.url);
                }
                ApiResponse response = apiService.sendRequest(request);
                if (response.statusCode != 200) {
                    throw new AssertionError("Expected HTTP 200, got " + response.statusCode);
                }
                captureTests.invoke(app, item, response);
            }

            Field savedVariablesField = ApiValidatorFxApp.class.getDeclaredField("savedVariables");
            savedVariablesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, String> savedVariables = (Map<String, String>) savedVariablesField.get(app);
            if (!"folder-script-ok".equals(savedVariables.get("capturedEcho"))) {
                throw new AssertionError("Expected capturedEcho=folder-script-ok, got " + savedVariables.get("capturedEcho"));
            }
            if (args.length > 0 && Files.isRegularFile(Path.of(args[0]))) {
                JSONObject paypalCollection = new JSONObject(Files.readString(Path.of(args[0]), StandardCharsets.UTF_8));
                TreeItem<?> paypalRoot = (TreeItem<?>) buildTree.invoke(app, paypalCollection);
                TreeItem<?> createOrder = findByName(paypalRoot, "Create order");
                if (createOrder == null) {
                    throw new AssertionError("Could not find Create order in " + args[0]);
                }
                ApiResponse paypalResponse = new ApiResponse();
                paypalResponse.statusCode = 200;
                paypalResponse.statusLine = "200 OK";
                paypalResponse.rawBody = new JSONObject()
                        .put("id", "ORDER-123")
                        .put("purchase_units", new JSONArray())
                        .toString();
                paypalResponse.prettyBody = paypalResponse.rawBody;
                paypalResponse.headersText = "Content-Type: application/json";
                captureRequestTests.invoke(app, createOrder.getValue(), paypalResponse);
                if (!"ORDER-123".equals(savedVariables.get("order_id"))) {
                    throw new AssertionError("Expected PayPal Create order script to capture order_id, got "
                            + savedVariables.get("order_id"));
                }
            }

            System.out.println("PASS Postman folder runner smoke test: folder flattened, scripts applied, requests executed, variables captured.");
        } finally {
            server.stop(0);
            Platform.exit();
        }
    }

    private static void handleEcho(HttpExchange exchange) throws java.io.IOException {
        String query = exchange.getRequestURI().getRawQuery();
        String value = query == null ? "" : query.replace("folder=", "");
        byte[] body = new JSONObject().put("id", "ORDER-123").put("echo", value).toString().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(body);
        }
    }

    private static JSONObject collection(int port) {
        JSONArray folderEvents = new JSONArray()
                .put(event("prerequest", "const suffix = '-ok';\npm.collectionVariables.set('folderValue', 'folder-script' + suffix);"));
        JSONArray requestEvents = new JSONArray()
                .put(event("prerequest", "pm.variables.set('requestLoaded', 'yes');"))
                .put(event("test", "pm.test('response is ok', function () {\n"
                        + "  pm.expect(pm.response.code).to.eql(200);\n"
                        + "});\n"
                        + "const jsonData = pm.response.json();\n"
                        + "pm.expect(jsonData).to.have.property('id');\n"
                        + "pm.expect(jsonData.echo).to.include('script');\n"
                        + "pm.collectionVariables.set('capturedEcho', jsonData.echo);"));
        JSONObject first = request("First", port, requestEvents);
        JSONObject second = request("Second", port, new JSONArray());
        JSONObject folder = new JSONObject()
                .put("name", "Smoke Folder")
                .put("event", folderEvents)
                .put("item", new JSONArray().put(first).put(second));
        return new JSONObject()
                .put("info", new JSONObject().put("name", "Smoke Collection"))
                .put("item", new JSONArray().put(folder));
    }

    private static JSONObject request(String name, int port, JSONArray events) {
        JSONObject request = new JSONObject()
                .put("method", "GET")
                .put("url", "http://127.0.0.1:" + port + "/echo?folder={{folderValue}}")
                .put("header", new JSONArray());
        return new JSONObject()
                .put("name", name)
                .put("request", request)
                .put("event", events);
    }

    private static JSONObject event(String listen, String script) {
        return new JSONObject()
                .put("listen", listen)
                .put("script", new JSONObject().put("exec", new JSONArray(List.of(script.split("\\n")))));
    }

    private static TreeItem<?> findByName(TreeItem<?> item, String name) {
        if (item == null || item.getValue() == null) {
            return null;
        }
        if (String.valueOf(item.getValue()).endsWith("  " + name) || String.valueOf(item.getValue()).equals(name)) {
            return item;
        }
        for (TreeItem<?> child : item.getChildren()) {
            TreeItem<?> match = findByName(child, name);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    private static String textAreaValue(ApiValidatorFxApp app, String fieldName) throws Exception {
        Field field = ApiValidatorFxApp.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        TextArea textArea = (TextArea) field.get(app);
        return textArea == null ? "" : textArea.getText();
    }

    private static int listSize(ApiValidatorFxApp app, String fieldName) throws Exception {
        Field field = ApiValidatorFxApp.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = field.get(app);
        return value instanceof List<?> list ? list.size() : 0;
    }

    private static void waitForRows(ApiValidatorFxApp app, String fieldName) throws Exception {
        long deadline = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < deadline) {
            if (listSize(app, fieldName) > 0) {
                return;
            }
            Thread.sleep(50);
        }
    }

    private static void runOnFxAndWait(ThrowingRunnable action) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable throwable) {
                failure.set(throwable);
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        if (failure.get() instanceof Exception exception) {
            throw exception;
        }
        if (failure.get() != null) {
            throw new RuntimeException(failure.get());
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
