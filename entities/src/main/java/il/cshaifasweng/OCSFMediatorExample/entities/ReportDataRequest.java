package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDataRequest implements Serializable {
    private String requestId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int branchId;
    private String periodLabel;
    private List<Integer> branchIds = new ArrayList<>();

    public ReportDataRequest() {
    }

    public ReportDataRequest(String requestId, LocalDate startDate, LocalDate endDate, int branchId, String periodLabel) {
        this.requestId = requestId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.branchId = branchId;
        this.periodLabel = periodLabel;
    }

    public ReportDataRequest(String requestId, LocalDate startDate, LocalDate endDate, int branchId,
                             String periodLabel, List<Integer> branchIds) {
        this(requestId, startDate, endDate, branchId, periodLabel);
        if (branchIds != null) {
            this.branchIds = new ArrayList<>(branchIds);
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getPeriodLabel() {
        return periodLabel;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public List<Integer> getBranchIds() {
        return branchIds;
    }

    public void setBranchIds(List<Integer> branchIds) {
        this.branchIds = branchIds != null ? new ArrayList<>(branchIds) : new ArrayList<>();
    }
}
