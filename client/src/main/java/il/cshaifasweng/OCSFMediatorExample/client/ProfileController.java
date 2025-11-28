package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {

    @FXML private Button backBtn;
    @FXML private Text accountTypeText;
    @FXML private Label accountIdLabel;
    @FXML private Label userIdLabel;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private Label subscriptionStatusLabel;
    @FXML private VBox subscriptionBenefitsBox;
    @FXML private Button manageSubscriptionBtn;
    @FXML private Label branchLabel;
    @FXML private TextField creditCardField;
    @FXML private TextField expiryMonthField;
    @FXML private TextField expiryYearField;
    @FXML private PasswordField cvvField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button changePasswordBtn;
    @FXML private Label successMessage;
    @FXML private Label errorMessage;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;

    private Account currentAccount;

    @FXML
    void initialize() {
        // Load current account data
        currentAccount = SimpleClient.getAccount();
        if (currentAccount != null) {
            loadAccountData();
        } else {
            showError("No account logged in");
        }
    }

    /**
     * Load account data into form fields
     */
    private void loadAccountData() {
        // Account identification
        accountIdLabel.setText(String.valueOf(currentAccount.getAccountID()));
        userIdLabel.setText(String.valueOf(currentAccount.getID()));
        
        // Determine account type based on privilege
        String accountType = getAccountType(currentAccount.getPrivialge());
        accountTypeText.setText(accountType + " Account");

        // Personal information
        fullNameField.setText(currentAccount.getFullName());
        emailField.setText(currentAccount.getEmail());
        phoneField.setText(String.valueOf(currentAccount.getPhoneNumber()));
        addressField.setText(currentAccount.getAddress());

        // Subscription status
        if (currentAccount.isSubscription()) {
            subscriptionStatusLabel.setText("Active");
            subscriptionStatusLabel.setStyle("-fx-background-color: #81c784; -fx-text-fill: white; -fx-padding: 5 15; -fx-background-radius: 5;");
            subscriptionBenefitsBox.setVisible(true);
            subscriptionBenefitsBox.setManaged(true);
            manageSubscriptionBtn.setText("Cancel Subscription");
        } else {
            subscriptionStatusLabel.setText("Inactive");
            subscriptionStatusLabel.setStyle("-fx-background-color: #e57373; -fx-text-fill: white; -fx-padding: 5 15; -fx-background-radius: 5;");
            subscriptionBenefitsBox.setVisible(false);
            subscriptionBenefitsBox.setManaged(false);
            manageSubscriptionBtn.setText("Subscribe Now");
        }

        // Branch information
        branchLabel.setText("FlowerShop - Branch #" + currentAccount.getBelongShop());

        // Payment information (masked for security)
        String cardNumber = String.valueOf(currentAccount.getCreditCardNumber());
        if (cardNumber.length() >= 4) {
            String maskedCard = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
            creditCardField.setText(maskedCard);
        }
        expiryMonthField.setText(String.valueOf(currentAccount.getCreditMonthExpire()));
        expiryYearField.setText(String.valueOf(currentAccount.getCreditYearExpire()));
        cvvField.setText("***"); // Always masked
    }

    /**
     * Get account type name based on privilege level
     */
    private String getAccountType(int privilege) {
        switch (privilege) {
            case 0: return "Guest";
            case 1: return "Customer";
            case 2: return "Worker";
            case 3: return "Manager";
            case 4: return "Chain Manager";
            default: return "Unknown";
        }
    }

    @FXML
    void saveProfile() {
        hideMessages();

        try {
            // Validate inputs
            if (fullNameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || addressField.getText().isEmpty()) {
                showError("Please fill in all required fields");
                return;
            }

            // Update account object
            currentAccount.setFullName(fullNameField.getText());
            currentAccount.setEmail(emailField.getText());
            currentAccount.setPhoneNumber(Long.parseLong(phoneField.getText()));
            currentAccount.setAddress(addressField.getText());

            // Update credit card if changed
            if (!creditCardField.getText().contains("*")) {
                try {
                    currentAccount.setCreditCardNumber(Long.parseLong(creditCardField.getText()));
                } catch (NumberFormatException e) {
                    showError("Invalid credit card number");
                    return;
                }
            }

            // Update expiry dates
            try {
                currentAccount.setCreditMonthExpire(Integer.parseInt(expiryMonthField.getText()));
                currentAccount.setCreditYearExpire(Integer.parseInt(expiryYearField.getText()));
            } catch (NumberFormatException e) {
                showError("Invalid expiry date");
                return;
            }

            // TODO: Send update to server via SimpleClient
            // For now, just update the local account
            SimpleClient.setAccount(currentAccount);

            showSuccess("Profile updated successfully!");

        } catch (Exception e) {
            showError("Error updating profile: " + e.getMessage());
        }
    }

    @FXML
    void changePassword() {
        hideMessages();

        // Validate password fields
        if (currentPasswordField.getText().isEmpty() || newPasswordField.getText().isEmpty() ||
            confirmPasswordField.getText().isEmpty()) {
            showError("Please fill in all password fields");
            return;
        }

        // Verify current password
        if (!currentPasswordField.getText().equals(currentAccount.getPassword())) {
            showError("Current password is incorrect");
            return;
        }

        // Verify new passwords match
        if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
            showError("New passwords do not match");
            return;
        }

        // Validate new password strength
        if (newPasswordField.getText().length() < 6) {
            showError("Password must be at least 6 characters long");
            return;
        }

        // Update password
        currentAccount.setPassword(newPasswordField.getText());
        
        // TODO: Send password update to server
        SimpleClient.setAccount(currentAccount);

        // Clear password fields
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();

        showSuccess("Password changed successfully!");
    }

    @FXML
    void manageSubscription() {
        hideMessages();

        if (currentAccount.isSubscription()) {
            // Cancel subscription
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancel Subscription");
            alert.setHeaderText("Are you sure you want to cancel your subscription?");
            alert.setContentText("You will lose all subscription benefits.");
            
            if (alert.showAndWait().get() == ButtonType.OK) {
                currentAccount.setSubscription(false);
                // TODO: Send update to server
                SimpleClient.setAccount(currentAccount);
                loadAccountData(); // Refresh display
                showSuccess("Subscription cancelled");
            }
        } else {
            // Activate subscription
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Subscribe");
            alert.setHeaderText("Activate FlowerShop Subscription");
            alert.setContentText("Get 20% discount on all orders, free delivery, and more!\nMonthly fee: $9.99");
            
            if (alert.showAndWait().get() == ButtonType.OK) {
                currentAccount.setSubscription(true);
                // TODO: Send update to server
                SimpleClient.setAccount(currentAccount);
                loadAccountData(); // Refresh display
                showSuccess("Subscription activated!");
            }
        }
    }

    @FXML
    void cancelChanges() {
        // Reload original data
        loadAccountData();
        hideMessages();
    }

    @FXML
    void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Catalog.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading home page: " + e.getMessage());
        }
    }

    private void showSuccess(String message) {
        successMessage.setText("✓ " + message);
        successMessage.setVisible(true);
        errorMessage.setVisible(false);

        // Auto-hide after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> successMessage.setVisible(false));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
        successMessage.setVisible(false);
    }

    private void hideMessages() {
        successMessage.setVisible(false);
        errorMessage.setVisible(false);
    }
}
