package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Cart;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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

    String email_regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";
    String creditCard_regex = "^\\d{16}$";
    String CVV_regex = "^\\d{3}$";
    String phoneNum_regex = "^\\d{10}$";
    String ID_regex = "^\\d{9}$";


    @FXML
        void AddCustomerToDB(ActionEvent event) throws IOException {
        email_regex_error.setVisible(false);
        phone_regex_error.setVisible(false);
        card_regex_error.setVisible(false);
        CVV_regex_error.setVisible(false);
        fieldsError.setVisible(false);
        shopError.setVisible(false);
        ID_Bad.setVisible(false);

        if(registerShop.isSelected())
        {
            if(selectChain.getSelectionModel().getSelectedIndex() == -1)
            {
                shopError.setVisible(true);
            }
        }
        if(Email.getText() == "" || Password.getText() == "" || Name.getText() == "" || userID.getText() == "" || PhoneNumber.getText() == "" || Street_Address.getText() == "" || City_Address.getText() == "" || CardNumber.getText() == "" || CVV.getText() == "" || chooseMonth.getSelectionModel().getSelectedIndex() == -1 || chooseYear.getSelectionModel().getSelectedIndex() == -1)
        {
            fieldsError.setVisible(true);
        }
        Pattern pattern = Pattern.compile(email_regex);
        Matcher matcher = pattern.matcher(Email.getText());
        if(!matcher.matches()){
            email_regex_error.setVisible(true);
        }
        pattern = Pattern.compile(creditCard_regex);
        matcher = pattern.matcher(CardNumber.getText());
        if(!matcher.matches()){
            card_regex_error.setVisible(true);
        }
        pattern = Pattern.compile(CVV_regex);
        matcher = pattern.matcher(CVV.getText());
        if(!matcher.matches()){
            CVV_regex_error.setVisible(true);
        }
        pattern = Pattern.compile(phoneNum_regex);
        matcher = pattern.matcher(PhoneNumber.getText());
        if(!matcher.matches()){
            phone_regex_error.setVisible(true);
        }
        pattern = Pattern.compile(ID_regex);
        matcher = pattern.matcher(userID.getText());
        if(!matcher.matches()){
            ID_Bad.setVisible(true);
        }
        else if(!RegisteredAccounts.contains(Email.getText()))
        {
            String Address = Street_Address.getText() + ", " + City_Address.getText();
            int shopID = 0;
            if(registerChain.isSelected())
                shopID = 0;
            else
            {
                switch (selectChain.getValue().toString())
                {
                    case "ID 0: - Chain":
                        shopID = 0;
                        break;
                    case "ID 1: Tiberias, Big Danilof":
                        shopID  = 1;
                        break;
                    case "ID 2: Haifa, Merkaz Zeiv":
                        shopID  = 2;
                        break;
                    case "ID 3: Tel Aviv, Ramat Aviv":
                        shopID  = 3;
                        break;
                    case "ID 4: Eilat, Ice mall":
                        shopID  = 4;
                        break;
                    case "ID 5: Be'er Sheva, Big Beer Sheva":
                        shopID = 5;
                        break;
                }

            }
            long id = Integer.parseInt(userID.getText());
            System.out.println("ID = " + id);
            //Account new_acc = new Account(Name.getText(),Address,Email.getText(),Password.getText(),Long.parseLong(PhoneNumber.getText()),Long.parseLong(CardNumber.getText()),Integer.parseInt(chooseYear.getSelectionModel().getSelectedItem()),Integer.parseInt(chooseMonth.getSelectionModel().getSelectedItem()) ,Integer.parseInt(CVV.getText()), shopID);
            Account new_acc = new Account(0,Name.getText(),id,Address,Email.getText(),Password.getText(),Long.parseLong(PhoneNumber.getText()),Long.parseLong(CardNumber.getText()),Integer.parseInt(chooseMonth.getSelectionModel().getSelectedItem()),Integer.parseInt(chooseYear.getSelectionModel().getSelectedItem()),Integer.parseInt(CVV.getText()),false,shopID,subscription.isSelected());
            new_acc.setPrivialge(1);
            System.out.println("Registering To Shop " + shopID);
            RegisteredAccounts.add(Email.getText());


            UpdateMessage new_msg2=new UpdateMessage("account","add");
            //account_num++;
            //new_acc.setAccountID(account_num);
            //new_msg2.setId(account_num);
            new_msg2.setAccount(new_acc);
            try {
                System.out.println("before sending updateMessage to server ");
                SimpleClient.getClient().sendToServer(new_msg2); // sends the updated product to the server class
                System.out.println("afater sending updateMessage to server ");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInPrim.fxml"));
            Parent roott = loader.load();
            LogInPrimary cc = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(roott));
            stage.setTitle("Delivery Panel");
            stage.show();
            Stage stagee = (Stage)RegisterButton.getScene().getWindow();
            stagee.close();

        }
        else{
            ErrorMsg.setVisible(true);
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

    @FXML
    void backkk(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInPrim.fxml"));
        Parent roott = loader.load();
        LogInPrimary cc = loader.getController();
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
