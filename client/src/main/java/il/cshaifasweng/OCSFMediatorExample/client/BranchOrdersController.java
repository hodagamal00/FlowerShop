package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.getAllOrdersMessage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class BranchOrdersController {

    @FXML private Button dashboardBtn;
    @FXML private Button homeBtn;
    @FXML private ComboBox<String> statusFilterCombo;
    @FXML private TextField searchField;
    @FXML private Button searchBtn;
    @FXML private Button refreshBtn;
    @FXML private Label orderCountLabel;
    @FXML private TableView<OrderRow> ordersTable;
    @FXML private TableColumn<OrderRow, Integer> orderIdCol;
    @FXML private TableColumn<OrderRow, String> customerCol;
    @FXML private TableColumn<OrderRow, String> orderDateCol;
    @FXML private TableColumn<OrderRow, String> deliveryTimeCol;
    @FXML private TableColumn<OrderRow, String> typeCol;
    @FXML private TableColumn<OrderRow, String> totalCol;
    @FXML private TableColumn<OrderRow, String> statusCol;
    @FXML private TableColumn<OrderRow, Void> actionsCol;
    @FXML private Button viewDetailsBtn;
    @FXML private Button updateStatusBtn;
    @FXML private Button printBtn;
    @FXML private Label successMessage;
    @FXML private Label errorMessage;

    private final ObservableList<OrderRow> allOrders = FXCollections.observableArrayList();
    private final ObservableList<OrderRow> filteredOrders = FXCollections.observableArrayList();
    private int currentBranchId = -1;

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        setupStatusFilter();
        setupTable();
        resolveCurrentBranch();
        requestOrders();
    }

    private void setupStatusFilter() {
        statusFilterCombo.setItems(FXCollections.observableArrayList(
            "All", "Pending", "Confirmed", "Preparing", "Ready", "In Delivery", "Completed", "Cancelled"
        ));
        statusFilterCombo.setValue("All");
        statusFilterCombo.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
    }

    private void setupTable() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        deliveryTimeCol.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        addActionsColumn();

        ordersTable.setItems(filteredOrders);
    }

    private void addActionsColumn() {
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("View");
            private final HBox container = new HBox(viewButton);

            {
                container.setSpacing(6);
                viewButton.setOnAction(event -> {
                    OrderRow row = getTableView().getItems().get(getIndex());
                    ordersTable.getSelectionModel().select(row);
                    viewOrderDetails();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void resolveCurrentBranch() {
        Account account = SimpleClient.getAccount();
        if (account != null) {
            currentBranchId = account.getBelongShop();
        }
    }

    private void requestOrders() {
        try {
            SimpleClient.getClient().sendToServer(new getAllOrdersMessage());
        } catch (IOException e) {
            showError("Unable to load orders. Please try again.");
        }
    }

    @FXML
    void searchOrders() {
        applyFilters();
        showSuccess("Search completed");
    }

    @FXML
    void refreshOrders() {
        requestOrders();
        showSuccess("Orders refreshed");
    }

    @FXML
    void viewOrderDetails() {
        OrderRow selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an order first");
            return;
        }

        OrderDetailsController.setOrder(selected.getOrder(), selected.getStatus());
        NavigationService.getInstance().navigate("OrderDetails");
    }

    @FXML
    void updateOrderStatus() {
        OrderRow selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an order first");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(selected.getStatus(),
            "Pending", "Confirmed", "Preparing", "Ready", "In Delivery", "Completed", "Cancelled");
        dialog.setTitle("Update Order Status");
        dialog.setHeaderText("Select new status");
        dialog.setContentText("New status:");

        dialog.showAndWait().ifPresent(status -> {
            updateOrderStatus(selected, status);
            applyFilters();
            showSuccess("Order status updated to: " + status);
        });
    }

    @FXML
    void printOrder() {
        OrderRow selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an order first");
            return;
        }

        Order order = selected.getOrder();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Receipt");
        alert.setHeaderText("Order #" + order.getOrderID());
        alert.setContentText(buildReceipt(order));
        alert.showAndWait();
        showSuccess("Receipt preview generated");
    }

    @FXML
    void goToDashboard() {
        NavigationService.getInstance().navigate("WorkerDashboard");
    }

    @FXML
    void goToHome() {
        NavigationService.getInstance().navigate("Catalog");
    }

    @Subscribe
    public void passOrders(PassOrdersFromServer passOrders) {
        List<Order> receivedOrders = passOrders.getRecievedOrders();
        List<Order> branchOrders = filterOrdersForBranch(receivedOrders);
        allOrders.setAll(branchOrders.stream()
            .map(this::buildRow)
            .collect(Collectors.toList()));
        applyFilters();
    }

    private List<Order> filterOrdersForBranch(List<Order> orders) {
        if (currentBranchId <= 0) {
            return orders;
        }
        return orders.stream()
            .filter(order -> order.getShopID() == currentBranchId)
            .collect(Collectors.toList());
    }

    private OrderRow buildRow(Order order) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime orderDate = safeDate(order.getOrderDate());
        LocalDateTime deliveryDate = safeDate(order.getDelivery_time());

        String status = deriveInitialStatus(order);
        String type = order.isPickUp() ? "Pickup" : "Delivery";
        String customer = "Account #" + order.getAccountID();
        String orderDateText = orderDate != null ? orderDate.format(dateFormatter) : "-";
        String deliveryText = deliveryDate != null ? deliveryDate.format(timeFormatter) : "-";
        String totalText = String.format(Locale.US, "$%.2f", (double) order.getTotalPrice());
        return new OrderRow(order, order.getOrderID(), customer, orderDateText, deliveryText, type, totalText, status);
    }

    private LocalDateTime safeDate(LocalDateTime dateTime) {
        return dateTime;
    }

    private String deriveInitialStatus(Order order) {
        if (order.isCancelled()) {
            return "Cancelled";
        }
        if (order.isDelivered()) {
            return "Completed";
        }
        return "Pending";
    }

    private void applyFilters() {
        String searchTerm = Optional.ofNullable(searchField.getText()).orElse("").trim().toLowerCase(Locale.US);
        String statusFilter = Optional.ofNullable(statusFilterCombo.getValue()).orElse("All");

        List<OrderRow> filtered = allOrders.stream()
            .filter(order -> matchesSearch(order, searchTerm))
            .filter(order -> matchesStatus(order, statusFilter))
            .collect(Collectors.toList());

        filteredOrders.setAll(filtered);
        orderCountLabel.setText(String.format("(%d total)", filteredOrders.size()));
    }

    private boolean matchesSearch(OrderRow order, String searchTerm) {
        if (searchTerm.isBlank()) {
            return true;
        }

        return order.getCustomer().toLowerCase(Locale.US).contains(searchTerm)
            || String.valueOf(order.getOrderId()).contains(searchTerm);
    }

    private boolean matchesStatus(OrderRow order, String statusFilter) {
        if (statusFilter == null || "All".equalsIgnoreCase(statusFilter)) {
            return true;
        }
        return Objects.equals(order.getStatus(), statusFilter);
    }

    private void updateOrderStatus(OrderRow row, String status) {
        row.setStatus(status);
        Order order = row.getOrder();
        if ("Cancelled".equalsIgnoreCase(status)) {
            order.setCancelled(true);
            order.setDelivered(false);
        } else if ("Completed".equalsIgnoreCase(status) || "Delivered".equalsIgnoreCase(status)) {
            order.setDelivered(true);
            order.setCancelled(false);
            sendDeliveryUpdate(order);
        } else {
            order.setCancelled(false);
            order.setDelivered(false);
        }
    }

    private void sendDeliveryUpdate(Order order) {
        try {
            SimpleClient.getClient().sendToServer(order);
        } catch (IOException e) {
            showError("Order update failed to send to server.");
        }
    }

    private String buildReceipt(Order order) {
        StringBuilder builder = new StringBuilder();
        builder.append("Customer: Account #").append(order.getAccountID()).append("\n");
        builder.append("Order Date: ").append(order.getOrderDay()).append("/")
            .append(order.getOrderMonth()).append("/").append(order.getOrderYear()).append("\n");
        builder.append("Delivery/Pickup: ")
            .append(order.isPickUp() ? "Pickup" : "Delivery").append("\n");
        if (!order.isPickUp()) {
            builder.append("Address: ").append(order.getDeliveredAddress()).append("\n");
        }
        builder.append("Total: $").append(order.getTotalPrice()).append("\n");
        builder.append("Status: ").append(order.isCancelled() ? "Cancelled" : (order.isDelivered() ? "Completed" : "Pending"));
        return builder.toString();
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

    public static class OrderRow {
        private final Order order;
        private final SimpleIntegerProperty orderId;
        private final SimpleStringProperty customer;
        private final SimpleStringProperty orderDate;
        private final SimpleStringProperty deliveryTime;
        private final SimpleStringProperty type;
        private final SimpleStringProperty total;
        private final SimpleStringProperty status;

        public OrderRow(Order order, int orderId, String customer, String orderDate,
                        String deliveryTime, String type, String total, String status) {
            this.order = order;
            this.orderId = new SimpleIntegerProperty(orderId);
            this.customer = new SimpleStringProperty(customer);
            this.orderDate = new SimpleStringProperty(orderDate);
            this.deliveryTime = new SimpleStringProperty(deliveryTime);
            this.type = new SimpleStringProperty(type);
            this.total = new SimpleStringProperty(total);
            this.status = new SimpleStringProperty(status);
        }

        public Order getOrder() {
            return order;
        }

        public int getOrderId() {
            return orderId.get();
        }

        public String getCustomer() {
            return customer.get();
        }

        public String getOrderDate() {
            return orderDate.get();
        }

        public String getDeliveryTime() {
            return deliveryTime.get();
        }

        public String getType() {
            return type.get();
        }

        public String getTotal() {
            return total.get();
        }

        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }
    }
}
