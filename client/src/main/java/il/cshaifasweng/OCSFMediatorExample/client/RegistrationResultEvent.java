package il.cshaifasweng.OCSFMediatorExample.client;

import java.util.EventObject;

public class RegistrationResultEvent extends EventObject {
    private boolean success;
    private String message;

    public RegistrationResultEvent(boolean success, String message) {
        super("registration_result");
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
