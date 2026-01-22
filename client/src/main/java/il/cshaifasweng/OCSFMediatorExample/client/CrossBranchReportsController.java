package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import il.cshaifasweng.OCSFMediatorExample.entities.BranchSettings;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.ReportDataRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.ReportDataResponse;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private final Map<Integer, String> branchNames = new LinkedHashMap<>();
    private final Map<Integer, BranchMetrics> currentMetrics = new LinkedHashMap<>();
    private final Map<Integer, BranchMetrics> previousMetrics = new LinkedHashMap<>();
    private List<Order> currentOrders = new ArrayList<>();
    private List<Order> previousOrders = new ArrayList<>();
    private LocalDate currentStart;
    private LocalDate currentEnd;
    private String currentRequestId;
    private String previousRequestId;
    
    @FXML
    public void initialize() {
        // Check privileges - Chain Manager level required (4)
        if (!AccessGuard.requireMinPrivilege(4)) {
            return;
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        
        setupTableColumns();
        setDefaultDates();
        
        // Auto-generate initial report
        Platform.runLater(this::handleGenerateReport);
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
        
        requestReportData(startDate, endDate);
    }

    private void requestReportData(LocalDate startDate, LocalDate endDate) {
        currentStart = startDate;
        currentEnd = endDate;
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        LocalDate previousEnd = startDate.minusDays(1);
        LocalDate previousStart = previousEnd.minusDays(Math.max(0, days - 1));

        currentRequestId = UUID.randomUUID().toString();
        previousRequestId = UUID.randomUUID().toString();

        try {
            SimpleClient.getClient().sendToServer(
                new ReportDataRequest(currentRequestId, startDate, endDate, 0, "CURRENT")
            );
            SimpleClient.getClient().sendToServer(
                new ReportDataRequest(previousRequestId, previousStart, previousEnd, 0, "PREVIOUS")
            );
        } catch (IOException e) {
            showError("Failed to request report data.");
        }
    }

    @Subscribe
    public void onReportDataResponse(ReportDataResponse response) {
        if (response == null || response.getRequestId() == null) {
            return;
        }
        Platform.runLater(() -> {
            if (!response.isSuccess()) {
                showError(response.getErrorMessage() != null ? response.getErrorMessage() : "Failed to load report data.");
                return;
            }

            if (response.getRequestId().equals(currentRequestId)) {
                currentOrders = response.getOrders() != null ? response.getOrders() : new ArrayList<>();
                updateBranchNames(response.getBranches());
                buildMetrics(currentOrders, currentMetrics);
            } else if (response.getRequestId().equals(previousRequestId)) {
                previousOrders = response.getOrders() != null ? response.getOrders() : new ArrayList<>();
                buildMetrics(previousOrders, previousMetrics);
            } else {
                return;
            }

            if (!currentMetrics.isEmpty() && currentStart != null) {
                generateRevenueComparison();
                generateOrderTrends();
                generateMarketShare();
                updateKPIs();
            }
        });
    }

    private void updateBranchNames(List<BranchSettings> branches) {
        branchNames.clear();
        if (branches != null) {
            for (BranchSettings settings : branches) {
                branchNames.put(settings.getBranchId(),
                        settings.getBranchName() != null ? settings.getBranchName() : ("Branch " + settings.getBranchId()));
            }
        }
    }

    private void buildMetrics(List<Order> orders, Map<Integer, BranchMetrics> metricsMap) {
        metricsMap.clear();
        for (Order order : orders) {
            int branchId = order.getShopID();
            BranchMetrics metrics = metricsMap.computeIfAbsent(branchId, id -> new BranchMetrics());
            metrics.totalOrders += 1;
            metrics.totalRevenue += order.getTotalPrice();
        }
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
        List<Integer> branchIds = new ArrayList<>(branchNames.keySet());
        branchIds.sort(Comparator.naturalOrder());

        for (Integer branchId : branchIds) {
            String branch = branchNames.get(branchId);
            BranchMetrics current = currentMetrics.getOrDefault(branchId, new BranchMetrics());
            BranchMetrics previous = previousMetrics.getOrDefault(branchId, new BranchMetrics());
            double currentRevenue = current.totalRevenue;
            double previousRevenue = previous.totalRevenue;
            int orders = current.totalOrders;
            double avgOrderValue = orders > 0 ? currentRevenue / orders : 0.0;
            double growth = previousRevenue > 0 ? ((currentRevenue - previousRevenue) / previousRevenue) * 100 : 0.0;

            currentSeries.getData().add(new XYChart.Data<>(branch, currentRevenue));
            previousSeries.getData().add(new XYChart.Data<>(branch, previousRevenue));

            revenueDataList.add(new RevenueData(
                branch,
                orders,
                currentRevenue,
                avgOrderValue,
                previousRevenue > 0 ? String.format("%.1f%%", growth) : "N/A"
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

        if (currentStart == null || currentEnd == null) {
            return;
        }
        int weeks = (int) Math.max(1, ChronoUnit.DAYS.between(currentStart, currentEnd) / 7 + 1);
        Map<Integer, Map<Integer, Integer>> weeklyCounts = new HashMap<>();

        for (Order order : currentOrders) {
            try {
                LocalDate date = LocalDate.of(order.getOrderYear(), order.getOrderMonth(), order.getOrderDay());
                int weekIndex = (int) (ChronoUnit.DAYS.between(currentStart, date) / 7) + 1;
                weekIndex = Math.max(1, Math.min(weeks, weekIndex));
                weeklyCounts
                    .computeIfAbsent(order.getShopID(), id -> new HashMap<>())
                    .merge(weekIndex, 1, Integer::sum);
            } catch (Exception ignored) {
                // Skip invalid dates
            }
        }

        List<Integer> branchIds = new ArrayList<>(branchNames.keySet());
        branchIds.sort(Comparator.naturalOrder());
        for (Integer branchId : branchIds) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(branchNames.get(branchId));
            Map<Integer, Integer> counts = weeklyCounts.getOrDefault(branchId, new HashMap<>());
            for (int week = 1; week <= weeks; week++) {
                int orders = counts.getOrDefault(week, 0);
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

        ObservableList<PieChart.Data> revenueShareData = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> orderShareData = FXCollections.observableArrayList();
        double totalRevenue = currentMetrics.values().stream().mapToDouble(m -> m.totalRevenue).sum();
        int totalOrders = currentMetrics.values().stream().mapToInt(m -> m.totalOrders).sum();

        for (Map.Entry<Integer, String> entry : branchNames.entrySet()) {
            int branchId = entry.getKey();
            String branch = entry.getValue();
            BranchMetrics metrics = currentMetrics.getOrDefault(branchId, new BranchMetrics());
            double revenue = metrics.totalRevenue;
            int orders = metrics.totalOrders;
            double revenuePercent = totalRevenue > 0 ? (revenue / totalRevenue) * 100 : 0;
            double orderPercent = totalOrders > 0 ? (orders * 100.0) / totalOrders : 0;

            revenueShareData.add(new PieChart.Data(
                branch + String.format(" (%.1f%%)", revenuePercent),
                revenue
            ));
            orderShareData.add(new PieChart.Data(
                branch + String.format(" (%.1f%%)", orderPercent),
                orders
            ));
        }

        revenueSharePieChart.setData(revenueShareData);
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
            
            double totalCurrent = currentMetrics.values().stream().mapToDouble(m -> m.totalRevenue).sum();
            double totalPrevious = previousMetrics.values().stream().mapToDouble(m -> m.totalRevenue).sum();
            if (totalPrevious > 0) {
                double growth = ((totalCurrent - totalPrevious) / totalPrevious) * 100;
                networkGrowthLabel.setText(String.format("%+.1f%%", growth));
            } else {
                networkGrowthLabel.setText("N/A");
            }
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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

    private static class BranchMetrics {
        private int totalOrders;
        private double totalRevenue;
    }
}
