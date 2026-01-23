package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class OrderProductParser {

    private OrderProductParser() {
    }

    public static String buildProductsSummary(List<Product> cart) {
        if (cart == null || cart.isEmpty()) {
            return "";
        }
        Map<Integer, Integer> counts = new LinkedHashMap<>();
        for (Product product : cart) {
            if (product == null) {
                continue;
            }
            int id = product.getID();
            if (id <= 0) {
                continue;
            }
            counts.put(id, counts.getOrDefault(id, 0) + 1);
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(entry.getKey()).append(":").append(entry.getValue());
        }
        return builder.toString();
    }

    public static List<OrderItem> parseItems(String products, Map<Integer, Product> catalog) {
        List<OrderItem> items = new ArrayList<>();
        if (products == null || products.trim().isEmpty()) {
            return items;
        }
        String trimmed = products.trim();
        if (trimmed.contains(":")) {
            parseIdQuantityItems(trimmed, catalog, items);
        } else {
            parseLegacyItems(trimmed, items);
        }
        return items;
    }

    private static void parseIdQuantityItems(String products, Map<Integer, Product> catalog, List<OrderItem> items) {
        for (String token : products.split(",")) {
            String entry = token.trim();
            if (entry.isEmpty()) {
                continue;
            }
            String[] parts = entry.split(":");
            if (parts.length < 2) {
                continue;
            }
            int id = parseInt(parts[0]);
            int quantity = parseInt(parts[1]);
            if (id <= 0 || quantity <= 0) {
                continue;
            }
            Product product = catalog != null ? catalog.get(id) : null;
            String name = product != null ? product.getName() : "Product #" + id;
            double unitPrice = product != null ? product.getPrice() : 0.0;
            items.add(new OrderItem(id, name, quantity, unitPrice));
        }
    }

    private static void parseLegacyItems(String products, List<OrderItem> items) {
        for (String token : products.split("%")) {
            String entry = token.trim();
            if (entry.isEmpty()) {
                continue;
            }
            String[] parts = entry.split(" - ", 2);
            String name = parts[0].trim();
            if (name.isEmpty()) {
                continue;
            }
            double price = parts.length > 1 ? parsePrice(parts[1]) : 0.0;
            items.add(new OrderItem(-1, name, 1, price));
        }
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static double parsePrice(String value) {
        if (value == null) {
            return 0.0;
        }
        String normalized = value.replaceAll("[^0-9.]", "");
        if (normalized.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    public static class OrderItem {
        private final int productId;
        private final String name;
        private final int quantity;
        private final double unitPrice;

        public OrderItem(int productId, String name, int quantity, double unitPrice) {
            this.productId = productId;
            this.name = name;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public int getProductId() {
            return productId;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public double getLineTotal() {
            return unitPrice * quantity;
        }

        public String formatLine() {
            return String.format(Locale.US, "%s x%d (₪%.2f)", name, quantity, unitPrice);
        }
    }
}
