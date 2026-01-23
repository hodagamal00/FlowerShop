package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Branch Settings page - Manager view
 * Configure branch-specific settings including information, hours, delivery, notifications
 * Requires Manager privilege (level 3+)
 */
public class BranchSettingsController {

    // Navigation
    
    // Branch Information
    @FXML private TextField branchNameField;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField managerField;
    
    // Operating Hours
    @FXML private ComboBox<String> weekdayOpenCombo;
    @FXML private ComboBox<String> weekdayCloseCombo;
    @FXML private ComboBox<String> weekendOpenCombo;
    @FXML private ComboBox<String> weekendCloseCombo;
    @FXML private CheckBox closedWeekendsCheckbox;
    
    // Delivery Settings
    @FXML private TextField deliveryFeeField;
    @FXML private TextField freeDeliveryThresholdField;
    @FXML private TextField maxDeliveryDistanceField;
    @FXML private CheckBox sameDayDeliveryCheckbox;
    @FXML private CheckBox scheduledDeliveryCheckbox;
    
    // Notification Settings
    @FXML private CheckBox newOrderNotifCheckbox;
    @FXML private CheckBox orderStatusNotifCheckbox;
    @FXML private CheckBox lowStockNotifCheckbox;
    @FXML private CheckBox newComplaintNotifCheckbox;
    @FXML private CheckBox customerFeedbackNotifCheckbox;
    @FXML private CheckBox promotionNotifCheckbox;
    
    // Action Buttons
    @FXML private Button saveSettingsButton;
    @FXML private Button resetButton;
    
    // Data
    private int currentBranchId = 1;
    private BranchSettings originalSettings;
    
    @FXML
    public void initialize() {
        // Check privileges - Manager level required (3+)
        if (!AccessGuard.requireMinPrivilege(3)) {
            return;
        }
        
        setupTimeComboBoxes();
        loadBranchSettings();
        setupClosedWeekendsListener();
    }
    
    /**
     * Setup time combo boxes with hour options
     */
    private void setupTimeComboBoxes() {
        ObservableList<String> hours = FXCollections.observableArrayList();
        
        // Generate hours from 00:00 to 23:00
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d:00", i));
            hours.add(String.format("%02d:30", i));
        }
        
        weekdayOpenCombo.setItems(hours);
        weekdayCloseCombo.setItems(hours);
        weekendOpenCombo.setItems(hours);
        weekendCloseCombo.setItems(hours);
        
        // Set default values
        weekdayOpenCombo.setValue("09:00");
        weekdayCloseCombo.setValue("18:00");
        weekendOpenCombo.setValue("10:00");
        weekendCloseCombo.setValue("16:00");
    }
    
    /**
     * Setup listener for closed weekends checkbox
     */
    private void setupClosedWeekendsListener() {
        closedWeekendsCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            weekendOpenCombo.setDisable(newVal);
            weekendCloseCombo.setDisable(newVal);
        });
    }
    
    /**
     * Load branch settings from server
     */
    private void loadBranchSettings() {
        // TODO: Send request to server to get branch settings
        // Message: new GetBranchSettings(currentBranchId)
        // SimpleClient.getClient().sendToServer(message);
        
        // For now, load sample data
        loadSampleSettings();
    }
    
    /**
     * Load sample settings for demonstration
     */
    private void loadSampleSettings() {
        originalSettings = new BranchSettings();
        originalSettings.branchName = "Main Street Branch";
        originalSettings.address = "123 Main Street";
        originalSettings.city = "Tel Aviv";
        originalSettings.postalCode = "12345";
        originalSettings.phone = "+972-3-1234567";
        originalSettings.email = "mainstreet@flowershop.com";
        originalSettings.manager = "Sarah Cohen";
        originalSettings.deliveryFee = 15.0;
        originalSettings.freeDeliveryThreshold = 200.0;
        originalSettings.maxDeliveryDistance = 25.0;
        
        applySettingsToForm(originalSettings);
    }
    
    /**
     * Apply settings to form fields
     */
    private void applySettingsToForm(BranchSettings settings) {
        branchNameField.setText(settings.branchName);
        addressField.setText(settings.address);
        cityField.setText(settings.city);
        postalCodeField.setText(settings.postalCode);
        phoneField.setText(settings.phone);
        emailField.setText(settings.email);
        managerField.setText(settings.manager);
        deliveryFeeField.setText(String.valueOf(settings.deliveryFee));
        freeDeliveryThresholdField.setText(String.valueOf(settings.freeDeliveryThreshold));
        maxDeliveryDistanceField.setText(String.valueOf(settings.maxDeliveryDistance));
    }
    
    /**
     * Save all settings
     */
    @FXML
    private void handleSaveSettings() {
        // Validate input
        if (!validateSettings()) {
            return;
        }
        
        BranchSettings newSettings = collectSettingsFromForm();
        
        // TODO: Send update request to server
        // Message: new UpdateBranchSettings(currentBranchId, newSettings)
        // SimpleClient.getClient().sendToServer(message);
        
        // Update original settings
        originalSettings = newSettings;
        
        showSuccess("Branch settings saved successfully!");
    }
    
    /**
     * Validate settings form
     */
    private boolean validateSettings() {
        // Validate branch name
        if (branchNameField.getText().trim().isEmpty()) {
            showError("Please enter a branch name.");
            return false;
        }
        
        // Validate contact phone
        if (phoneField.getText().trim().isEmpty()) {
            showError("Please enter a contact phone number.");
            return false;
        }
        
        // Validate contact email
        String email = emailField.getText().trim();
        if (email.isEmpty() || !email.contains("@")) {
            showError("Please enter a valid email address.");
            return false;
        }
        
        // Validate delivery fee
        try {
            double fee = Double.parseDouble(deliveryFeeField.getText().trim());
            if (fee < 0) {
                showError("Delivery fee cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid delivery fee.");
            return false;
        }
        
        // Validate free delivery threshold
        try {
            double threshold = Double.parseDouble(freeDeliveryThresholdField.getText().trim());
            if (threshold < 0) {
                showError("Free delivery threshold cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid free delivery threshold.");
            return false;
        }
        
        // Validate max delivery distance
        try {
            double distance = Double.parseDouble(maxDeliveryDistanceField.getText().trim());
            if (distance <= 0) {
                showError("Maximum delivery distance must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid maximum delivery distance.");
            return false;
        }
        
        // Validate operating hours
        if (!closedWeekendsCheckbox.isSelected()) {
            if (weekendOpenCombo.getValue() == null || weekendCloseCombo.getValue() == null) {
                showError("Please select weekend operating hours or mark as closed.");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Collect settings from form fields
     */
    private BranchSettings collectSettingsFromForm() {
        BranchSettings settings = new BranchSettings();
        
        settings.branchName = branchNameField.getText().trim();
        settings.address = addressField.getText().trim();
        settings.city = cityField.getText().trim();
        settings.postalCode = postalCodeField.getText().trim();
        settings.phone = phoneField.getText().trim();
        settings.email = emailField.getText().trim();
        settings.manager = managerField.getText().trim();
        
        settings.weekdayOpen = weekdayOpenCombo.getValue();
        settings.weekdayClose = weekdayCloseCombo.getValue();
        settings.weekendOpen = closedWeekendsCheckbox.isSelected() ? "Closed" : weekendOpenCombo.getValue();
        settings.weekendClose = closedWeekendsCheckbox.isSelected() ? "Closed" : weekendCloseCombo.getValue();
        
        settings.deliveryFee = Double.parseDouble(deliveryFeeField.getText().trim());
        settings.freeDeliveryThreshold = Double.parseDouble(freeDeliveryThresholdField.getText().trim());
        settings.maxDeliveryDistance = Double.parseDouble(maxDeliveryDistanceField.getText().trim());
        settings.sameDayDelivery = sameDayDeliveryCheckbox.isSelected();
        settings.scheduledDelivery = scheduledDeliveryCheckbox.isSelected();
        
        settings.notifyNewOrders = newOrderNotifCheckbox.isSelected();
        settings.notifyOrderStatus = orderStatusNotifCheckbox.isSelected();
        settings.notifyLowStock = lowStockNotifCheckbox.isSelected();
        settings.notifyNewComplaints = newComplaintNotifCheckbox.isSelected();
        settings.notifyCustomerFeedback = customerFeedbackNotifCheckbox.isSelected();
        settings.notifyPromotionExpiry = promotionNotifCheckbox.isSelected();
        
        return settings;
    }
    
    /**
     * Reset settings to default or last saved values
     */
    @FXML
    private void handleReset() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reset Settings");
        confirm.setHeaderText("Reset to Last Saved Settings");
        confirm.setContentText("Are you sure you want to reset all changes?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (originalSettings != null) {
                    applySettingsToForm(originalSettings);
                    
                    // Reset checkboxes to defaults
                    sameDayDeliveryCheckbox.setSelected(true);
                    scheduledDeliveryCheckbox.setSelected(true);
                    closedWeekendsCheckbox.setSelected(false);
                    newOrderNotifCheckbox.setSelected(true);
                    orderStatusNotifCheckbox.setSelected(true);
                    lowStockNotifCheckbox.setSelected(true);
                    newComplaintNotifCheckbox.setSelected(true);
                    customerFeedbackNotifCheckbox.setSelected(false);
                    promotionNotifCheckbox.setSelected(true);
                    
                    showInfo("Settings reset to last saved values.");
                }
            }
        });
    }
    
    
    /**
     * Check if there are unsaved changes
     */
    private boolean hasUnsavedChanges() {
        // TODO: Implement proper change detection
        // Compare current form values with originalSettings
        return false;
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
     * Data model for branch settings
     */
    private static class BranchSettings {
        String branchName;
        String address;
        String city;
        String postalCode;
        String phone;
        String email;
        String manager;
        
        String weekdayOpen;
        String weekdayClose;
        String weekendOpen;
        String weekendClose;
        
        double deliveryFee;
        double freeDeliveryThreshold;
        double maxDeliveryDistance;
        boolean sameDayDelivery;
        boolean scheduledDelivery;
        
        boolean notifyNewOrders;
        boolean notifyOrderStatus;
        boolean notifyLowStock;
        boolean notifyNewComplaints;
        boolean notifyCustomerFeedback;
        boolean notifyPromotionExpiry;
    }
}
