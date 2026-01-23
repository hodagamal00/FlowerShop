package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Controller for the {@code AppShell.fxml}.  This class manages the
 * application shell which contains a header, a central content area
 * and a status bar.  It provides methods for swapping the centre
 * content and updating header elements such as the cart badge and
 * login/profile buttons.
 */
public class AppShellController {

    @FXML private Button loginButton;
    @FXML private Button logoutButton;
    @FXML private VBox profileContainer;
    @FXML private Button profileButton;
    @FXML private Label profileNameLabel;
    @FXML private Label statusLabel;
    @FXML private StackPane contentPane;
    @FXML private FlowPane navBar;
    @FXML private Label loggedInIndicatorLabel;

    private final ToggleGroup navToggleGroup = new ToggleGroup();
    private final Map<String, ToggleButton> navButtons = new HashMap<>();
    private String currentViewName = "";

    private static final List<NavDestination> NAV_LINKS = List.of(
            NavDestination.forAllUsers("Home", "HomePage"),
            NavDestination.forAllUsers("Catalog", "Catalog"),
            NavDestination.forLoggedIn("Cart", "cart", 1),
            NavDestination.forLoggedIn("Checkout", "checkout", 1),
            NavDestination.forLoggedIn("Orders", "myorders", 1),
            NavDestination.forLoggedIn("Complaints", "mycomplaints", 1),
            NavDestination.forLoggedIn("My Account", "Profile", 1),
            NavDestination.forGuestsOnly("Login", "Login"),
            NavDestination.forGuestsOnly("Register", "register"),
            NavDestination.forLoggedIn("Admin Panel", "admincontrol", 3),
            NavDestination.forLoggedIn("Deliveries", "delivery", 2),
            NavDestination.forLoggedIn("Branch Orders", "BranchOrders", 2),
            NavDestination.forLoggedIn("Reports", "BranchReports", 3)
    );
    /**
     * Called by the FXML loader after the fields have been injected.
     * Registers this controller with the {@link NavigationService} and
     * attaches basic navigation handlers to the header buttons.
     */
    public void initialize() {
        NavigationService.getInstance().setAppShellController(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (profileNameLabel != null) {
            profileNameLabel.setVisible(false);
        }
        updateAccountIndicator();
        // Attach simple handlers that delegate navigation to the
        // NavigationService.  These may be overridden or extended
        // by individual controllers as needed.
        if (loginButton != null) {
            loginButton.setOnAction(e -> NavigationService.getInstance().navigate("Login"));
        }
        if (logoutButton != null) {
            logoutButton.setOnAction(e -> handleLogout());
        }
        if (profileButton != null) {
            profileButton.setOnAction(e -> NavigationService.getInstance().navigate("Profile"));
        }
        buildNavigationBar(SimpleClient.getUser());
        updateLoginState(SimpleClient.getUser());

    }

    /**
     * Replaces the content of the centre pane with the given node.
     *
     * @param node the node to display in the centre
     */
    public void setContent(Node node) {

        contentPane.getChildren().setAll(node);
    }

    public boolean isActive() {
        return contentPane != null && contentPane.getScene() != null;
    }
    public void handleNavigationChange(String viewName) {
        if (navBar == null) {
            return;
        }
        String normalized = normalizeViewName(viewName);
        ToggleButton button = navButtons.get(normalized);
        currentViewName = normalized;
        Platform.runLater(() -> {
            if (button != null) {
                navToggleGroup.selectToggle(button);
            } else {
                navToggleGroup.selectToggle(null);
            }
        });
        updateAccountIndicator();
    }
    /**
     * Shows or hides the login and profile buttons based on login state.
     * When the user is logged in, the login button is hidden and the
     * profile button is visible.
     *
     * @param loggedIn whether the user is currently logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        if (loginButton != null && profileButton != null) {
            loginButton.setVisible(!loggedIn);
            profileButton.setVisible(loggedIn);
        }
        if (logoutButton != null) {
            logoutButton.setVisible(loggedIn);
            logoutButton.setManaged(loggedIn);
        }
        if (!loggedIn && profileNameLabel != null) {
            profileNameLabel.setVisible(false);
            profileNameLabel.setText("");
        }
        updateAccountIndicator();
    }

    /**
     * Updates the status message at the bottom of the application.
     *
     * @param message the message to display
     */
    public void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    /**
     * Displays the logged-in account name in the header and toggles the
     * appropriate login/profile buttons.
     *
     * @param fullName The display name of the logged-in account
     */
    public void showAccountName(String fullName) {
        setLoggedIn(true);
        if (profileNameLabel != null) {
            profileNameLabel.setText(fullName != null ? fullName : "");
            profileNameLabel.setVisible(fullName != null && !fullName.isBlank());
        }
        updateAccountIndicator();
    }

    @Subscribe
    public void handlePassAccountEvent(PassAccountEvent event) {
        Account account = event.getRecievedAccount();
        if (account == null) {
            return;
        }
        Platform.runLater(() -> updateLoginState(account));
    }
    public void onAccountReceived(PassAccountEvent event) {
        updateLoginState(event.getRecievedAccount());
    }

    private void updateLoginState(Account account) {
        if (loginButton == null || profileButton == null || profileContainer == null || profileNameLabel == null) {
            return;
        }

        Account effectiveAccount = account;
        if (effectiveAccount == null || (isNullOrBlank(effectiveAccount.getFullName()) && isNullOrBlank(effectiveAccount.getEmail()))) {
            effectiveAccount = SimpleClient.getUser();
        }

        final Account finalAccount = effectiveAccount;
        Platform.runLater(() -> {
            boolean loggedIn = finalAccount != null;
            String displayName = "";
            if (loggedIn) {
                displayName = isNullOrBlank(finalAccount.getFullName()) ? finalAccount.getEmail() : finalAccount.getFullName();
                if (isNullOrBlank(displayName)) {
                    loggedIn = false;
                }
            }

            profileNameLabel.setText(loggedIn ? displayName : "");
            updateAccountIndicator();
            updateProfileButtonVisibility(finalAccount);

            loginButton.setVisible(!loggedIn);
            loginButton.setManaged(!loggedIn);
            if (logoutButton != null) {
                logoutButton.setVisible(loggedIn);
                logoutButton.setManaged(loggedIn);
            }

            boolean showProfile = finalAccount != null && finalAccount.getPrivilegeLevel() == 1;
            profileContainer.setVisible(showProfile);
            profileContainer.setManaged(showProfile);
            buildNavigationBar(finalAccount);

        });
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    private void updateAccountIndicator() {
        if (loggedInIndicatorLabel == null) {
            return;
        }
        Account account = SimpleClient.getUser();
        String indicatorText = buildAccountIndicatorText(account);
        loggedInIndicatorLabel.setText(indicatorText);
        loggedInIndicatorLabel.setVisible(true);
        loggedInIndicatorLabel.setManaged(true);
    }

    private String buildAccountIndicatorText(Account account) {
        if (account == null) {
            return "Guest";
        }
        String displayName = isNullOrBlank(account.getFullName()) ? account.getEmail() : account.getFullName();
        if (isNullOrBlank(displayName)) {
            return "Guest";
        }
        String role = formatRole(account.getPrivilegeLevel());
        return role.isEmpty()
                ? "Logged in: " + displayName.trim()
                : "Logged in: " + displayName.trim() + " (" + role + ")";
    }

    private String formatRole(int privilegeLevel) {
        return switch (privilegeLevel) {
            case 1 -> "Customer";
            case 2 -> "Worker";
            case 3 -> "Manager";
            case 4 -> "Chain Manager";
            default -> "";
        };
    }

    private void updateProfileButtonVisibility(Account account) {
        if (profileButton == null) {
            return;
        }
        boolean show = account != null && account.getPrivilegeLevel() == 1;
        profileButton.setVisible(show);
        profileButton.setManaged(show);
    }

    @FXML
    private void handleLogout() {
        SimpleClient.logoutCurrentUser();
        updateLoginState(null);
        updateAccountIndicator();
        NavigationService.getInstance().navigate("HomePage");
    }
    private void buildNavigationBar() {
        buildNavigationBar(SimpleClient.getUser());
    }

    private void buildNavigationBar(Account account) {
        if (navBar == null) {
            return;
        }
        navButtons.clear();
        navBar.getChildren().clear();

        int privilege = account != null ? account.getPrivilegeLevel() : 0;
        boolean loggedIn = account != null;
        boolean isCustomer = privilege == 1;

        for (NavDestination destination : NAV_LINKS) {
            if (!destination.isVisibleFor(privilege, loggedIn)) {
                continue;
            }
            String viewName = destination.getViewName();
            if ("Profile".equalsIgnoreCase(viewName) && privilege != 1) {
                continue;
            }
            if (isCustomerOnlyView(viewName) && !isCustomer) {
                continue;
            }
            ToggleButton button = new ToggleButton(destination.getLabel());
            button.setToggleGroup(navToggleGroup);
            button.setFocusTraversable(false);
            button.getStyleClass().addAll("nav-link", "pill");
            button.setOnAction(event -> NavigationService.getInstance().navigate(destination.getViewName()));

            String normalized = normalizeViewName(destination.getViewName());
            navButtons.put(normalized, button);
            navBar.getChildren().add(button);
        }
        selectCurrentNavButton();

    }

    private boolean isCustomerOnlyView(String viewName) {
        if (viewName == null) {
            return false;
        }
        String normalized = normalizeViewName(viewName);
        return normalized.equals("cart")
                || normalized.equals("checkout")
                || normalized.equals("myorders")
                || normalized.equals("mycomplaints")
                || normalized.equals("profile");
    }

    private String normalizeViewName(String viewName) {
        return viewName == null ? "" : viewName.toLowerCase(Locale.ROOT);
    }
    private void selectCurrentNavButton() {
        if (currentViewName == null) {
            return;
        }
        ToggleButton button = navButtons.get(currentViewName);
        if (button != null) {
            navToggleGroup.selectToggle(button);
        } else {
            navToggleGroup.selectToggle(null);
        }
    }
    private static class NavDestination {
        private final String label;
        private final String viewName;
        private final int minPrivilege;
        private final boolean requiresLogin;
        private final boolean guestOnly;

        private NavDestination(String label, String viewName, int minPrivilege, boolean requiresLogin, boolean guestOnly) {
            this.label = label;
            this.viewName = viewName;
            this.minPrivilege = minPrivilege;
            this.requiresLogin = requiresLogin;
            this.guestOnly = guestOnly;
        }

        static NavDestination forAllUsers(String label, String viewName) {
            return new NavDestination(label, viewName, 0, false, false);
        }

        static NavDestination forLoggedIn(String label, String viewName, int minPrivilege) {
            return new NavDestination(label, viewName, minPrivilege, true, false);
        }

        static NavDestination forGuestsOnly(String label, String viewName) {
            return new NavDestination(label, viewName, 0, false, true);
        }

        String getLabel() {
            return label;
        }

        String getViewName() {
            return viewName;
        }

        boolean isVisibleFor(int privilege, boolean loggedIn) {
            if (guestOnly) {
                return !loggedIn;
            }
            if (requiresLogin && !loggedIn) {
                return false;
            }
            return privilege >= minPrivilege;
        }
    }
}
