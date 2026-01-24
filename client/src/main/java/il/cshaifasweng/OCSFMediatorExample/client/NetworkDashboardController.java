package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import il.cshaifasweng.OCSFMediatorExample.entities.BranchSettings;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.ReportDataRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.ReportDataResponse;
import javafx.scene.paint.Color;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Controller for Network Dashboard - Chain Manager view
 * Displays overview of all branches with performance metrics and charts
 * Requires Chain Manager privilege (level 4)
 */
public class NetworkDashboardController {

    // Navigation
    
    // Network Statistics
    @FXML private Label totalBranchesLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label activeUsersLabel;
    
    // Chart
    @FXML private ComboBox<String> chartPeriodCombo;
    @FXML private BarChart<String, Number> branchComparisonChart;
    @FXML private CategoryAxis branchXAxis;
    @FXML private NumberAxis revenueYAxis;
    
    // Branches Table
    @FXML private TableView<BranchData> branchesTable;
    @FXML private TableColumn<BranchData, Integer> branchIdCol;
    @FXML private TableColumn<BranchData, String> branchNameCol;
    @FXML private TableColumn<BranchData, String> branchLocationCol;
    @FXML private TableColumn<BranchData, String> branchManagerCol;
    @FXML private TableColumn<BranchData, Integer> branchOrdersCol;
    @FXML private TableColumn<BranchData, String> branchRevenueCol;
    @FXML private TableColumn<BranchData, String> branchStatusCol;
    @FXML private TableColumn<BranchData, Void> branchActionsCol;
    @FXML private Button refreshButton;
    @FXML private Button viewReportsButton;
    @FXML private Label emptyStateLabel;
    @FXML private Label dashboardStatusLabel;
    
    // Quick Actions
    @FXML private Button globalSettingsButton;
    @FXML private Button networkPromotionsButton;
    @FXML private Button roleManagementButton;
    
    // Data
    private final ObservableList<BranchData> branchesList = FXCollections.observableArrayList();
    private final Map<Integer, BranchMetrics> branchMetrics = new LinkedHashMap<>();
    private List<BranchSettings> branchSettings = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private String currentRequestId;
    private LocalDate rangeStart;
    private LocalDate rangeEnd;
    private boolean refreshRequested;
    
    @FXML
    public void initialize() {
        // Check privileges - Chain Manager level required (4)
        if (!AccessGuard.requireMinPrivilege(4)) {
            return;
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setupChartPeriodCombo();
        setupBranchesTable();
        requestNetworkData();
    }
    
    /**
     * Setup chart period combo box
     */
    private void setupChartPeriodCombo() {
        ObservableList<String> periods = FXCollections.observableArrayList(
            "Last 7 Days",
            "Last 30 Days",
            "Last 90 Days",
            "This Year"
        );
        chartPeriodCombo.setItems(periods);
        chartPeriodCombo.setValue("Last 30 Days");
    }
    
    /**
     * Setup branches table columns
     */
    private void setupBranchesTable() {
        branchIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        branchNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        branchLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        branchManagerCol.setCellValueFactory(new PropertyValueFactory<>("manager"));
        branchOrdersCol.setCellValueFactory(new PropertyValueFactory<>("ordersCount"));
        branchRevenueCol.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        branchStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Add action buttons
        addActionButtons();
        
        branchesTable.setItems(branchesList);
    }
    
    /**
     * Add action buttons to branches table
     */
    private void addActionButtons() {
        branchActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            
            {
                viewBtn.setStyle("-fx-background-color: #ba68c8; -fx-text-fill: white; -fx-font-size: 12px;");
                viewBtn.setOnAction(event -> {
                    BranchData branch = getTableView().getItems().get(getIndex());
                    handleViewBranchDetails(branch);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewBtn);
            }
        });
    }
    
    /**
     * Load network data from server
     */
    private void requestNetworkData() {
        LocalDate today = LocalDate.now();
        String period = chartPeriodCombo != null ? chartPeriodCombo.getValue() : "Last 30 Days";
        if ("Last 7 Days".equals(period)) {
            rangeStart = today.minusDays(6);
            rangeEnd = today;
        } else if ("Last 90 Days".equals(period)) {
            rangeStart = today.minusDays(89);
            rangeEnd = today;
        } else if ("This Year".equals(period)) {
            rangeStart = LocalDate.of(today.getYear(), 1, 1);
            rangeEnd = today;
        } else {
            rangeStart = today.minusDays(29);
            rangeEnd = today;
        }

        currentRequestId = UUID.randomUUID().toString();
        ReportDataRequest request = new ReportDataRequest(currentRequestId, rangeStart, rangeEnd, 0, "DASHBOARD");
        request.setBranchIds(new ArrayList<>());
        try {
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            showError("Unable to load network data. Please try again.");
        }
    }
    
    /**
     * Update network statistics
     */
    private void updateStatistics() {
        int branchCount = !branchSettings.isEmpty() ? branchSettings.size() : branchMetrics.size();
        totalBranchesLabel.setText(String.valueOf(branchCount));

        double totalRevenue = branchMetrics.values().stream()
            .mapToDouble(metrics -> metrics.revenue)
            .sum();
        totalRevenueLabel.setText(String.format("₪%.2f", totalRevenue));

        int totalOrders = branchMetrics.values().stream()
            .mapToInt(metrics -> metrics.orders)
            .sum();
        totalOrdersLabel.setText(String.valueOf(totalOrders));

        Set<Integer> activeUsers = new HashSet<>();
        for (Order order : orders) {
            if (!order.isCancelled()) {
                activeUsers.add(order.getAccountID());
            }
        }
        activeUsersLabel.setText(String.valueOf(activeUsers.size()));
    }
    
    /**
     * Update comparison chart
     */
    private void updateChart() {
        branchComparisonChart.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");
        
        branchesList.stream()
            .sorted(Comparator.comparingInt(BranchData::getId))
            .forEach(branch -> series.getData().add(new XYChart.Data<>(branch.getName(), branch.getRevenueValue())));
        
        branchComparisonChart.getData().add(series);
    }
    
    /**
     * Handle chart period change
     */
    @FXML
    private void handlePeriodChange() {
        // TODO: Reload data based on selected period
        requestNetworkData();
    }
    
    /**
     * Handle refresh button
     */
    @FXML
    private void handleRefresh() {
        refreshRequested = true;
        requestNetworkData();
    }
    
    /**
     * View branch details
     */
    private void handleViewBranchDetails(BranchData branch) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Branch Details");
        // BranchData does not define getBranchName(); use getName() instead
        info.setHeaderText(branch.getName());
        info.setContentText(
            "Location: " + branch.getLocation() + "\n" +
            "Manager: " + branch.getManager() + "\n" +
            "Orders (30d): " + branch.getOrdersCount() + "\n" +
            "Revenue (30d): " + branch.getRevenue() + "\n" +
            "Status: " + branch.getStatus()
        );
        info.showAndWait();
    }
    
    /**
     * Navigate to Cross-Branch Reports
     */
    @FXML
    private void handleViewReports() {
        NavigationService.getInstance().navigate("CrossBranchReports");
    }
    
    /**
     * Navigate to Global Settings
     */
    @FXML
    private void handleGlobalSettings() {
        NavigationService.getInstance().navigate("GlobalSettings");
    }
    
    /**
     * Navigate to Network Promotions
     */
    @FXML
    private void handleNetworkPromotions() {
        NavigationService.getInstance().navigate("NetworkPromotions");
    }
    
    /**
     * Navigate to Role Management
     */
    @FXML
    private void handleRoleManagement() {
        NavigationService.getInstance().navigate("RoleManagement");
    }
    
    /**
     * Show error alert
     */
    private void showError(String message) {
        if (dashboardStatusLabel != null) {
            dashboardStatusLabel.setText(message);
            dashboardStatusLabel.setTextFill(Color.web("#e57373"));
            dashboardStatusLabel.setVisible(true);
            dashboardStatusLabel.setManaged(true);
        }
    }
    
    /**
     * Show info alert
     */
    private void showInfo(String message) {
        if (dashboardStatusLabel != null) {
            dashboardStatusLabel.setText(message);
            dashboardStatusLabel.setTextFill(Color.web("#7b1fa2"));
            dashboardStatusLabel.setVisible(true);
            dashboardStatusLabel.setManaged(true);
        }
    }

    @Subscribe
    public void onReportDataResponse(ReportDataResponse response) {
        if (response == null || response.getRequestId() == null || !response.getRequestId().equals(currentRequestId)) {
            return;
        }
        if (!response.isSuccess()) {
            Platform.runLater(() -> showError(response.getErrorMessage() != null ? response.getErrorMessage()
                : "Failed to load network data."));
            return;
        }
        branchSettings = response.getBranches() != null ? response.getBranches() : new ArrayList<>();
        orders = response.getOrders() != null ? response.getOrders() : new ArrayList<>();
        buildBranchMetrics();
        Platform.runLater(() -> {
            updateBranchTable();
            updateStatistics();
            updateChart();
            if (refreshRequested) {
                if (branchesList.isEmpty()) {
                    showError("No branch data available for the selected period.");
                } else {
                    showInfo("Network data refreshed successfully.");
                }
                refreshRequested = false;
            }
        });
    }

    private void buildBranchMetrics() {
        branchMetrics.clear();
        for (Order order : orders) {
            if (order.isCancelled()) {
                continue;
            }
            int branchId = order.getShopID();
            BranchMetrics metrics = branchMetrics.computeIfAbsent(branchId, id -> new BranchMetrics());
            metrics.orders += 1;
            metrics.revenue += order.getTotalPrice();
        }
    }

    private void updateBranchTable() {
        branchesList.clear();
        Map<Integer, BranchSettings> settingsById = new HashMap<>();
        for (BranchSettings settings : branchSettings) {
            settingsById.put(settings.getBranchId(), settings);
        }
        List<Integer> branchIds = new ArrayList<>(settingsById.keySet());
        if (branchIds.isEmpty()) {
            branchIds.addAll(branchMetrics.keySet());
        }
        branchIds.sort(Integer::compareTo);
        for (Integer branchId : branchIds) {
            BranchSettings settings = settingsById.get(branchId);
            BranchMetrics metrics = branchMetrics.getOrDefault(branchId, new BranchMetrics());
            String name = settings != null ? settings.getBranchName() : ("Branch " + branchId);
            String location = settings != null ? settings.getBranchAddress() : "—";
            String manager = settings != null ? settings.getManagerName() : "—";
            String status = settings != null && settings.isActive() ? "Active" : "Inactive";
            branchesList.add(new BranchData(
                branchId,
                name,
                location,
                manager,
                metrics.orders,
                String.format("₪%.2f", metrics.revenue),
                status,
                metrics.revenue
            ));
        }
        boolean empty = branchesList.isEmpty();
        if (emptyStateLabel != null) {
            emptyStateLabel.setVisible(empty);
            emptyStateLabel.setManaged(empty);
        }
    }

    private static class BranchMetrics {
        private int orders;
        private double revenue;
    }
    
    /**
     * Data model for branches table
     */
    public static class BranchData {
        private final int id;
        private final String name;
        private final String location;
        private final String manager;
        private final int ordersCount;
        private final String revenue;
        private final String status;
        private final double revenueValue;
        
        public BranchData(int id, String name, String location, String manager,
                         int ordersCount, String revenue, String status, double revenueValue) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.manager = manager;
            this.ordersCount = ordersCount;
            this.revenue = revenue;
            this.status = status;
            this.revenueValue = revenueValue;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public String getLocation() { return location; }
        public String getManager() { return manager; }
        public int getOrdersCount() { return ordersCount; }
        public String getRevenue() { return revenue; }
        public String getStatus() { return status; }
        public double getRevenueValue() { return revenueValue; }
    }
}
