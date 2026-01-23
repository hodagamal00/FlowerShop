package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class WorkerDashboardController {

    @FXML private Button ordersBtn;
    @FXML private Button catalogBtn;
    @FXML private Button deliveriesBtn;
    @FXML private Button complaintsBtn;
    @FXML private Text branchNameText;
    @FXML private Text pendingDeliveriesText;
    @FXML private Text ordersTodayText;
    @FXML private Text complaintsText;
    @FXML private Text productsText;
    @FXML private TableView<?> deliveriesTable;
    @FXML private TableColumn<?, ?> deliveryOrderIdCol;
    @FXML private TableColumn<?, ?> deliveryCustomerCol;
    @FXML private TableColumn<?, ?> deliveryAddressCol;
    @FXML private TableColumn<?, ?> deliveryTimeCol;
    @FXML private TableColumn<?, ?> deliveryStatusCol;
    @FXML private TableView<?> ordersTable;
    @FXML private TableColumn<?, ?> orderIdCol;
    @FXML private TableColumn<?, ?> customerCol;
    @FXML private TableColumn<?, ?> orderDateCol;
    @FXML private TableColumn<?, ?> totalCol;
    @FXML private TableColumn<?, ?> orderStatusCol;

    @FXML
    void initialize() {
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Get branch from logged in account
        if (SimpleClient.getAccount() != null) {
            int branchId = SimpleClient.getAccount().getBelongShop();
            branchNameText.setText("#" + branchId);
        }

        // TODO: Load actual data from server
        // For now, display sample statistics
        pendingDeliveriesText.setText("12");
        ordersTodayText.setText("28");
        complaintsText.setText("5");
        productsText.setText("156");
    }

    @FXML
    void goToHome() {
        NavigationService.getInstance().navigate("Catalog");
    }

    @FXML
    void goToOrders() {
        NavigationService.getInstance().navigate("BranchOrders");
    }

    @FXML
    void goToCatalog() {
        NavigationService.getInstance().navigate("CatalogManagement");
    }

    @FXML
    void goToDeliveries() {
        NavigationService.getInstance().navigate("delivery");
    }

    @FXML
    void goToComplaints() {
        NavigationService.getInstance().navigate("replycomplaint");
    }
}
