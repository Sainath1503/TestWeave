import com.sun.net.httpserver.HttpServer;
import model.ApiRequest;
import model.ApiRequestBodyPart;
import service.ApiService;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApiServiceTransportSmokeTest {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        AtomicBoolean cookieSeen = new AtomicBoolean(false);
        AtomicBoolean multipartSeen = new AtomicBoolean(false);
        AtomicBoolean binarySeen = new AtomicBoolean(false);

        server.createContext("/set-cookie", exchange -> {
            exchange.getResponseHeaders().add("Set-Cookie", "tw_session=abc123; Path=/");
            byte[] body = "cookie-set".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.createContext("/check-cookie", exchange -> {
            String cookie = exchange.getRequestHeaders().getFirst("Cookie");
            cookieSeen.set(cookie != null && cookie.contains("tw_session=abc123"));
            byte[] body = (cookieSeen.get() ? "ok" : "missing").getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(cookieSeen.get() ? 200 : 400, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.createContext("/multipart", exchange -> {
            String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.ISO_8859_1);
            multipartSeen.set(contentType != null
                    && contentType.toLowerCase().contains("multipart/form-data")
                    && body.contains("name=\"note\"")
                    && body.contains("hello multipart")
                    && body.contains("name=\"upload\"")
                    && body.contains("file-content-123"));
            byte[] out = (multipartSeen.get() ? "ok" : "bad").getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(multipartSeen.get() ? 200 : 400, out.length);
            exchange.getResponseBody().write(out);
            exchange.close();
        });
        server.createContext("/binary", exchange -> {
            byte[] bytes = exchange.getRequestBody().readAllBytes();
            binarySeen.set(Arrays.equals(bytes, new byte[]{1, 2, 3, 4, 5}));
            byte[] out = (binarySeen.get() ? "ok" : "bad").getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(binarySeen.get() ? 200 : 400, out.length);
            exchange.getResponseBody().write(out);
            exchange.close();
        });

        server.start();
        try {
            String baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
            ApiService service = new ApiService();

            ApiRequest setCookie = new ApiRequest();
            setCookie.method = "GET";
            setCookie.url = baseUrl + "/set-cookie";
            service.sendRequest(setCookie);

            ApiRequest checkCookie = new ApiRequest();
            checkCookie.method = "GET";
            checkCookie.url = baseUrl + "/check-cookie";
            service.sendRequest(checkCookie);

            var uploadFile = Files.createTempFile("tw-upload", ".txt");
            Files.writeString(uploadFile, "file-content-123", StandardCharsets.UTF_8);
            ApiRequest multipart = new ApiRequest();
            multipart.method = "POST";
            multipart.url = baseUrl + "/multipart";
            multipart.bodyMode = "formdata";
            multipart.multipartParts.add(ApiRequestBodyPart.text("note", "hello multipart"));
            multipart.multipartParts.add(ApiRequestBodyPart.file("upload", uploadFile.toString(), "text/plain"));
            service.sendRequest(multipart);

            var binaryFile = Files.createTempFile("tw-binary", ".bin");
            Files.write(binaryFile, new byte[]{1, 2, 3, 4, 5});
            ApiRequest binary = new ApiRequest();
            binary.method = "POST";
            binary.url = baseUrl + "/binary";
            binary.bodyMode = "binary";
            binary.binaryFilePath = binaryFile.toString();
            service.sendRequest(binary);

            if (!cookieSeen.get() || !multipartSeen.get() || !binarySeen.get()) {
                throw new IllegalStateException("Feature verification failed: cookie=" + cookieSeen.get()
                        + ", multipart=" + multipartSeen.get() + ", binary=" + binarySeen.get());
            }
            System.out.println("FEATURE_TEST_PASS cookie jar, multipart file upload, binary body");
        } finally {
            server.stop(0);
        }
    }
}
