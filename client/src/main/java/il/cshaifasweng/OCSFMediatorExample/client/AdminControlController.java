package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AdminControlController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="accID"
    private TextField accID; // Value injected by FXMLLoader

    @FXML // fx:id="accIDTxt"
    private Text accIDTxt; // Value injected by FXMLLoader

    @FXML // fx:id="accountsList"
    private ListView<String> accountsList; // Value injected by FXMLLoader

    @FXML // fx:id="accountsText"
    private Text accountsText; // Value injected by FXMLLoader

    @FXML // fx:id="address"
    private TextField address; // Value injected by FXMLLoader

    @FXML // fx:id="addressTxt"
    private Text addressTxt; // Value injected by FXMLLoader

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="creditMonthTxt"
    private Text creditMonthTxt; // Value injected by FXMLLoader

    @FXML // fx:id="creditNumTxt"
    private Text creditNumTxt; // Value injected by FXMLLoader

    @FXML // fx:id="creditexpYearTxt"
    private Text creditexpYearTxt; // Value injected by FXMLLoader

    @FXML // fx:id="creditexpmonth"
    private TextField creditexpmonth; // Value injected by FXMLLoader

    @FXML // fx:id="creditexpyear"
    private TextField creditexpyear; // Value injected by FXMLLoader

    @FXML // fx:id="creditnum"
    private TextField creditnum; // Value injected by FXMLLoader

    @FXML // fx:id="cusIDTxt"
    private Text cusIDTxt; // Value injected by FXMLLoader

    @FXML // fx:id="customerID"
    private TextField customerID; // Value injected by FXMLLoader

    @FXML // fx:id="cvv"
    private TextField cvv; // Value injected by FXMLLoader

    @FXML // fx:id="cvvTxt"
    private Text cvvTxt; // Value injected by FXMLLoader

    @FXML // fx:id="email"
    private TextField email; // Value injected by FXMLLoader

    @FXML // fx:id="emailTxt"
    private Text emailTxt; // Value injected by FXMLLoader

    @FXML // fx:id="freeze"
    private Button freeze; // Value injected by FXMLLoader

    @FXML // fx:id="idk"
    private Button idk; // Value injected by FXMLLoader

    @FXML // fx:id="loadProfile"
    private Button loadProfile; // Value injected by FXMLLoader

    @FXML // fx:id="logged"
    private TextField logged; // Value injected by FXMLLoader

    @FXML // fx:id="loggedTxt"
    private Text loggedTxt; // Value injected by FXMLLoader

    @FXML // fx:id="name"
    private TextField name; // Value injected by FXMLLoader

    @FXML // fx:id="nameTxt"
    private Text nameTxt; // Value injected by FXMLLoader

    @FXML // fx:id="passTxt"
    private Text passTxt; // Value injected by FXMLLoader

    @FXML // fx:id="password"
    private TextField password; // Value injected by FXMLLoader

    @FXML // fx:id="phone"
    private TextField phone; // Value injected by FXMLLoader

    @FXML // fx:id="phoneTxt"
    private Text phoneTxt; // Value injected by FXMLLoader

    @FXML // fx:id="profileType"
    private ComboBox<String> profileType; // Value injected by FXMLLoader

    @FXML // fx:id="shop"
    private TextField shop; // Value injected by FXMLLoader

    @FXML // fx:id="shopTxt"
    private Text shopTxt; // Value injected by FXMLLoader

    @FXML // fx:id="sub"
    private TextField sub; // Value injected by FXMLLoader

    @FXML // fx:id="subTxt"
    private Text subTxt; // Value injected by FXMLLoader

    @FXML // fx:id="wait"
    private Label wait; // Value injected by FXMLLoader

    @FXML // fx:id="Save"
    private Button Save; // Value injected by FXMLLoader

    @FXML // fx:id="privilageField"
    private TextField privilageField; // Value injected by FXMLLoader

    @FXML
    void SaveChanges(ActionEvent event) {
        if(profileType.getSelectionModel().getSelectedItem() == "Customers")
        {
            Account acc = new Account(Integer.parseInt(accID.getText()),name.getText(),Integer.parseInt(customerID.getText()),address.getText(),email.getText(),password.getText(),Long.parseLong(phone.getText()),Long.parseLong(creditnum.getText()),Integer.parseInt(creditexpmonth.getText()),Integer.parseInt(creditexpyear.getText()),Integer.parseInt(cvv.getText()),false,thisShop,thisSub);
            acc.setPrivialge(Integer.parseInt(privilageField.getText()));
            UpdateMessage update_acc = new UpdateMessage("account","edit");
            update_acc.setAccount(acc);
            try {
                SimpleClient.getClient().sendToServer(update_acc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(profileType.getSelectionModel().getSelectedItem() == "Workers")
        {
            Worker work = new Worker(name.getText(),email.getText(),password.getText(),Integer.parseInt(customerID.getText()));
            UpdateMessage update_worker = new UpdateMessage("worker","edit");
            update_worker.setWorker(work);
            try {
                SimpleClient.getClient().sendToServer(update_worker);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(profileType.getSelectionModel().getSelectedItem() == "Managers")
        {
            System.out.println("Name : " + name.getText());
            System.out.println("Mail: " + email.getText());
            System.out.println("Pass : " + password.getText());
            System.out.println("custemrID : " + customerID.getText());
            Manager manager = new Manager(name.getText(),email.getText(),password.getText(),Integer.parseInt(customerID.getText()));
            UpdateMessage update_manager = new UpdateMessage("manager","edit");
            update_manager.setManager(manager);
            System.out.println(update_manager.getManager().getEmail());
            try {
                SimpleClient.getClient().sendToServer(update_manager);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean thisSub = false;
    int thisShop = 0;
    int selectType = 0;
    @FXML
    void loadSelectProfile(ActionEvent event)
    {
            String SelectedIDString = "";
            int SelectedID;
            String SelectedComplaint = accountsList.getSelectionModel().getSelectedItem();
            Account selectedAcc = new Account();
            Worker selectedWork = new Worker();
            Manager selectedMan = new Manager();
            for (int i = 1; SelectedComplaint.charAt(i) != ' '; i++) {
                SelectedIDString = SelectedIDString + Character.toString(SelectedComplaint.charAt(i));
            }
            SelectedID = Integer.parseInt(SelectedIDString);
            if(selectType == 1)
            {
                for(int i = 0 ; i < all_accounts.size() ; i++)
                {
                    if (SelectedID == all_accounts.get(i).getAccountID())
                        selectedAcc = all_accounts.get(i);
                }
                accID.setText(Integer.toString(selectedAcc.getAccountID()));
                customerID.setText(Long.toString(selectedAcc.getID()));
                name.setText(selectedAcc.getFullName());
                address.setText(selectedAcc.getAddress());
                email.setText(selectedAcc.getEmail());
                password.setText(selectedAcc.getPassword());
                phone.setText(Long.toString(selectedAcc.getPhoneNumber()));
                creditnum.setText(Long.toString(selectedAcc.getCreditCardNumber()));
                creditexpmonth.setText(Integer.toString(selectedAcc.getCreditMonthExpire()));
                creditexpyear.setText(Integer.toString(selectedAcc.getCreditYearExpire()));
                cvv.setText(Integer.toString(selectedAcc.getCcv()));
                privilageField.setText(Integer.toString(selectedAcc.getPrivialge()));
                switch (selectedAcc.getBelongShop()) {
                    case 0:
                        shop.setText("ID 0: - Chain");
                        thisShop = 0;
                        break;
                    case 1:
                        shop.setText("Tiberias, Big Danilof");
                        thisShop = 1;
                        break;
                    case 2:
                        shop.setText("Haifa, Merkaz Zeiv");
                        thisShop = 2;
                        break;
                    case 3:
                        shop.setText("Tel Aviv, Ramat Aviv");
                        thisShop = 3;
                        break;
                    case 4:
                        shop.setText("Eilat, Ice mall");
                        thisShop = 4;
                        break;
                    case 5:
                        shop.setText("Be'er Sheva, Big Beer Sheva");
                        thisShop = 5;
                        break;
                }
                if(selectedAcc.isSubscription() == true) {
                    thisSub = true;
                    sub.setText("Yes");
                }
                else {
                    thisSub = false;
                    sub.setText("No");
                }
            }
            if(selectType == 2)
            {
                for(int i = 0 ; i < all_workers.size() ; i++) {
                    if (SelectedID == all_workers.get(i).getPersonID())
                        selectedWork = all_workers.get(i);
                }
                // getPersonID returns a long; convert to String via String.valueOf to avoid
                // lossy conversion (int) warnings.
                accID.setText(String.valueOf(selectedWork.getPersonID()));
                email.setText(selectedWork.getEmail());
                name.setText(selectedWork.getFullName());
                password.setText(selectedWork.getPassword());
                customerID.setText(String.valueOf(selectedWork.getPersonID()));
            }
            if(selectType == 3) {
                for (int i = 0; i < all_managers.size(); i++) {
                    if (SelectedID == all_managers.get(i).getPersonID())
                        selectedMan = all_managers.get(i);
                }
                customerID.setText(String.valueOf(selectedMan.getPersonID()));
                email.setText(selectedMan.getEmail());
                name.setText(selectedMan.getFullName());
                password.setText(selectedMan.getPassword());
                switch (selectedMan.getShopID()) {
                    case 0:
                        shop.setText("ID 0: - Chain");
                        break;
                    case 1:
                        shop.setText("Tiberias, Big Danilof");
                        break;
                    case 2:
                        shop.setText("Haifa, Merkaz Zeiv");
                        break;
                    case 3:
                        shop.setText("Tel Aviv, Ramat Aviv");
                        break;
                    case 4:
                        shop.setText("Eilat, Ice mall");
                        break;
                    case 5:
                        shop.setText("Be'er Sheva, Big Beer Sheva");
                        break;
                }
            }
        }

    @FXML
    void openCatalog(ActionEvent event) throws IOException {

        PassAccountEvent recievedAcc = new PassAccountEvent(currentUser);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(recievedAcc);
                        System.out.println("the server sent me the account , NICE 4 !!");
                    }
                },4000
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Catalog.fxml"));
        Parent roott = loader.load();
        CatalogController cc = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(roott));
        stage.setTitle("Catalog");
        stage.show();
        Stage stagee = (Stage)backButton.getScene().getWindow();
        stagee.close();

    }

    @FXML
    void selectType(ActionEvent event) {
        String aString = "";
        accountsList.getItems().clear();
        if(profileType.getSelectionModel().getSelectedItem() == "Customers")
        {
            selectType = 1;
            for(int i = 0 ; i < all_accounts.size() ; i++)
            {
                aString = "#" + all_accounts.get(i).getAccountID() + " - " + all_accounts.get(i).getFullName();
                accountsList.getItems().add(aString);
                aString = "";
            }
        }
        else if(profileType.getSelectionModel().getSelectedItem() == "Workers")
        {
            selectType = 2;
            for(int i = 0 ; i < all_workers.size() ; i++)
            {
                aString = "#" + all_workers.get(i).getPersonID() + " - " + all_workers.get(i).getFullName();
                accountsList.getItems().add(aString);
                aString = "";
            }

        }
        else if(profileType.getSelectionModel().getSelectedItem() == "Managers")
        {
            selectType = 3;
            for(int i = 0 ; i < all_managers.size() ; i++)
            {
                aString = "#" + all_managers.get(i).getPersonID() + " - " + all_managers.get(i).getFullName();
                accountsList.getItems().add(aString);
                aString = "";
            }
        }
        loadProfile.setVisible(true);

    }

    public List<Account> all_accounts = new ArrayList<>();
    public List<Manager> all_managers = new ArrayList<>();
    public List<Worker> all_workers = new ArrayList<>();
    Account currentUser;
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        EventBus.getDefault().register(this);
        assert accID != null : "fx:id=\"accID\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert accIDTxt != null : "fx:id=\"accIDTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert accountsList != null : "fx:id=\"accountsList\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert accountsText != null : "fx:id=\"accountsText\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert address != null : "fx:id=\"address\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert addressTxt != null : "fx:id=\"addressTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert creditMonthTxt != null : "fx:id=\"creditMonthTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert creditNumTxt != null : "fx:id=\"creditNumTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert creditexpYearTxt != null : "fx:id=\"creditexpYearTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert creditexpmonth != null : "fx:id=\"creditexpmonth\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert creditexpyear != null : "fx:id=\"creditexpyear\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert creditnum != null : "fx:id=\"creditnum\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert cusIDTxt != null : "fx:id=\"cusIDTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert customerID != null : "fx:id=\"customerID\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert cvv != null : "fx:id=\"cvv\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert cvvTxt != null : "fx:id=\"cvvTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert emailTxt != null : "fx:id=\"emailTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert freeze != null : "fx:id=\"freeze\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert idk != null : "fx:id=\"idk\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert loadProfile != null : "fx:id=\"loadProfile\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert logged != null : "fx:id=\"logged\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert loggedTxt != null : "fx:id=\"loggedTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert nameTxt != null : "fx:id=\"nameTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert passTxt != null : "fx:id=\"passTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert phone != null : "fx:id=\"phone\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert phoneTxt != null : "fx:id=\"phoneTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert profileType != null : "fx:id=\"profileType\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert shop != null : "fx:id=\"shop\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert shopTxt != null : "fx:id=\"shopTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert sub != null : "fx:id=\"sub\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert subTxt != null : "fx:id=\"subTxt\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert Save != null : "fx:id=\"Save\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert privilageField != null : "fx:id=\"privilageField\" was not injected: check your FXML file 'admincontrol.fxml'.";

        try {
            SimpleClient.getClient().sendToServer("get Managers");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SimpleClient.getClient().sendToServer("get Workers");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SimpleClient.getClient().sendToServer("get Accounts");
        } catch (IOException e) {
            e.printStackTrace();
        }

        profileType.getItems().add("Customers");
        profileType.getItems().add("Workers");
        profileType.getItems().add("Managers");
        loadProfile.setVisible(false);

        freeze.setVisible(false);
        profileType.setDisable(true);
        loadProfile.setDisable(true);
        Save.setDisable(true);
        backButton.setDisable(true);


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        profileType.setDisable(false);
                        loadProfile.setDisable(false);
                        Save.setDisable(false);
                        backButton.setDisable(false);
                        wait.setVisible(false);
                    }
                },4500
        );
    }

    @Subscribe
    public void getManagersOrWorkersFromDB(FoundTable foundTable){
        if(foundTable.getMessage().equals("managers table found")){
            all_managers = foundTable.getRecievedManagers();
            System.out.println("all managers size is " + all_managers.size());
        }
        else{
            all_workers = foundTable.getRecievedWokers();
            System.out.println("all workers size is " + all_workers.size());
        }
    }
    @Subscribe
    public void getAllAccountFromDB(List<Account> receivedAccounts){
        System.out.println("in getAllAccountFromDB");
        all_accounts = receivedAccounts;
        System.out.println("all accounts size is " + all_accounts.size());
    }

    @Subscribe
    public void PassAccountEvent(PassAccountEventAdmin passAcc){ // added 30/7
        System.out.println("Arrived To Pass Account - deliveryManager!");
        Account recvAccount = passAcc.getRecievedAccount();
        System.out.println(recvAccount.getPassword());
        System.out.println(recvAccount.getAccountID());
        System.out.println(recvAccount.getEmail());
        System.out.println(recvAccount.getFullName());
        System.out.println(recvAccount.getAddress());
        System.out.println(recvAccount.getCreditCardNumber());
        System.out.println(recvAccount.getCreditMonthExpire());
        currentUser = recvAccount;
    }

}