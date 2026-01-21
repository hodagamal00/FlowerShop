package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class SecondaryController {

    @FXML private Button apply_changes;
    @FXML private Button edit_product;
    @FXML private Button back_button;

    @FXML private DialogPane flower_details;
    @FXML private DialogPane flower_name;
    @FXML private DialogPane flower_price;

    @FXML private ImageView flower_image;

    @FXML private TextArea setDetails;
    @FXML private TextField setName;
    @FXML private TextField setPrice;

    private boolean canEditProducts;

    /** Holds the selected product */
    private Product currentProduct;

    private boolean hasProductEditPermission() {
        Account currentUser = SimpleClient.getUser();
        return currentUser != null && currentUser.getPrivilegeLevel() >= 2;
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /** Load selected product */
    private Product requireSelectedProduct(String actionContext) {
        Product p = CatalogController.getCurrent_button();
        if (p == null) {
            showAlert(Alert.AlertType.ERROR,
                    "Product Unavailable",
                    "No Product Selected",
                    "Cannot " + actionContext + " because no product is selected.");
        }
        return p;
    }

    //----------------------------------------------------------------//
    // EVENT HANDLERS
    //----------------------------------------------------------------//

    @FXML
    void returnWindow(ActionEvent event) {
        CatalogController.setReturnedFromSecondaryController(true);
        NavigationService.getInstance().navigate("Catalog");
    }

    @FXML
    void edit_product(ActionEvent event) {
        apply_changes.setVisible(true);
        canEditProducts = hasProductEditPermission();
        if (!canEditProducts) {
            showAlert(Alert.AlertType.WARNING, "Insufficient Permissions",
                    "Editing Restricted",
                    "Only employees and managers can modify product details.");
            return;
        }
        currentProduct = requireSelectedProduct("edit product details");
        if (currentProduct == null) {
            return;
        }

        // Show editable fields
        setDetails.setVisible(true);
        setName.setVisible(true);
        setPrice.setVisible(true);
        apply_changes.setVisible(true);
        // Load existing details
        setDetails.setText(currentProduct.getDetails());
        setName.setText(currentProduct.getName());
        setPrice.setText(String.valueOf(currentProduct.getPrice()));
    }

    @FXML
    void updateProduct(ActionEvent event)
    {
        if (!hasProductEditPermission()) {
            showAlert(Alert.AlertType.WARNING, "Insufficient Permissions",
                    "Editing Restricted",
                    "Only employees and managers can modify product details.");
            return;
        }

        if (currentProduct == null) {
            showAlert(Alert.AlertType.ERROR, "Product Unavailable",
                    "No Product Selected",
                    "Cannot update product details because no product is selected.");
            return;
        }

        try {
            double newPrice = Double.parseDouble(setPrice.getText());
            currentProduct.setPrice(newPrice);
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR,
                    "Invalid Price",
                    "Price must be numeric",
                    "The price was not updated.");
            return;
        }

        currentProduct.setDetails(setDetails.getText());
        currentProduct.setName(setName.getText());

        // Update UI
        flower_details.setContentText(currentProduct.getDetails());
        flower_name.setContentText(currentProduct.getName());
        flower_price.setContentText(String.valueOf(currentProduct.getPrice()));

        apply_changes.setVisible(false);
        setDetails.setVisible(false);
        setName.setVisible(false);
        setPrice.setVisible(false);

        try {
            SimpleClient.getClient().sendToServer(currentProduct);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Update Failed",
                    "Server Error",
                    "Could not send update to server.");
        }
    }

    //----------------------------------------------------------------//
    // INITIALIZATION
    //----------------------------------------------------------------//

    @FXML
    void initialize() throws MalformedURLException {

        canEditProducts = hasProductEditPermission();

        setDetails.setVisible(false);
        setName.setVisible(false);
        setPrice.setVisible(false);
        apply_changes.setVisible(false);

        // set all fields details using product object
        currentProduct = CatalogController.getCurrent_button();
        if (currentProduct == null) {
            showAlert(Alert.AlertType.ERROR,
                    "Product Unavailable",
                    "No Product Selected",
                    "Cannot load product details.");
            return;
        }

        flower_details.setContentText(currentProduct.getDetails());
        flower_price.setContentText(String.valueOf(currentProduct.getPrice()));
        flower_name.setContentText(currentProduct.getName());

        String img_path = "src/main/resources/images/flower" + currentProduct.getID() + ".png";
        File file = new File(img_path);
        flower_image.setImage(new Image(file.toURI().toURL().toExternalForm(), true));
    }
}
