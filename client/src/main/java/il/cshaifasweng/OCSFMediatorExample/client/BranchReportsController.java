package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for Branch Reports page - Manager view
 * Displays income reports with BarChart and complaints breakdown with PieChart
 * Requires Manager privilege (level 3+)
 */
public class BranchReportsController {

    // Navigation
    @FXML private Button backButton;
    
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
    
    // Complaints Report Section
    @FXML private VBox complaintsReportCard;
    @FXML private Label totalComplaintsLabel;
    @FXML private PieChart complaintsPieChart;
    @FXML private TableView<ComplaintData> complaintsTable;
    @FXML private TableColumn<ComplaintData, String> complaintStatusCol;
    @FXML private TableColumn<ComplaintData, Integer> complaintCountCol;
    @FXML private TableColumn<ComplaintData, String> complaintPercentCol;
    @FXML private TableColumn<ComplaintData, String> complaintAvgTimeCol;
    
    // Data storage
    private List<Order> branchOrders = new ArrayList<>();
    private List<Complaint> branchComplaints = new ArrayList<>();
    private int currentBranchId = 1; // Default branch, should be loaded from logged-in user

    // Map to track sample order types when generating sample data.  In a real implementation,
    // the server would return order details including product types.  Here we assign random
    // types for demonstration purposes.
    private Map<Integer, String> orderTypeMap = new HashMap<>();

    // Summary strings for orders by type and complaints by date, displayed alongside totals.
    private String ordersByTypeSummary = "";
    private String complaintsByDateSummary = "";
    
    @FXML
    public void initialize() {
        // Check privileges - Manager level required (3+)
        if (!checkManagerPrivileges()) {
            showAccessDenied();
            return;
        }
        
        setupReportTypes();
        setupTableColumns();
        setDefaultDates();
        loadBranchInfo();
        
        // Auto-generate initial report
        Platform.runLater(this::handleGenerateReport);
    }
    
    /**
     * Check if user has manager privileges
     */
    private boolean checkManagerPrivileges() {
        // TODO: Get current user privilege from session
        // For now, assume user is manager
        // In production: return SimpleClient.getCurrentUser().getPrivilege() >= 3;
        return true;
    }
    
    /**
     * Show access denied message and navigate back
     */
    private void showAccessDenied() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access Denied");
        alert.setHeaderText("Insufficient Privileges");
        alert.setContentText("You need Manager privileges to access this page.");
        alert.showAndWait();
        handleBackToCatalog();
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
        // TODO: Get branch info from server based on currentBranchId
        // For now, use placeholder
        branchLabel.setText("Branch: Main Street Branch (ID: " + currentBranchId + ")");
    }
    
    /**
     * Generate report based on selected filters
     */
    @FXML
    private void handleGenerateReport() {
        String reportType = reportTypeCombo.getValue();
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
        
        // Load data from server
        loadReportData(startDate, endDate);
        
        // Generate selected reports
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
     * Load report data from server
     */
    private void loadReportData(LocalDate startDate, LocalDate endDate) {
        // TODO: Send request to server to get orders and complaints for this branch and date range
        // Message format: new GetBranchReportData(currentBranchId, startDate, endDate)
        // SimpleClient.getClient().sendToServer(message);
        
        // For now, generate sample data
        generateSampleData(startDate, endDate);
    }
    
    /**
     * Generate sample data for demonstration
     */
    private void generateSampleData(LocalDate startDate, LocalDate endDate) {
        branchOrders = new ArrayList<>();
        branchComplaints = new ArrayList<>();
        orderTypeMap.clear();
        
        Random random = new Random();
        
        // Generate sample orders
        for (int i = 0; i < 50; i++) {
            Order order = new Order();
            order.setId(i + 1);
            order.setTotalPrice((int)(50.0 + random.nextDouble() * 200.0));
            
            // Random date within range
            long daysBetween = endDate.toEpochDay() - startDate.toEpochDay();
            LocalDate orderDate = startDate.plusDays(random.nextInt((int) daysBetween + 1));
            order.setOrderDate(orderDate.atStartOfDay());
            
            // Random status
            String[] statuses = {"Completed", "Pending", "In Progress", "Cancelled"};
            order.setStatus(statuses[random.nextInt(statuses.length)]);
            
            branchOrders.add(order);

            // Assign a random product type for this order.  In a real scenario this would
            // come from the order's product list.  Types include Bouquet, Plant, Accessory.
            String[] types = {"Bouquet", "Plant", "Accessory"};
            String type = types[random.nextInt(types.length)];
            orderTypeMap.put(order.getId(), type);
        }
        
        // Generate sample complaints
        for (int i = 0; i < 15; i++) {
            Complaint complaint = new Complaint();
            complaint.setId(i + 1);
            
            // Random status
            String[] statuses = {"Pending", "In Progress", "Resolved", "Rejected"};
            complaint.setStatus(statuses[random.nextInt(statuses.length)]);
            
            // Random response time (in hours)
            complaint.setResponseTime(random.nextInt(24) + 1);
            
            branchComplaints.add(complaint);
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
            .filter(o -> "Completed".equals(o.getStatus()))
            .collect(Collectors.groupingBy(order -> {
                LocalDate orderDate = order.getOrderDate().toLocalDate();
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
        totalIncomeLabel.setText(String.format("Total: ₪%.2f", totalIncome));
    }
    
    /**
     * Generate orders report with statistics
     */
    private void generateOrdersReport() {
        int completed = (int) branchOrders.stream().filter(o -> "Completed".equals(o.getStatus())).count();
        int pending = (int) branchOrders.stream().filter(o -> "Pending".equals(o.getStatus()) || "In Progress".equals(o.getStatus())).count();
        int cancelled = (int) branchOrders.stream().filter(o -> "Cancelled".equals(o.getStatus())).count();
        
        totalOrdersLabel.setText("Total Orders: " + branchOrders.size());
        completedOrdersLabel.setText(String.valueOf(completed));
        pendingOrdersLabel.setText(String.valueOf(pending));
        cancelledOrdersLabel.setText(String.valueOf(cancelled));

        // Also compute and display distribution by product type.
        generateOrdersByTypeSummary();
        if (!ordersByTypeSummary.isEmpty()) {
            totalOrdersLabel.setText("Total Orders: " + branchOrders.size() + " (" + ordersByTypeSummary + ")");
        }
    }

    /**
     * Compute a summary of orders grouped by product type.  For sample data we use the
     * orderTypeMap populated in generateSampleData().  In a real implementation, this
     * information would come from the server or from the Order entity itself.
     */
    private void generateOrdersByTypeSummary() {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (Order o : branchOrders) {
            String type = orderTypeMap.getOrDefault(o.getId(), "Unknown");
            counts.put(type, counts.getOrDefault(type, 0L) + 1);
        }
        StringBuilder sb = new StringBuilder();
        counts.forEach((type, count) -> {
            if (sb.length() > 0) sb.append(", ");
            sb.append(type).append(": ").append(count);
        });
        ordersByTypeSummary = sb.toString();
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

        // Also compute complaints histogram by date and append to total label
        generateComplaintsByDateSummary();
        if (!complaintsByDateSummary.isEmpty()) {
            int totalComplaintsCount = branchComplaints.size();
            totalComplaintsLabel.setText("Total: " + totalComplaintsCount + " (" + complaintsByDateSummary + ")");
        }
    }
    private String resolveStatusLabel(Complaint complaint) {
        String status = complaint.getSlaStatus();
        if (status == null || status.isEmpty()) {
            status = complaint.getStatus();
        }
        return status != null ? status : "Pending";
    }

    /**
     * Compute a summary of complaints grouped by date (day/month).  For each unique
     * day-month combination we count the number of complaints.  In a real implementation,
     * you could populate a bar chart for histogram visualization.
     */
    private void generateComplaintsByDateSummary() {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (Complaint c : branchComplaints) {
            // For sample data we don't set day/month/year; generate random if zero
            int day = c.getDay();
            int month = c.getMonth();
            if (day == 0) {
                day = (int)(Math.random() * 28) + 1;
                month = (int)(Math.random() * 12) + 1;
            }
            String key = String.format("%02d/%02d", day, month);
            counts.put(key, counts.getOrDefault(key, 0L) + 1);
        }
        StringBuilder sb = new StringBuilder();
        counts.forEach((date, count) -> {
            if (sb.length() > 0) sb.append(", ");
            sb.append(date).append(": ").append(count);
        });
        complaintsByDateSummary = sb.toString();
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
     * Navigate back to catalog
     */
    @FXML
    private void handleBackToCatalog() {
        try {
            App.setRoot("primary");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to navigate to catalog.");
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
}
