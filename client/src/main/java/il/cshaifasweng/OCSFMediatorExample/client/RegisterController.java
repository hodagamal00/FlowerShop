package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class RegisterController {


    @FXML // fx:id="fieldsError"
    private Label fieldsError; // Value injected by FXMLLoader


    @FXML // fx:id="shopError"
    private Label shopError; // Value injected by FXMLLoader

    @FXML
    private ResourceBundle resources;

    @FXML
    private ComboBox<String> selectChain;
    @FXML
    private URL location;

    @FXML
    private TextField CVV;

    @FXML
    private TextField CardNumber;

    @FXML
    private TextField City_Address;

    @FXML
    private TextField Email;

    @FXML
    private TextField Name;

    @FXML
    private TextField PhoneNumber;

    @FXML
    private Button RegisterButton;

    @FXML
    private Button backk;


    @FXML
    private TextField Street_Address;

    @FXML
    private TextField Validity;

    @FXML
    private TextField userID;

    @FXML
    private Label ErrorMsg;

    @FXML
    private TextField Password;

    @FXML
    private Label CVV_regex_error;

    @FXML
    private Label card_regex_error;

    @FXML
    private Label email_regex_error;

    @FXML
    private Label phone_regex_error;


    @FXML
    private ComboBox<String> chooseMonth;

    @FXML
    private ComboBox<String> chooseYear;


    @FXML // fx:id="registerChain"
    private CheckBox registerChain; // Value injected by FXMLLoader

    @FXML // fx:id="registerShop"
    private CheckBox registerShop; // Value injected by FXMLLoader

    @FXML // fx:id="subscription"
    private CheckBox subscription; // Value injected by FXMLLoader


    @FXML // fx:id="ID_Bad"
    private Label ID_Bad; // Value injected by FXMLLoader

    private LinkedList<String> RegisteredAccounts = new LinkedList<>(); // list of all registered emails
    private boolean registrationPending = false;
    private String pendingEmail;

    String email_regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";
    String creditCard_regex = "^\\d{16}$";
    String CVV_regex = "^\\d{3}$";
    String phoneNum_regex = "^\\d{10}$";
    String ID_regex = "^\\d{9}$";


    @FXML
    void AddCustomerToDB(ActionEvent event) throws IOException {
        // Clear previous error messages
        email_regex_error.setVisible(false);
        phone_regex_error.setVisible(false);
        card_regex_error.setVisible(false);
        CVV_regex_error.setVisible(false);
        fieldsError.setVisible(false);
        shopError.setVisible(false);
        ID_Bad.setVisible(false);
        ErrorMsg.setVisible(false);
        boolean hasError = false;

        // Validate required fields
        if(registerShop.isSelected()) {
            if(selectChain.getSelectionModel().getSelectedIndex() == -1) {
                shopError.setText("Please select a shop to register with");
                shopError.setVisible(true);
                hasError = true;
            }
        }

        if(Email.getText().isEmpty() || Password.getText().isEmpty() || Name.getText().isEmpty() ||
                userID.getText().isEmpty() || PhoneNumber.getText().isEmpty() || Street_Address.getText().isEmpty() ||
                City_Address.getText().isEmpty() || CardNumber.getText().isEmpty() || CVV.getText().isEmpty() ||
                chooseMonth.getSelectionModel().getSelectedIndex() == -1 || chooseYear.getSelectionModel().getSelectedIndex() == -1) {
            fieldsError.setText("Please fill in all required fields");
            fieldsError.setVisible(true);
            hasError = true;
        }

        // Validate email format
        Pattern pattern = Pattern.compile(email_regex);
        Matcher matcher = pattern.matcher(Email.getText());
        if(!matcher.matches()){
            email_regex_error.setText("Please enter a valid email address");
            email_regex_error.setVisible(true);
            hasError = true;
        }

        // Validate credit card
        pattern = Pattern.compile(creditCard_regex);
        matcher = pattern.matcher(CardNumber.getText());
        if(!matcher.matches()){
            card_regex_error.setText("Credit card must be 16 digits");
            card_regex_error.setVisible(true);
            hasError = true;
        }

        // Validate CVV
        pattern = Pattern.compile(CVV_regex);
        matcher = pattern.matcher(CVV.getText());
        if(!matcher.matches()){
            CVV_regex_error.setText("CVV must be 3 digits");
            CVV_regex_error.setVisible(true);
            hasError = true;
        }

        // Validate phone
        pattern = Pattern.compile(phoneNum_regex);
        matcher = pattern.matcher(PhoneNumber.getText());
        if(!matcher.matches()){
            phone_regex_error.setText("Phone number must be 10 digits");
            phone_regex_error.setVisible(true);
            hasError = true;
        }

        // Validate ID
        pattern = Pattern.compile(ID_regex);
        matcher = pattern.matcher(userID.getText());
        if(!matcher.matches()){
            ID_Bad.setText("ID must be 9 digits");
            ID_Bad.setVisible(true);
            hasError = true;
        }

        // FIX 1: Check local duplicate email before sending to server
        if(RegisteredAccounts.contains(Email.getText())) {
            ErrorMsg.setText("This email is already registered. Please use a different email or try logging in.");
            ErrorMsg.setVisible(true);
            hasError = true;
        }

        if(!hasError) {
            try {
                String Address = Street_Address.getText() + ", " + City_Address.getText();
                int shopID = 0;

                if(registerChain.isSelected()) {
                    shopID = 0;
                } else {
                    switch (selectChain.getValue()) {
                        case "ID 1: Tiberias, Big Danilof": shopID = 1; break;
                        case "ID 2: Haifa, Merkaz Zeiv": shopID = 2; break;
                        case "ID 3: Tel Aviv, Ramat Aviv": shopID = 3; break;
                        case "ID 4: Eilat, Ice mall": shopID = 4; break;
                        case "ID 5: Be'er Sheva, Big Beer Sheva": shopID = 5; break;
                        default: shopID = 0; break;
                    }
                }

                long id = Long.parseLong(userID.getText());
                Account new_acc = new Account(0, Name.getText(), id, Address, Email.getText(),
                        Password.getText(), Long.parseLong(PhoneNumber.getText()),
                        Long.parseLong(CardNumber.getText()),
                        Integer.parseInt(chooseMonth.getSelectionModel().getSelectedItem()),
                        Integer.parseInt(chooseYear.getSelectionModel().getSelectedItem()),
                        Integer.parseInt(CVV.getText()), true, shopID, subscription.isSelected());
                new_acc.setPrivialge(1);

                System.out.println("Registering To Shop " + shopID);

                UpdateMessage new_msg2 = new UpdateMessage("account", "add");
                new_msg2.setAccount(new_acc);

                System.out.println("before sending updateMessage to server ");
                registrationPending = true;
                pendingEmail = new_acc.getEmail();
                RegisterButton.setDisable(true);
                RegisterButton.setText("Registering...");
                SimpleClient.getClient().sendToServer(new_msg2);
                System.out.println("after sending updateMessage to server ");

            } catch (IOException e) {
                e.printStackTrace();
                registrationPending = false;
                pendingEmail = null;
                RegisterButton.setDisable(false);
                RegisterButton.setText("Register");
                ErrorMsg.setText("Unable to contact the server. Please check your internet connection and try again.");
                ErrorMsg.setVisible(true);
            } catch (NumberFormatException e) {
                ErrorMsg.setText("Invalid number format in one of the fields. Please check your input.");
                ErrorMsg.setVisible(true);
            }
        }
    }

    @FXML
    void checkedChain(ActionEvent event)
    {
        if(registerChain.isSelected()) {
            registerShop.setSelected(false);
            selectChain.setVisible(false);
        }
        else
        {
            registerShop.setSelected(true);
            selectChain.setVisible(true);
        }

    }

    @FXML
    void checkedShop(ActionEvent event)
    {
        if(registerShop.isSelected())
        {
            registerChain.setSelected(false);
            selectChain.setVisible(true);
        }
        else
        {
            registerChain.setSelected(true);
            selectChain.setVisible(false);
        }
    }
    @FXML
    void checkSubscription(ActionEvent event)
    {
        if(subscription.isSelected())
        {
            registerChain.setSelected(true);
            registerShop.setSelected(false);
            selectChain.setVisible(false);
        }
    }

    @FXML
    void initialize() {
        assert City_Address != null : "fx:id=\"City_Address\" was not injected: check your FXML file 'register.fxml'.";
        assert Email != null : "fx:id=\"Email\" was not injected: check your FXML file 'register.fxml'.";
        assert Name != null : "fx:id=\"Name\" was not injected: check your FXML file 'register.fxml'.";
        assert PhoneNumber != null : "fx:id=\"PhoneNumber\" was not injected: check your FXML file 'register.fxml'.";
        assert RegisterButton != null : "fx:id=\"RegisterButton\" was not injected: check your FXML file 'register.fxml'.";
        assert Street_Address != null : "fx:id=\"Street_Address\" was not injected: check your FXML file 'register.fxml'.";
        assert userID != null : "fx:id=\"ZipCode\" was not injected: check your FXML file 'register.fxml'.";
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        selectChain.getItems().add("ID 1: Tiberias, Big Danilof");
        selectChain.getItems().add("ID 2: Haifa, Merkaz Zeiv");
        selectChain.getItems().add("ID 3: Tel Aviv, Ramat Aviv");
        selectChain.getItems().add("ID 4: Eilat, Ice mall");
        selectChain.getItems().add("ID 5: Be'er Sheva, Big Beer Sheva");

        selectChain.setVisible(true);
        registerShop.setSelected(true);

        shopError.setVisible(false);
        fieldsError.setVisible(false);


        ErrorMsg.setVisible(false);
        CVV_regex_error.setVisible(false);
        phone_regex_error.setVisible(false);
        email_regex_error.setVisible(false);
        card_regex_error.setVisible(false);
        ID_Bad.setVisible(false);

        int i;
        for(i = 1 ; i < 13 ; i++)
        { chooseMonth.getItems().add(String.valueOf(i)); }
        for( i = 2000 ; i < 2030 ; i++)
        { chooseYear.getItems().add(String.valueOf(i));  }
    }
    @Subscribe
    public void handleAccountCreated(PassAccountEvent event) {
        if(!registrationPending) {
            return;
        }
        Account createdAccount = event.getRecievedAccount();
        if(createdAccount == null || pendingEmail == null) {
            return;
        }
        if(!createdAccount.getEmail().equalsIgnoreCase(pendingEmail)) {
            return;
        }

        registrationPending = false;
        pendingEmail = null;
        RegisteredAccounts.add(createdAccount.getEmail());

        Platform.runLater(() -> {
            RegisterButton.setDisable(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText("Welcome to FlowerShop!");
            alert.setContentText("Your account has been created and you're now signed in.");
            alert.showAndWait();
            NavigationService.getInstance().navigate("primary");
        });
    }

    @Subscribe
    public void handleRegistrationFailure(RegistrationResultEvent event) {
        if(event.isSuccess()) {
            return;
        }
        registrationPending = false;
        pendingEmail = null;
        Platform.runLater(() -> {
            RegisterButton.setDisable(false);
            ErrorMsg.setText(event.getMessage());
            ErrorMsg.setVisible(true);
        });
    }
    @FXML
    void backkk(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent roott = loader.load();
        LoginController cc = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(roott));
        stage.setTitle("Login");
        stage.show();
        Stage stagee = (Stage) backk.getScene().getWindow();
        // do what you have to do
        stagee.close();
    }
    public LinkedList<String> getRegisteredAccounts(){
        return RegisteredAccounts;
    }

}
