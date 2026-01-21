package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

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
    private String pendingView;

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

        // If a navigation attempt happened before the shell controller was
        // registered, replay it now so the initial view (e.g., the Home page)
        // is visible instead of leaving the content area empty.
        if (pendingView != null) {
            String viewToNavigate = pendingView;
            pendingView = null;
            navigate(viewToNavigate);
        }
    }

    /**
     * Loads an FXML view and sets it into the AppShell's centre region.
     * The FXML file must reside in the same package as the App class.
     *
     * @param fxml the simple name of the FXML file (without extension)
     */
    public void navigate(String fxml) {
        if (appShellController == null || !appShellController.isActive()) {
            // Controller not yet registered; fall back to the legacy scene
            // replacement to keep navigation working in standalone stages.
            try {
                App.setRoot(resolveViewName(fxml));
            } catch (IOException e) {
                // If we cannot swap roots yet, remember the request so it can be
                // executed once the shell is ready.
                pendingView = fxml;
            }
            return;
        }
        try {
            String resolvedView = resolveViewName(fxml);
            FXMLLoader loader = new FXMLLoader(App.class.getResource(resolvedView + ".fxml"));
            Parent view = loader.load();
            Node content = ensureScrollable(view);
            appShellController.setContent(content);
            appShellController.handleNavigationChange(resolvedView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String resolveViewName(String fxml) {
        if (fxml == null) {
            return "Catalog";
        }
        String normalized = fxml.trim();
        if (normalized.equalsIgnoreCase("primary") || normalized.equalsIgnoreCase("catalog")) {
            return "Catalog";
        }
        return normalized;
    }
    /**
     * Ensures that the supplied view is scrollable by wrapping it in a
     * {@link ScrollPane} when necessary.  Views that already use a scroll
     * pane are left intact, while new wrappers receive sensible defaults to
     * provide vertical scrolling without affecting existing layouts.
     *
     * @param view the view loaded from FXML
     * @return a node that supports scrolling when the content exceeds the viewport
     */
    private Node ensureScrollable(Parent view) {
        if (view instanceof ScrollPane existing) {
            configureScrollPane(existing, false);
            return existing;
        }


        ScrollPane wrapper = new ScrollPane(view);
        configureScrollPane(wrapper, true);
        return wrapper;
    }

    private void configureScrollPane(ScrollPane scrollPane, boolean enforceFitToWidth) {
        if (enforceFitToWidth) {
            scrollPane.setFitToWidth(true);
        }

        if (scrollPane.isFitToWidth()) {
            Node content = scrollPane.getContent();
            if (content instanceof Region region && !region.minWidthProperty().isBound()) {
                region.minWidthProperty().bind(scrollPane.widthProperty());
            }
        }

        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        if (!scrollPane.getStyleClass().contains("app-scroll-container")) {
            scrollPane.getStyleClass().add("app-scroll-container");
        }
    }

    /**
     * Updates the shell to reflect the current login state.  Controllers can
     * call this after a successful authentication to toggle the login/profile
     * buttons in the header.
     *
     * @param loggedIn whether the user is logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        if (appShellController != null) {
            appShellController.setLoggedIn(loggedIn);
        }
    }

    /**
     * Convenience helper that forwards status updates to the shell's status
     * bar.  When the shell hasn't been initialised yet the call is ignored.
     *
     * @param message the status message to display
     */
    public void setStatus(String message) {
        if (appShellController != null) {
            appShellController.setStatus(message);
        }
    }
}
