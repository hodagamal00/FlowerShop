package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyComplaintsController {


    Account currentUser;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="answerBool"
    private TextField answerBool; // Value injected by FXMLLoader

    @FXML // fx:id="backToCatalog"
    private Button backToCatalog; // Value injected by FXMLLoader

    @FXML // fx:id="complaintID"
    private TextField complaintID; // Value injected by FXMLLoader

    @FXML // fx:id="complaintList"
    private ListView<String> complaintList; // Value injected by FXMLLoader

    @FXML // fx:id="complaintText"
    private TextField complaintText; // Value injected by FXMLLoader

    @FXML // fx:id="loadButton"
    private Button loadButton; // Value injected by FXMLLoader

    @FXML // fx:id="orderID"
    private TextField orderID; // Value injected by FXMLLoader

    @FXML // fx:id="refundMoney"
    private TextField refundMoney; // Value injected by FXMLLoader

    @FXML // fx:id="replyWorker"
    private TextField replyWorker; // Value injected by FXMLLoader

    @FXML // fx:id="wait"
    private Label wait; // Value injected by FXMLLoader

    @FXML
    void openCatalog(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent roott = loader.load();
        PrimaryController cc = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(roott));
        stage.setTitle("Catalog");
        stage.show();
        Stage stagee = (Stage)backToCatalog.getScene().getWindow();
        stagee.close();

        Account recAcc = currentUser;
        System.out.println("the server sent me the account , NICE 2 !!");
        PassAccountEvent recievedAcc = new PassAccountEvent(recAcc);
        System.out.println("the server sent me the account , NICE 3 !!");
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(recievedAcc);
                        System.out.println("the server sent me the account , NICE 4 !!");
                    }
                },2000
        );

    }

    List<Complaint> allComplaints ;

    @FXML
    void loadComplaints(ActionEvent event) {
        String aString = "";
        if (loadButton.getText().equals("Load Complaints")) {
            for (int z = 0; z < allComplaints.size(); z++) {
                if(currentUser.getAccountID() == allComplaints.get(z).getCustomerID()) {
                    aString = "#" + allComplaints.get(z).getComplaintID() + " - " + allComplaints.get(z).getDay() + "/" + allComplaints.get(z).getMonth() + "/" + allComplaints.get(z).getYear();
                    complaintList.getItems().add(aString);
                    aString = "";
                }
            }
            loadButton.setText("Load Selected Complaint");
        } else {
            String SelectedIDString = "";
            int SelectedID;
            String SelectedComplaint = complaintList.getSelectionModel().getSelectedItem();
            Complaint selectedComplaint = new Complaint();
            for (int i = 1; SelectedComplaint.charAt(i) != ' '; i++) {
                SelectedIDString = SelectedIDString + Character.toString(SelectedComplaint.charAt(i));
            }
            SelectedID = Integer.parseInt(SelectedIDString);
            for (int i = 0; i < complaintList.getItems().size(); i++) {
                if (allComplaints.get(i).getComplaintID() == SelectedID) {
                    selectedComplaint = allComplaints.get(i);
                }
            }
            complaintID.setText(Integer.toString(selectedComplaint.getComplaintID()));
            orderID.setText(Integer.toString(selectedComplaint.getOrderID()));
            if(selectedComplaint.isAccepted() == true) {
                answerBool.setText("Yes");
                refundMoney.setText(Integer.toString(selectedComplaint.getReturnedmoneyvalue()));
            }
            else {
                answerBool.setText("No");
                refundMoney.setText("0");
            }
            replyWorker.setText(Integer.toString(selectedComplaint.getAnswerworkerID()));
            complaintText.setText(selectedComplaint.getComplaintText());
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        EventBus.getDefault().register(this);
        assert answerBool != null : "fx:id=\"answerBool\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert backToCatalog != null : "fx:id=\"backToCatalog\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert complaintID != null : "fx:id=\"complaintID\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert complaintList != null : "fx:id=\"complaintList\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert complaintText != null : "fx:id=\"complaintText\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert loadButton != null : "fx:id=\"loadButton\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert orderID != null : "fx:id=\"orderID\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert refundMoney != null : "fx:id=\"refundMoney\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert replyWorker != null : "fx:id=\"replyWorker\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'mycomplaints.fxml'.";

        loadButton.setDisable(true);
        backToCatalog.setDisable(true);
        wait.setVisible(true);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        loadButton.setDisable(false);
                        backToCatalog.setDisable(false);
                        wait.setVisible(false);
                    }
                },4500
        );
    }
    @Subscribe
    public void PassAccountEvent(PassAccountEventComplaints passAcc){ // added today
        System.out.println("Arrived To Pass Account - complaints!");
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
    @Subscribe
    public void complaintEvent(PassAllComplaintsEvent allComps){ // added new 30/7
        System.out.println("arrived to complaintEvent Subscriber in my complaints!!!!!");
        List<Complaint> recievedComplaints = allComps.getComplaintsToPass();
        allComplaints = allComps.getComplaintsToPass();
        for(int i=0;i<recievedComplaints.size();i++){
            System.out.println(recievedComplaints.get(i).getDay());
        }
    }
}
