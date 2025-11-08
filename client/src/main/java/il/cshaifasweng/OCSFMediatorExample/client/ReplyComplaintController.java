/**
 * Sample Skeleton for 'replycomplaint.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
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

public class ReplyComplaintController {


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="accountID"
    private TextField accountID; // Value injected by FXMLLoader

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="complaintDate"
    private TextField complaintDate; // Value injected by FXMLLoader

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

    @FXML // fx:id="refundCheck"
    private CheckBox refundCheck; // Value injected by FXMLLoader

    @FXML // fx:id="refundPercent"
    private ComboBox<String> refundPercent; // Value injected by FXMLLoader

    @FXML // fx:id="sendButton"
    private Button sendButton; // Value injected by FXMLLoader


    @FXML // fx:id="other"
    private Text other; // Value injected by FXMLLoader


    @FXML // fx:id="wait"
    private Label wait; // Value injected by FXMLLoader

    @FXML
    private TextField replyText;

    @FXML
    void SendReply(ActionEvent event)
    {
        int PercentInt = 0;
        String TempPercent = "";
        boolean willReturnMoney=  false;
        int returnedMoney = 0;
        selectedComplaint.setAccepted(true);
        selectedComplaint.setAnswerworkerID(currentUser.getAccountID());
        selectedComplaint.setReplyText(replyText.getText());
        if(refundCheck.isSelected())
        {
            willReturnMoney = true;
            TempPercent = Character.toString(refundPercent.getSelectionModel().getSelectedItem().charAt(0)) + Character.toString(refundPercent.getSelectionModel().getSelectedItem().charAt(1));
            PercentInt = Integer.parseInt(TempPercent);
        }
        selectedComplaint.setReturnedMoney(willReturnMoney);
        selectedComplaint.setReturnedmoneyvalue(PercentInt);

        System.out.println("ReplyComplaintController before updating complaint");
        UpdateMessage update_complaint = new UpdateMessage("complaint","edit");
        update_complaint.setComplaint(selectedComplaint);

        try {
            SimpleClient.getClient().sendToServer(update_complaint);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ReplyComplaintController before updating complaint");
        UpdateMessage update_complainte = new UpdateMessage("complaint","edit");
        update_complainte.setComplaint(selectedComplaint);

        try {
            SimpleClient.getClient().sendToServer(update_complaint);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ReplyComplaintController after updating complaint");

        Calendar calle = Calendar.getInstance();
        int currentYear = calle.get(Calendar.YEAR);
        int currentMonth = calle.get(Calendar.MONTH);
        currentMonth++;
        int currentHour = calle.get(Calendar.HOUR_OF_DAY);
        int currentMintue = calle.get(Calendar.MINUTE);
        int currentDay = calle.get(Calendar.DAY_OF_MONTH);

        Message confirm = new Message();
        confirm.setCustomerID(selectedComplaint.getCustomerID());
        confirm.setMsgText(currentYear + "/" + currentMonth + "/" + currentDay + " - " + currentHour + ":" + currentMintue + ":" + "\n" +  "Your Complaint Has Been Answered!");

        UpdateMessage updateMessage1 = new UpdateMessage("message", "add");
        updateMessage1.setMessage(confirm);
        System.out.println("before try - edit");
        try {
            System.out.println("before sending updateMessage to server ");
            SimpleClient.getClient().sendToServer(updateMessage1);
            System.out.println("afater sending updateMessage to server ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @FXML
    void addRefund(ActionEvent event)
    {
        if(refundCheck.isSelected())
        {
            refundPercent.setVisible(true);
        }
        else
            refundPercent.setVisible(false);
    }

    @FXML
    void addRefundPercent(ActionEvent event) {

    }

    Complaint selectedComplaint = new Complaint();
    @FXML
    void loadComplaints(ActionEvent event)
    {
        String aString = "";
        if(loadButton.getText().equals("Load Complaints"))
        {
            for(int z = 0 ; z < retrievedComplaints.size() ; z++)
            {
                if(retrievedComplaints.get(z).isAccepted() == false)
                {
                    aString = "#" + retrievedComplaints.get(z).getComplaintID() + " - " + retrievedComplaints.get(z).getDay() + "/" + retrievedComplaints.get(z).getMonth() + "/" + retrievedComplaints.get(z).getYear();
                    complaintList.getItems().add(aString);
                    aString = "";
                }
            }
            loadButton.setText("Load Selected Complaint");
        }
        else
        {
            sendButton.setVisible(true);
            String SelectedIDString = "";
            int SelectedID;
            String SelectedComplaint = complaintList.getSelectionModel().getSelectedItem();
            for (int i = 1; SelectedComplaint.charAt(i) != ' '; i++) {
                SelectedIDString = SelectedIDString + Character.toString(SelectedComplaint.charAt(i));
            }
            SelectedID = Integer.parseInt(SelectedIDString);
            for (int i = 0; i < retrievedComplaints.size(); i++) {
                if (retrievedComplaints.get(i).getComplaintID() == SelectedID) {
                    selectedComplaint = retrievedComplaints.get(i);
                    break;
                }
            }
            complaintList.getItems().clear();
            loadButton.setText("Load Complaints");

            complaintID.setText(String.valueOf(selectedComplaint.getComplaintID()));
            accountID.setText(String.valueOf(selectedComplaint.getCustomerID()));
            orderID.setText(String.valueOf(selectedComplaint.getOrderID()));
            complaintDate.setText(selectedComplaint.getDate());
            complaintText.setText(selectedComplaint.getComplaintText());
        }

    }
    @FXML
    void BackToCatalog(ActionEvent event) throws IOException {

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent roott = loader.load();
        PrimaryController cc = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(roott));
        stage.setTitle("Catalog");
        stage.show();
        Stage stagee = (Stage)backButton.getScene().getWindow();
        stagee.close();

    }

    Account currentUser;
    List<Complaint> retrievedComplaints = new ArrayList<>();
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        assert accountID != null : "fx:id=\"accountID\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert complaintDate != null : "fx:id=\"complaintDate\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert complaintID != null : "fx:id=\"complaintID\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert complaintList != null : "fx:id=\"complaintList\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert complaintText != null : "fx:id=\"complaintText\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert loadButton != null : "fx:id=\"loadButton\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert orderID != null : "fx:id=\"orderID\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert refundCheck != null : "fx:id=\"refundCheck\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert refundPercent != null : "fx:id=\"refundPercent\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert sendButton != null : "fx:id=\"sendButton\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert other != null : "fx:id=\"other\" was not injected: check your FXML file 'replycomplaint.fxml'.";

        refundPercent.getItems().add("25%");
        refundPercent.getItems().add("50%");
        refundPercent.getItems().add("75%");
        refundPercent.getItems().add("100%");

        //Complaint a = new Complaint(0,23,22,false,false,"Fuck you",2,0,false,0,23,2,2004,"");
        refundPercent.setVisible(false);
        loadButton.setDisable(true);
        backButton.setDisable(true);
        other.setVisible(false);
        wait.setVisible(true);
        sendButton.setVisible(false);
        //String aString = "Complaint #" + a.getComplaintID() + " " + "Received: " + a.getDay() + "/" + a.getMonth() + "/" + a.getYear();
        //complaintList.getItems().add(aString);
        //retrievedComplaints.add(a);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        loadButton.setDisable(false);
                        backButton.setDisable(false);
                        other.setVisible(true);
                        wait.setVisible(false);
                    }
                },4500
        );
        try {
            SimpleClient.getClient().sendToServer("get complaints");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void retrieveAllComplaints(PassAllComplaintsEvent complaints){
        System.out.println("we're in retrieveAllComplaints");
        List<Complaint> allComplaints = complaints.getComplaintsToPass();
        System.out.println("SIZE = " + allComplaints.size());
        retrievedComplaints = allComplaints;
        for(int i = 0 ; i < allComplaints.size(); i++) {
            System.out.println(allComplaints.get(i).getComplaintText());
        }
    }
    @Subscribe
    public void PassAccountEventReplyComplaint(PassAccountEventReplyComplaint passAcc){ // added today
        System.out.println("Arrived To Pass Account - Reply");
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
