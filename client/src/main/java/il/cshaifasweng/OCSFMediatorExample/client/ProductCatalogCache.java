package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProductCatalogCache {
    private static final Map<Integer, Product> PRODUCTS = new ConcurrentHashMap<>();

    private ProductCatalogCache() {
    }

    public static void update(List<Product> products) {
        if (products == null) {
            return;
        }
        PRODUCTS.clear();
        for (Product product : products) {
            if (product != null) {
                PRODUCTS.put(product.getID(), product);
            }
        }
    }

    public static Product getById(int id) {
        return PRODUCTS.get(id);
    }

    public static Map<Integer, Product> snapshot() {
        if (PRODUCTS.isEmpty()) {
            return Collections.emptyMap();
        }
        return new HashMap<>(PRODUCTS);
    }
}
