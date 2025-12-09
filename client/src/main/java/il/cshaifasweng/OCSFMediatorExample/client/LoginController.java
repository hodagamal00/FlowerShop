package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.client.NavigationService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField Email;

    @FXML
    private Label ErrorMsg;

    @FXML
    private Label ErrorMsgPass;

    @FXML
    private Button Guest;

    @FXML
    private Button LogIn;

    @FXML
    private PasswordField Password;

    @FXML
    private Button RegisterTab;

    @FXML
    private Button OpenCatalogplz;

    @FXML
    private Text logSucc;

    @FXML
    private Button backLog;

    @FXML
    private Text alLog;

    String login_flag = "";

    int requestFix = 0;
    boolean alreadyLogged = false;
    boolean itWorked = false;

    private ActionEvent lastLoginEvent;
    private Account authenticatedAccount;
    private boolean navigationPendingAccount;
    private boolean accountDetailsRequested;

    @FXML
    void ReturnFromLogin(ActionEvent event) {
        logSucc.setVisible(false);
        OpenCatalogplz.setVisible(false);
        Email.setVisible(true);
        Password.setVisible(true);
        LogIn.setVisible(true);
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
        backLog.setVisible(false);
        RegisterTab.setVisible(true);
        Guest.setVisible(true);
        requestFix = 0;
    }

    @FXML
    void gotoCatalog(ActionEvent event) throws IOException {
        CatalogFlag.setFlagg(0);
        NavigationService.getInstance().navigate("primary");
    }

    @FXML
    void gotoLogInSecondary(ActionEvent event) throws IOException {
        RegisterTab.setVisible(false);
        Guest.setVisible(false);
        LogIn.setVisible(true);
        Email.setVisible(true);
        Password.setVisible(true);
        backLog.setVisible(true);
        requestFix = 0;
    }

    @FXML
    void gotoRegisterPage(ActionEvent event) throws IOException {
        NavigationService.getInstance().navigate("register");
    }

    @FXML
    void openCatalogFunc(ActionEvent event) throws IOException {
        CatalogFlag.setFlagg(1);

        String theEmail = Email.getText();
        try {
            SimpleClient.getClient().sendToServer(new MailClass(theEmail));
            SimpleClient.getClient().sendToServer(new GetAllComplaints());
            SimpleClient.getClient().sendToServer(new GetAllMessages());
        } catch (IOException e) {
            e.printStackTrace();
        }

        NavigationService.getInstance().navigate("primary");
    }

    @FXML
    void CustomerLogIn(ActionEvent event) throws IOException {
        login_flag = "customer";
        CatalogFlag.setFlagg(1);
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
        Email.setVisible(true);
        Password.setVisible(true);
        LogIn.setVisible(true);
    }

    @FXML
    void EmployeeLogIn(ActionEvent event) throws IOException {
        CatalogFlag.setFlagg(2);
        login_flag = "employee";
        Email.setVisible(true);
        Password.setVisible(true);
        LogIn.setVisible(true);
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
    }

    @FXML
    void ManagerLogIn(ActionEvent event) throws IOException {
        CatalogFlag.setFlagg(3);
        login_flag = "employee";
        Email.setVisible(true);
        Password.setVisible(true);
        LogIn.setVisible(true);
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
    }

    @FXML
    void initialize() {
        assert Email != null : "fx:id=\"Email\" was not injected: check your FXML file 'Login.fxml'.";
        assert ErrorMsg != null : "fx:id=\"ErrorMsg\" was not injected: check your FXML file 'Login.fxml'.";
        assert ErrorMsgPass != null : "fx:id=\"ErrorMsgPass\" was not injected: check your FXML file 'Login.fxml'.";
        assert Guest != null : "fx:id=\"Guest\" was not injected: check your FXML file 'Login.fxml'.";
        assert LogIn != null : "fx:id=\"LogIn\" was not injected: check your FXML file 'Login.fxml'.";
        assert OpenCatalogplz != null : "fx:id=\"OpenCatalogplz\" was not injected: check your FXML file 'Login.fxml'.";
        assert Password != null : "fx:id=\"Password\" was not injected: check your FXML file 'Login.fxml'.";
        assert RegisterTab != null : "fx:id=\"RegisterTab\" was not injected: check your FXML file 'Login.fxml'.";
        assert alLog != null : "fx:id=\"alLog\" was not injected: check your FXML file 'Login.fxml'.";
        assert backLog != null : "fx:id=\"backLog\" was not injected: check your FXML file 'Login.fxml'.";
        assert logSucc != null : "fx:id=\"logSucc\" was not injected: check your FXML file 'Login.fxml'.";

        logSucc.setVisible(false);
        OpenCatalogplz.setVisible(false);
        EventBus.getDefault().register(this);

        Email.setVisible(true);
        Password.setVisible(true);
        LogIn.setVisible(true);

        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
        backLog.setVisible(false);
        alLog.setVisible(false);
    }

    @FXML
    void handleLogin(ActionEvent event) {
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
        alLog.setVisible(false);
        logSucc.setVisible(false);
        OpenCatalogplz.setVisible(false);

        String email = Email.getText().trim();
        String password = Password.getText();

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                ErrorMsg.setText("Please enter your email address");
                ErrorMsg.setVisible(true);
            }
            if (password.isEmpty()) {
                ErrorMsgPass.setText("Please enter your password");
                ErrorMsgPass.setVisible(true);
            }
            return;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        if (!email.matches(emailRegex)) {
            ErrorMsg.setText("Please enter a valid email address");
            ErrorMsg.setVisible(true);
            return;
        }

        lastLoginEvent = event;
        accountDetailsRequested = false;

        LogIn.setDisable(true);
        LogIn.setText("Logging in...");

        attemptLoginAsync(email, password);
    }

    private void resetLoginButton() {
        Platform.runLater(() -> {
            LogIn.setDisable(false);
            LogIn.setText("Log In");
        });
    }

    private void attemptLoginAsync(String email, String password) {
        SimpleClient client = SimpleClient.getClient();
        if (client == null) {
            ErrorMsg.setText("Unable to access the server. Please try again later.");
            ErrorMsg.setVisible(true);
            resetLoginButton();
            return;
        }

        new Thread(() -> {
            if (!client.isConnected()) {
                try {
                    client.openConnection();
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        ErrorMsg.setText("Unable to connect to server. Please check your internet connection.");
                        ErrorMsg.setVisible(true);
                        resetLoginButton();
                    });
                    return;
                }
            }

            try {
                CheckMail loginRequest = new CheckMail(email, password);
                client.sendToServer(loginRequest);
            } catch (IOException e) {
                Platform.runLater(() -> {
                    ErrorMsg.setText("Unable to connect to server. Please check your internet connection.");
                    ErrorMsg.setVisible(true);
                    resetLoginButton();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    ErrorMsg.setText("An error occurred. Please try again.");
                    ErrorMsg.setVisible(true);
                    resetLoginButton();
                });
                e.printStackTrace();
            }
        }).start();
    }

    private void handleLoginSuccess(Account account) {
        resetLoginButton();
        if (account == null) {
            navigationPendingAccount = true;
            showSuccessMessage("Login successful! Loading your account details...");
            requestAccountDetails();
            return;
        }

        authenticatedAccount = account;
        navigationPendingAccount = false;
        String displayName = account.getFullName();
        if (displayName == null || displayName.isBlank()) {
            displayName = account.getEmail();
        }
        showSuccessMessage(String.format("Welcome %s! Redirecting to your dashboard...", displayName));
        sendPostLoginData(account);
        CatalogFlag.setFlagg(1);
        navigateAfterLogin(account);
        lastLoginEvent = null;
        accountDetailsRequested = false;
    }

    private void showSuccessMessage(String message) {
        Platform.runLater(() -> {
            logSucc.setText(message);
            logSucc.setVisible(true);
            OpenCatalogplz.setVisible(false);
            alLog.setVisible(false);
        });
    }

    private void navigateAfterLogin(Account account) {
        Platform.runLater(() -> {
            int privilege = account.getPrivialge();
            String targetView;
            if (privilege >= 4) {
                targetView = "NetworkDashboard";
            } else if (privilege >= 3) {
                targetView = "log_manager";
            } else if (privilege >= 2) {
                targetView = "WorkerDashboard";
            } else {
                targetView = "primary";
            }
            NavigationService.getInstance().navigate(targetView);
        });
    }

    @Subscribe
    public void checkMailInDB(MailChecker checkML) {
        System.out.println("IM HERE :DDD");
        Platform.runLater(() -> {
            if (!checkML.getExistsMail()) {   // incorrect email
                System.out.println("arrived to case incorrect email succesfully");
                ErrorMsg.setVisible(true);
                resetLoginButton();
            } else if (!checkML.getExistsPassword()) {  // wrong password
                System.out.println("arrived to case incorrect password succesfully");
                ErrorMsgPass.setVisible(true);
                resetLoginButton();
            } else if (!checkML.isLoggedIn()) {         // good email + pass
                System.out.println("WE GOT HERE, GOOD EMAIL");
                itWorked = true;
                requestFix++;
                Account account = resolveAuthenticatedAccount();
                handleLoginSuccess(account);
            } else {                                    // already logged in
                System.out.println("Already Logged In");
                alreadyLogged = true;
                resetLoginButton();
                alLog.setText("User already logged in from another session.");
                alLog.setVisible(true);
            }
        });
    }

    @Subscribe
    public void checkMailPass(MailPassMatch checkEmailPass) {
        System.out.println("Checking Mail IN DB");
        Platform.runLater(() -> {
            if (checkEmailPass.getexists()) {
                Account account = resolveAuthenticatedAccount();
                handleLoginSuccess(account);
            } else {
                ErrorMsgPass.setVisible(true);
                resetLoginButton();
            }
        });
    }

    @Subscribe
    public void onAccountReceived(PassAccountEvent event) {
        Account account = SimpleClient.getUser();
        if (account == null) {
            account = event.getRecievedAccount();
        }
        if (account == null) {
            return;
        }
        authenticatedAccount = account;
        if (navigationPendingAccount) {
            handleLoginSuccess(account);
        }
    }

    private Account resolveAuthenticatedAccount() {
        Account account = authenticatedAccount;
        if (account == null) {
            account = SimpleClient.getUser();
        }
        return account;
    }

    private void requestAccountDetails() {
        String email = Email.getText().trim();
        if (email.isEmpty()) {
            return;
        }
        if (accountDetailsRequested) {
            return;
        }
        accountDetailsRequested = true;
        new Thread(() -> {
            try {
                SimpleClient.getClient().sendToServer(new MailClass(email));
            } catch (IOException e) {
                accountDetailsRequested = false;
                navigationPendingAccount = false;
                Platform.runLater(() -> {
                    logSucc.setVisible(false);
                    alLog.setText("Unable to load account details. Please try again.");
                    alLog.setVisible(true);
                });
                resetLoginButton();
            }
        }).start();
    }

    private void sendPostLoginData(Account account) {
        if (account == null) {
            return;
        }
        new Thread(() -> {
            try {
                SimpleClient.getClient().sendToServer(new GetAllComplaints());
                SimpleClient.getClient().sendToServer(new GetAllMessages());
            } catch (IOException e) {
                Platform.runLater(() -> {
                    alLog.setText("Logged in, but we couldn't refresh account data.");
                    alLog.setVisible(true);
                });
            }
        }).start();
    }
}
