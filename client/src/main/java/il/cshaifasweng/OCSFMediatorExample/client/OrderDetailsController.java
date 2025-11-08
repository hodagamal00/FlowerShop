package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailsController {

    @FXML private Button backBtn;
    @FXML private Text orderIdText;
    @FXML private Label statusBadge;
    @FXML private Label orderDateLabel;
    @FXML private Label deliveryTimeLabel;
    @FXML private Label orderTypeLabel;
    @FXML private TableView<?> itemsTable;
    @FXML private TableColumn<?, ?> productNameCol;
    @FXML private TableColumn<?, ?> quantityCol;
    @FXML private TableColumn<?, ?> priceCol;
    @FXML private TableColumn<?, ?> subtotalCol;
    @FXML private Text deliveryInfoTitle;
    @FXML private VBox deliveryInfoContainer;
    @FXML private Label deliveryAddressLabel;
    @FXML private Label deliveryFeeLabel;
    @FXML private VBox pickupInfoContainer;
    @FXML private Label pickupLocationLabel;
    @FXML private Label subtotalLabel;
    @FXML private Label feeLabel;
    @FXML private HBox discountRow;
    @FXML private Label discountLabel;
    @FXML private Text totalLabel;
    @FXML private Label cancellationPolicyLabel;
    @FXML private Text refundAmountText;
    @FXML private Button cancelOrderBtn;
    @FXML private Button trackOrderBtn;
    @FXML private Label successMessage;
    @FXML private Label errorMessage;

    private static Order selectedOrder;
    private Order currentOrder;

    @FXML
    void initialize() {
        if (selectedOrder != null) {
            loadOrderDetails(selectedOrder);
        }
    }

    /**
     * Static method to set the order to display
     */
    public static void setOrder(Order order) {
        selectedOrder = order;
    }

    /**
     * Load and display order details
     */
    private void loadOrderDetails(Order order) {
        this.currentOrder = order;

        // Order header
        orderIdText.setText(String.valueOf(order.getId()));
        
        // Status badge
        String status = order.getStatus() != null ? order.getStatus() : "Pending";
        statusBadge.setText(status);
        updateStatusBadgeStyle(status);

        // Dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, hh:mm a");
        orderDateLabel.setText(dateFormat.format(new Date()));
        deliveryTimeLabel.setText(order.getDelivery_time() != null ? 
            order.getDelivery_time().toString() : "TBD");

        // Order type
        boolean isDelivery = order.getAddress() != null && !order.getAddress().isEmpty();
        orderTypeLabel.setText(isDelivery ? "Delivery" : "Pickup");

        // Delivery/Pickup information
        if (isDelivery) {
            deliveryInfoTitle.setText("Delivery Information");
            deliveryInfoContainer.setVisible(true);
            deliveryInfoContainer.setManaged(true);
            pickupInfoContainer.setVisible(false);
            pickupInfoContainer.setManaged(false);
            
            deliveryAddressLabel.setText(order.getAddress());
            deliveryFeeLabel.setText("$9.99");
            feeLabel.setText("$9.99");
        } else {
            deliveryInfoTitle.setText("Pickup Information");
            deliveryInfoContainer.setVisible(false);
            deliveryInfoContainer.setManaged(false);
            pickupInfoContainer.setVisible(true);
            pickupInfoContainer.setManaged(true);
            
            pickupLocationLabel.setText("FlowerShop - Branch #" + order.getShop());
            feeLabel.setText("$0.00");
        }

        // Price summary
        double orderPrice = order.getPrice();
        double deliveryFee = isDelivery ? 9.99 : 0.0;
        double subtotal = orderPrice - deliveryFee;
        
        subtotalLabel.setText(String.format("$%.2f", subtotal));
        totalLabel.setText(String.format("$%.2f", orderPrice));

        // Calculate potential refund
        calculateRefund(order);

        // Disable cancel button if order is already cancelled or completed
        if ("Cancelled".equals(status) || "Delivered".equals(status) || "Completed".equals(status)) {
            cancelOrderBtn.setDisable(true);
        }
    }

    /**
     * Update status badge styling based on order status
     */
    private void updateStatusBadgeStyle(String status) {
        String style = "-fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 5; -fx-font-size: 16px; -fx-font-weight: bold; ";
        
        switch (status.toLowerCase()) {
            case "pending":
                style += "-fx-background-color: #ffb74d;"; // Orange
                break;
            case "confirmed":
            case "preparing":
                style += "-fx-background-color: #64b5f6;"; // Blue
                break;
            case "in delivery":
            case "out for delivery":
                style += "-fx-background-color: #ba68c8;"; // Purple
                break;
            case "completed":
            case "delivered":
                style += "-fx-background-color: #81c784;"; // Green
                break;
            case "cancelled":
                style += "-fx-background-color: #e57373;"; // Red
                break;
            default:
                style += "-fx-background-color: #9e9e9e;"; // Gray
        }
        
        statusBadge.setStyle(style);
    }

    /**
     * Calculate and display potential refund
     */
    private void calculateRefund(Order order) {
        // Determine current date/time
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        int cancelDay = now.getDayOfMonth();
        int cancelMonth = now.getMonthValue();
        int cancelYear = now.getYear();
        int cancelHour = now.getHour();
        int cancelMin = now.getMinute();
        // Use Order.calculateRefund to determine refund percentage and set
        // refund status/amount internally.  The method returns 1.0, 0.5 or 0.0.
        double refundFactor = order.calculateRefund(cancelDay, cancelMonth, cancelYear, cancelHour, cancelMin);
        double refundAmount = order.getPrice() * refundFactor;
        double refundPercent = refundFactor * 100.0;
        refundAmountText.setText(String.format("$%.2f (%.0f%%)", refundAmount, refundPercent));
    }

    @FXML
    void cancelOrder() {
        // Confirm cancellation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Order");
        alert.setHeaderText("Are you sure you want to cancel this order?");
        // Calculate refund for display
        // We call calculateRefund() with current time to determine the actual refund.
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        double refundFactor = currentOrder.calculateRefund(
            now.getDayOfMonth(), now.getMonthValue(), now.getYear(), now.getHour(), now.getMinute());
        double refundAmount = currentOrder.getPrice() * refundFactor;
        String refundText = String.format("Your refund will be: $%.2f (%d%%)\nThis action cannot be undone.",
            refundAmount, (int)(refundFactor * 100));
        alert.setContentText(refundText);
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            // TODO: Send cancel request to server
            // For now, just update local status
            
            statusBadge.setText("Cancelled");
            updateStatusBadgeStyle("Cancelled");
            cancelOrderBtn.setDisable(true);
            
            showSuccess("Order cancelled successfully. Refund will be processed.");
        }
    }

    @FXML
    void trackOrder() {
        // Show tracking information
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Tracking");
        alert.setHeaderText("Order #" + currentOrder.getId());
        
        String trackingInfo = "Order Status: " + (currentOrder.getStatus() != null ? currentOrder.getStatus() : "Pending") + "\n";
        trackingInfo += "Estimated Delivery: " + (currentOrder.getDelivery_time() != null ? currentOrder.getDelivery_time() : "TBD") + "\n";
        trackingInfo += "\nTracking updates will be sent to your email.";
        
        alert.setContentText(trackingInfo);
        alert.showAndWait();
    }

    @FXML
    void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("myorders.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading my orders page: " + e.getMessage());
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
