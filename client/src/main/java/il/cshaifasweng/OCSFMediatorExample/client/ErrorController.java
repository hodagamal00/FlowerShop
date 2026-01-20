package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import il.cshaifasweng.OCSFMediatorExample.client.NavigationService;
import il.cshaifasweng.OCSFMediatorExample.client.App;

/**
 * Controller for the Error page
 * Displays user-friendly error messages and provides recovery options
 */
public class ErrorController {

    @FXML private Label usernameLabel;
    @FXML private Button logoutBtn;
    
    @FXML private Hyperlink catalogLink;
    @FXML private Hyperlink ordersLink;
    @FXML private Hyperlink complaintsLink;
    @FXML private Hyperlink accountLink;
    
    @FXML private Label errorTitleLabel;
    @FXML private Label errorMessageLabel;
    @FXML private Label errorCodeLabel;
    @FXML private Label errorDetailsLabel;
    
    @FXML private Button backBtn;
    @FXML private Button homeBtn;
    @FXML private Button retryBtn;

    // Error context
    private static String lastErrorTitle = "Oops! Something Went Wrong";
    private static String lastErrorMessage = "We encountered an unexpected error while processing your request.";
    private static String lastErrorCode = "ERR_UNKNOWN";
    private static String lastErrorDetails = "No additional details available";
    private static String lastPage = "catalog"; // Page to return to on retry/back
    
    /**
     * Set error information before navigating to error page
     */
    public static void setErrorInfo(String title, String message, String code, String details) {
        lastErrorTitle = title != null ? title : "Oops! Something Went Wrong";
        lastErrorMessage = message != null ? message : "We encountered an unexpected error.";
        lastErrorCode = code != null ? code : "ERR_UNKNOWN";
        lastErrorDetails = details != null ? details : "No additional details available";
    }
    
    /**
     * Set error information with throwable
     */
    public static void setErrorInfo(String title, String message, Throwable throwable) {
        lastErrorTitle = title != null ? title : "Oops! Something Went Wrong";
        lastErrorMessage = message != null ? message : "We encountered an unexpected error.";
        lastErrorCode = "ERR_EXCEPTION";
        lastErrorDetails = throwable != null ? throwable.getClass().getSimpleName() + ": " + throwable.getMessage() : "No additional details available";
    }
    
    /**
     * Set the page to return to
     */
    public static void setReturnPage(String page) {
        lastPage = page != null ? page : "catalog";
    }

    @FXML
    private void initialize() {
        // Load current user information
        loadUserInfo();
        
        // Load error information
        loadErrorInfo();
        
        // Configure navigation visibility based on user role
        configureNavigationByRole();
    }

    /**
     * Load current user information
     */
    private void loadUserInfo() {
        // Get current user from session/client
        // For now, using placeholder
        try {
            // Account currentUser = SimpleClient.getClient().getCurrentUser();
            // if (currentUser != null) {
            //     usernameLabel.setText(currentUser.getUserName());
            // } else {
            //     usernameLabel.setText("Guest");
            // }
            usernameLabel.setText("Guest");
        } catch (Exception e) {
            usernameLabel.setText("Guest");
        }
    }

    /**
     * Load error information into UI
     */
    private void loadErrorInfo() {
        errorTitleLabel.setText(lastErrorTitle);
        errorMessageLabel.setText(lastErrorMessage);
        errorCodeLabel.setText("Error Code: " + lastErrorCode);
        errorDetailsLabel.setText(lastErrorDetails);
    }

    /**
     * Configure navigation links based on user role/privilege
     */
    private void configureNavigationByRole() {
        // Get current user privilege level
        // int privilegeLevel = getPrivilegeLevel();
        int privilegeLevel = 0; // Guest by default
        
        // Show/hide links based on privilege
        if (privilegeLevel < 1) { // Guest
            ordersLink.setVisible(false);
            complaintsLink.setVisible(false);
            accountLink.setVisible(false);
            logoutBtn.setVisible(false);
        }
    }

    /**
     * Handle navigation to catalog
     */
    @FXML
    private void handleCatalog(ActionEvent event) {
        navigateToPage(event, "catalog");
    }

    /**
     * Handle navigation to orders
     */
    @FXML
    private void handleOrders(ActionEvent event) {
        if (SimpleClient.getUser() == null) {
            NavigationService.getInstance().navigate("Login");
            return;
        }
        navigateToPage(event, "orders");
    }

    /**
     * Handle navigation to complaints
     */
    @FXML
    private void handleComplaints(ActionEvent event) {
        if (SimpleClient.getUser() == null) {
            NavigationService.getInstance().navigate("Login");
            return;
        }
        navigateToPage(event, "complaints");
    }

    /**
     * Handle navigation to account
     */
    @FXML
    private void handleAccount(ActionEvent event) {
        if (SimpleClient.getUser() == null) {
            NavigationService.getInstance().navigate("Login");
            return;
        }
        navigateToPage(event, "account");
    }

    /**
     * Handle logout
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        SimpleClient.logoutCurrentUser();
        navigateToPage(event, "catalog");
    }

    /**
     * Handle go back button - returns to previous page or catalog
     */
    private void handleGoBack(ActionEvent event) {
        navigateToPage(event, lastPage);
    }

    /**
     * Handle go home button - returns to catalog
     */
    @FXML
    private void handleGoHome(ActionEvent event) {
        navigateToPage(event, "catalog");
    }

    /**
     * Handle retry button - attempts to return to the page that caused the error
     */
    @FXML
    private void handleRetry(ActionEvent event) {
        navigateToPage(event, lastPage);
    }

    /**
     * Navigate to a specific page
     */
    private void navigateToPage(ActionEvent event, String page) {
        try {
            String view = resolveViewName(page);
            FXMLLoader shellLoader = new FXMLLoader(App.class.getResource("AppShell.fxml"));
            Parent shellRoot = shellLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();

            if (scene == null) {
                scene = new Scene(shellRoot, 1520, 800);
                stage.setScene(scene);
            } else {
                scene.setRoot(shellRoot);
            }

            stage.setMaximized(true);
            NavigationService.getInstance().navigate(view);

        } catch (IOException e) {
            System.err.println("Error navigating to page: " + page);
            e.printStackTrace();
            // If navigation fails, show error in console
            showErrorAlert("Navigation Error", "Failed to navigate to " + page);
        }
    }

    private String resolveViewName(String page) {
        if (page == null) {
            return "Catalog";
        }

        switch (page.toLowerCase()) {
            case "catalog":
                return "Catalog";
            case "orders":
                return "myorders";
            case "complaints":
                return "mycomplaints";
            case "account":
                return "Profile";
            default:
                return page;
        }
    }

    /**
     * Show error alert (fallback if FXML loading fails)
     */
    private void showErrorAlert(String title, String message) {
        System.err.println("ERROR: " + title + " - " + message);
        // Could use Alert dialog here if needed
    }

    /**
     * Get current user privilege level
     */
    private int getPrivilegeLevel() {
        try {
            // Account currentUser = SimpleClient.getClient().getCurrentUser();
            // return currentUser != null ? currentUser.getPrivilegeLevel() : 0;
            return 0; // Guest
        } catch (Exception e) {
            return 0; // Guest on error
        }
    }
}
