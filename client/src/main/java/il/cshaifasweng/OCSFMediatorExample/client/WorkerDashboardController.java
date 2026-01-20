package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class WorkerDashboardController {

    @FXML private Button homeBtn;
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
        loadScene("Catalog.fxml", homeBtn);
    }

    @FXML
    void goToOrders() {
        loadScene("BranchOrders.fxml", ordersBtn);
    }

    @FXML
    void goToCatalog() {
        loadScene("CatalogManagement.fxml", catalogBtn);
    }

    @FXML
    void goToDeliveries() {
        loadScene("delivery.fxml", deliveriesBtn);
    }

    @FXML
    void goToComplaints() {
        loadScene("replycomplaint.fxml", complaintsBtn);
    }

    private void loadScene(String fxml, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxml + ": " + e.getMessage());
        }
    }
}
