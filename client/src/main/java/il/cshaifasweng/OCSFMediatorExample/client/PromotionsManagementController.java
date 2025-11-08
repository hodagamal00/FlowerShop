package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Promotions Management page - Manager view
 * Create, edit, and manage promotional campaigns for the branch
 * Requires Manager privilege (level 3+)
 */
public class PromotionsManagementController {

    // Navigation
    @FXML private Button backButton;
    
    // Form
    @FXML private Label formTitleLabel;
    @FXML private TextField promotionNameField;
    @FXML private TextField discountField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> applyToCombo;
    @FXML private CheckBox activeCheckbox;
    @FXML private Button savePromotionButton;
    @FXML private Button clearFormButton;
    
    // Promotions Table
    @FXML private TableView<PromotionData> promotionsTable;
    @FXML private TableColumn<PromotionData, Integer> promotionIdCol;
    @FXML private TableColumn<PromotionData, String> promotionNameCol;
    @FXML private TableColumn<PromotionData, Double> discountCol;
    @FXML private TableColumn<PromotionData, String> startDateCol;
    @FXML private TableColumn<PromotionData, String> endDateCol;
    @FXML private TableColumn<PromotionData, String> applyToCol;
    @FXML private TableColumn<PromotionData, String> statusCol;
    @FXML private TableColumn<PromotionData, Void> actionsCol;
    @FXML private Button refreshButton;
    
    // Statistics
    @FXML private Label activePromotionsLabel;
    @FXML private Label productsOnSaleLabel;
    @FXML private Label avgDiscountLabel;
    
    // Data
    private ObservableList<PromotionData> promotionsList = FXCollections.observableArrayList();
    private PromotionData editingPromotion = null;
    private int currentBranchId = 1;
    
    @FXML
    public void initialize() {
        // Check privileges - Manager level required (3+)
        if (!checkManagerPrivileges()) {
            showAccessDenied();
            return;
        }
        
        setupApplyToCombo();
        setupPromotionsTable();
        loadPromotions();
        updateStatistics();
    }
    
    /**
     * Check if user has manager privileges
     */
    private boolean checkManagerPrivileges() {
        // TODO: Get current user privilege from session
        // return SimpleClient.getCurrentUser().getPrivilege() >= 3;
        return true;
    }
    
    /**
     * Show access denied message
     */
    private void showAccessDenied() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access Denied");
        alert.setHeaderText("Insufficient Privileges");
        alert.setContentText("You need Manager privileges to access this page.");
        alert.showAndWait();
        handleBackToCatalog();
    }
    
    /**
     * Setup apply-to combo box
     */
    private void setupApplyToCombo() {
        ObservableList<String> options = FXCollections.observableArrayList(
            "All Products",
            "Category: Roses",
            "Category: Tulips",
            "Category: Orchids",
            "Category: Lilies",
            "Category: Mixed Bouquets",
            "Specific Products (Select from list)"
        );
        applyToCombo.setItems(options);
        applyToCombo.setValue("All Products");
    }
    
    /**
     * Setup promotions table columns
     */
    private void setupPromotionsTable() {
        promotionIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        promotionNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discountPercent"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        applyToCol.setCellValueFactory(new PropertyValueFactory<>("applyTo"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Add action buttons column
        addActionButtons();
        
        promotionsTable.setItems(promotionsList);
    }
    
    /**
     * Add action buttons to table
     */
    private void addActionButtons() {
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);
            
            {
                editBtn.setStyle("-fx-background-color: #ba68c8; -fx-text-fill: white; -fx-font-size: 12px;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px;");
                
                editBtn.setOnAction(event -> {
                    PromotionData promotion = getTableView().getItems().get(getIndex());
                    handleEditPromotion(promotion);
                });
                
                deleteBtn.setOnAction(event -> {
                    PromotionData promotion = getTableView().getItems().get(getIndex());
                    handleDeletePromotion(promotion);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }
    
    /**
     * Load promotions from server
     */
    private void loadPromotions() {
        // TODO: Send request to server to get all promotions for this branch
        // Message: new GetBranchPromotions(currentBranchId)
        // SimpleClient.getClient().sendToServer(message);
        
        // For now, generate sample data
        generateSamplePromotions();
    }
    
    /**
     * Generate sample promotions for demonstration
     */
    private void generateSamplePromotions() {
        promotionsList.clear();
        
        promotionsList.add(new PromotionData(
            1,
            "Spring Sale 2025",
            15.0,
            "2025-03-01",
            "2025-03-31",
            "All Products",
            "Active"
        ));
        
        promotionsList.add(new PromotionData(
            2,
            "Valentine's Day Special",
            20.0,
            "2025-02-10",
            "2025-02-14",
            "Category: Roses",
            "Active"
        ));
        
        promotionsList.add(new PromotionData(
            3,
            "Mother's Day Discount",
            10.0,
            "2025-05-01",
            "2025-05-12",
            "Category: Mixed Bouquets",
            "Scheduled"
        ));
    }
    
    /**
     * Save promotion (create or update)
     */
    @FXML
    private void handleSavePromotion() {
        // Validate input
        if (!validatePromotionForm()) {
            return;
        }
        
        String name = promotionNameField.getText().trim();
        double discount = Double.parseDouble(discountField.getText().trim());
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String applyTo = applyToCombo.getValue();
        boolean active = activeCheckbox.isSelected();
        
        if (editingPromotion != null) {
            // Update existing promotion
            updatePromotion(editingPromotion, name, discount, startDate, endDate, applyTo, active);
        } else {
            // Create new promotion
            createPromotion(name, discount, startDate, endDate, applyTo, active);
        }
        
        handleClearForm();
        loadPromotions();
        updateStatistics();
    }
    
    /**
     * Validate promotion form
     */
    private boolean validatePromotionForm() {
        if (promotionNameField.getText().trim().isEmpty()) {
            showError("Please enter a promotion name.");
            return false;
        }
        
        try {
            double discount = Double.parseDouble(discountField.getText().trim());
            if (discount <= 0 || discount > 100) {
                showError("Discount must be between 0 and 100.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid discount percentage.");
            return false;
        }
        
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showError("Please select both start and end dates.");
            return false;
        }
        
        if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
            showError("Start date must be before end date.");
            return false;
        }
        
        if (applyToCombo.getValue() == null) {
            showError("Please select what the promotion applies to.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Create new promotion
     */
    private void createPromotion(String name, double discount, LocalDate startDate, 
                                  LocalDate endDate, String applyTo, boolean active) {
        // TODO: Send create promotion request to server
        // Message: new CreatePromotion(currentBranchId, name, discount, startDate, endDate, applyTo, active)
        // SimpleClient.getClient().sendToServer(message);
        
        showSuccess("Promotion created successfully!");
    }
    
    /**
     * Update existing promotion
     */
    private void updatePromotion(PromotionData promotion, String name, double discount, 
                                  LocalDate startDate, LocalDate endDate, String applyTo, boolean active) {
        // TODO: Send update promotion request to server
        // Message: new UpdatePromotion(promotion.getId(), name, discount, startDate, endDate, applyTo, active)
        // SimpleClient.getClient().sendToServer(message);
        
        showSuccess("Promotion updated successfully!");
    }
    
    /**
     * Edit promotion
     */
    private void handleEditPromotion(PromotionData promotion) {
        editingPromotion = promotion;
        formTitleLabel.setText("Edit Promotion (ID: " + promotion.getId() + ")");
        
        // PromotionData provides getName() and getDiscountPercent(); there is no getPromotionName().
        promotionNameField.setText(promotion.getName());
        discountField.setText(String.valueOf(promotion.getDiscountPercent()));
        startDatePicker.setValue(LocalDate.parse(promotion.getStartDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        endDatePicker.setValue(LocalDate.parse(promotion.getEndDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        applyToCombo.setValue(promotion.getApplyTo());
        activeCheckbox.setSelected("Active".equals(promotion.getStatus()));
        
        savePromotionButton.setText("Update Promotion");
    }
    
    /**
     * Delete promotion
     */
    private void handleDeletePromotion(PromotionData promotion) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Promotion");
        // Use getName() since PromotionData does not define getPromotionName().
        confirm.setContentText("Are you sure you want to delete promotion: " + promotion.getName() + "?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Send delete request to server
                // Message: new DeletePromotion(promotion.getId())
                // SimpleClient.getClient().sendToServer(message);
                
                promotionsList.remove(promotion);
                updateStatistics();
                showSuccess("Promotion deleted successfully!");
            }
        });
    }
    
    /**
     * Clear form
     */
    @FXML
    private void handleClearForm() {
        editingPromotion = null;
        formTitleLabel.setText("Create New Promotion");
        promotionNameField.clear();
        discountField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        applyToCombo.setValue("All Products");
        activeCheckbox.setSelected(true);
        savePromotionButton.setText("Save Promotion");
    }
    
    /**
     * Refresh promotions list
     */
    @FXML
    private void handleRefresh() {
        loadPromotions();
        updateStatistics();
        showInfo("Promotions list refreshed.");
    }
    
    /**
     * Update statistics labels
     */
    private void updateStatistics() {
        long activeCount = promotionsList.stream()
            .filter(p -> "Active".equals(p.getStatus()))
            .count();
        
        double avgDiscount = promotionsList.stream()
            .filter(p -> "Active".equals(p.getStatus()))
            .mapToDouble(PromotionData::getDiscountPercent)
            .average()
            .orElse(0.0);
        
        // TODO: Get actual count of products on sale from server
        int productsOnSale = (int) activeCount * 15; // Sample calculation
        
        activePromotionsLabel.setText(String.valueOf(activeCount));
        productsOnSaleLabel.setText(String.valueOf(productsOnSale));
        avgDiscountLabel.setText(String.format("%.1f%%", avgDiscount));
    }
    
    /**
     * Navigate back to catalog
     */
    @FXML
    private void handleBackToCatalog() {
        try {
            App.setRoot("primary");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to navigate to catalog.");
        }
    }
    
    /**
     * Show error alert
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show success alert
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show info alert
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Data model for promotions table
     */
    public static class PromotionData {
        private final int id;
        private final String name;
        private final double discountPercent;
        private final String startDate;
        private final String endDate;
        private final String applyTo;
        private final String status;
        
        public PromotionData(int id, String name, double discountPercent, String startDate,
                            String endDate, String applyTo, String status) {
            this.id = id;
            this.name = name;
            this.discountPercent = discountPercent;
            this.startDate = startDate;
            this.endDate = endDate;
            this.applyTo = applyTo;
            this.status = status;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public double getDiscountPercent() { return discountPercent; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getApplyTo() { return applyTo; }
        public String getStatus() { return status; }
    }
}
