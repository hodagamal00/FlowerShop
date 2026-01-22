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
        CartItemsList.getItems().setAll(CartService.getInstance().getItems().stream()
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.collectingAndThen(
                        java.util.stream.Collectors.toMap(
                                product -> product.getID() + "|" + product.getName() + "|" + product.getPrice(),
                                product -> new int[] {1},
                                (existing, ignored) -> {
                                    existing[0] += 1;
                                    return existing;
                                },
                                java.util.LinkedHashMap::new
                        ),
                        map -> {
                            java.util.List<String> labels = new java.util.ArrayList<>();
                            for (var entry : map.entrySet()) {
                                String[] parts = entry.getKey().split("\\|", 3);
                                String name = parts.length > 1 ? parts[1] : entry.getKey();
                                int quantity = entry.getValue()[0];
                                labels.add(quantity > 1 ? String.format("%s x%d", name, quantity) : name);
                            }
                            return labels;
                        }
                )));
    }
}
