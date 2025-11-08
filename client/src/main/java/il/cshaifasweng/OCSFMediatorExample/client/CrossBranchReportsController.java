package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Controller for Cross-Branch Reports - Chain Manager view
 * Displays comprehensive analytics across all branches with multiple chart types
 * Requires Chain Manager privilege (level 4)
 */
public class CrossBranchReportsController {

    // Navigation
    @FXML private Button backButton;
    
    // Filters
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private CheckBox selectAllBranchesCheckbox;
    @FXML private Button generateButton;
    @FXML private Button exportButton;
    
    // Revenue Comparison
    @FXML private BarChart<String, Number> revenueComparisonChart;
    @FXML private CategoryAxis revenueBranchAxis;
    @FXML private NumberAxis revenueAxis;
    @FXML private TableView<RevenueData> revenueTable;
    @FXML private TableColumn<RevenueData, String> revenueBranchCol;
    @FXML private TableColumn<RevenueData, Integer> revenueOrdersCol;
    @FXML private TableColumn<RevenueData, Double> revenueAmountCol;
    @FXML private TableColumn<RevenueData, Double> revenueAvgCol;
    @FXML private TableColumn<RevenueData, String> revenueGrowthCol;
    
    // Order Trends
    @FXML private LineChart<String, Number> orderTrendsChart;
    @FXML private CategoryAxis trendTimeAxis;
    @FXML private NumberAxis trendOrdersAxis;
    
    // Market Share
    @FXML private PieChart revenueSharePieChart;
    @FXML private PieChart orderSharePieChart;
    
    // KPIs
    @FXML private Label bestPerformerLabel;
    @FXML private Label avgRevenueLabel;
    @FXML private Label networkGrowthLabel;
    
    // Data
    private List<String> branchNames = Arrays.asList(
        "Main Street", "Downtown", "Shopping Mall", "Airport", "University"
    );
    
    @FXML
    public void initialize() {
        // Check privileges - Chain Manager level required (4)
        if (!checkChainManagerPrivileges()) {
            showAccessDenied();
            return;
        }
        
        setupTableColumns();
        setDefaultDates();
        
        // Auto-generate initial report
        Platform.runLater(this::handleGenerateReport);
    }
    
    /**
     * Check if user has chain manager privileges
     */
    private boolean checkChainManagerPrivileges() {
        // TODO: Get current user privilege from session
        // return SimpleClient.getCurrentUser().getPrivilege() >= 4;
        return true;
    }
    
    /**
     * Show access denied message
     */
    private void showAccessDenied() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access Denied");
        alert.setHeaderText("Insufficient Privileges");
        alert.setContentText("You need Chain Manager privileges to access this page.");
        alert.showAndWait();
        handleBackToDashboard();
    }
    
    /**
     * Setup table columns
     */
    private void setupTableColumns() {
        revenueBranchCol.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        revenueOrdersCol.setCellValueFactory(new PropertyValueFactory<>("totalOrders"));
        revenueAmountCol.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
        revenueAvgCol.setCellValueFactory(new PropertyValueFactory<>("avgOrderValue"));
        revenueGrowthCol.setCellValueFactory(new PropertyValueFactory<>("growthPercent"));
    }
    
    /**
     * Set default date range
     */
    private void setDefaultDates() {
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(30));
    }
    
    /**
     * Handle select all branches checkbox
     */
    @FXML
    private void handleSelectAllBranches() {
        // TODO: Implement branch selection logic
        // For now, just visual feedback
    }
    
    /**
     * Generate comprehensive cross-branch report
     */
    @FXML
    private void handleGenerateReport() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
        // Validate dates
        if (startDate == null || endDate == null) {
            showError("Please select both start and end dates.");
            return;
        }
        
        if (startDate.isAfter(endDate)) {
            showError("Start date must be before end date.");
            return;
        }
        
        // Load data and generate all charts
        generateRevenueComparison();
        generateOrderTrends();
        generateMarketShare();
        updateKPIs();
    }
    
    /**
     * Generate revenue comparison chart and table
     */
    private void generateRevenueComparison() {
        revenueComparisonChart.getData().clear();
        
        XYChart.Series<String, Number> currentSeries = new XYChart.Series<>();
        currentSeries.setName("Current Period");
        
        XYChart.Series<String, Number> previousSeries = new XYChart.Series<>();
        previousSeries.setName("Previous Period");
        
        ObservableList<RevenueData> revenueDataList = FXCollections.observableArrayList();
        Random random = new Random();
        
        for (String branch : branchNames) {
            double currentRevenue = 15000 + random.nextDouble() * 15000;
            double previousRevenue = 12000 + random.nextDouble() * 13000;
            int orders = 90 + random.nextInt(110);
            double avgOrderValue = currentRevenue / orders;
            double growth = ((currentRevenue - previousRevenue) / previousRevenue) * 100;
            
            currentSeries.getData().add(new XYChart.Data<>(branch, currentRevenue));
            previousSeries.getData().add(new XYChart.Data<>(branch, previousRevenue));
            
            revenueDataList.add(new RevenueData(
                branch,
                orders,
                currentRevenue,
                avgOrderValue,
                String.format("%.1f%%", growth)
            ));
        }
        
        revenueComparisonChart.getData().addAll(currentSeries, previousSeries);
        revenueTable.setItems(revenueDataList);
    }
    
    /**
     * Generate order trends line chart
     */
    private void generateOrderTrends() {
        orderTrendsChart.getData().clear();
        
        Random random = new Random();
        
        // Create a series for each branch
        for (String branch : branchNames) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(branch);
            
            // Generate weekly data
            for (int week = 1; week <= 4; week++) {
                int orders = 20 + random.nextInt(30);
                series.getData().add(new XYChart.Data<>("Week " + week, orders));
            }
            
            orderTrendsChart.getData().add(series);
        }
    }
    
    /**
     * Generate market share pie charts
     */
    private void generateMarketShare() {
        revenueSharePieChart.getData().clear();
        orderSharePieChart.getData().clear();
        
        Random random = new Random();
        
        // Revenue share
        ObservableList<PieChart.Data> revenueShareData = FXCollections.observableArrayList();
        double totalRevenue = 0;
        Map<String, Double> branchRevenues = new HashMap<>();
        
        for (String branch : branchNames) {
            double revenue = 15000 + random.nextDouble() * 15000;
            branchRevenues.put(branch, revenue);
            totalRevenue += revenue;
        }
        
        for (Map.Entry<String, Double> entry : branchRevenues.entrySet()) {
            double percentage = (entry.getValue() / totalRevenue) * 100;
            revenueShareData.add(new PieChart.Data(
                entry.getKey() + String.format(" (%.1f%%)", percentage),
                entry.getValue()
            ));
        }
        
        revenueSharePieChart.setData(revenueShareData);
        
        // Order share
        ObservableList<PieChart.Data> orderShareData = FXCollections.observableArrayList();
        int totalOrders = 0;
        Map<String, Integer> branchOrders = new HashMap<>();
        
        for (String branch : branchNames) {
            int orders = 90 + random.nextInt(110);
            branchOrders.put(branch, orders);
            totalOrders += orders;
        }
        
        for (Map.Entry<String, Integer> entry : branchOrders.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / totalOrders;
            orderShareData.add(new PieChart.Data(
                entry.getKey() + String.format(" (%.1f%%)", percentage),
                entry.getValue()
            ));
        }
        
        orderSharePieChart.setData(orderShareData);
        
        // Apply colors to pie charts
        applyPieChartColors();
    }
    
    /**
     * Apply custom colors to pie chart segments
     */
    private void applyPieChartColors() {
        Platform.runLater(() -> {
            String[] colors = {"#ba68c8", "#ce93d8", "#e1bee7", "#ab47bc", "#9c27b0"};
            
            // Revenue share colors
            int i = 0;
            for (PieChart.Data data : revenueSharePieChart.getData()) {
                data.getNode().setStyle("-fx-pie-color: " + colors[i % colors.length] + ";");
                i++;
            }
            
            // Order share colors
            i = 0;
            for (PieChart.Data data : orderSharePieChart.getData()) {
                data.getNode().setStyle("-fx-pie-color: " + colors[i % colors.length] + ";");
                i++;
            }
        });
    }
    
    /**
     * Update KPI labels
     */
    private void updateKPIs() {
        // Find best performer
        ObservableList<RevenueData> items = revenueTable.getItems();
        if (!items.isEmpty()) {
            RevenueData best = items.stream()
                .max(Comparator.comparing(RevenueData::getTotalRevenue))
                .orElse(null);
            
            if (best != null) {
                bestPerformerLabel.setText(best.getBranchName());
            }
            
            // Calculate average revenue
            double avgRevenue = items.stream()
                .mapToDouble(RevenueData::getTotalRevenue)
                .average()
                .orElse(0.0);
            avgRevenueLabel.setText(String.format("₪%.2f", avgRevenue));
            
            // Calculate network growth (sample)
            Random random = new Random();
            double growth = 5.0 + random.nextDouble() * 15.0;
            networkGrowthLabel.setText(String.format("+%.1f%%", growth));
        }
    }
    
    /**
     * Export report to PDF
     */
    @FXML
    private void handleExportReport() {
        // TODO: Implement PDF export functionality
        showInfo("Export feature will generate a comprehensive PDF report with all charts and data tables.");
    }
    
    /**
     * Navigate back to dashboard
     */
    @FXML
    private void handleBackToDashboard() {
        try {
            App.setRoot("NetworkDashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to navigate to dashboard.");
        }
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
     * Data model for revenue table
     */
    public static class RevenueData {
        private final String branchName;
        private final int totalOrders;
        private final double totalRevenue;
        private final double avgOrderValue;
        private final String growthPercent;
        
        public RevenueData(String branchName, int totalOrders, double totalRevenue,
                          double avgOrderValue, String growthPercent) {
            this.branchName = branchName;
            this.totalOrders = totalOrders;
            this.totalRevenue = totalRevenue;
            this.avgOrderValue = avgOrderValue;
            this.growthPercent = growthPercent;
        }
        
        public String getBranchName() { return branchName; }
        public int getTotalOrders() { return totalOrders; }
        public double getTotalRevenue() { return totalRevenue; }
        public double getAvgOrderValue() { return avgOrderValue; }
        public String getGrowthPercent() { return growthPercent; }
    }
}
