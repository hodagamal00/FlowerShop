package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    }

    @FXML
    void gotoCatalog(ActionEvent event) throws IOException {
        CatalogFlag.setFlagg(0);
        NavigationService.getInstance().navigate("primary");
    }

    @FXML
    void gotoLogInSecondary(ActionEvent event) {
        RegisterTab.setVisible(false);
        Guest.setVisible(false);
        LogIn.setVisible(true);
        Email.setVisible(true);
        Password.setVisible(true);
        backLog.setVisible(true);
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
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
        backLog.setVisible(false);
        alLog.setVisible(false);

        // חשוב בשביל ה־@Subscribe
        EventBus.getDefault().register(this);
    }

    @FXML
    void handleLogin(ActionEvent event) {
        // تنظيف رسائل قديمة
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
        alLog.setVisible(false);
        logSucc.setVisible(false);
        OpenCatalogplz.setVisible(false);

        String email = Email.getText().trim();
        String password = Password.getText();

        // فحص أساسي
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

        // فحص فورمات الإيميل
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        if (!email.matches(emailRegex)) {
            ErrorMsg.setText("Please enter a valid email address");
            ErrorMsg.setVisible(true);
            return;
        }

        // تجهيز زر اللوج-إن
        LogIn.setDisable(true);
        LogIn.setText("Logging in...");

        // إرسال الطلب للسيرفر
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
                System.out.println("LoginController: sent CheckMail to server");
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

    private void resetLoginButton() {
        Platform.runLater(() -> {
            LogIn.setDisable(false);
            LogIn.setText("Log In");
        });
    }

    // ===================== ردود السيرفر =====================

    /** نجاح اللوج إن – السيرفر بعث Account / Manager / Worker → SimpleClient عمل PassAccountEvent */
    @Subscribe
    public void onAccountReceived(PassAccountEvent event) {
        Account account = SimpleClient.getUser();
        if (account == null) {
            account = event.getRecievedAccount();
        }
        if (account == null) return;

        final Account acc = account;
        Platform.runLater(() -> {
            resetLoginButton();
            logSucc.setText("Welcome " + (acc.getFullName() == null || acc.getFullName().isBlank()
                    ? acc.getEmail()
                    : acc.getFullName()) + "!");
            logSucc.setVisible(true);
            ErrorMsg.setVisible(false);
            ErrorMsgPass.setVisible(false);
            alLog.setVisible(false);

            int privilege = acc.getPrivialge();
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

            CatalogFlag.setFlagg(1);
            NavigationService.getInstance().navigate(targetView);
        });
    }

    /** فشل اللوج إن – السيرفر رجّع String → SimpleClient حوله لـ MailChecker */
    @Subscribe
    public void onMailCheck(MailChecker checkML) {

        System.out.println("LoginController: got MailChecker event " +
                "existsMail=" + checkML.getExistsMail() +
                ", existsPass=" + checkML.getExistsPassword() +
                ", loggedIn=" + checkML.isLoggedIn());

        Platform.runLater(() -> {
            // أول شيء نرجّع الزر لوضعه الطبيعي
            resetLoginButton();

            if (!checkML.getExistsMail()) {
                ErrorMsg.setText("We couldn't find an account with that email.");
                ErrorMsg.setVisible(true);
            } else if (!checkML.getExistsPassword()) {
                ErrorMsgPass.setText("The password you entered is incorrect.");
                ErrorMsgPass.setVisible(true);
            } else if (checkML.isLoggedIn()) {
                alLog.setText("User already logged in from another session.");
                alLog.setVisible(true);
            }
            // حالة النجاح (existsMail=true, existsPassword=true, loggedIn=false)
            // تعالج في onAccountReceived لما يوصل الـAccount نفسه
        });
    }
}
