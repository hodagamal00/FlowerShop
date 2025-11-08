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
        client.openConnection();
        // Load the AppShell as the main application scene
        FXMLLoader loader = new FXMLLoader(App.class.getResource("AppShell.fxml"));
        Parent root = loader.load();
        // The shell controller registers itself with the NavigationService in its initialize()
        scene = new Scene(root, 1520, 800);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        // Start at the catalog (home) screen inside the AppShell.  Guests can
        // browse the catalog without logging in.  The login page is still
        // accessible via the header's login button.
        NavigationService.getInstance().navigate("primary");
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

    public static void main(String[] args) {
        launch();
    }

}