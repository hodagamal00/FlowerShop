package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simple FXML smoke test that loads core customer-facing screens
 * and reports any load errors. This does not launch the full app.
 */
public class FxmlSmokeTest {

    private static final AtomicBoolean TOOLKIT_STARTED = new AtomicBoolean(false);
    private static final List<String> CUSTOMER_FXML = List.of(
        "HomePage.fxml",
        "Catalog.fxml",
        "ProductDetails.fxml",
        "ProductDetailsModal.fxml",
        "cart.fxml",
        "checkout.fxml",
        "OrderConfirmation.fxml",
        "myorders.fxml",
        "complaint.fxml",
        "mycomplaints.fxml",
        "Login.fxml",
        "LogInSecond.fxml",
        "register.fxml",
        "Profile.fxml"
    );

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll() {
        startToolkit();

        List<String> failures = new ArrayList<>();
        for (String fxml : CUSTOMER_FXML) {
            try {
                loadOnFxThread(fxml);
                System.out.println("[FXML OK] " + fxml);
            } catch (Exception ex) {
                failures.add(fxml + ": " + ex.getMessage());
                System.err.println("[FXML FAIL] " + fxml);
                ex.printStackTrace(System.err);
            }
        }

        if (!failures.isEmpty()) {
            throw new IllegalStateException("FXML smoke test failures: " + failures);
        }
    }

    private static void startToolkit() {
        if (TOOLKIT_STARTED.compareAndSet(false, true)) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            awaitLatch(latch, "JavaFX platform startup timed out");
        }
    }

    private static void loadOnFxThread(String fxml) throws IOException {
        CountDownLatch latch = new CountDownLatch(1);
        final Exception[] exceptionHolder = new Exception[1];

        Platform.runLater(() -> {
            try {
                URL resource = FxmlSmokeTest.class.getResource(fxml);
                if (resource == null) {
                    throw new IllegalStateException("FXML resource not found: " + fxml);
                }
                FXMLLoader loader = new FXMLLoader(resource);
                loader.load();
            } catch (Exception ex) {
                exceptionHolder[0] = ex;
            } finally {
                latch.countDown();
            }
        });

        awaitLatch(latch, "FXML load timed out: " + fxml);

        if (exceptionHolder[0] != null) {
            if (exceptionHolder[0] instanceof IOException ioEx) {
                throw ioEx;
            }
            throw new IllegalStateException("Failed to load " + fxml, exceptionHolder[0]);
        }
    }

    private static void awaitLatch(CountDownLatch latch, String errorMessage) {
        try {
            if (!latch.await(15, TimeUnit.SECONDS)) {
                throw new IllegalStateException(errorMessage);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(errorMessage, ex);
        }
    }
}
