package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class ComplaintUpdateResponse implements Serializable {
    private final boolean success;
    private final String message;
    private final Complaint complaint;

    public ComplaintUpdateResponse(boolean success, String message, Complaint complaint) {
        this.success = success;
        this.message = message;
        this.complaint = complaint;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Complaint getComplaint() {
        return complaint;
    }
}
