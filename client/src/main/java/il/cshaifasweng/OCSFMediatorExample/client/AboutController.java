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

/**
 * Controller for the About page
 * Displays company information, system details, and contact information
 */
public class AboutController {

    @FXML private Label usernameLabel;
    @FXML private Button logoutBtn;
    
    @FXML private Hyperlink catalogLink;
    @FXML private Hyperlink ordersLink;
    @FXML private Hyperlink complaintsLink;
    @FXML private Hyperlink accountLink;
    
    @FXML private Label versionLabel;
    @FXML private Label buildDateLabel;
    @FXML private Label platformLabel;
    @FXML private Label javaVersionLabel;

    @FXML
    private void initialize() {
        // Load current user information
        loadUserInfo();
        
        // Load system information
        loadSystemInfo();
        
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
     * Load system information from runtime
     */
    private void loadSystemInfo() {
        try {
            // Get actual system information
            String javaVersion = System.getProperty("java.version");
            String javafxVersion = System.getProperty("javafx.version");
            
            // Update labels with real data
            if (javaVersion != null) {
                javaVersionLabel.setText("Java " + javaVersion);
            }
            
            if (javafxVersion != null) {
                platformLabel.setText("JavaFX " + javafxVersion);
            }
            
            // Version and build date would typically come from application properties
            // versionLabel.setText(AppConfig.VERSION);
            // buildDateLabel.setText(AppConfig.BUILD_DATE);
            
        } catch (Exception e) {
            System.err.println("Error loading system info: " + e.getMessage());
        }
    }

    /**
     * Configure navigation links based on user role/privilege
     */
    private void configureNavigationByRole() {
        // Get current user privilege level
        int privilegeLevel = getPrivilegeLevel();
        
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
        navigateToPage(event, "orders");
    }

    /**
     * Handle navigation to complaints
     */
    @FXML
    private void handleComplaints(ActionEvent event) {
        navigateToPage(event, "complaints");
    }

    /**
     * Handle navigation to account
     */
    @FXML
    private void handleAccount(ActionEvent event) {
        navigateToPage(event, "account");
    }

    /**
     * Handle logout
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        // Perform logout
        // SimpleClient.getClient().logout();
        navigateToPage(event, "catalog");
    }

    /**
     * Handle contact support button
     */
    @FXML
    private void handleContactSupport(ActionEvent event) {
        // Navigate to complaints page or show contact dialog
        navigateToPage(event, "complaints");
    }

    /**
     * Navigate to a specific page
     */
    private void navigateToPage(ActionEvent event, String page) {
        try {
            Parent root = null;
            
            switch (page.toLowerCase()) {
                case "catalog":
                    root = FXMLLoader.load(getClass().getResource("primary.fxml"));
                    break;
                case "orders":
                    root = FXMLLoader.load(getClass().getResource("orders.fxml"));
                    break;
                case "complaints":
                    root = FXMLLoader.load(getClass().getResource("complaints.fxml"));
                    break;
                case "account":
                    root = FXMLLoader.load(getClass().getResource("account.fxml"));
                    break;
                default:
                    root = FXMLLoader.load(getClass().getResource("primary.fxml"));
                    break;
            }
            
            if (root != null) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            
        } catch (IOException e) {
            System.err.println("Error navigating to page: " + page);
            e.printStackTrace();
            // Show error page if navigation fails
            showErrorPage(event, "Navigation Error", "Failed to navigate to " + page);
        }
    }

    /**
     * Show error page with error details
     */
    private void showErrorPage(ActionEvent event, String title, String message) {
        try {
            ErrorController.setErrorInfo(title, message, "NAV_ERROR", "Failed to load requested page");
            ErrorController.setReturnPage("about");
            
            Parent root = FXMLLoader.load(getClass().getResource("Error.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            System.err.println("ERROR: " + title + " - " + message);
            ex.printStackTrace();
        }
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
