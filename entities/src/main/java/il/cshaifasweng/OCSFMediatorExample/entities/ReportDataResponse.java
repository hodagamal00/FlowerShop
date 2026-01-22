package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportDataResponse implements Serializable {
    private boolean success;
    private String errorMessage;
    private String requestId;
    private String periodLabel;
    private int branchId;
    private double totalRevenue;
    private Map<String, Integer> ordersByProductType;
    private Map<LocalDate, Integer> complaintsHistogram;
    private List<Order> orders = new ArrayList<>();
    private List<Complaint> complaints = new ArrayList<>();
    private List<BranchSettings> branches = new ArrayList<>();

    public ReportDataResponse() {
    }

    public ReportDataResponse(boolean success, String errorMessage, String requestId, String periodLabel, int branchId,
                              double totalRevenue, Map<String, Integer> ordersByProductType,
                              Map<LocalDate, Integer> complaintsHistogram,
                              List<Order> orders, List<Complaint> complaints, List<BranchSettings> branches) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.requestId = requestId;
        this.periodLabel = periodLabel;
        this.branchId = branchId;
        this.totalRevenue = totalRevenue;
        this.ordersByProductType = ordersByProductType;
        this.complaintsHistogram = complaintsHistogram;
        if (orders != null) {
            this.orders = orders;
        }
        if (complaints != null) {
            this.complaints = complaints;
        }
        if (branches != null) {
            this.branches = branches;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPeriodLabel() {
        return periodLabel;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Map<String, Integer> getOrdersByProductType() {
        return ordersByProductType;
    }

    public void setOrdersByProductType(Map<String, Integer> ordersByProductType) {
        this.ordersByProductType = ordersByProductType;
    }

    public Map<LocalDate, Integer> getComplaintsHistogram() {
        return complaintsHistogram;
    }

    public void setComplaintsHistogram(Map<LocalDate, Integer> complaintsHistogram) {
        this.complaintsHistogram = complaintsHistogram;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders != null ? orders : new ArrayList<>();
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints != null ? complaints : new ArrayList<>();
    }

    public List<BranchSettings> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchSettings> branches) {
        this.branches = branches != null ? branches : new ArrayList<>();
    }
}
