package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.util.List;

public class FxmlSmokeLoader {
    public static void main(String[] args) {
        Platform.startup(() -> {});
        List<String> fxmlFiles = List.of(
                "AppShell",
                "HomePage",
                "Login",
                "Catalog",
                "myorders"
        );

        for (String fxml : fxmlFiles) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
                loader.load();
                System.out.println("Loaded " + fxml + ".fxml");
            } catch (Exception e) {
                System.err.println("Failed to load " + fxml + ".fxml: " + e.getMessage());
                e.printStackTrace();
                Platform.exit();
                System.exit(1);
            }
        }

        Platform.exit();
    }
}
