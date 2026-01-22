package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportDataResponse implements Serializable {
    private boolean success;
    private String errorMessage;
    private String requestId;
    private String periodLabel;
    private int branchId;
    private List<Order> orders = new ArrayList<>();
    private List<Complaint> complaints = new ArrayList<>();
    private List<BranchSettings> branches = new ArrayList<>();

    public ReportDataResponse() {
    }

    public ReportDataResponse(boolean success, String errorMessage, String requestId, String periodLabel, int branchId,
                              List<Order> orders, List<Complaint> complaints, List<BranchSettings> branches) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.requestId = requestId;
        this.periodLabel = periodLabel;
        this.branchId = branchId;
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
