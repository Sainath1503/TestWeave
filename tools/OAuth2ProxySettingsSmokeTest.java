import com.sun.net.httpserver.HttpServer;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import ui.ApiValidatorFxApp;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class OAuth2ProxySettingsSmokeTest {

    public static void main(String[] args) throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        Platform.startup(started::countDown);
        started.await();

        AtomicBoolean proxySawTokenRequest = new AtomicBoolean(false);
        AtomicBoolean basicAuthSeen = new AtomicBoolean(false);
        AtomicBoolean formGrantSeen = new AtomicBoolean(false);
        HttpServer proxy = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        proxy.createContext("/", exchange -> {
            String authorization = exchange.getRequestHeaders().getFirst("Authorization");
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            proxySawTokenRequest.set(exchange.getRequestURI().toString().contains("/oauth/token"));
            basicAuthSeen.set(authorization != null && authorization.startsWith("Basic "));
            formGrantSeen.set(body.contains("grant_type=client_credentials") && body.contains("scope=orders"));
            byte[] out = new JSONObject()
                    .put("access_token", "proxy-oauth-token")
                    .put("refresh_token", "proxy-refresh-token")
                    .put("expires_in", 3600)
                    .toString()
                    .getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, out.length);
            try (OutputStream output = exchange.getResponseBody()) {
                output.write(out);
            }
        });
        proxy.start();

        try {
            ApiValidatorFxApp app = new ApiValidatorFxApp();
            Method createApiPanel = ApiValidatorFxApp.class.getDeclaredMethod("createApiPanel");
            createApiPanel.setAccessible(true);
            runOnFxAndWait(() -> createApiPanel.invoke(app));

            setCombo(app, "authTypeBox", "OAuth2");
            setCombo(app, "oauthGrantTypeBox", "client_credentials");
            setText(app, "oauthTokenUrlField", "http://token.example.test/oauth/token");
            setText(app, "oauthClientIdField", "client-id");
            setPasswordText(app, "oauthClientSecretField", "client-secret");
            setText(app, "oauthScopeField", "orders");
            setCheck(app, "oauthBasicAuthCheck", true);
            setCheck(app, "sslVerificationDisabledCheck", true);
            setCheck(app, "proxyEnabledCheck", true);
            setText(app, "proxyHostField", "127.0.0.1");
            setText(app, "proxyPortField", String.valueOf(proxy.getAddress().getPort()));

            Method requestOAuth2Token = ApiValidatorFxApp.class.getDeclaredMethod("requestOAuth2Token");
            requestOAuth2Token.setAccessible(true);
            JSONObject token = (JSONObject) requestOAuth2Token.invoke(app);

            Field savedVariablesField = ApiValidatorFxApp.class.getDeclaredField("savedVariables");
            savedVariablesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, String> savedVariables = (Map<String, String>) savedVariablesField.get(app);

            if (!"proxy-oauth-token".equals(token.optString("access_token"))
                    || !"proxy-oauth-token".equals(savedVariables.get("access_token"))
                    || !"proxy-refresh-token".equals(savedVariables.get("refresh_token"))
                    || !proxySawTokenRequest.get()
                    || !basicAuthSeen.get()
                    || !formGrantSeen.get()) {
                throw new AssertionError("OAuth2/proxy settings verification failed: token=" + token
                        + ", saved=" + savedVariables
                        + ", proxySaw=" + proxySawTokenRequest.get()
                        + ", basic=" + basicAuthSeen.get()
                        + ", form=" + formGrantSeen.get());
            }
            System.out.println("FEATURE_TEST_PASS OAuth2 token flow and proxy/SSL settings");
        } finally {
            proxy.stop(0);
            Platform.exit();
        }
    }

    private static void setText(ApiValidatorFxApp app, String fieldName, String value) throws Exception {
        ((TextField) field(app, fieldName)).setText(value);
    }

    private static void setPasswordText(ApiValidatorFxApp app, String fieldName, String value) throws Exception {
        setText(app, fieldName, value);
    }

    private static void setCheck(ApiValidatorFxApp app, String fieldName, boolean value) throws Exception {
        ((CheckBox) field(app, fieldName)).setSelected(value);
    }

    private static void setCombo(ApiValidatorFxApp app, String fieldName, String value) throws Exception {
        @SuppressWarnings("unchecked")
        ComboBox<String> box = (ComboBox<String>) field(app, fieldName);
        box.setValue(value);
    }

    private static Object field(ApiValidatorFxApp app, String fieldName) throws Exception {
        Field field = ApiValidatorFxApp.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(app);
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
