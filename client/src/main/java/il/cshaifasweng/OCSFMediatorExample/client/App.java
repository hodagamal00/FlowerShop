package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import il.cshaifasweng.OCSFMediatorExample.client.NavigationService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private SimpleClient client;

    @Override
    public void start(Stage stage) throws IOException {
        // Register for warning events via EventBus
        EventBus.getDefault().register(this);
        // Open the client connection
        client = SimpleClient.getClient();
        try {
            client.openConnection();
        } catch (IOException ex) {
            showConnectionError(stage, ex);
            return;
        }        // Load the AppShell as the main application scene
        FXMLLoader loader = new FXMLLoader(App.class.getResource("AppShell.fxml"));
        Parent root = loader.load();
        // The shell controller registers itself with the NavigationService in its initialize()
        scene = new Scene(root, 1520, 800);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        // Start directly on the catalog so products are visible immediately.
        NavigationService.getInstance().navigate("Catalog");
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }



    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        SimpleClient.logoutCurrentUser();
        EventBus.getDefault().unregister(this);
        super.stop();
    }

    @Subscribe
    public void onWarningEvent(WarningEvent event) {
        System.out.println("happened in APP");
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING,
                    String.format("Message: %s\nTimestamp: %s\n",
                            event.getWarning().getMessage(),
                            event.getWarning().getTime().toString())

            );
            alert.show();
        });

    }
    private void showConnectionError(Stage stage, IOException ex) {
        ErrorController.setErrorInfo(
                "Cannot Connect to Server",
                "We couldn't reach the FlowerShop server. Please verify the server is running and try again.",
                "ERR_CONNECTION",
                ex != null ? ex.getMessage() : "The server connection failed."
        );
        ErrorController.setReturnPage("Catalog");

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("Error.fxml"));
            Parent errorRoot = loader.load();
            scene = new Scene(errorRoot, 1520, 800);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException loadException) {
            Alert fallback = new Alert(
                    AlertType.ERROR,
                    "Cannot connect to the server. Please ensure it is running and try again.\n\nDetails: " + loadException.getMessage()
            );
            fallback.showAndWait();
        }
    }
    public static void main(String[] args) {
        launch();
    }

}
