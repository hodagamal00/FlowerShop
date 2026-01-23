package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderConfirmationController {

    @FXML private Button viewOrdersBtn;
    @FXML private Text orderNumberText;
    @FXML private Label orderDateLabel;
    @FXML private Text totalAmountText;
    @FXML private Label paymentMethodLabel;
    @FXML private Label paymentStatusLabel;
    @FXML private VBox orderItemsContainer;
    @FXML private Label subtotalLabel;
    @FXML private Label deliveryFeeSummaryLabel;
    @FXML private HBox discountRow;
    @FXML private Label discountLabel;
    @FXML private Text orderTotalSummaryText;
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
        orderNumberText.setText(order.getId() > 0 ? "#" + order.getId() : "Pending");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        java.time.LocalDateTime orderDate = null;
        try {
            orderDate = order.getOrderDate();
        } catch (Exception e) {
            orderDate = null;
        }
        if (orderDate != null) {
            orderDateLabel.setText(dateFormat.format(java.sql.Timestamp.valueOf(orderDate)));
        } else {
            orderDateLabel.setText(dateFormat.format(new Date()));
        }
        
        totalAmountText.setText(formatCurrency(order.getPrice()));
        
        // Payment method (get last 4 digits of credit card from account)
        long cardNumber = order.getCreditCardNumber();
        if (cardNumber <= 0 && SimpleClient.getAccount() != null) {
            cardNumber = SimpleClient.getAccount().getCreditCardNumber();
        }
        if (cardNumber > 0) {
            String lastFour = String.valueOf(cardNumber).substring(String.valueOf(cardNumber).length() - 4);
            String methodLabel = order.getPaymentMethod() != null ? order.getPaymentMethod() : "Credit Card";
            paymentMethodLabel.setText(formatPaymentMethod(methodLabel, lastFour));
        } else {
            paymentMethodLabel.setText(order.getPaymentMethod() != null ? order.getPaymentMethod() : "Credit Card");
        }
        paymentStatusLabel.setText("Payment recorded at placement");

        renderOrderSummary(order, delivery);

        // Configure delivery/pickup display
        if (delivery) {
            deliveryTypeTitle.setText("Delivery Information");
            deliveryAddressContainer.setVisible(true);
            deliveryAddressContainer.setManaged(true);
            pickupInfoContainer.setVisible(false);
            pickupInfoContainer.setManaged(false);
            
            // Set delivery details
            if (order.getDeliveredAddress() != null && !order.getDeliveredAddress().isBlank()) {
                deliveryAddressLabel.setText(order.getDeliveredAddress());
            } else if (SimpleClient.getAccount() != null) {
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
            
            deliveryFeeLabel.setText(formatCurrency(PricingService.roundCurrency(order.getDeliveryFee())));
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
        paymentStatusLabel.setText("Payment recorded at placement");

        orderItemsContainer.getChildren().clear();
        orderItemsContainer.getChildren().add(buildItemRow("Red Roses Bouquet", 1, 89.99));
        orderItemsContainer.getChildren().add(buildItemRow("Greeting Card", 1, 9.99));
        subtotalLabel.setText("$99.98");
        deliveryFeeSummaryLabel.setText("$9.99");
        discountRow.setVisible(true);
        discountRow.setManaged(true);
        discountLabel.setText("-$10.00");
        orderTotalSummaryText.setText("$99.97");
        
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

    private void renderOrderSummary(Order order, boolean delivery) {
        orderItemsContainer.getChildren().clear();
        List<OrderProductParser.OrderItem> items = OrderProductParser.parseItems(
                order.getProducts(), ProductCatalogCache.snapshot());
        List<Double> itemPrices = new ArrayList<>();
        for (OrderProductParser.OrderItem item : items) {
            itemPrices.add(item.getLineTotal());
            orderItemsContainer.getChildren().add(buildItemRow(item.getName(), item.getQuantity(), item.getUnitPrice()));
        }

        double subtotal = PricingService.calculateSubtotal(itemPrices);
        double deliveryFee = delivery ? PricingService.roundCurrency(order.getDeliveryFee()) : 0.0;
        double total = PricingService.roundCurrency(order.getPrice());
        double discount = PricingService.calculateDiscountAmount(subtotal, deliveryFee, total);

        subtotalLabel.setText(formatCurrency(subtotal));
        deliveryFeeSummaryLabel.setText(formatCurrency(deliveryFee));
        if (discount > 0.01) {
            discountRow.setVisible(true);
            discountRow.setManaged(true);
            discountLabel.setText(String.format(Locale.US, "-%.2f₪", discount));
        } else {
            discountRow.setVisible(false);
            discountRow.setManaged(false);
        }
        orderTotalSummaryText.setText(formatCurrency(total));
    }

    private HBox buildItemRow(String name, int quantity, double price) {
        Label nameLabel = new Label(String.format("%s x%d", name, quantity));
        nameLabel.setPrefWidth(420);
        Label priceLabel = new Label(formatCurrency(price));
        HBox row = new HBox(10, nameLabel, priceLabel);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return row;
    }

    private String formatPaymentMethod(String method, String lastFour) {
        String normalized = method.replace('_', ' ').toLowerCase(Locale.US);
        String[] words = normalized.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (word.isBlank()) {
                continue;
            }
            builder.append(Character.toUpperCase(word.charAt(0)))
                .append(word.substring(1))
                .append(' ');
        }
        return builder.toString().trim() + " (****" + lastFour + ")";
    }

    private double parsePrice(String rawValue) {
        String cleaned = rawValue.replaceAll("[^0-9.]", "");
        if (cleaned.isBlank()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String formatCurrency(double value) {
        return String.format(Locale.US, "%.2f₪", PricingService.roundCurrency(value));
    }

    @FXML
    void viewOrderDetails() {
        // Navigate to order details page
        if (confirmedOrder != null) {
            OrderDetailsController.setOrder(confirmedOrder, null);
        }
        
        NavigationService.getInstance().navigate("OrderDetails");
    }

    @FXML
    void goToMyOrders() {
        NavigationService.getInstance().navigate("myorders");
    }

    @FXML
    void goToHome() {
        NavigationService.getInstance().navigate("Catalog");
    }
}
