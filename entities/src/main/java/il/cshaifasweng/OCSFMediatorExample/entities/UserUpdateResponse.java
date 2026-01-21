package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class UserUpdateResponse implements Serializable {
    private final boolean success;
    private final String message;

    public UserUpdateResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorMessage() {
        return message;
    }
}
