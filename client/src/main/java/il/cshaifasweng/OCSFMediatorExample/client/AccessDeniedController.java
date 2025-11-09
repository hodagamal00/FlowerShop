package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

/**
 * Controller for the Access Denied page
 * Shown when users attempt to access pages above their privilege level
 */
public class AccessDeniedController {

    @FXML private Label usernameLabel;
    @FXML private Button logoutBtn;
    
    @FXML private Hyperlink catalogLink;
    @FXML private Hyperlink ordersLink;
    @FXML private Hyperlink complaintsLink;
    @FXML private Hyperlink accountLink;
    
    @FXML private Label deniedMessageLabel;
    @FXML private Label currentRoleLabel;
    @FXML private Label requiredRoleLabel;
    
    @FXML private Button backBtn;
    @FXML private Button catalogBtn;

    // Access context
    private static int currentPrivilegeLevel = 0;
    private static int requiredPrivilegeLevel = 3;
    private static String attemptedPage = "Unknown Page";
    private static String returnPage = "catalog";
    
    // Privilege level names
    private static final String[] PRIVILEGE_NAMES = {
        "Guest (Level 0)",
        "Customer (Level 1)",
        "Worker (Level 2)",
        "Manager (Level 3)",
        "Chain Manager (Level 4)"
    };

    /**
     * Set access denial information before navigating to this page
     */
    public static void setAccessInfo(int currentLevel, int requiredLevel, String page) {
        currentPrivilegeLevel = Math.max(0, Math.min(4, currentLevel));
        requiredPrivilegeLevel = Math.max(0, Math.min(4, requiredLevel));
        attemptedPage = page != null ? page : "Unknown Page";
    }
    
    /**
     * Set the page to return to
     */
    public static void setReturnPage(String page) {
        returnPage = page != null ? page : "catalog";
    }

    @FXML
    private void initialize() {
        // Load current user information
        loadUserInfo();
        
        // Load access denial information
        loadAccessInfo();
        
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
            //     currentPrivilegeLevel = currentUser.getPrivilegeLevel();
            // } else {
            //     usernameLabel.setText("Guest");
            //     currentPrivilegeLevel = 0;
            // }
            usernameLabel.setText("Guest");
        } catch (Exception e) {
            usernameLabel.setText("Guest");
        }
    }

    /**
     * Load access denial information into UI
     */
    private void loadAccessInfo() {
        // Set current role label
        if (currentPrivilegeLevel >= 0 && currentPrivilegeLevel < PRIVILEGE_NAMES.length) {
            currentRoleLabel.setText(PRIVILEGE_NAMES[currentPrivilegeLevel]);
        } else {
            currentRoleLabel.setText("Unknown (Level " + currentPrivilegeLevel + ")");
        }
        
        // Set required role label
        if (requiredPrivilegeLevel >= 0 && requiredPrivilegeLevel < PRIVILEGE_NAMES.length) {
            requiredRoleLabel.setText(PRIVILEGE_NAMES[requiredPrivilegeLevel]);
        } else {
            requiredRoleLabel.setText("Unknown (Level " + requiredPrivilegeLevel + ")");
        }
        
        // Set custom message based on attempted page
        String message = "You don't have permission to access this page.";
        if (!attemptedPage.equals("Unknown Page")) {
            message = "You don't have permission to access the " + attemptedPage + " page.";
        }
        deniedMessageLabel.setText(message);
    }

    /**
     * Configure navigation links based on user role/privilege
     */
    private void configureNavigationByRole() {
        // Get current user privilege level
        int privilegeLevel = currentPrivilegeLevel;
        
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
        if (currentPrivilegeLevel >= 1) {
            navigateToPage(event, "orders");
        }
    }

    /**
     * Handle navigation to complaints
     */
    @FXML
    private void handleComplaints(ActionEvent event) {
        if (currentPrivilegeLevel >= 1) {
            navigateToPage(event, "complaints");
        }
    }

    /**
     * Handle navigation to account
     */
    @FXML
    private void handleAccount(ActionEvent event) {
        if (currentPrivilegeLevel >= 1) {
            navigateToPage(event, "account");
        }
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
     * Handle go back button - returns to previous page or catalog
     */
    @FXML
    private void handleGoBack(ActionEvent event) {
        navigateToPage(event, returnPage);
    }

    /**
     * Navigate to a specific page
     */
    private void navigateToPage(ActionEvent event, String page) {
        if (event != null) {
            event.consume();
        }
        String target;
        switch (page.toLowerCase()) {
            case "catalog":
                target = "primary";
                break;
            case "orders":
                target = "myorders";
                break;
            case "complaints":
                target = "mycomplaints";
                break;
            case "account":
                target = "Profile";
                break;
            default:
                target = "primary";
                break;
        }

        NavigationService.getInstance().navigate(target);
    }
}
