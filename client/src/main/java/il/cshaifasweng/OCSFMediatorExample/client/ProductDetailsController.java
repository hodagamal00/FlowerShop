package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductDetailsController {

    @FXML private Button backToCatalogBtn;
    @FXML private Button viewCartBtn;
    @FXML private Button closeBtn;
    @FXML private ImageView productImage;
    @FXML private Text productNameText;
    @FXML private Label idLabel;
    @FXML private Label skuLabel;
    @FXML private Label categoryLabel;
    @FXML private Label colorLabel;
    @FXML private Text originalPriceText;
    @FXML private Text priceText;
    @FXML private Label discountBadge;
    @FXML private Label priceRangeLabel;
    @FXML private Text descriptionText;
    @FXML private VBox customOptionsContainer;
    @FXML private TextField customColorField;
    @FXML private TextField customPriceField;
    @FXML private TextArea greetingCardArea;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Button addToCartBtn;
    @FXML private Button buyNowBtn;
    @FXML private Label successMessage;
    @FXML private Label errorMessage;

    private Product currentProduct;
    private Product selectedProduct;

    @FXML
    void initialize() {
        if (quantitySpinner != null) {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1);
            quantitySpinner.setValueFactory(valueFactory);
        }
    }

    /**
     * Instance method to set the product to display
     * Call this after loading the ProductDetails.fxml scene
     */
    public void setProduct(Product product) {
        selectedProduct = product;
        if (selectedProduct != null) {
            loadProductDetails(selectedProduct);
        }
    }

    /**
     * Load and display product details
     */
    private void loadProductDetails(Product product) {
        this.currentProduct = product;

        // Basic information
        productNameText.setText(product.getName());
        idLabel.setText(String.valueOf(product.getID()));
        skuLabel.setText(product.getSku() != null ? product.getSku() : "N/A");
        categoryLabel.setText(product.getCategory() != null ? product.getCategory() : "General");
        colorLabel.setText(product.getColor() != null ? product.getColor() : "Mixed");
        descriptionText.setText(product.getDetails() != null ? product.getDetails() : "No description available");

        // Load product image
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            try {
                Image img = new Image(getClass().getResourceAsStream(product.getImage()));
                productImage.setImage(img);
            } catch (Exception e) {
                System.out.println("Could not load product image: " + product.getImage());
                // Use placeholder if image not found
            }
        }

        // Handle pricing
        if (product.isCustomProduct()) {
            // Show price range for custom products
            priceRangeLabel.setVisible(true);
            priceRangeLabel.setManaged(true);
            priceRangeLabel.setText(String.format("Price Range: $%.2f - $%.2f", 
                product.getPriceRangeMin(), product.getPriceRangeMax()));
            priceText.setText("Custom Price");
            
            // Show custom options
            customOptionsContainer.setVisible(true);
            customOptionsContainer.setManaged(true);
        } else {
            // Regular product pricing
            double actualPrice = product.getActualPrice();
            priceText.setText(String.format("$%.2f", actualPrice));

            // Show discount information if on promotion
            if (product.isPromotion() && product.getDiscountPercent() > 0) {
                double originalPrice = product.getPrice();
                originalPriceText.setText(String.format("$%.2f", originalPrice));
                originalPriceText.setVisible(true);
                
                discountBadge.setText(String.format("%.0f%% OFF", product.getDiscountPercent()));
                discountBadge.setVisible(true);
            }
        }
    }

    @FXML
    void addToCart() {
        // Hide previous messages
        successMessage.setVisible(false);
        errorMessage.setVisible(false);

        if (currentProduct == null) {
            errorMessage.setText("Product details are not available.");
            errorMessage.setVisible(true);
            return;
        }

        // Check if user is logged in
        if (SimpleClient.getAccount() == null) {
            errorMessage.setText("Please login to add items to cart");
            errorMessage.setVisible(true);
            return;
        }

        // Validate custom product fields if applicable
        if (currentProduct.isCustomProduct()) {
            if (customPriceField.getText().isEmpty()) {
                errorMessage.setText("Please enter your desired price");
                errorMessage.setVisible(true);
                return;
            }
            
            try {
                double customPrice = Double.parseDouble(customPriceField.getText());
                if (customPrice < currentProduct.getPriceRangeMin() || customPrice > currentProduct.getPriceRangeMax()) {
                    errorMessage.setText(String.format("Price must be between $%.2f and $%.2f", 
                        currentProduct.getPriceRangeMin(), currentProduct.getPriceRangeMax()));
                    errorMessage.setVisible(true);
                    return;
                }
            } catch (NumberFormatException e) {
                errorMessage.setText("Please enter a valid price");
                errorMessage.setVisible(true);
                return;
            }
        }

        // Get quantity
        int quantity = quantitySpinner.getValue();

        // Add to cart logic here
        // TODO: Implement cart addition via SimpleClient
        // For now, just show success message
        
        successMessage.setText(String.format("✓ Added %d item(s) to cart successfully!", quantity));
        successMessage.setVisible(true);

        // Auto-hide success message after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> successMessage.setVisible(false));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void buyNow() {
        // Add to cart first
        addToCart();
        
        // If successful, navigate to checkout
        if (successMessage.isVisible()) {
            try {
                Thread.sleep(500); // Brief pause to show success message
                viewCart(); // Go to cart/checkout
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void viewCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cart.fxml"));
            Parent root = loader.load();
            Stage stage = getCurrentStage();
            if (stage == null) {
                return;
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading cart page: " + e.getMessage());
        }
    }

    @FXML
    void goBackToCatalog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Catalog.fxml"));
            Parent root = loader.load();
            Stage stage = getCurrentStage();
            if (stage == null) {
                return;
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading catalog page: " + e.getMessage());
        }
    }

    @FXML
    void closeModal() {
        Stage stage = getCurrentStage();
        if (stage != null) {
            stage.close();
        }
    }

    private Stage getCurrentStage() {
        if (closeBtn != null && closeBtn.getScene() != null) {
            return (Stage) closeBtn.getScene().getWindow();
        }
        if (viewCartBtn != null && viewCartBtn.getScene() != null) {
            return (Stage) viewCartBtn.getScene().getWindow();
        }
        if (backToCatalogBtn != null && backToCatalogBtn.getScene() != null) {
            return (Stage) backToCatalogBtn.getScene().getWindow();
        }
        if (priceText != null && priceText.getScene() != null) {
            return (Stage) priceText.getScene().getWindow();
        }
        return null;
    }
}
