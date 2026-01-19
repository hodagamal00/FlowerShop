package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CartController
{
    @FXML
    private ListView<String> CartItemsList;

    @FXML
    void initialize() {
        if (CartItemsList == null) {
            return;
        }
        CartItemsList.getItems().clear();
        for (var product : CartService.getInstance().getItems()) {
            if (product != null) {
                CartItemsList.getItems().add(product.getName());
            }
        }
    }
}
