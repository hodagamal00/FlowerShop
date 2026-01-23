package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controller for Network Dashboard - Chain Manager view
 * Displays overview of all branches with performance metrics and charts
 * Requires Chain Manager privilege (level 4)
 */
public class NetworkDashboardController {

    // Navigation
    @FXML private Button backButton;
    
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
    
    // Quick Actions
    @FXML private Button globalSettingsButton;
    @FXML private Button networkPromotionsButton;
    @FXML private Button roleManagementButton;
    
    // Data
    private ObservableList<BranchData> branchesList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        // Check privileges - Chain Manager level required (4)
        if (!AccessGuard.requireMinPrivilege(4)) {
            return;
        }
        
        setupChartPeriodCombo();
        setupBranchesTable();
        loadNetworkData();
        updateStatistics();
        updateChart();
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
    private void loadNetworkData() {
        // TODO: Send request to server to get all branches data
        // Message: new GetNetworkData()
        // SimpleClient.getClient().sendToServer(message);
        
        // For now, generate sample data
        generateSampleBranches();
    }
    
    /**
     * Generate sample branch data
     */
    private void generateSampleBranches() {
        branchesList.clear();
        
        String[] branchNames = {
            "Main Street Branch",
            "Downtown Branch",
            "Shopping Mall Branch",
            "Airport Branch",
            "University Branch"
        };
        
        String[] locations = {
            "Tel Aviv",
            "Jerusalem",
            "Haifa",
            "Be'er Sheva",
            "Netanya"
        };
        
        String[] managers = {
            "Sarah Cohen",
            "David Levi",
            "Rachel Mizrahi",
            "Michael Green",
            "Tamar Ben-David"
        };
        
        Random random = new Random();
        
        for (int i = 0; i < 5; i++) {
            int orders = 80 + random.nextInt(120);
            double revenue = 12000 + random.nextDouble() * 18000;
            String status = i < 4 ? "Active" : "Active";
            
            branchesList.add(new BranchData(
                i + 1,
                branchNames[i],
                locations[i],
                managers[i],
                orders,
                String.format("₪%.2f", revenue),
                status,
                revenue
            ));
        }
    }
    
    /**
     * Update network statistics
     */
    private void updateStatistics() {
        totalBranchesLabel.setText(String.valueOf(branchesList.size()));
        
        double totalRevenue = branchesList.stream()
            .mapToDouble(BranchData::getRevenueValue)
            .sum();
        totalRevenueLabel.setText(String.format("₪%.2f", totalRevenue));
        
        int totalOrders = branchesList.stream()
            .mapToInt(BranchData::getOrdersCount)
            .sum();
        totalOrdersLabel.setText(String.valueOf(totalOrders));
        
        // TODO: Get actual active users count from server
        activeUsersLabel.setText(String.valueOf(totalOrders * 3)); // Sample calculation
    }
    
    /**
     * Update comparison chart
     */
    private void updateChart() {
        branchComparisonChart.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");
        
        for (BranchData branch : branchesList) {
            // BranchData does not define getBranchName(); use getName() to retrieve
            // the branch's display name.
            series.getData().add(new XYChart.Data<>(branch.getName(), branch.getRevenueValue()));
        }
        
        branchComparisonChart.getData().add(series);
    }
    
    /**
     * Handle chart period change
     */
    @FXML
    private void handlePeriodChange() {
        // TODO: Reload data based on selected period
        String period = chartPeriodCombo.getValue();
        // Reload data from server for the selected period
        updateChart();
    }
    
    /**
     * Handle refresh button
     */
    @FXML
    private void handleRefresh() {
        loadNetworkData();
        updateStatistics();
        updateChart();
        showInfo("Network data refreshed successfully.");
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
        try {
            App.setRoot("CrossBranchReports");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load Cross-Branch Reports page.");
        }
    }
    
    /**
     * Navigate to Global Settings
     */
    @FXML
    private void handleGlobalSettings() {
        try {
            App.setRoot("GlobalSettings");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load Global Settings page.");
        }
    }
    
    /**
     * Navigate to Network Promotions
     */
    @FXML
    private void handleNetworkPromotions() {
        try {
            App.setRoot("NetworkPromotions");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load Network Promotions page.");
        }
    }
    
    /**
     * Navigate to Role Management
     */
    @FXML
    private void handleRoleManagement() {
        try {
            App.setRoot("RoleManagement");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load Role Management page.");
        }
    }
    
    /**
     * Navigate back to catalog
     */
    @FXML
    private void handleBackToCatalog() {
        NavigationService.getInstance().navigate("Catalog");
    }
    
    /**
     * Show error alert
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show info alert
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
