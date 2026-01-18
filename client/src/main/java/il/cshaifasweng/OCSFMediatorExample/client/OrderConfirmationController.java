package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderConfirmationController {

    @FXML private Button backToHomeBtn;
    @FXML private Button viewOrdersBtn;
    @FXML private Text orderNumberText;
    @FXML private Label orderDateLabel;
    @FXML private Text totalAmountText;
    @FXML private Label paymentMethodLabel;
    @FXML private Text deliveryTypeTitle;
    @FXML private VBox deliveryAddressContainer;
    @FXML private Label deliveryAddressLabel;
    @FXML private Label estimatedDeliveryLabel;
    @FXML private Label deliveryFeeLabel;
    @FXML private VBox pickupInfoContainer;
    @FXML private Label pickupLocationLabel;
    @FXML private Label pickupTimeLabel;
    @FXML private Button viewOrderDetailsBtn;
    @FXML private Button continueshoppingBtn;

    private static Order confirmedOrder;
    private static boolean isDelivery = true;

    @FXML
    void initialize() {
        // Load order details if an order was confirmed
        if (confirmedOrder != null) {
            loadOrderConfirmation(confirmedOrder, isDelivery);
        } else {
            // Display sample data for testing
            loadSampleData();
        }
    }

    /**
     * Static method to set the confirmed order
     * Call this before loading the OrderConfirmation.fxml scene
     */
    public static void setOrder(Order order, boolean delivery) {
        confirmedOrder = order;
        isDelivery = delivery;
    }

    /**
     * Load and display order confirmation details
     */
    private void loadOrderConfirmation(Order order, boolean delivery) {
        // Order basic info
        orderNumberText.setText("#" + order.getId());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        orderDateLabel.setText(dateFormat.format(new Date()));
        
        totalAmountText.setText(String.format("$%.2f", order.getPrice()));
        
        // Payment method (get last 4 digits of credit card from account)
        if (SimpleClient.getAccount() != null) {
            long cardNumber = SimpleClient.getAccount().getCreditCardNumber();
            String lastFour = String.valueOf(cardNumber).substring(String.valueOf(cardNumber).length() - 4);
            paymentMethodLabel.setText("Credit Card (****" + lastFour + ")");
        }

        // Configure delivery/pickup display
        if (delivery) {
            deliveryTypeTitle.setText("Delivery Information");
            deliveryAddressContainer.setVisible(true);
            deliveryAddressContainer.setManaged(true);
            pickupInfoContainer.setVisible(false);
            pickupInfoContainer.setManaged(false);
            
            // Set delivery details
            if (SimpleClient.getAccount() != null) {
                deliveryAddressLabel.setText(SimpleClient.getAccount().getAddress());
            }
            
            // Set estimated delivery time (order delivery time if available)
            if (order.getDelivery_time() != null) {
                estimatedDeliveryLabel.setText(order.getDelivery_time().toString());
            } else {
                // Calculate estimated delivery (2 days from now, example)
                SimpleDateFormat deliveryFormat = new SimpleDateFormat("yyyy-MM-dd, hh:mm a");
                Date estimatedDate = new Date(System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000));
                estimatedDeliveryLabel.setText(deliveryFormat.format(estimatedDate));
            }
            
            deliveryFeeLabel.setText("$9.99"); // Fixed delivery fee
        } else {
            deliveryTypeTitle.setText("Pickup Information");
            deliveryAddressContainer.setVisible(false);
            deliveryAddressContainer.setManaged(false);
            pickupInfoContainer.setVisible(true);
            pickupInfoContainer.setManaged(true);
            
            // Set pickup details
            // Assuming belongShop is the branch ID
            if (SimpleClient.getAccount() != null) {
                int branchId = SimpleClient.getAccount().getBelongShop();
                pickupLocationLabel.setText("FlowerShop - Branch #" + branchId);
            } else {
                pickupLocationLabel.setText("FlowerShop - Main Branch");
            }
            
            // Set pickup time
            if (order.getDelivery_time() != null) {
                pickupTimeLabel.setText(order.getDelivery_time().toString());
            } else {
                // Calculate ready time (next day, example)
                SimpleDateFormat pickupFormat = new SimpleDateFormat("yyyy-MM-dd, hh:mm a");
                Date pickupDate = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
                pickupTimeLabel.setText(pickupFormat.format(pickupDate));
            }
        }
    }

    /**
     * Load sample data for testing
     */
    private void loadSampleData() {
        orderNumberText.setText("#12345");
        orderDateLabel.setText("2025-11-08 11:30 AM");
        totalAmountText.setText("$149.97");
        paymentMethodLabel.setText("Credit Card (****1234)");
        
        // Show delivery by default
        deliveryTypeTitle.setText("Delivery Information");
        deliveryAddressContainer.setVisible(true);
        deliveryAddressContainer.setManaged(true);
        pickupInfoContainer.setVisible(false);
        pickupInfoContainer.setManaged(false);
        
        deliveryAddressLabel.setText("123 Main Street, City, State 12345");
        estimatedDeliveryLabel.setText("2025-11-10, 2:00 PM - 4:00 PM");
        deliveryFeeLabel.setText("$9.99");
    }

    @FXML
    void viewOrderDetails() {
        // Navigate to order details page
        if (confirmedOrder != null) {
            OrderDetailsController.setOrder(confirmedOrder, null);
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderDetails.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) viewOrderDetailsBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading order details page: " + e.getMessage());
        }
    }

    @FXML
    void goToMyOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("myorders.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) viewOrdersBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading my orders page: " + e.getMessage());
        }
    }

    @FXML
    void goToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Catalog.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backToHomeBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading home page: " + e.getMessage());
        }
    }
}
