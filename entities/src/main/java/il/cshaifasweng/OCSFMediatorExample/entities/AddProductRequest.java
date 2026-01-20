package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class AddProductRequest implements Serializable {

    private Product product;

    public AddProductRequest(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
