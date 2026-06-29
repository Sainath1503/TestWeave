import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import ui.ApiValidatorFxApp;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class EmbeddedHermesButtonSmokeTest {
    public static void main(String[] args) throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        Platform.startup(started::countDown);
        started.await();

        AtomicReference<Throwable> failure = new AtomicReference<>();
        AtomicBoolean found = new AtomicBoolean(false);
        CountDownLatch done = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                ApiValidatorFxApp app = new ApiValidatorFxApp();
                Method method = ApiValidatorFxApp.class.getDeclaredMethod("createHermesAgentPanel");
                method.setAccessible(true);
                Object node = method.invoke(app);
                found.set(containsButton((javafx.scene.Node) node, "Embedded Hermes CLI"));
            } catch (Throwable throwable) {
                failure.set(throwable);
            } finally {
                done.countDown();
            }
        });
        done.await();
        Platform.exit();

        if (failure.get() != null) {
            throw new RuntimeException(failure.get());
        }
        if (!found.get()) {
            throw new AssertionError("Embedded Hermes CLI button was not found.");
        }
        System.out.println("FEATURE_TEST_PASS Embedded Hermes CLI button present");
    }

    private static boolean containsButton(javafx.scene.Node node, String text) {
        if (node instanceof Button button && text.equals(button.getText())) {
            return true;
        }
        if (node instanceof ScrollPane scrollPane) {
            return containsButton(scrollPane.getContent(), text);
        }
        if (node instanceof Parent parent) {
            for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
                if (containsButton(child, text)) {
                    return true;
                }
            }
        }
        return false;
    }
}
