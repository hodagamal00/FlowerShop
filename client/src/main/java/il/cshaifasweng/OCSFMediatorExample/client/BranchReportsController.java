package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.BranchSettings;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.ReportDataRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.ReportDataResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for Branch Reports page - Manager view
 * Displays income reports with BarChart and complaints breakdown with PieChart
 * Requires Manager privilege (level 3+)
 */
public class BranchReportsController {

    // Navigation
    
    // Header
    @FXML private Label branchLabel;
    
    // Filters
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button generateButton;
    @FXML private Button exportButton;
    
    // Income Report Section
    @FXML private VBox incomeReportCard;
    @FXML private Label totalIncomeLabel;
    @FXML private BarChart<String, Number> incomeBarChart;
    @FXML private CategoryAxis incomeXAxis;
    @FXML private NumberAxis incomeYAxis;
    @FXML private TableView<IncomeData> incomeTable;
    @FXML private TableColumn<IncomeData, String> incomePeriodCol;
    @FXML private TableColumn<IncomeData, Integer> incomeOrdersCol;
    @FXML private TableColumn<IncomeData, Double> incomeAmountCol;
    @FXML private TableColumn<IncomeData, Double> incomeAvgCol;
    
    // Orders Report Section
    @FXML private VBox ordersReportCard;
    @FXML private Label totalOrdersLabel;
    @FXML private Label completedOrdersLabel;
    @FXML private Label pendingOrdersLabel;
    @FXML private Label cancelledOrdersLabel;
    @FXML private BarChart<String, Number> ordersByTypeChart;
    @FXML private CategoryAxis ordersByTypeXAxis;
    @FXML private NumberAxis ordersByTypeYAxis;
    @FXML private TableView<OrderTypeData> ordersByTypeTable;
    @FXML private TableColumn<OrderTypeData, String> orderTypeCol;
    @FXML private TableColumn<OrderTypeData, Integer> orderTypeCountCol;
    @FXML private Label ordersByTypeEmptyLabel;
    
    // Complaints Report Section
    @FXML private VBox complaintsReportCard;
    @FXML private Label totalComplaintsLabel;
    @FXML private PieChart complaintsPieChart;
    @FXML private BarChart<String, Number> complaintsHistogramChart;
    @FXML private CategoryAxis complaintsHistogramXAxis;
    @FXML private NumberAxis complaintsHistogramYAxis;
    @FXML private Label complaintsEmptyLabel;
    @FXML private TableView<ComplaintData> complaintsTable;
    @FXML private TableColumn<ComplaintData, String> complaintStatusCol;
    @FXML private TableColumn<ComplaintData, Integer> complaintCountCol;
    @FXML private TableColumn<ComplaintData, String> complaintPercentCol;
    @FXML private TableColumn<ComplaintData, String> complaintAvgTimeCol;
    
    // Data storage
    private List<Order> branchOrders = new ArrayList<>();
    private List<Complaint> branchComplaints = new ArrayList<>();
    private Map<LocalDate, Integer> complaintsHistogram = new java.util.HashMap<>();
    private Map<String, Integer> ordersByProductType = new java.util.HashMap<>();
    private double reportTotalRevenue = 0.0;
    private int currentBranchId = 1;
    private String pendingRequestId;
    private String lastReportType;
    
    @FXML
    public void initialize() {
        // Check privileges - Manager level required (3+)
        if (!AccessGuard.requireMinPrivilege(3)) {
            return;
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setupReportTypes();
        setupTableColumns();
        setDefaultDates();
        loadBranchInfo();
        
        // Auto-generate initial report
        Platform.runLater(this::handleGenerateReport);
    }
    
    /**
     * Setup report type combo box
     */
    private void setupReportTypes() {
        ObservableList<String> reportTypes = FXCollections.observableArrayList(
            "Income Report",
            "Orders Report",
            "Complaints Report",
            "All Reports"
        );
        reportTypeCombo.setItems(reportTypes);
        reportTypeCombo.setValue("All Reports");
    }
    
    /**
     * Setup table columns with cell value factories
     */
    private void setupTableColumns() {
        // Income table
        incomePeriodCol.setCellValueFactory(new PropertyValueFactory<>("period"));
        incomeOrdersCol.setCellValueFactory(new PropertyValueFactory<>("ordersCount"));
        incomeAmountCol.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
        incomeAvgCol.setCellValueFactory(new PropertyValueFactory<>("avgOrderValue"));

        orderTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        orderTypeCountCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        
        // Complaints table
        complaintStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        complaintCountCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        complaintPercentCol.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        complaintAvgTimeCol.setCellValueFactory(new PropertyValueFactory<>("avgResponseTime"));
    }
    
    /**
     * Set default date range (last 30 days)
     */
    private void setDefaultDates() {
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(30));
    }
    
    /**
     * Load branch information
     */
    private void loadBranchInfo() {
        Account account = SimpleClient.getAccount();
        if (account != null) {
            if (account.getBelongShop() > 0) {
                currentBranchId = account.getBelongShop();
            } else if (account instanceof il.cshaifasweng.OCSFMediatorExample.entities.Manager) {
                int managerShop = ((il.cshaifasweng.OCSFMediatorExample.entities.Manager) account).getShopID();
                if (managerShop > 0) {
                    currentBranchId = managerShop;
                }
            }
        }
        branchLabel.setText("Branch: ID " + currentBranchId);
    }
    
    /**
     * Generate report based on selected filters
     */
    @FXML
    private void handleGenerateReport() {
        String reportType = reportTypeCombo.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        lastReportType = reportType;
        
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
    
    /**
     * Load report data from server
     */
    private void requestReportData(LocalDate startDate, LocalDate endDate) {
        Account account = SimpleClient.getAccount();
        if (account == null) {
            showError("Please log in to generate reports.");
            return;
        }
        if (account.getBelongShop() > 0) {
            currentBranchId = account.getBelongShop();
        } else if (account instanceof il.cshaifasweng.OCSFMediatorExample.entities.Manager) {
            int managerShop = ((il.cshaifasweng.OCSFMediatorExample.entities.Manager) account).getShopID();
            if (managerShop > 0) {
                currentBranchId = managerShop;
            }
        }
        pendingRequestId = UUID.randomUUID().toString();
        ReportDataRequest request = new ReportDataRequest(pendingRequestId, startDate, endDate, currentBranchId, "CURRENT");
        try {
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            showError("Failed to request report data.");
        }
    }

    @Subscribe
    public void onReportDataResponse(ReportDataResponse response) {
        if (response == null || response.getRequestId() == null || !response.getRequestId().equals(pendingRequestId)) {
            return;
        }
        Platform.runLater(() -> {
            if (!response.isSuccess()) {
                showError(response.getErrorMessage() != null ? response.getErrorMessage() : "Failed to load report data.");
                return;
            }

            branchOrders = response.getOrders() != null ? response.getOrders() : new ArrayList<>();
            branchComplaints = response.getComplaints() != null ? response.getComplaints() : new ArrayList<>();
            complaintsHistogram = response.getComplaintsHistogram() != null
                    ? response.getComplaintsHistogram()
                    : new java.util.HashMap<>();
            ordersByProductType = response.getOrdersByProductType() != null
                    ? response.getOrdersByProductType()
                    : new java.util.HashMap<>();
            reportTotalRevenue = response.getTotalRevenue();
            updateBranchLabel(response.getBranches());
            refreshReports(lastReportType);
        });
    }

    private void updateBranchLabel(List<BranchSettings> branches) {
        if (branches != null && !branches.isEmpty()) {
            BranchSettings branch = branches.get(0);
            String name = branch.getBranchName() != null ? branch.getBranchName() : ("ID " + branch.getBranchId());
            branchLabel.setText("Branch: " + name + " (ID: " + branch.getBranchId() + ")");
            return;
        }
        branchLabel.setText("Branch: ID " + currentBranchId);
    }

    private void refreshReports(String reportType) {
        if (reportType == null) {
            reportType = "All Reports";
        }
        switch (reportType) {
            case "Income Report":
                generateIncomeReport();
                incomeReportCard.setVisible(true);
                ordersReportCard.setVisible(false);
                complaintsReportCard.setVisible(false);
                break;
            case "Orders Report":
                generateOrdersReport();
                incomeReportCard.setVisible(false);
                ordersReportCard.setVisible(true);
                complaintsReportCard.setVisible(false);
                break;
            case "Complaints Report":
                generateComplaintsReport();
                incomeReportCard.setVisible(false);
                ordersReportCard.setVisible(false);
                complaintsReportCard.setVisible(true);
                break;
            case "All Reports":
            default:
                generateIncomeReport();
                generateOrdersReport();
                generateComplaintsReport();
                incomeReportCard.setVisible(true);
                ordersReportCard.setVisible(true);
                complaintsReportCard.setVisible(true);
                break;
        }
    }
    
    /**
     * Generate income report with BarChart
     */
    private void generateIncomeReport() {
        // Clear previous data
        incomeBarChart.getData().clear();
        
        // Group orders by week
        Map<String, List<Order>> ordersByWeek = branchOrders.stream()
            .filter(o -> "Delivered".equals(o.getStatus()))
            .collect(Collectors.groupingBy(order -> {
                LocalDate orderDate = order.getDelivery_time() != null
                        ? order.getDelivery_time().toLocalDate()
                        : order.getOrderDate().toLocalDate();
                int weekOfYear = orderDate.getDayOfYear() / 7;
                return "Week " + weekOfYear;
            }));
        
        // Calculate income per week
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Weekly Income");
        
        double totalIncome = 0.0;
        ObservableList<IncomeData> incomeDataList = FXCollections.observableArrayList();
        
        for (Map.Entry<String, List<Order>> entry : ordersByWeek.entrySet()) {
            String week = entry.getKey();
            List<Order> weekOrders = entry.getValue();
            
            double weekIncome = weekOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
            
            double avgOrderValue = weekIncome / weekOrders.size();
            
            series.getData().add(new XYChart.Data<>(week, weekIncome));
            incomeDataList.add(new IncomeData(week, weekOrders.size(), weekIncome, avgOrderValue));
            
            totalIncome += weekIncome;
        }
        
        incomeBarChart.getData().add(series);
        incomeTable.setItems(incomeDataList);
        double displayTotal = reportTotalRevenue > 0 ? reportTotalRevenue : totalIncome;
        totalIncomeLabel.setText(String.format("Total: ₪%.2f", displayTotal));
    }
    
    /**
     * Generate orders report with statistics
     */
    private void generateOrdersReport() {
        int completed = (int) branchOrders.stream().filter(o -> "Delivered".equals(o.getStatus())).count();
        int pending = (int) branchOrders.stream().filter(o -> "Pending".equals(o.getStatus()) || "In Progress".equals(o.getStatus())).count();
        int cancelled = (int) branchOrders.stream().filter(o -> "Cancelled".equals(o.getStatus())).count();
        
        totalOrdersLabel.setText("Total Orders: " + branchOrders.size());
        completedOrdersLabel.setText(String.valueOf(completed));
        pendingOrdersLabel.setText(String.valueOf(pending));
        cancelledOrdersLabel.setText(String.valueOf(cancelled));

        updateOrdersByTypeChart(ordersByProductType);
    }
    
    /**
     * Generate complaints report with PieChart
     */
    private void generateComplaintsReport() {
        // Clear previous data
        complaintsPieChart.getData().clear();
        
        // Group complaints by status
        Map<String, Long> complaintsByStatus = branchComplaints.stream()
                .collect(Collectors.groupingBy(this::resolveStatusLabel, Collectors.counting()));
        
        // Calculate total complaints
        int totalComplaints = branchComplaints.size();
        long overdueCount = complaintsByStatus.getOrDefault("OVERDUE", 0L);
        totalComplaintsLabel.setText(overdueCount > 0
                ? "Total: " + totalComplaints + " (" + overdueCount + " overdue)"
                : "Total: " + totalComplaints);
        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        ObservableList<ComplaintData> complaintDataList = FXCollections.observableArrayList();
        
        for (Map.Entry<String, Long> entry : complaintsByStatus.entrySet()) {
            String status = entry.getKey();
            long count = entry.getValue();
            double percentage = (count * 100.0) / totalComplaints;
            
            // Calculate average response time for this status
            double avgResponseTime = branchComplaints.stream()
                    .filter(c -> status.equals(resolveStatusLabel(c)))
                .mapToInt(Complaint::getResponseTime)
                .average()
                .orElse(0.0);
            
            pieChartData.add(new PieChart.Data(status + " (" + count + ")", count));
            complaintDataList.add(new ComplaintData(
                status,
                (int) count,
                String.format("%.1f%%", percentage),
                String.format("%.1f hours", avgResponseTime)
            ));
        }
        
        complaintsPieChart.setData(pieChartData);
        complaintsTable.setItems(complaintDataList);
        
        // Apply colors to pie chart segments
        applyPieChartColors();
        updateComplaintsHistogramChart(complaintsHistogram);

    }
    private String resolveStatusLabel(Complaint complaint) {
        String status = complaint.getSlaStatus();
        if (status == null || status.isEmpty()) {
            status = complaint.getStatus();
        }
        return status != null ? status : "Pending";
    }

    /**
     * Apply custom colors to pie chart segments
     */
    private void applyPieChartColors() {
        Platform.runLater(() -> {
            Map<String, String> statusColors = Map.of(
                "Pending", "#ff9800",
                "In Progress", "#2196f3",
                "Resolved", "#4caf50",
                "Rejected", "#f44336"
            );
            
            complaintsPieChart.getData().forEach(data -> {
                String status = data.getName().split(" \\(")[0];
                String color = statusColors.getOrDefault(status, "#ba68c8");
                data.getNode().setStyle("-fx-pie-color: " + color + ";");
            });
        });
    }

    private void updateComplaintsHistogramChart(Map<LocalDate, Integer> histogram) {
        if (histogram == null || histogram.isEmpty()) {
            if (complaintsHistogramChart != null) {
                complaintsHistogramChart.getData().clear();
            }
            if (complaintsEmptyLabel != null) {
                complaintsEmptyLabel.setVisible(true);
                complaintsEmptyLabel.setManaged(true);
            }
            return;
        }
        if (complaintsEmptyLabel != null) {
            complaintsEmptyLabel.setVisible(false);
            complaintsEmptyLabel.setManaged(false);
        }
        BarChart<String, Number> chart = ensureComplaintsHistogramChart();
        chart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Complaints");

        histogram.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> series.getData().add(
                        new XYChart.Data<>(entry.getKey().toString(), entry.getValue())));

        chart.getData().add(series);
    }

    private void updateOrdersByTypeChart(Map<String, Integer> typeCounts) {
        if (ordersByTypeChart != null) {
            ordersByTypeChart.getData().clear();
        }
        ObservableList<OrderTypeData> tableItems = FXCollections.observableArrayList();
        if (typeCounts == null || typeCounts.isEmpty()) {
            if (ordersByTypeEmptyLabel != null) {
                ordersByTypeEmptyLabel.setVisible(true);
                ordersByTypeEmptyLabel.setManaged(true);
            }
            ordersByTypeTable.setItems(tableItems);
            return;
        }
        if (ordersByTypeEmptyLabel != null) {
            ordersByTypeEmptyLabel.setVisible(false);
            ordersByTypeEmptyLabel.setManaged(false);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Orders by Type");
        for (Map.Entry<String, Integer> entry : typeCounts.entrySet()) {
            String type = entry.getKey();
            int count = entry.getValue() != null ? entry.getValue() : 0;
            series.getData().add(new XYChart.Data<>(type, count));
            tableItems.add(new OrderTypeData(type, count));
        }
        if (ordersByTypeChart != null) {
            ordersByTypeChart.getData().add(series);
        }
        ordersByTypeTable.setItems(tableItems);
    }

    private BarChart<String, Number> ensureComplaintsHistogramChart() {
        if (complaintsHistogramChart != null) {
            return complaintsHistogramChart;
        }
        CategoryAxis xAxis = complaintsHistogramXAxis != null ? complaintsHistogramXAxis : new CategoryAxis();
        NumberAxis yAxis = complaintsHistogramYAxis != null ? complaintsHistogramYAxis : new NumberAxis();
        if (complaintsHistogramXAxis == null) {
            xAxis.setLabel("Date");
            complaintsHistogramXAxis = xAxis;
        }
        if (complaintsHistogramYAxis == null) {
            yAxis.setLabel("Complaints");
            complaintsHistogramYAxis = yAxis;
        }
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Complaints by Day");
        chart.setLegendVisible(false);
        complaintsHistogramChart = chart;
        if (complaintsReportCard != null && !complaintsReportCard.getChildren().contains(chart)) {
            complaintsReportCard.getChildren().add(chart);
        }
        return chart;
    }
    
    /**
     * Export report to PDF
     */
    @FXML
    private void handleExportReport() {
        // TODO: Implement PDF export functionality
        // Could use libraries like iText or Apache PDFBox
        showInfo("Export feature will generate a PDF report with all charts and data.");
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
    
    // Data model classes for TableViews
    
    public static class IncomeData {
        private final String period;
        private final int ordersCount;
        private final double totalIncome;
        private final double avgOrderValue;
        
        public IncomeData(String period, int ordersCount, double totalIncome, double avgOrderValue) {
            this.period = period;
            this.ordersCount = ordersCount;
            this.totalIncome = totalIncome;
            this.avgOrderValue = avgOrderValue;
        }
        
        public String getPeriod() { return period; }
        public int getOrdersCount() { return ordersCount; }
        public double getTotalIncome() { return totalIncome; }
        public double getAvgOrderValue() { return avgOrderValue; }
    }
    
    public static class ComplaintData {
        private final String status;
        private final int count;
        private final String percentage;
        private final String avgResponseTime;
        
        public ComplaintData(String status, int count, String percentage, String avgResponseTime) {
            this.status = status;
            this.count = count;
            this.percentage = percentage;
            this.avgResponseTime = avgResponseTime;
        }
        
        public String getStatus() { return status; }
        public int getCount() { return count; }
        public String getPercentage() { return percentage; }
        public String getAvgResponseTime() { return avgResponseTime; }
    }

    public static class OrderTypeData {
        private final String type;
        private final int count;

        public OrderTypeData(String type, int count) {
            this.type = type;
            this.count = count;
        }

        public String getType() {
            return type;
        }

        public int getCount() {
            return count;
        }
    }
}
