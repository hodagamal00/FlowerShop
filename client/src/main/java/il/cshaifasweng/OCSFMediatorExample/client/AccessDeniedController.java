package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;

import java.util.List;

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

    @FXML private VBox privilegeDetailsBox;

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
    private static final PrivilegeDescriptor[] PRIVILEGE_DESCRIPTORS = {
            new PrivilegeDescriptor(0, "Level 0 – Guest",
                    "Role: Unregistered user (no account / no login)",
                    List.of(
                            "Browse the public catalog (view all products by category, price, color, etc.)",
                            "View product details (name, price, image, description)",
                            "Add items to a temporary cart (not saved between sessions)",
                            "Cannot place orders, submit complaints, or view personal data",
                            "Access the Login and Registration pages only"
                    )),
            new PrivilegeDescriptor(1, "👩‍💼 Level 1 – Customer",
                    "Role: Registered user with an account",
                    List.of(
                            "Create and confirm orders (from catalog or custom bouquets)",
                            "Specify delivery information (address, receiver name, phone)",
                            "Pay for orders (credit card, subscription)",
                            "Cancel orders according to policy (refund rules)",
                            "Submit and view complaints",
                            "View order history",
                            "Receive notifications about orders, deliveries, and complaints",
                            "If subscribed: get discounts and network-wide purchase rights"
                    )),
            new PrivilegeDescriptor(2, "👩‍🔧 Level 2 – Worker",
                    "Role: Flower shop employee (customer service or store clerk)",
                    List.of(
                            "View and manage branch orders (accept, prepare, mark as delivered)",
                            "Handle complaints assigned to the branch",
                            "Access branch inventory (add/update stock availability)",
                            "Send status updates to customers (e.g., delivery dispatched)",
                            "Cannot modify catalog or prices (manager-only task)"
                    )),
            new PrivilegeDescriptor(3, "👩‍🏫 Level 3 – Branch Manager",
                    "Role: Manager of a specific store/branch",
                    List.of(
                            "Manage catalog items for their branch (add, update, remove)",
                            "View and analyze branch reports (sales, orders, complaints)",
                            "Manage branch workers and accounts",
                            "Approve discounts and promotions for their branch only"
                    )),
            new PrivilegeDescriptor(4, "👩‍💼 Level 4 – Chain Manager",
                    "Role: Central administrator of the entire flower network",
                    List.of(
                            "Manage all branches, managers, and workers",
                            "Update and synchronize the global catalog",
                            "View and compare reports across all branches",
                            "Modify system configurations, user privileges, and accounts",
                            "Suspend or reactivate user accounts",
                            "Create global promotions",
                            "Supervise complaint resolution and system performance",
                            "Monitor activity and analytics for the entire network"
                    ))
    };

    private static final class PrivilegeDescriptor {
        final int level;
        final String title;
        final String roleDescription;
        final List<String> capabilities;

        PrivilegeDescriptor(int level, String title, String roleDescription, List<String> capabilities) {
            this.level = level;
            this.title = title;
            this.roleDescription = roleDescription;
            this.capabilities = capabilities;
        }
    }

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
        // Build privilege breakdown
        populatePrivilegeDetails();
    }

    /**
     * Load current user information
     */
    private void loadUserInfo() {
        try {
            Account currentUser = SimpleClient.getUser();
            if (currentUser != null) {
                String displayName = currentUser.getFullName();
                if (displayName == null || displayName.isBlank()) {
                    displayName = currentUser.getEmail();
                }
                if (displayName == null || displayName.isBlank()) {
                    displayName = "User";
                }
                usernameLabel.setText(displayName);
            } else {
                usernameLabel.setText("Guest");
            }
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

        boolean loggedIn = privilegeLevel >= 1;

        setLinkVisibility(ordersLink, loggedIn);
        setLinkVisibility(complaintsLink, loggedIn);
        setLinkVisibility(accountLink, loggedIn);

        if (logoutBtn != null) {
            logoutBtn.setVisible(loggedIn);
            logoutBtn.setManaged(loggedIn);
        }
    }

    private void setLinkVisibility(Hyperlink link, boolean visible) {
        if (link != null) {
            link.setVisible(visible);
            link.setManaged(visible);
        }
    }

    private void populatePrivilegeDetails() {
        if (privilegeDetailsBox == null) {
            return;
        }

        privilegeDetailsBox.getChildren().clear();
        for (PrivilegeDescriptor descriptor : PRIVILEGE_DESCRIPTORS) {
            VBox box = new VBox(6);
            box.setFillWidth(true);
            box.getStyleClass().add("card");
            box.setStyle(descriptor.level == currentPrivilegeLevel
                    ? "-fx-border-color: #6a1b9a; -fx-border-width: 2; -fx-background-color: rgba(106,27,154,0.08); -fx-border-radius: 10; -fx-background-radius: 10;"
                    : descriptor.level == requiredPrivilegeLevel
                    ? "-fx-border-color: #c0392b; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;"
                    : "-fx-border-color: rgba(0,0,0,0.05); -fx-border-radius: 10; -fx-background-radius: 10;");

            Label titleLabel = new Label(descriptor.title);
            titleLabel.getStyleClass().add("strong");
            Label roleLabel = new Label(descriptor.roleDescription);
            roleLabel.getStyleClass().add("text-secondary");
            roleLabel.setWrapText(true);

            box.getChildren().addAll(titleLabel, roleLabel);
            for (String capability : descriptor.capabilities) {
                Label capabilityLabel = new Label("• " + capability);
                capabilityLabel.setWrapText(true);
                box.getChildren().add(capabilityLabel);
            }

            privilegeDetailsBox.getChildren().add(box);
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
     * Navigate to a specific page
     */
    private void navigateToPage(ActionEvent event, String page) {
        if (event != null) {
            event.consume();
        }
        String target;
        switch (page.toLowerCase()) {
            case "catalog":
                target = "Catalog";
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
                target = "Catalog";
                break;
        }

        NavigationService.getInstance().navigate(target);
    }
}
