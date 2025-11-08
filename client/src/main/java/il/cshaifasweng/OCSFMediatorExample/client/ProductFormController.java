package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProductFormController {

    @FXML private Label formTitleLabel;
    @FXML private ImageView productImageView;
    @FXML private Button chooseImageBtn;
    @FXML private Button removeImageBtn;
    @FXML private Label imagePathLabel;
    @FXML private TextField nameField;
    @FXML private TextField skuField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ComboBox<String> colorCombo;
    @FXML private TextField priceField;
    @FXML private TextArea detailsArea;
    @FXML private CheckBox promotionCheckBox;
    @FXML private Label discountLabel;
    @FXML private TextField discountField;
    @FXML private CheckBox customProductCheckBox;
    @FXML private VBox customFieldsContainer;
    @FXML private ComboBox<String> customTypeCombo;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private Label statusLabel;

    private Product currentProduct;
    private String selectedImagePath;
    private boolean isEditMode = false;
    private static final String IMAGES_FOLDER = "product_images/";
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB

    @FXML
    void initialize() {
        setupComboBoxes();
        setupValidation();
        setDefaultImage();
    }

    private void setupComboBoxes() {
        // Category options
        categoryCombo.setItems(FXCollections.observableArrayList(
            "Bouquet",
            "Arrangement",
            "Flowering Pot",
            "Bridal Bouquet",
            "Single Flower",
            "Mixed Flowers",
            "Gift Set"
        ));
        
        // Color options
        colorCombo.setItems(FXCollections.observableArrayList(
            "Red",
            "Pink",
            "White",
            "Yellow",
            "Orange",
            "Purple",
            "Blue",
            "Mixed",
            "Pastel"
        ));
        
        // Custom type options
        customTypeCombo.setItems(FXCollections.observableArrayList(
            "Bridal Bouquet",
            "Anniversary Arrangement",
            "Birthday Special",
            "Flowering Pot Custom",
            "Corporate Gift"
        ));
    }

    private void setupValidation() {
        // Only allow numbers and decimal point in price fields
        priceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                priceField.setText(oldVal);
            }
        });
        
        discountField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                discountField.setText(oldVal);
            }
        });
        
        minPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                minPriceField.setText(oldVal);
            }
        });
        
        maxPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                maxPriceField.setText(oldVal);
            }
        });
    }

    private void setDefaultImage() {
        // Set a placeholder image
        try {
            Image placeholderImage = new Image(getClass().getResourceAsStream("placeholder.png"));
            productImageView.setImage(placeholderImage);
        } catch (Exception e) {
            // If placeholder doesn't exist, just leave empty
            System.out.println("Placeholder image not found");
        }
    }

    /**
     * Opens file chooser to select product image
     */
    @FXML
    void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Product Image");
        
        // Set file extension filters
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        // Show open dialog
        Stage stage = (Stage) chooseImageBtn.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            // Check file size
            if (selectedFile.length() > MAX_IMAGE_SIZE) {
                showStatus("Error: Image size exceeds 5MB limit", true);
                return;
            }
            
            try {
                // Create images directory if it doesn't exist
                Path imagesDir = Paths.get("client/src/main/resources/il/cshaifasweng/OCSFMediatorExample/client/" + IMAGES_FOLDER);
                if (!Files.exists(imagesDir)) {
                    Files.createDirectories(imagesDir);
                }
                
                // Generate unique filename
                String originalFileName = selectedFile.getName();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
                
                // Copy file to project images folder
                Path destinationPath = imagesDir.resolve(uniqueFileName);
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                
                // Store relative path for database
                selectedImagePath = IMAGES_FOLDER + uniqueFileName;
                
                // Display image preview
                Image image = new Image(selectedFile.toURI().toString());
                productImageView.setImage(image);
                
                // Update path label
                imagePathLabel.setText(uniqueFileName);
                imagePathLabel.setStyle("-fx-text-fill: #81c784; -fx-font-size: 11px;");
                
                showStatus("Image uploaded successfully", false);
                
            } catch (IOException e) {
                e.printStackTrace();
                showStatus("Error uploading image: " + e.getMessage(), true);
            }
        }
    }

    /**
     * Removes the selected image
     */
    @FXML
    void removeImage() {
        selectedImagePath = null;
        setDefaultImage();
        imagePathLabel.setText("No image selected");
        imagePathLabel.setStyle("-fx-text-fill: #7b1fa2; -fx-font-size: 11px;");
        showStatus("Image removed", false);
    }

    /**
     * Toggles promotion-related fields
     */
    @FXML
    void togglePromotionFields() {
        boolean isEnabled = promotionCheckBox.isSelected();
        discountLabel.setDisable(!isEnabled);
        discountField.setDisable(!isEnabled);
        
        if (!isEnabled) {
            discountField.clear();
        }
    }

    /**
     * Toggles custom product fields
     */
    @FXML
    void toggleCustomFields() {
        boolean isEnabled = customProductCheckBox.isSelected();
        customFieldsContainer.setDisable(!isEnabled);
        
        if (!isEnabled) {
            customTypeCombo.getSelectionModel().clearSelection();
            minPriceField.clear();
            maxPriceField.clear();
        }
    }

    /**
     * Validates and saves the product
     */
    @FXML
    void saveProduct() {
        // Validate required fields
        if (!validateForm()) {
            return;
        }
        
        try {
            // Create or update product
            if (currentProduct == null) {
                currentProduct = new Product();
                // Generate new ID (in real app, this would be auto-generated by database)
                currentProduct.setID((int) (Math.random() * 100000));
            }
            
            // Set basic fields
            currentProduct.setName(nameField.getText().trim());
            currentProduct.setSku(skuField.getText().trim());
            currentProduct.setCategory(categoryCombo.getValue());
            currentProduct.setColor(colorCombo.getValue());
            currentProduct.setPrice(Double.parseDouble(priceField.getText().trim()));
            currentProduct.setDetails(detailsArea.getText().trim());
            
            // Set image path
            if (selectedImagePath != null) {
                currentProduct.setImage(selectedImagePath);
            }
            
            // Set promotion fields
            currentProduct.setPromotion(promotionCheckBox.isSelected());
            if (promotionCheckBox.isSelected() && !discountField.getText().isEmpty()) {
                currentProduct.setDiscountPercent(Double.parseDouble(discountField.getText()));
            } else {
                currentProduct.setDiscountPercent(0.0);
            }
            
            // Set custom product fields
            currentProduct.setCustomProduct(customProductCheckBox.isSelected());
            if (customProductCheckBox.isSelected()) {
                currentProduct.setCustomType(customTypeCombo.getValue());
                if (!minPriceField.getText().isEmpty()) {
                    currentProduct.setPriceRangeMin(Double.parseDouble(minPriceField.getText()));
                }
                if (!maxPriceField.getText().isEmpty()) {
                    currentProduct.setPriceRangeMax(Double.parseDouble(maxPriceField.getText()));
                }
            }
            
            // Send product to server for saving
            try {
                UpdateMessage message = new UpdateMessage("product", "");
                if (isEditMode) {
                    message.setUpdateFunction("edit");
                } else {
                    message.setUpdateFunction("add");
                }
                message.setProduct(currentProduct);
                SimpleClient.getClient().sendToServer(message);
                System.out.println("Product " + (isEditMode ? "updated" : "saved") + " successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                showStatus("Error saving product to server: " + e.getMessage(), true);
                return;
            }
            
            showStatus("Product saved successfully!", false);
            
            // Close window after 1 second
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(() -> {
                        Stage stage = (Stage) saveBtn.getScene().getWindow();
                        stage.close();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Error saving product: " + e.getMessage(), true);
        }
    }

    /**
     * Validates the form fields
     */
    private boolean validateForm() {
        // Check required fields
        if (nameField.getText().trim().isEmpty()) {
            showStatus("Error: Product name is required", true);
            nameField.requestFocus();
            return false;
        }
        
        if (skuField.getText().trim().isEmpty()) {
            showStatus("Error: SKU is required", true);
            skuField.requestFocus();
            return false;
        }
        
        if (categoryCombo.getValue() == null) {
            showStatus("Error: Category is required", true);
            categoryCombo.requestFocus();
            return false;
        }
        
        if (colorCombo.getValue() == null) {
            showStatus("Error: Color is required", true);
            colorCombo.requestFocus();
            return false;
        }
        
        if (priceField.getText().trim().isEmpty()) {
            showStatus("Error: Price is required", true);
            priceField.requestFocus();
            return false;
        }
        
        // Validate price is a valid number
        try {
            double price = Double.parseDouble(priceField.getText());
            if (price <= 0) {
                showStatus("Error: Price must be greater than 0", true);
                priceField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showStatus("Error: Invalid price format", true);
            priceField.requestFocus();
            return false;
        }
        
        // Validate promotion discount
        if (promotionCheckBox.isSelected()) {
            if (discountField.getText().trim().isEmpty()) {
                showStatus("Error: Discount percentage is required for promotions", true);
                discountField.requestFocus();
                return false;
            }
            
            try {
                double discount = Double.parseDouble(discountField.getText());
                if (discount < 0 || discount > 100) {
                    showStatus("Error: Discount must be between 0 and 100", true);
                    discountField.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                showStatus("Error: Invalid discount format", true);
                discountField.requestFocus();
                return false;
            }
        }
        
        // Validate custom product fields
        if (customProductCheckBox.isSelected()) {
            if (customTypeCombo.getValue() == null) {
                showStatus("Error: Custom type is required for custom products", true);
                customTypeCombo.requestFocus();
                return false;
            }
            
            if (!minPriceField.getText().isEmpty() && !maxPriceField.getText().isEmpty()) {
                try {
                    double minPrice = Double.parseDouble(minPriceField.getText());
                    double maxPrice = Double.parseDouble(maxPriceField.getText());
                    if (minPrice >= maxPrice) {
                        showStatus("Error: Min price must be less than max price", true);
                        minPriceField.requestFocus();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    showStatus("Error: Invalid price range format", true);
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     * Closes the form window
     */
    @FXML
    void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Loads product data for editing
     */
    public void setProduct(Product product) {
        this.currentProduct = product;
        this.isEditMode = true;
        
        formTitleLabel.setText("Edit Product");
        
        // Populate fields
        nameField.setText(product.getName());
        skuField.setText(product.getSku());
        categoryCombo.setValue(product.getCategory());
        colorCombo.setValue(product.getColor());
        priceField.setText(String.valueOf(product.getPrice()));
        detailsArea.setText(product.getDetails());
        
        // Load image if exists
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            selectedImagePath = product.getImage();
            try {
                String imagePath = getClass().getResource(product.getImage()).toExternalForm();
                Image image = new Image(imagePath);
                productImageView.setImage(image);
                imagePathLabel.setText(product.getImage());
                imagePathLabel.setStyle("-fx-text-fill: #81c784; -fx-font-size: 11px;");
            } catch (Exception e) {
                System.out.println("Could not load product image: " + e.getMessage());
            }
        }
        
        // Promotion settings
        promotionCheckBox.setSelected(product.isPromotion());
        if (product.isPromotion()) {
            discountField.setText(String.valueOf(product.getDiscountPercent()));
            togglePromotionFields();
        }
        
        // Custom product settings
        customProductCheckBox.setSelected(product.isCustomProduct());
        if (product.isCustomProduct()) {
            customTypeCombo.setValue(product.getCustomType());
            minPriceField.setText(String.valueOf(product.getPriceRangeMin()));
            maxPriceField.setText(String.valueOf(product.getPriceRangeMax()));
            toggleCustomFields();
        }
    }

    /**
     * Shows status message
     */
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? 
            "-fx-text-fill: #e57373; -fx-font-size: 13px; -fx-font-weight: bold;" : 
            "-fx-text-fill: #81c784; -fx-font-size: 13px; -fx-font-weight: bold;");
        statusLabel.setVisible(true);
        
        // Auto-hide success messages after 3 seconds
        if (!isError) {
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    javafx.application.Platform.runLater(() -> statusLabel.setVisible(false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
