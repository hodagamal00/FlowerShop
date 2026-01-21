package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class CancelOrderResponse implements Serializable {
    private final boolean success;
    private final String message;
    private final int orderId;
    private final double refundAmount;
    private final double refundPercent;
    private final String refundStatus;

    public CancelOrderResponse(boolean success, String message, int orderId, double refundAmount,
                               double refundPercent, String refundStatus) {
        this.success = success;
        this.message = message;
        this.orderId = orderId;
        this.refundAmount = refundAmount;
        this.refundPercent = refundPercent;
        this.refundStatus = refundStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public double getRefundPercent() {
        return refundPercent;
    }

    public String getRefundStatus() {
        return refundStatus;
    }
}
