package il.cshaifasweng.OCSFMediatorExample.client;

public class InitDatabaseEvent {
    private boolean initDatabase;

    public InitDatabaseEvent(boolean initDatabase) {
        this.initDatabase = initDatabase;
    }

    public boolean isInitDatabase() {
        return initDatabase;
    }

    public void setInitDatabase(boolean initDatabase) {
        this.initDatabase = initDatabase;
    }
}
