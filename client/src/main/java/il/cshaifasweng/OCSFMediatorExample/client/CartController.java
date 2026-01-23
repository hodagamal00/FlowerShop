package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartController
{
    @FXML
    private ListView<String> CartItemsList;

    @FXML
    void initialize() {
        if (CartItemsList == null) {
            return;
        }
        List<Product> items = CartService.getInstance().getItems();
        CartItemsList.getItems().setAll(buildCartLineLabels(items));
    }

    private List<String> buildCartLineLabels(List<Product> items) {
        List<CartLine> lines = buildCartLines(items);
        List<String> labels = new ArrayList<>();
        for (CartLine line : lines) {
            String label = line.quantity > 1
                    ? String.format("%s x%d", line.product.getName(), line.quantity)
                    : line.product.getName();
            labels.add(label);
        }
        return labels;
    }

    private List<CartLine> buildCartLines(List<Product> items) {
        Map<String, CartLine> lines = new LinkedHashMap<>();
        if (items != null) {
            for (Product product : items) {
                if (product == null) {
                    continue;
                }
                String key = product.getID() + "|" + product.getName() + "|" + product.getPrice();
                CartLine line = lines.get(key);
                if (line == null) {
                    line = new CartLine(product, 0);
                    lines.put(key, line);
                }
                line.quantity += 1;
            }
        }
        return new ArrayList<>(lines.values());
    }

    private static class CartLine {
        private final Product product;
        private int quantity;

        private CartLine(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }
}
