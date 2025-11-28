package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class BranchOrdersController {

    @FXML private Button dashboardBtn;
    @FXML private Button homeBtn;
    @FXML private ComboBox<String> statusFilterCombo;
    @FXML private TextField searchField;
    @FXML private Button searchBtn;
    @FXML private Button refreshBtn;
    @FXML private Label orderCountLabel;
    @FXML private TableView<?> ordersTable;
    @FXML private TableColumn<?, ?> orderIdCol;
    @FXML private TableColumn<?, ?> customerCol;
    @FXML private TableColumn<?, ?> orderDateCol;
    @FXML private TableColumn<?, ?> deliveryTimeCol;
    @FXML private TableColumn<?, ?> typeCol;
    @FXML private TableColumn<?, ?> totalCol;
    @FXML private TableColumn<?, ?> statusCol;
    @FXML private TableColumn<?, ?> actionsCol;
    @FXML private Button viewDetailsBtn;
    @FXML private Button updateStatusBtn;
    @FXML private Button printBtn;
    @FXML private Label successMessage;
    @FXML private Label errorMessage;

    @FXML
    void initialize() {
        // Initialize status filter
        statusFilterCombo.setItems(FXCollections.observableArrayList(
            "All", "Pending", "Confirmed", "Preparing", "Ready", "In Delivery", "Completed", "Cancelled"
        ));
        statusFilterCombo.setValue("All");

        loadOrders();
    }

    private void loadOrders() {
        // TODO: Load orders from server for current branch
        orderCountLabel.setText("(125 total)");
    }

    @FXML
    void searchOrders() {
        String searchTerm = searchField.getText();
        String statusFilter = statusFilterCombo.getValue();
        
        // TODO: Filter orders based on search and status
        showSuccess("Search completed");
    }

    @FXML
    void refreshOrders() {
        loadOrders();
        showSuccess("Orders refreshed");
    }

    @FXML
    void viewOrderDetails() {
        // TODO: Get selected order and navigate to OrderDetails
        showError("Please select an order first");
    }

    @FXML
    void updateOrderStatus() {
        // Show status update dialog
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Confirmed", 
            "Pending", "Confirmed", "Preparing", "Ready", "In Delivery", "Completed", "Cancelled");
        dialog.setTitle("Update Order Status");
        dialog.setHeaderText("Select new status");
        dialog.setContentText("New status:");

        dialog.showAndWait().ifPresent(status -> {
            // TODO: Update order status on server
            showSuccess("Order status updated to: " + status);
        });
    }

    @FXML
    void printOrder() {
        // TODO: Generate and print order receipt
        showSuccess("Print function will be implemented");
    }

    @FXML
    void goToDashboard() {
        loadScene("WorkerDashboard.fxml", dashboardBtn);
    }

    @FXML
    void goToHome() {
        loadScene("Catalog.fxml", homeBtn);
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
            showError("Error loading page: " + e.getMessage());
        }
    }

    private void showSuccess(String message) {
        successMessage.setText("✓ " + message);
        successMessage.setVisible(true);
        errorMessage.setVisible(false);
    }

    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
        successMessage.setVisible(false);
    }
}
