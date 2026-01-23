package il.cshaifasweng.OCSFMediatorExample.entities;

// Removed java.awt import. Using AWT alongside java.util.List
// can cause ambiguous reference errors because both frameworks
// define a List class. This message class does not use AWT
// components, so the import is unnecessary.
import java.io.Serializable;
import java.util.List;

public class getAllOrdersMessage implements Serializable { // added 18/7

    List<Order> orderList ;
    private int branchId;
    public getAllOrdersMessage(){

    }
    public void setOrderList(List<Order> givenlistOfOrders){
        orderList = givenlistOfOrders ;
    }
    public List<Order> getOrderList(){
        return orderList;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
}
