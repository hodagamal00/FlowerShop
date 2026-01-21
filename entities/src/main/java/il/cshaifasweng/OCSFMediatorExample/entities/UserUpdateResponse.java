package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class UserUpdateResponse implements Serializable {
    private final boolean success;
    private final String errorMessage;

    public UserUpdateResponse(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
