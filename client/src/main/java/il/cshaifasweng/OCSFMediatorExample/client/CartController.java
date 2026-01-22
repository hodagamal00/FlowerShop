package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.Locale;

public class CartController
{
    @FXML
    private ListView<Product> CartItemsList;

    @FXML
    void initialize() {
        if (CartItemsList == null) {
            return;
        }
        CartItemsList.setItems(CartService.getInstance().getObservableItems());
        CartItemsList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatCartItemDisplay(item));
            }
        });
    }

    private String formatCartItemDisplay(Product product) {
        if (product == null) {
            return "";
        }
        if (!product.isCustomProduct()) {
            return product.getName();
        }
        String type = safeTrim(product.getCustomType());
        String color = safeTrim(product.getColor());
        StringBuilder title = new StringBuilder("Custom Item");
        if (!type.isEmpty() || !color.isEmpty()) {
            title.append(" (");
            if (!type.isEmpty()) {
                title.append(type);
            }
            if (!color.isEmpty()) {
                if (!type.isEmpty()) {
                    title.append(", ");
                }
                title.append(color);
            }
            title.append(")");
        }
        String budgetLabel = formatCustomBudgetLabel(product);
        if (!budgetLabel.isEmpty()) {
            title.append("\n").append(budgetLabel);
        }
        return title.toString();
    }

    private String formatCustomBudgetLabel(Product product) {
        double minBudget = product.getPriceRangeMin();
        double maxBudget = product.getPriceRangeMax();
        if (minBudget <= 0 && maxBudget <= 0) {
            return "";
        }
        if (minBudget > 0 && maxBudget > 0 && maxBudget > minBudget) {
            return String.format(Locale.US, "Budget: %s–%s ₪", formatBudgetValue(minBudget), formatBudgetValue(maxBudget));
        }
        double value = maxBudget > 0 ? maxBudget : minBudget;
        return String.format(Locale.US, "Budget: %s ₪", formatBudgetValue(value));
    }

    private String formatBudgetValue(double value) {
        if (value == Math.rint(value)) {
            return String.format(Locale.US, "%.0f", value);
        }
        return String.format(Locale.US, "%.2f", value);
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}
