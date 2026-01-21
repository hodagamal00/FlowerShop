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

public class LoginController {

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
        NavigationService.getInstance().navigate("Catalog");
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
    void openCatalogFunc(ActionEvent event) throws IOException {
        // A guest or authenticated user can proceed to the catalog.  We
        // persist the current email (for message retrieval) but rely on
        // the NavigationService to swap the centre content instead of
        // opening a new window.  This method is invoked when the
        // "Go to Catalog" button is clicked after successful
        // authentication.
        CatalogFlag.setFlagg(1);
        String theEmail = Email.getText();
        // Send current email to server for message retrieval
        try {
            SimpleClient.getClient().sendToServer(new MailClass(theEmail));
            SimpleClient.getClient().sendToServer(new GetAllComplaints());
            SimpleClient.getClient().sendToServer(new GetAllMessages());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Navigate to the catalog (primary) view within the AppShell
        NavigationService.getInstance().navigate("Catalog");
    }



    @FXML
    void CustomerLogIn(ActionEvent event) throws IOException {
        login_flag = "customer";
        CatalogFlag.setFlagg(1);
      /*  UpdateMessage new_msg=new UpdateMessage("account","add");
        Date date=new Date();
        Account new_acc=new Account("khaled","sakhnin","@eee","332",457,889,date,445,2);
        new_msg.setAccount(new_acc);
        try {
            System.out.println("before sending updateMessage to server ");
            SimpleClient.getClient().sendToServer(new_msg); // sends the updated product to the server class
            System.out.println("afater sending updateMessage to server ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
        //bak.setVisible(true);
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
        //  bak.setVisible(true);
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
        //  bak.setVisible(true);
        ErrorMsgPass.setVisible(false);
    }



    int requestFix = 0;
    boolean alreadyLogged = false;
    private String login_flag;
    private ActionEvent lastLoginEvent;
    private Account authenticatedAccount;
    private boolean navigationPendingAccount;
    private boolean accountDetailsRequested;
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
        // Remember the triggering event so we can navigate after a successful login
        lastLoginEvent = event;
        // Reset any stale state from previous attempts
        accountDetailsRequested = false;


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
                if (login_flag == null || login_flag.isBlank()) {
                    login_flag = "customer";
                }
                CheckMail loginRequest = new CheckMail(email, login_flag, password);
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
        if (account == null) {
            return;
        }
        handleLoginSuccess(account);
    }

    private void showSuccessMessage(Account account) {
        Platform.runLater(() -> {
            resetLoginButton();
            logSucc.setText("Welcome " + (account.getFullName() == null || account.getFullName().isBlank()
                    ? account.getEmail()
                    : account.getFullName()) + "!");
            logSucc.setVisible(true);
            ErrorMsg.setVisible(false);
            ErrorMsgPass.setVisible(false);
            alLog.setVisible(false);

            int privilege = account.getPrivialge();
            String targetView;
            if (privilege >= 4) {
                targetView = "NetworkDashboard";
            } else if (privilege >= 3) {
                targetView = "log_manager";
            } else if (privilege >= 2) {
                targetView = "WorkerDashboard";
            } else {
                targetView = "Catalog";
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
            } else {
                // Credentials are valid; ensure we have account details to continue.
                Account account = resolveAuthenticatedAccount();
                if (account != null) {
                    handleLoginSuccess(account);
                } else {
                    alLog.setText("Logging in...");
                    alLog.setVisible(true);
                    requestAccountDetails();
                }
            }
            // حالة النجاح (existsMail=true, existsPassword=true, loggedIn=false)
            // تعالج في onAccountReceived لما يوصل الـAccount نفسه
        });
    }
    boolean itWorked = false;

    @Subscribe
    public void checkMailPass(MailPassMatch checkEmailPass) throws IOException {
        System.out.println("Checking Mail IN DB");
        if(checkEmailPass.getexists()==true)
        {
            Account account = resolveAuthenticatedAccount();
            handleLoginSuccess(account);
        }
        else{
            ErrorMsgPass.setVisible(true);
            resetLoginButton();
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

    private void handleLoginSuccess(Account account) {
        if (account == null) {
            resetLoginButton();
            return;
        }
        authenticatedAccount = account;
        navigationPendingAccount = false;
        showSuccessMessage(account);
        sendPostLoginData(account);
        CatalogFlag.setFlagg(1);
        lastLoginEvent = null;
        accountDetailsRequested = false;
    }
}
