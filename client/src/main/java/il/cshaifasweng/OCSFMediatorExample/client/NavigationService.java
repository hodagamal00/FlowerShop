package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

/**
 * NavigationService is a singleton responsible for loading FXML views
 * and injecting them into the centre of the {@link AppShellController}'s
 * BorderPane.  Controllers should use this service instead of
 * opening new stages or calling {@link App#setRoot(String)}.  This
 * approach keeps a single application window (the AppShell) and
 * swaps out the centre content as the user navigates through the app.
 */
public class NavigationService {

    private static NavigationService instance;
    private AppShellController appShellController;

    private NavigationService() {
        // private to enforce singleton pattern
    }

    /**
     * Returns the singleton instance of the navigation service.
     */
    public static synchronized NavigationService getInstance() {
        if (instance == null) {
            instance = new NavigationService();
        }
        return instance;
    }

    /**
     * Registers the {@link AppShellController} that owns the central content
     * pane.  This method should be called once when the AppShell is
     * created in {@link App#start(javafx.stage.Stage)}.
     *
     * @param controller the shell controller
     */
    public void setAppShellController(AppShellController controller) {
        this.appShellController = controller;
    }

    /**
     * Loads an FXML view and sets it into the AppShell's centre region.
     * The FXML file must reside in the same package as the App class.
     *
     * @param fxml the simple name of the FXML file (without extension)
     */
    public void navigate(String fxml) {
        if (appShellController == null) {
            // Controller not yet registered; do nothing
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            Parent view = loader.load();
            // Replace the content in the shell's centre pane
            appShellController.setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}