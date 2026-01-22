package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.UserUpdateResponse;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Optional;

public class MyAccountController {

    @FXML private Label lblAccountId;
    @FXML private Label lblAccountType;
    @FXML private Label lblEmail;
    @FXML private TextField txtName;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtCardNumber;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private Account currentAccount;
    private boolean awaitingSaveResponse = false;

    @FXML
    void initialize() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        loadAccount();
    }

    private void loadAccount() {
        Account account = SimpleClient.getUser();
        if (account == null) {
            NavigationService.getInstance().navigate("Login");
            return;
        }
        if (account.getPrivilegeLevel() != 1) {
            NavigationService.getInstance().navigate("AccessDenied");
            return;
        }
        currentAccount = account;
        if (lblAccountId != null) {
            lblAccountId.setText(String.valueOf(account.getAccountID()));
        }
        if (lblEmail != null) {
            lblEmail.setText(Optional.ofNullable(account.getEmail()).orElse("-"));
        }
        if (lblAccountType != null) {
            lblAccountType.setText(account.isSubscription() ? "Subscription Customer" : "Standard Customer");
        }
        if (txtName != null) {
            txtName.setText(Optional.ofNullable(account.getFullName()).orElse(""));
        }
        if (txtPassword != null) {
            txtPassword.clear();
        }
        if (txtCardNumber != null) {
            txtCardNumber.clear();
            String masked = maskCardNumber(account.getCreditCardNumber());
            txtCardNumber.setPromptText(masked.isEmpty() ? "Enter card number" : masked);
        }
    }

    @FXML
    private void onSaveClicked(ActionEvent event) {
        if (currentAccount == null) {
            NavigationService.getInstance().navigate("Login");
            return;
        }

        String name = txtName != null ? txtName.getText().trim() : "";
        if (name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation error", "Name is required.");
            return;
        }

        String password = txtPassword != null ? txtPassword.getText().trim() : "";
        if (!password.isEmpty() && password.length() < 4) {
            showAlert(Alert.AlertType.WARNING, "Validation error", "Password must be at least 4 characters.");
            return;
        }

        String cardInput = txtCardNumber != null ? txtCardNumber.getText().trim() : "";
        Long cardNumber = null;
        if (!cardInput.isEmpty()) {
            if (!cardInput.matches("\\d{12,19}")) {
                showAlert(Alert.AlertType.WARNING, "Validation error", "Card number must be 12-19 digits.");
                return;
            }
            try {
                cardNumber = Long.parseLong(cardInput);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Validation error", "Card number is invalid.");
                return;
            }
        }

        Account updated = buildUpdatedAccount(name, password, cardNumber);
        UpdateMessage update = new UpdateMessage("account", "edit");
        update.setAccount(updated);
        awaitingSaveResponse = true;
        try {
            SimpleClient.getClient().sendToServer(update);
        } catch (IOException e) {
            awaitingSaveResponse = false;
            showAlert(Alert.AlertType.ERROR, "Save failed", "Unable to reach the server. Please try again.");
        }
    }

    @FXML
    private void onCancelClicked(ActionEvent event) {
        NavigationService.getInstance().navigate("Catalog");
    }

    @Subscribe
    public void handleUserUpdateResponse(UserUpdateResponse response) {
        if (!awaitingSaveResponse) {
            return;
        }
        awaitingSaveResponse = false;
        if (response == null || !response.isSuccess()) {
            String message = response != null ? response.getMessage() : null;
            if (message == null || message.isBlank()) {
                message = "Failed to save account details. Please try again.";
            }
            showAlert(Alert.AlertType.ERROR, "Save failed", message);
            return;
        }
        SimpleClient.setAccount(currentAccount);
        EventBus.getDefault().post(new PassAccountEvent(currentAccount));
        showAlert(Alert.AlertType.INFORMATION, "Saved", "Account details saved successfully.");
        NavigationService.getInstance().navigate("Catalog");
    }

    private Account buildUpdatedAccount(String name, String password, Long cardNumber) {
        Account updated = new Account();
        updated.setAccountID(currentAccount.getAccountID());
        updated.setID((int) currentAccount.getID());
        updated.setFullName(name);
        updated.setAddress(currentAccount.getAddress());
        updated.setEmail(currentAccount.getEmail());
        updated.setPassword(password.isEmpty() ? currentAccount.getPassword() : password);
        updated.setPhoneNumber(currentAccount.getPhoneNumber());
        updated.setCreditCardNumber(cardNumber != null ? cardNumber : currentAccount.getCreditCardNumber());
        updated.setCreditMonthExpire(currentAccount.getCreditMonthExpire());
        updated.setCreditYearExpire(currentAccount.getCreditYearExpire());
        updated.setCcv(currentAccount.getCcv());
        updated.setLoggedIn(currentAccount.getLoggedIn());
        updated.setBelongShop(currentAccount.getBelongShop());
        updated.setSubscription(currentAccount.isSubscription());
        updated.setPrivialge(currentAccount.getPrivialge());
        updated.setFrozen(currentAccount.getFrozen());
        updated.setCreditBalance(currentAccount.getCreditBalance());
        currentAccount = updated;
        return updated;
    }

    private String maskCardNumber(long cardNumber) {
        if (cardNumber <= 0) {
            return "";
        }
        String digits = Long.toString(cardNumber);
        if (digits.length() <= 4) {
            return digits;
        }
        return "•••• " + digits.substring(digits.length() - 4);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
