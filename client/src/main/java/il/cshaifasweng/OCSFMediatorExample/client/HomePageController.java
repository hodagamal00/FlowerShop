package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import il.cshaifasweng.OCSFMediatorExample.client.PassAccountEvent;

/**
 * Controller for the HomePage view. Wires the primary call-to-action
 * buttons to the {@link NavigationService} so visitors can jump to the
 * catalog, orders, cart, or login screens from the landing page.
 */
public class HomePageController {

    @FXML private Button browseCatalogButton;
    @FXML private Button complaintsButton;
    @FXML private Button loginButton;
    @FXML private Button createAccountButton;
    @FXML private Button trackOrdersButton;
    @FXML private Button openCatalogButton;
    @FXML private Button startCustomButton;
    @FXML private Button viewOrdersButton;
    @FXML private Button viewAllButton;
    @FXML private VBox catalogCard;
    @FXML private VBox customCard;
    @FXML private VBox ordersCard;
    @FXML private VBox managerActionsBox;
    @FXML private Button managerCatalogButton;
    @FXML private Button managerDashboardButton;
    @FXML private Button managerOrdersButton;
    @FXML private Button managerReportsButton;
    @FXML private Button managerPanelButton;

    @FXML
    private void initialize() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // Wire buttons defensively so the landing page always navigates, even
        // if the onAction attributes are removed during scene editor tweaks.
        attachHandler(browseCatalogButton, this::handleBrowseCatalog);
        attachHandler(complaintsButton, this::handleComplaints);
        attachHandler(loginButton, this::handleLogin);
        attachHandler(createAccountButton, this::handleCreateAccount);
        attachHandler(openCatalogButton, this::handleOpenCatalog);
        attachHandler(viewAllButton, this::handleViewAll);
        attachHandler(trackOrdersButton, this::handleTrackOrders);
        attachHandler(viewOrdersButton, this::handleTrackOrders);
        attachHandler(startCustomButton, this::handleStartCustom);
        attachHandler(managerCatalogButton, this::handleManageCatalog);
        attachHandler(managerDashboardButton, this::handleManagerDashboard);
        attachHandler(managerOrdersButton, this::handleBranchOrders);
        attachHandler(managerReportsButton, this::handleManagerReports);
        attachHandler(managerPanelButton, this::handleManagerPanel);
        updateRoleAwareUI();
    }

    @FXML
    private void handleBrowseCatalog(ActionEvent event) {
        CatalogFlag.setFlagg(0);
        NavigationService.getInstance().navigate("Catalog");
    }

    @FXML
    private void handleComplaints(ActionEvent event) {
        int privilege = SimpleClient.getPrivilegeLevel();
        if (privilege >= 2) {
            NavigationService.getInstance().navigate("replycomplaint");
            return;
        }
        if (requireLogin("submit a complaint")) {
            NavigationService.getInstance().navigate("mycomplaints");
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        NavigationService.getInstance().navigate("Login");
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        NavigationService.getInstance().navigate("register");
    }

    @FXML
    private void handleOpenCatalog(ActionEvent event) {
        handleBrowseCatalog(event);
    }

    @FXML
    private void handleViewAll(ActionEvent event) {
        handleBrowseCatalog(event);
    }

    @FXML
    private void handleTrackOrders(ActionEvent event) {
        if (requireLogin("track your orders")) {
            NavigationService.getInstance().navigate("myorders");
        }
    }

    @FXML
    private void handleStartCustom(ActionEvent event) {
        if (requireLogin("start a custom order")) {
            NavigationService.getInstance().navigate("Catalog");
        }
    }

    @FXML
    private void handleManageCatalog(ActionEvent event) {
        NavigationService.getInstance().navigate("Catalog");
    }

    @FXML
    private void handleManagerDashboard(ActionEvent event) {
        NavigationService.getInstance().navigate("NetworkDashboard");
    }

    @FXML
    private void handleBranchOrders(ActionEvent event) {
        NavigationService.getInstance().navigate("BranchOrders");
    }

    @FXML
    private void handleManagerReports(ActionEvent event) {
        NavigationService.getInstance().navigate("CrossBranchReports");
    }

    @FXML
    private void handleManagerPanel(ActionEvent event) {
        NavigationService.getInstance().navigate("admincontrol");
    }

    private void attachHandler(Button button, EventHandler<ActionEvent> handler) {
        if (button != null && handler != null) {
            button.setOnAction(handler);
        }
    }

    private boolean isLoggedIn() {
        return SimpleClient.getCurrentUser() != null;
    }

    private boolean requireLogin(String actionLabel) {
        if (isLoggedIn()) {
            return true;
        }
        NavigationService.getInstance().setStatus("Please log in to " + actionLabel + ".");
        NavigationService.getInstance().navigate("Login");
        return false;
    }

    private void updateRoleAwareUI() {
        int privilege = SimpleClient.getPrivilegeLevel();
        boolean isManagerView = privilege >= 2;
        setVisibleManaged(managerActionsBox, isManagerView);
        setVisibleManaged(loginButton, !isManagerView);
        setVisibleManaged(createAccountButton, !isManagerView);
        setVisibleManaged(trackOrdersButton, privilege == 1);
        setVisibleManaged(startCustomButton, privilege == 1);
        setVisibleManaged(viewOrdersButton, privilege == 1);
        setVisibleManaged(complaintsButton, privilege <= 1);
        setVisibleManaged(customCard, privilege <= 1);
        setVisibleManaged(ordersCard, privilege <= 1);
        setVisibleManaged(catalogCard, true);
    }

    @Subscribe
    public void onAccountUpdated(PassAccountEvent event) {
        updateRoleAwareUI();
    }

    private void setVisibleManaged(Button button, boolean visible) {
        if (button == null) {
            return;
        }
        button.setVisible(visible);
        button.setManaged(visible);
    }

    private void setVisibleManaged(VBox box, boolean visible) {
        if (box == null) {
            return;
        }
        box.setVisible(visible);
        box.setManaged(visible);
    }
}
