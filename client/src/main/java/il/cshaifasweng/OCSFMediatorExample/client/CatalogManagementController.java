package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CatalogManagementController {

    @FXML private Button dashboardBtn;
    @FXML private Button homeBtn;
    @FXML private Button addProductBtn;
    @FXML private TextField searchField;
    @FXML private Button searchBtn;
    @FXML private Button refreshBtn;
    @FXML private Label productCountLabel;
    @FXML private TableView<Product> productsTable;
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
    
    private ObservableList<Product> productsList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        // Register this controller with EventBus to receive updates from the server
        EventBus.getDefault().register(this);
        
        // Setup table columns and load initial data
        setupTableColumns();
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
        
        // Promotion column - show "Yes" or "No"
        promotionCol.setCellValueFactory(new PropertyValueFactory<>("promotion"));
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

    private void loadProducts() {
        // TODO: Load products from server
        // Message message = new Message("#GET_ALL_PRODUCTS");
        // SimpleClient.getClient().sendToServer(message);
        
        // For now, use mock data for demonstration
        productsList.clear();
        
        // Sample products with image paths
        // Provide price as a double instead of string to match Product constructor
        Product p1 = new Product(1, "btn1", "Red Roses Bouquet", "Beautiful red roses", 49.99);
        p1.setSku("ROSE-RED-001");
        p1.setCategory("Bouquet");
        p1.setColor("Red");
        p1.setImage("product_images/red_roses.jpg");
        p1.setPromotion(true);
        p1.setDiscountPercent(15.0);
        productsList.add(p1);
        
        Product p2 = new Product(2, "btn2", "Pink Tulips", "Fresh spring tulips", 39.99);
        p2.setSku("TULIP-PINK-001");
        p2.setCategory("Bouquet");
        p2.setColor("Pink");
        p2.setImage("product_images/pink_tulips.jpg");
        productsList.add(p2);
        
        Product p3 = new Product(3, "btn3", "White Lilies Arrangement", "Elegant white lilies", 59.99);
        p3.setSku("LILY-WHITE-001");
        p3.setCategory("Arrangement");
        p3.setColor("White");
        p3.setImage("product_images/white_lilies.jpg");
        productsList.add(p3);
        
        productsTable.setItems(productsList);
        productCountLabel.setText("(" + productsList.size() + " products)");
    }
    
    /**
     * Event handler for updates from the server
     * This method is called when the server sends back an updated product list
     */
    @Subscribe
    public void onUpdateGuiEvent(UpdateGuiEvent event) {
        if (event.getProducts() != null) {
            // Update the product list with data from the server
            productsList.clear();
            productsList.addAll(event.getProducts());
            productsTable.setItems(productsList);
            productCountLabel.setText("(" + productsList.size() + " products)");
            System.out.println("Product catalog updated from server: " + productsList.size() + " products loaded");
        }
    }

    /**
     * Opens the product form dialog for adding new product
     */
    @FXML
    void addProduct() {
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
        alert.setContentText("Product: " + product.getName() + " (SKU: " + product.getSku() + ")\nThis action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Send delete request to server
                // Message message = new Message("#DELETE_PRODUCT");
                // message.setData(product.getID());
                // SimpleClient.getClient().sendToServer(message);
                
                // For now, remove from local list
                productsList.remove(product);
                productCountLabel.setText("(" + productsList.size() + " products)");
                showSuccess("Product deleted successfully");
            }
        });
    }

    @FXML
    void togglePromotion() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showError("Please select a product to toggle promotion");
            return;
        }
        
        // Toggle promotion status
        selectedProduct.setPromotion(!selectedProduct.isPromotion());
        
        // TODO: Update product on server
        // Message message = new Message("#UPDATE_PRODUCT");
        // message.setData(selectedProduct);
        // SimpleClient.getClient().sendToServer(message);
        
        productsTable.refresh();
        showSuccess("Promotion status toggled for " + selectedProduct.getName());
    }

    @FXML
    void searchProducts() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        
        if (searchTerm.isEmpty()) {
            loadProducts();
            return;
        }
        
        // Filter products based on search term
        ObservableList<Product> filteredList = FXCollections.observableArrayList();
        for (Product product : productsList) {
            if (product.getName().toLowerCase().contains(searchTerm) ||
                product.getSku().toLowerCase().contains(searchTerm) ||
                product.getCategory().toLowerCase().contains(searchTerm) ||
                product.getColor().toLowerCase().contains(searchTerm)) {
                filteredList.add(product);
            }
        }
        
        productsTable.setItems(filteredList);
        productCountLabel.setText("(" + filteredList.size() + " products found)");
        
        if (filteredList.isEmpty()) {
            showError("No products found matching: " + searchTerm);
        } else {
            showSuccess("Found " + filteredList.size() + " product(s)");
        }
    }

    @FXML
    void refreshProducts() {
        loadProducts();
        searchField.clear();
        showSuccess("Products refreshed");
    }

    @FXML
    void goToDashboard() {
        loadScene("WorkerDashboard.fxml", dashboardBtn);
    }

    @FXML
    void goToHome() {
        loadScene("primary.fxml", homeBtn);
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
}
