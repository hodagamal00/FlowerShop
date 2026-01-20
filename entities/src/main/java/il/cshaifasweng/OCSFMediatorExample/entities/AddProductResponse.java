package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class AddProductResponse implements Serializable {

    private boolean success;
    private String error;
    private Product createdProduct;

    public AddProductResponse(boolean success, String error, Product createdProduct) {
        this.success = success;
        this.error = error;
        this.createdProduct = createdProduct;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public Product getCreatedProduct() {
        return createdProduct;
    }
}
