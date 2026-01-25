package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsController {

    @FXML private ImageView productImage;
    @FXML private Text productNameText;
    @FXML private Label idLabel;
    @FXML private Label skuLabel;
    @FXML private Label categoryLabel;
    @FXML private Label colorLabel;
    @FXML private Text originalPriceText;
    @FXML private Text discountedPriceText;
    @FXML private Label saleLabel;
    @FXML private javafx.scene.layout.Region saleUnderline;
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
    @FXML private Button viewCartBtn;
    @FXML private Button closeBtn;

    private static Product pendingProduct;
    private Product currentProduct;
    private Product selectedProduct;

    @FXML
    void initialize() {
        if (quantitySpinner != null) {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1);
            quantitySpinner.setValueFactory(valueFactory);
        }
        bindManagedToVisible(originalPriceText, discountedPriceText, saleLabel, saleUnderline);
        if (selectedProduct == null && pendingProduct != null) {
            setProduct(pendingProduct);
            pendingProduct = null;
        } else if (selectedProduct == null) {
            showMissingProductState();
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

    public static void setPendingProduct(Product product) {
        pendingProduct = product;
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
        String details = product.getDetails();
        descriptionText.setText(details != null && !details.isBlank() ? details : "No description available");

        // Load product image (or fallback to placeholder)
        setProductImage(product);

        // Handle pricing
        priceRangeLabel.setVisible(false);
        priceRangeLabel.setManaged(false);
        originalPriceText.setVisible(false);
        saleLabel.setVisible(false);
        saleUnderline.setVisible(false);
        discountBadge.setVisible(false);
        customOptionsContainer.setVisible(false);
        customOptionsContainer.setManaged(false);

        if (product.isCustomProduct()) {
            // Show price range for custom products
            priceRangeLabel.setVisible(true);
            priceRangeLabel.setManaged(true);
            priceRangeLabel.setText(String.format("Price Range: $%.2f - $%.2f", 
                product.getPriceRangeMin(), product.getPriceRangeMax()));
            discountedPriceText.setText("Custom Price");
            
            // Show custom options
            customOptionsContainer.setVisible(true);
            customOptionsContainer.setManaged(true);
        } else {
            // Regular product pricing
            PricingService.PricingResult pricing = PricingService.calculatePricing(product, SimpleClient.getAccount());
            double actualPrice = pricing.getFinalPrice();
            discountedPriceText.setText(String.format("$%.2f", actualPrice));

            // Show discount information if on promotion
            if (pricing.isPromotionApplied()) {
                double originalPrice = pricing.getBasePrice();
                originalPriceText.setText(String.format("$%.2f", originalPrice));
                originalPriceText.setVisible(true);

                saleLabel.setVisible(true);
                saleUnderline.setVisible(true);
                
                discountBadge.setText(String.format("%.0f%% OFF", product.getNormalizedDiscountPercent()));
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

        if (!ensureLoggedIn("add items to cart")) {
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
        CartService.getInstance().addProduct(currentProduct, quantity);

        successMessage.setText(String.format("✓ Added %d item(s) to cart successfully!", quantity));
        successMessage.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> successMessage.setVisible(false));
        pause.play();
    }

    @FXML
    void buyNow() {
        errorMessage.setVisible(false);
        successMessage.setVisible(false);

        if (currentProduct == null) {
            errorMessage.setText("Product details are not available.");
            errorMessage.setVisible(true);
            return;
        }

        if (!ensureLoggedIn("buy items")) {
            return;
        }

        int quantity = quantitySpinner.getValue();
        List<Product> checkoutItems = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            checkoutItems.add(currentProduct);
        }

        Account account = SimpleClient.getAccount();
        PassAccountEventCheckout checkoutEvent = new PassAccountEventCheckout(account);
        checkoutEvent.productsToCheckout = checkoutItems;

        NavigationService.getInstance().navigate("checkout");
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(checkoutEvent);
                    }
                }, 500
        );
    }

    @FXML
    void viewCart() {
        if (!ensureLoggedIn("view the cart")) {
            return;
        }
        NavigationService.getInstance().navigate("cart");
    }

    @FXML
    void closeModal() {
        Stage stage = getCurrentStage();
        if (stage != null) {
            stage.close();
        }
    }

    private void setProductImage(Product product) {
        Image image = null;
        String imagePath = product.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            String normalizedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
            try (InputStream inputStream = getClass().getResourceAsStream(normalizedPath)) {
                if (inputStream != null) {
                    image = new Image(inputStream);
                }
            } catch (Exception e) {
                System.out.println("Could not load product image: " + imagePath);
            }
        }
        if (image == null) {
            try (InputStream inputStream = getClass().getResourceAsStream("placeholder.png")) {
                if (inputStream != null) {
                    image = new Image(inputStream);
                }
            } catch (Exception e) {
                System.out.println("Placeholder image not found.");
            }
            if (image == null) {
                try (InputStream inputStream = getClass().getResourceAsStream("/placeholder.png")) {
                    if (inputStream != null) {
                        image = new Image(inputStream);
                    }
                } catch (Exception e) {
                    System.out.println("Placeholder image not found.");
                }
            }
        }
        if (image != null && productImage != null) {
            productImage.setImage(image);
        }
    }

    private Stage getCurrentStage() {
        if (discountedPriceText != null && discountedPriceText.getScene() != null) {
            return (Stage) discountedPriceText.getScene().getWindow();
        }
        if (productNameText != null && productNameText.getScene() != null) {
            return (Stage) productNameText.getScene().getWindow();
        }
        return null;
    }

    private boolean ensureLoggedIn(String actionLabel) {
        if (SimpleClient.getAccount() != null) {
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Required");
        alert.setHeaderText("Please log in to continue.");
        alert.setContentText("Guests can browse products, but you need an account to " + actionLabel + ".");
        alert.showAndWait();
        NavigationService.getInstance().navigate("Login");
        return false;
    }

    private void bindManagedToVisible(Node... nodes) {
        for (Node node : nodes) {
            if (node == null) {
                continue;
            }
            node.managedProperty().bind(node.visibleProperty());
        }
    }

    private void showMissingProductState() {
        if (productNameText != null) {
            productNameText.setText("Product details unavailable");
        }
        if (descriptionText != null) {
            descriptionText.setText("Please return to the catalog and select a product.");
        }
        if (addToCartBtn != null) {
            addToCartBtn.setDisable(true);
        }
        if (buyNowBtn != null) {
            buyNowBtn.setDisable(true);
        }
    }
}
