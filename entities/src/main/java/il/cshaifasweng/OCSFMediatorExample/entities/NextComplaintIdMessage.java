package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

/**
 * Message used to request and deliver the next sequential complaint ID.
 * The client sends an empty instance of this class to the server. The server
 * responds with the same message containing the next available ID.
 */
public class NextComplaintIdMessage implements Serializable {
    private int nextComplaintId;

    public NextComplaintIdMessage() {
    }

    public int getNextComplaintId() {
        return nextComplaintId;
    }

    public void setNextComplaintId(int nextComplaintId) {
        this.nextComplaintId = nextComplaintId;
    }
}