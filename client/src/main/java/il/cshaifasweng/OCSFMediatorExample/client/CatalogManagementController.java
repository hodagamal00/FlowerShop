package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.AddProductResponse;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.beans.property.ReadOnlyBooleanWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CatalogManagementController {

    @FXML private Button dashboardBtn;
    @FXML private Button homeBtn;
    @FXML private Button addProductBtn;
    @FXML private TextField searchField;
    @FXML private Button searchBtn;
    @FXML private Button refreshBtn;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private ComboBox<String> flowerTypeFilter;
    @FXML private ComboBox<String> colorFilter;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private Button applyFiltersBtn;
    @FXML private Button clearFiltersBtn;
    @FXML private Label productCountLabel;
    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, String> imageCol;
    @FXML private TableColumn<Product, Integer> idCol;
    @FXML private TableColumn<Product, String> skuCol;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, String> categoryCol;
    @FXML private TableColumn<Product, String> colorCol;
    // Use Double for price column; the Product entity exposes price as a double.
    @FXML private TableColumn<Product, Double> priceCol;
    @FXML private TableColumn<Product, Boolean> promotionCol;
    @FXML private TableColumn<Product, Void> actionsCol;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button togglePromotionBtn;
    @FXML private Label successMessage;
    @FXML private Label errorMessage;

    private final ObservableList<Product> productsList = FXCollections.observableArrayList();
    private final ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    private boolean canAddProducts = false;
    private boolean canEditPromotions = false;

    @FXML
    void initialize() {
        // Register this controller with EventBus to receive updates from the server
        EventBus.getDefault().register(this);

        // Setup table columns and load initial data
        setupTableColumns();
        setupFilterControls();
        applyPrivilegeVisibility(SimpleClient.getAccount());
        loadProducts();
    }

    /**
     * Sets up table columns with cell value factories
     */
    private void setupTableColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        skuCol.setCellValueFactory(new PropertyValueFactory<>("sku"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageCol.setCellFactory(column -> new TableCell<Product, String>() {
            private final ImageView thumbnail = new ImageView();

            {
                thumbnail.setFitHeight(60);
                thumbnail.setFitWidth(80);
                thumbnail.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    thumbnail.setImage(loadImage(imagePath));
                    setGraphic(thumbnail);
                }
            }
        });

        priceCol.setCellFactory(column -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    Product product = getTableRow() != null ? getTableRow().getItem() : null;
                    if (product == null) {
                        setText(String.format("$%.2f", price));
                        return;
                    }
                    PricingService.PricingResult pricing = PricingService.calculatePricing(product, SimpleClient.getAccount());
                    double basePrice = pricing.getBasePrice();
                    double finalPrice = pricing.getFinalPrice();
                    if (pricing.isPromotionApplied() || pricing.isSubscriptionDiscountApplied()) {
                        setText(String.format("$%.2f → $%.2f", basePrice, finalPrice));
                    } else {
                        setText(String.format("$%.2f", basePrice));
                    }
                }
            }
        });

        // Promotion column - show "Yes" or "No"
        promotionCol.setCellValueFactory(cellData ->
                new ReadOnlyBooleanWrapper(cellData.getValue() != null && cellData.getValue().hasActivePromotion()));
        promotionCol.setCellFactory(column -> new TableCell<Product, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Yes" : "No");
                    setStyle(item ? "-fx-text-fill: #81c784; -fx-font-weight: bold;" : "");
                }
            }
        });

        // Actions column - add Edit and Delete buttons
        actionsCol.setCellFactory(param -> new TableCell<Product, Void>() {
            private final Button editButton = new Button("✏️ Edit");
            private final Button deleteButton = new Button("🗑️ Delete");

            {
                editButton.getStyleClass().add("btn-secondary");
                editButton.setPrefWidth(80);
                editButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    openEditProductForm(product);
                });

                deleteButton.getStyleClass().add("btn-danger");
                deleteButton.setPrefWidth(90);
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    confirmAndDeleteProduct(product);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(8, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void setupFilterControls() {
        applyFiltersBtn.setOnAction(event -> applyFilters());
        clearFiltersBtn.setOnAction(event -> clearFilters());

        categoryFilter.setOnAction(event -> applyFilters());
        flowerTypeFilter.setOnAction(event -> applyFilters());
        colorFilter.setOnAction(event -> applyFilters());
    }

    private void loadProducts() {
        try {
            SimpleClient.getClient().sendToServer("first entry");
        } catch (IOException e) {
            showError("Unable to load products: " + e.getMessage());
        }
    }

    /**
     * Event handler for updates from the server
     * This method is called when the server sends back an updated product list
     */
    @Subscribe
    public void onUpdateGuiEvent(UpdateGuiEvent event) {
        if (event.getProducts() == null) {
            return;
        }
        javafx.application.Platform.runLater(() -> {
            // Update the product list with data from the server
            productsList.clear();
            productsList.addAll(event.getProducts());
            refreshFilterOptions();
            applyFilters();
            System.out.println("Product catalog updated from server: " + productsList.size() + " products loaded");
        });
    }

    @Subscribe
    public void onRetrieveDatabaseEvent(RetrieveDataBaseEvent event) {
        if (event.getRecievedList() == null) {
            return;
        }
        javafx.application.Platform.runLater(() -> {
            productsList.clear();
            productsList.addAll(event.getRecievedList());
            refreshFilterOptions();
            applyFilters();
        });
    }

    @Subscribe
    public void onAddProductResponse(AddProductResponse response) {
        if (response == null) {
            return;
        }
        if (response.isSuccess()) {
            Product createdProduct = response.getCreatedProduct();
            if (createdProduct != null) {
                productsList.add(createdProduct);
                refreshFilterOptions();
                applyFilters();
            }
            showSuccess("Product added successfully.");
        } else {
            showError(response.getError() != null ? response.getError() : "Unable to add product.");
        }
    }

    @Subscribe
    public void onPassAccountEvent(PassAccountEvent event) {
        applyPrivilegeVisibility(SimpleClient.getAccount());
    }

    /**
     * Opens the product form dialog for adding new product
     */
    @FXML
    void addProduct() {
        if (!canAddProducts) {
            showError("Only workers or managers can add products.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductForm.fxml"));
            Parent root = loader.load();

            ProductFormController controller = loader.getController();
            // No need to set product - it's in "Add" mode by default

            Stage stage = new Stage();
            stage.setTitle("Add New Product");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setWidth(650);
            stage.setHeight(750);
            stage.setMinWidth(600);
            stage.setMinHeight(600);
            stage.centerOnScreen();
            stage.setResizable(true);

// Refresh table when dialog closes
            stage.setOnHiding(event -> refreshProducts());

            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
            showError("Error opening product form: " + e.getMessage());
        }
    }
    
    /**
     * Opens the product form dialog for editing existing product
     */
    private void openEditProductForm(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductForm.fxml"));
            Parent root = loader.load();
            
            ProductFormController controller = loader.getController();
            controller.setProduct(product);

            Stage stage = new Stage();
            stage.setTitle("Edit Product");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setWidth(650);
            stage.setHeight(750);
            stage.setMinWidth(600);
            stage.setMinHeight(600);
            stage.centerOnScreen();
            stage.setResizable(true);

            stage.setOnHiding(event -> refreshProducts());

            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
            showError("Error opening product form: " + e.getMessage());
        }
    }

    @FXML
    void editProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showError("Please select a product to edit");
            return;
        }
        openEditProductForm(selectedProduct);
    }

    @FXML
    void deleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showError("Please select a product to delete");
            return;
        }
        confirmAndDeleteProduct(selectedProduct);
    }
    
    /**
     * Confirms deletion and removes product
     */
    private void confirmAndDeleteProduct(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Are you sure you want to delete this product?");
        StringBuilder content = new StringBuilder("Product: ").append(product.getName());
        if (shouldShowSku()) {
            content.append(" (SKU: ").append(product.getSku()).append(")");
        }
        content.append("\nThis action cannot be undone.");
        alert.setContentText(content.toString());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Send delete request to server
                UpdateMessage message = new UpdateMessage("product", "remove");
                message.setDelteId(String.valueOf(product.getID()));
                try {
                    SimpleClient.getClient().sendToServer(message);
                    showSuccess("Product deletion requested.");
                } catch (IOException e) {
                    showError("Unable to delete product: " + e.getMessage());
                }
            }
        });
    }

    private boolean shouldShowSku() {
        Account account = SimpleClient.getAccount();
        return account != null && account.getPrivilegeLevel() == 1;
    }

    @FXML
    void togglePromotion() {
        if (!canEditPromotions) {
            showError("Only managers can edit promotions.");
            return;
        }
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showError("Please select a product to toggle promotion");
            return;
        }
        
        // Toggle promotion status
        boolean enablePromotion = !selectedProduct.isPromotion();
        selectedProduct.setPromotion(enablePromotion);
        if (!enablePromotion) {
            selectedProduct.setDiscountPercent(0.0);
        }

        UpdateMessage message = new UpdateMessage("product", "edit");
        message.setProduct(selectedProduct);
        try {
            SimpleClient.getClient().sendToServer(message);
            showSuccess("Promotion update requested for " + selectedProduct.getName());
        } catch (IOException e) {
            showError("Unable to update promotion: " + e.getMessage());
        }
    }

    @FXML
    void searchProducts() {
        applyFilters();
    }

    @FXML
    void refreshProducts() {
        loadProducts();
        searchField.clear();
        minPriceField.clear();
        maxPriceField.clear();
        categoryFilter.getSelectionModel().clearSelection();
        flowerTypeFilter.getSelectionModel().clearSelection();
        colorFilter.getSelectionModel().clearSelection();
        showSuccess("Products refreshed");
    }

    @FXML
    void applyFilters() {
        String searchTerm = searchField.getText() == null ? "" : searchField.getText().toLowerCase().trim();
        String selectedCategory = categoryFilter.getSelectionModel().getSelectedItem();
        String selectedFlowerType = flowerTypeFilter.getSelectionModel().getSelectedItem();
        String selectedColor = colorFilter.getSelectionModel().getSelectedItem();
        Double minPrice = parsePrice(minPriceField.getText());
        Double maxPrice = parsePrice(maxPriceField.getText());

        List<Product> filtered = productsList.stream()
                .filter(product -> searchTerm.isEmpty() ||
                        containsIgnoreCase(product.getName(), searchTerm) ||
                        containsIgnoreCase(product.getSku(), searchTerm) ||
                        containsIgnoreCase(product.getCategory(), searchTerm) ||
                        containsIgnoreCase(product.getColor(), searchTerm))
                .filter(product -> selectedCategory == null || selectedCategory.equals("All Categories") || selectedCategory.equals(product.getCategory()))
                .filter(product -> selectedFlowerType == null || selectedFlowerType.equals("All Flower Types") || selectedFlowerType.equals(product.getCustomType()))
                .filter(product -> selectedColor == null || selectedColor.equals("All Colors") || (product.getColor() != null && selectedColor.equals(product.getColor())))
                .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        filteredProducts.setAll(filtered);
        productsTable.setItems(filteredProducts);
        productCountLabel.setText("(" + filteredProducts.size() + " products)");

        if (filteredProducts.isEmpty()) {
            showError("No products match the current filters.");
        } else {
            successMessage.setVisible(false);
            errorMessage.setVisible(false);
        }
    }

    @FXML
    void clearFilters() {
        searchField.clear();
        minPriceField.clear();
        maxPriceField.clear();
        categoryFilter.getSelectionModel().clearSelection();
        flowerTypeFilter.getSelectionModel().clearSelection();
        colorFilter.getSelectionModel().clearSelection();
        applyFilters();
        showSuccess("Filters cleared");
    }

    @FXML
    void goToDashboard() {
        loadScene("WorkerDashboard.fxml", dashboardBtn);
    }

    @FXML
    void goToHome() {
        loadScene("Catalog.fxml", homeBtn);
    }

    private void loadScene(String fxml, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading page: " + e.getMessage());
        }
    }

    private void showSuccess(String message) {
        successMessage.setText("✓ " + message);
        successMessage.setVisible(true);
        errorMessage.setVisible(false);
    }

    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
        successMessage.setVisible(false);
    }

    private void applyPrivilegeVisibility(Account account) {
        int privilege = account != null ? account.getPrivilegeLevel() : 0;
        canAddProducts = privilege >= 2;
        canEditPromotions = privilege >= 3;
        boolean showSku = privilege == 1;
        if (addProductBtn != null) {
            addProductBtn.setVisible(canAddProducts);
            addProductBtn.setManaged(canAddProducts);
            addProductBtn.setDisable(!canAddProducts);
        }
        if (togglePromotionBtn != null) {
            togglePromotionBtn.setDisable(!canEditPromotions);
            togglePromotionBtn.setVisible(canEditPromotions);
            togglePromotionBtn.setManaged(canEditPromotions);
        }
        if (skuCol != null) {
            skuCol.setVisible(showSku);
        }
    }
    private void refreshFilterOptions() {
        populateFilterOptions(categoryFilter, productsList.stream()
                .map(Product::getCategory)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.toSet()), "All Categories");

        populateFilterOptions(flowerTypeFilter, productsList.stream()
                .map(Product::getCustomType)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.toSet()), "All Flower Types");

        populateFilterOptions(colorFilter, productsList.stream()
                .map(Product::getColor)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.toSet()), "All Colors");
    }

    private void populateFilterOptions(ComboBox<String> comboBox, Set<String> values, String defaultLabel) {
        comboBox.getItems().clear();
        comboBox.getItems().add(defaultLabel);
        comboBox.getItems().addAll(values.stream().sorted().collect(Collectors.toList()));
        comboBox.getSelectionModel().selectFirst();
    }

    private Double parsePrice(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            showError("Please enter a valid number for price filters.");
            return null;
        }
    }

    private Image loadImage(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        try (InputStream inputStream = getClass().getResourceAsStream(normalized)) {
            if (inputStream != null) {
                return new Image(inputStream);
            }
        } catch (IOException ignored) {
            // If the image cannot be loaded, we fall through to return a placeholder below.
        }
        WritableImage placeholder = new WritableImage(80, 60);
        for (int y = 0; y < 60; y++) {
            for (int x = 0; x < 80; x++) {
                placeholder.getPixelWriter().setColor(x, y, Color.LIGHTGRAY);
            }
        }
        return placeholder;
    }

    private boolean containsIgnoreCase(String source, String query) {
        return source != null && query != null && source.toLowerCase().contains(query.toLowerCase());
    }
}
