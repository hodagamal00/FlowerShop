package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;

// Removed unused AWT imports.  Keeping AWT alongside JavaFX can
// introduce ambiguous references (e.g., both have a Button class).  This
// controller relies on JavaFX for UI, so these imports are unnecessary.
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import il.cshaifasweng.OCSFMediatorExample.client.NavigationService;
import javafx.application.Platform;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
public class LoginController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Email"
    private TextField Email; // Value injected by FXMLLoader

    @FXML // fx:id="ErrorMsg"
    private Label ErrorMsg; // Value injected by FXMLLoader

    @FXML // fx:id="ErrorMsgPass"
    private Label ErrorMsgPass; // Value injected by FXMLLoader

    @FXML // fx:id="Guest"
    private Button Guest; // Value injected by FXMLLoader

    @FXML // fx:id="LogIn"
    private Button LogIn; // Value injected by FXMLLoader

    @FXML // fx:id="Password"
    private TextField Password; // Value injected by FXMLLoader

    @FXML // fx:id="RegisterTab"
    private Button RegisterTab; // Value injected by FXMLLoader


    @FXML // fx:id="OpenCatalogplz"
    private Button OpenCatalogplz; // Value injected by FXMLLoader


    @FXML // fx:id="logSucc"
    private Text logSucc; // Value injected by FXMLLoader

    @FXML // fx:id="backLog"
    private Button backLog; // Value injected by FXMLLoader


    @FXML // fx:id="alLog"
    private Text alLog; // Value injected by FXMLLoader

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
        // When using the AppShell, navigate to the catalog view by replacing
        // the centre content instead of opening a new window.  The flag
        // indicates that the catalog should be displayed for a guest (0) or
        // authenticated user (handled elsewhere).
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
        // When using the AppShell, simply replace the centre content with
        // the registration form instead of spawning a new stage.  The
        // AppShell remains visible and only the centre content changes.
        NavigationService.getInstance().navigate("register");

    }
    String login_flag = "";

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

        // By default, show the email/password fields and primary login button.  In the
        // original implementation these controls were hidden until a secondary
        // "Login" tab was clicked, which confused users because there were no
        // visible input fields on the login page.  To make the login screen
        // intuitive, we keep the credentials fields visible and hide only the
        // optional status labels.  The OpenCatalogplz button will remain
        // invisible until a successful login occurs.

        logSucc.setVisible(false);
        OpenCatalogplz.setVisible(false);
        EventBus.getDefault().register(this);
        // Show email and password input controls so the user can enter their
        // credentials immediately.
        Email.setVisible(true);
        Password.setVisible(true);
        LogIn.setVisible(true);
        // Hide error labels until needed
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
        // Hide the back button on first load; it becomes visible during login flows
        backLog.setVisible(false);
        // Hide the generic alert label initially
        alLog.setVisible(false);
    }
    @FXML
    void openCatalogFunc(ActionEvent event) throws IOException {
        // A guest or authenticated user can proceed to the catalog.  We
        // persist the current email (for message retrieval) but rely on
        // the NavigationService to swap the centre content instead of
        // opening a new window.  This method is invoked when the
        // "Continue to Catalog" button is clicked after successful
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
        NavigationService.getInstance().navigate("primary");
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
    private ActionEvent lastLoginEvent;
    private Account authenticatedAccount;
    private boolean navigationPendingAccount;

    void handleLogin(ActionEvent event) {
        lastLoginEvent = event;
        // Clear previous error messages
        ErrorMsg.setVisible(false);
        ErrorMsgPass.setVisible(false);
        alLog.setVisible(false);

        String email = Email.getText().trim();
        String password = Password.getText();

        // Basic validation
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

        // Basic email format validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        if (!email.matches(emailRegex)) {
            ErrorMsg.setText("Please enter a valid email address");
            ErrorMsg.setVisible(true);
            return;
        }

        // Disable login button during processing
        LogIn.setDisable(true);
        LogIn.setText("Logging in...");

        try {
            // Create CheckMail object and send to server
            CheckMail loginRequest = new CheckMail(email, password);
            SimpleClient.getClient().sendToServer(loginRequest);

        } catch (IOException e) {
            LogIn.setDisable(false);
            LogIn.setText("Log In");
            ErrorMsg.setText("Unable to connect to server. Please check your internet connection.");
            ErrorMsg.setVisible(true);
        } catch (Exception e) {
            LogIn.setDisable(false);
            LogIn.setText("Log In");
            ErrorMsg.setText("An error occurred. Please try again.");
            ErrorMsg.setVisible(true);
            e.printStackTrace();
        }
    }

    private void resetLoginButton() {
        Platform.runLater(() -> {
            LogIn.setDisable(false);
            LogIn.setText("Log In");
        });
    }

    private void handleLoginSuccess(Account account) {
        resetLoginButton();
        if (account == null) {
            navigationPendingAccount = true;
            return;
        }

        authenticatedAccount = account;
        navigationPendingAccount = false;
        CatalogFlag.setFlagg(1);
        navigateAfterLogin(account);
        lastLoginEvent = null;
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
    public void checkMailInDB(MailChecker checkML) throws IOException
    {
        System.out.println("IM HERE :DDD");
        if(checkML.getExistsMail() == false){ // case incorrect email
            System.out.println("arrived to case incorrect email succesfully");
            ErrorMsg.setVisible(true);
            resetLoginButton();

            /*MailPassMatch checkEmailPass = new MailPassMatch(Email.getText(),Password.getText(),login_flag);
            try {
                SimpleClient.getClient().sendToServer(checkEmailPass);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
        }
        else if(checkML.getExistsPassword() == false)
        { // case email found but the password is incorrect
            System.out.println("arrived to case incorrect password  succesfully");
            ErrorMsgPass.setVisible(true);
            resetLoginButton();
        }
        else if(checkML.isLoggedIn() == false)
        {
            System.out.println("WE GOT HERE, GOOD EMAIL");
            itWorked = true;
            requestFix++;
            Account account = resolveAuthenticatedAccount();
            handleLoginSuccess(account);
        }
        else if(checkML.isLoggedIn() == true)
        {
            System.out.println("Already Logged In");
            alreadyLogged = true;
            resetLoginButton();
            Platform.runLater(() -> {
                alLog.setText("User already logged in from another session.");
                alLog.setVisible(true);
            });
        }

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

}
