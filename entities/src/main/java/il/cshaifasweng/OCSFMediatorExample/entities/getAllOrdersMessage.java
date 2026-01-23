package il.cshaifasweng.OCSFMediatorExample.entities;

// Removed java.awt import. Using AWT alongside java.util.List
// can cause ambiguous reference errors because both frameworks
// define a List class. This message class does not use AWT
// components, so the import is unnecessary.
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class getAllOrdersMessage implements Serializable { // added 18/7

    private List<Order> orderList;
    private Integer branchId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;

    public getAllOrdersMessage(){

    }
    public void setOrderList(List<Order> givenlistOfOrders){
        orderList = givenlistOfOrders ;
    }
    public List<Order> getOrderList(){
        return orderList;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
