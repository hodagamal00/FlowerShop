package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartService {
    private static final CartService INSTANCE = new CartService();
    private final ObservableList<Product> items = FXCollections.observableArrayList();

    private CartService() {
    }

    public static CartService getInstance() {
        return INSTANCE;
    }

    public void addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }
        for (int i = 0; i < quantity; i++) {
            items.add(product);
        }
    }

    public List<Product> getItems() {
        return Collections.unmodifiableList(items);
    }

    public ObservableList<Product> getObservableItems() {
        return items;
    }

    public List<Product> getItemsCopy() {
        return new ArrayList<>(items);
    }

    public void clear() {
        items.clear();
    }

    public int getItemCount() {
        return items.size();
    }
}
