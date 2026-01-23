package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderConfirmationController {

    @FXML private Button backToHomeBtn;
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
        orderItemsContainer.getChildren().add(buildItemRow("Red Roses Bouquet", 89.99));
        orderItemsContainer.getChildren().add(buildItemRow("Greeting Card", 9.99));
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
        String products = order.getProducts();
        List<Double> itemPrices = new ArrayList<>();
        Account account = SimpleClient.getAccount();
        if (products != null && !products.isBlank()) {
            if (products.contains(":")) {
                String[] tokens = products.split(",");
                for (String token : tokens) {
                    if (token == null || token.isBlank()) {
                        continue;
                    }
                    String[] parts = token.trim().split(":");
                    try {
                        int productId = Integer.parseInt(parts[0].trim());
                        int qty = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 1;
                        Product product = SimpleClient.findCachedProductById(productId);
                        String name = product != null ? product.getName() : "Product #" + productId;
                        double unitPrice = product != null
                                ? PricingService.calculateDisplayPrice(product, account)
                                : 0.0;
                        double linePrice = unitPrice * Math.max(1, qty);
                        itemPrices.add(linePrice);
                        orderItemsContainer.getChildren().add(buildItemRow(name + " x" + qty, linePrice));
                    } catch (NumberFormatException ignored) {
                        // fallback to legacy parsing below
                    }
                }
            }
            if (itemPrices.isEmpty()) {
                String[] tokens = products.split("%");
                for (String token : tokens) {
                    if (token == null || token.isBlank()) {
                        continue;
                    }
                    String[] parts = token.split(" - ");
                    String name = parts[0].trim();
                    double price = 0.0;
                    if (parts.length > 1) {
                        price = parsePrice(parts[1]);
                    }
                    itemPrices.add(price);
                    orderItemsContainer.getChildren().add(buildItemRow(name, price));
                }
            }
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

    private HBox buildItemRow(String name, double price) {
        Label nameLabel = new Label(name);
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
