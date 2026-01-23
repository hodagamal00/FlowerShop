package il.cshaifasweng.OCSFMediatorExample.client;

public class CatalogRefreshEvent {
    private final String reason;

    public CatalogRefreshEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
