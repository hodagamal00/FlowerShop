package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;

/**
 * Controller for Global Settings - Chain Manager view
 * Configure network-wide settings that apply to all branches
 * Requires Chain Manager privilege (level 4)
 */
public class GlobalSettingsController {

    
    // Company Information
    @FXML private TextField companyNameField;
    @FXML private TextField corporateEmailField;
    @FXML private TextField corporatePhoneField;
    @FXML private TextField websiteField;
    
    // Business Policies
    @FXML private TextField deliveryFeeField;
    @FXML private TextField fullRefundWindowField;
    @FXML private TextField partialRefundPercentField;
    @FXML private TextField subscriptionDiscountField;
    @FXML private TextField complaintSLAField;
    @FXML private TextField loyaltyPointsRateField;
    
    // Feature Toggles
    @FXML private CheckBox subscriptionFeatureCheckbox;
    @FXML private CheckBox loyaltyProgramCheckbox;
    @FXML private CheckBox customProductsCheckbox;
    @FXML private CheckBox giftWrappingCheckbox;
    @FXML private CheckBox greetingCardsCheckbox;
    @FXML private CheckBox emailNotificationsCheckbox;
    @FXML private CheckBox smsNotificationsCheckbox;
    @FXML private CheckBox onlinePaymentCheckbox;
    
    // System Maintenance
    @FXML private CheckBox maintenanceModeCheckbox;
    @FXML private TextField maintenanceMessageField;
    
    @FXML private Button saveButton;
    @FXML private Button resetButton;
    
    @FXML
    public void initialize() {
        if (!AccessGuard.requireMinPrivilege(4)) {
            return;
        }
        loadGlobalSettings();
    }
    private void loadGlobalSettings() {
        // TODO: Load from server
        companyNameField.setText("FlowerShop Network");
        corporateEmailField.setText("corporate@flowershop.com");
        corporatePhoneField.setText("+972-3-1234567");
        websiteField.setText("www.flowershop.com");
        deliveryFeeField.setText("15.00");
        fullRefundWindowField.setText("3");
        partialRefundPercentField.setText("50");
        subscriptionDiscountField.setText("20");
        complaintSLAField.setText("24");
        loyaltyPointsRateField.setText("1");
    }
    
    @FXML
    private void handleSaveSettings() {
        if (!validateSettings()) {
            return;
        }
        
        // TODO: Send to server
        // GlobalSettings settings = collectSettings();
        // SimpleClient.getClient().sendToServer(new UpdateGlobalSettings(settings));
        
        showSuccess("Global settings saved successfully! Changes will be applied to all branches.");
    }
    
    private boolean validateSettings() {
        if (companyNameField.getText().trim().isEmpty()) {
            showError("Please enter a company name.");
            return false;
        }
        
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
        
        try {
            int hours = Integer.parseInt(fullRefundWindowField.getText().trim());
            if (hours <= 0) {
                showError("Refund window must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid refund window in hours.");
            return false;
        }
        
        return true;
    }
    
    @FXML
    private void handleReset() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reset Settings");
        confirm.setHeaderText("Reset to Last Saved Settings");
        confirm.setContentText("Are you sure you want to discard all changes?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                loadGlobalSettings();
                showInfo("Settings reset to last saved values.");
            }
        });
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
