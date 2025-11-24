package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the HomePage view. Wires the primary call-to-action
 * buttons to the {@link NavigationService} so visitors can jump to the
 * catalog, orders, cart, or login screens from the landing page.
 */
public class HomePageController {

    @FXML private Button browseCatalogButton;
    @FXML private Button trackOrdersButton;
    @FXML private Button openCatalogButton;
    @FXML private Button startCustomButton;
    @FXML private Button viewOrdersButton;
    @FXML private Button viewAllButton;

    @FXML
    private void handleBrowseCatalog(ActionEvent event) {
        NavigationService.getInstance().navigate("primary");
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
        if (isLoggedIn()) {
            NavigationService.getInstance().navigate("myorders");
        } else {
            NavigationService.getInstance().navigate("Login");
        }
    }

    @FXML
    private void handleStartCustom(ActionEvent event) {
        if (isLoggedIn()) {
            NavigationService.getInstance().navigate("cart");
        } else {
            NavigationService.getInstance().navigate("Login");
        }
    }

    private boolean isLoggedIn() {
        return SimpleClient.getUser() != null;
    }
}