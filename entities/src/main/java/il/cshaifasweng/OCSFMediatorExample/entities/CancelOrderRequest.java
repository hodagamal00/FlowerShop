package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class CancelOrderRequest implements Serializable {
    private int orderId;

    public CancelOrderRequest() {
    }

    public CancelOrderRequest(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
