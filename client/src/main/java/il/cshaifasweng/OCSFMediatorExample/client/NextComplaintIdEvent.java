package il.cshaifasweng.OCSFMediatorExample.client;

/**
 * Event dispatched through EventBus when the server returns the next
 * available complaint ID.
 */
public class NextComplaintIdEvent {
    private final int complaintId;

    public NextComplaintIdEvent(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getComplaintId() {
        return complaintId;
    }
}