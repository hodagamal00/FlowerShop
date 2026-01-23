/**
 * Sample Skeleton for 'replycomplaint.fxml' Controller Class
 */


package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.GetAllComplaints;
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

    @FXML
    private TextField createdAtField;

    @FXML
    private TextField respondedAtField;

    @FXML
    private TextField slaStatusField;

    @FXML
    private TextField compensationDecisionField;
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
        int compensationAmount = 0;
        boolean willReturnMoney=  false;
        selectedComplaint.setAccepted(true);
        selectedComplaint.setAnswerworkerID(currentUser.getAccountID());
        selectedComplaint.setReplyText(replyText.getText());
        selectedComplaint.setRespondedAt(new Date());
        if(refundCheck.isSelected())
        {
            willReturnMoney = true;
            String selection = refundPercent.getSelectionModel().getSelectedItem();
            if (selection == null || selection.isBlank()) {
                showAlert("Please select a compensation amount.");
                return;
            }
            compensationAmount = parseCompensationAmount(selection);
            if (compensationAmount < 0) {
                showAlert("Compensation amount must be a positive number.");
                return;
            }
            selectedComplaint.setCompensationDecision(compensationAmount + "₪ compensation approved");
        }
        else {
            selectedComplaint.setCompensationDecision("No compensation");
        }
        selectedComplaint.setReturnedMoney(willReturnMoney);
        selectedComplaint.setReturnedmoneyvalue(compensationAmount);

        System.out.println("ReplyComplaintController before updating complaint");
        UpdateMessage update_complaint = new UpdateMessage("complaint","edit");
        update_complaint.setComplaint(selectedComplaint);

        try {
            SimpleClient.getClient().sendToServer(update_complaint);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ReplyComplaintController before updating complaint");
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

    private int parseCompensationAmount(String selection) {
        String digitsOnly = selection.replaceAll("[^0-9]", "");
        if (digitsOnly.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(digitsOnly);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Compensation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isLateStatus(String status) {
        if (status == null) {
            return false;
        }
        return "RESOLVED_LATE".equalsIgnoreCase(status)
                || "OVERDUE".equalsIgnoreCase(status)
                || "LATE".equalsIgnoreCase(status);
    }

    Complaint selectedComplaint = new Complaint();
    @FXML
    void loadComplaints(ActionEvent event)
    {
        requestAllComplaints();
    }
    Account currentUser;
    List<Complaint> retrievedComplaints = new ArrayList<>();
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
        if (!AccessGuard.requireMinPrivilege(2)) {
            return;
        }
        EventBus.getDefault().register(this);
        assert accountID != null : "fx:id=\"accountID\" was not injected: check your FXML file 'replycomplaint.fxml'.";
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
        assert createdAtField != null : "fx:id=\"createdAtField\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert respondedAtField != null : "fx:id=\"respondedAtField\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert slaStatusField != null : "fx:id=\"slaStatusField\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert compensationDecisionField != null : "fx:id=\"compensationDecisionField\" was not injected: check your FXML file 'replycomplaint.fxml'.";

        complaintList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            loadSelectedComplaint(newValue);
        });

        refundPercent.getItems().add("25₪");
        refundPercent.getItems().add("50₪");
        refundPercent.getItems().add("75₪");
        refundPercent.getItems().add("100₪");

        //Complaint a = new Complaint(0,23,22,false,false,"Fuck you",2,0,false,0,23,2,2004,"");
        refundPercent.setVisible(false);
        loadButton.setDisable(true);
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
                        other.setVisible(true);
                        wait.setVisible(false);
                    }
                },4500
        );
        requestAllComplaints();
    }

    @Subscribe
    public void retrieveAllComplaints(PassAllComplaintsEvent complaints){
        System.out.println("we're in retrieveAllComplaints");
        List<Complaint> allComplaints = complaints.getComplaintsToPass();
        System.out.println("SIZE = " + allComplaints.size());
        retrievedComplaints = allComplaints;
        refreshComplaintList();
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
    private String formatTimestamp(Date date) {
        if (date == null) {
            return "-";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }

    private void requestAllComplaints() {
        try {
            SimpleClient.getClient().sendToServer(new GetAllComplaints());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshComplaintList() {
        complaintList.getItems().clear();
        if (retrievedComplaints == null) {
            return;
        }
        for (Complaint complaint : retrievedComplaints) {
            if (!complaint.isAccepted()) {
                String entry = "#" + complaint.getComplaintID() + " - " + complaint.getDay() + "/" + complaint.getMonth() + "/" + complaint.getYear();
                if (isLateStatus(complaint.getSlaStatus())) {
                    entry = entry + " (Late)";
                }
                complaintList.getItems().add(entry);
            }
        }
    }

    private void loadSelectedComplaint(String selectedEntry) {
        if (selectedEntry == null || selectedEntry.isBlank()) {
            return;
        }
        if (retrievedComplaints == null) {
            return;
        }
        int selectedId = parseComplaintId(selectedEntry);
        if (selectedId == -1) {
            return;
        }
        for (Complaint complaint : retrievedComplaints) {
            if (complaint.getComplaintID() == selectedId) {
                selectedComplaint = complaint;
                break;
            }
        }
        if (selectedComplaint == null) {
            return;
        }
        sendButton.setVisible(true);
        complaintID.setText(String.valueOf(selectedComplaint.getComplaintID()));
        accountID.setText(String.valueOf(selectedComplaint.getCustomerID()));
        orderID.setText(String.valueOf(selectedComplaint.getOrderID()));
        complaintDate.setText(selectedComplaint.getDate());
        complaintText.setText(selectedComplaint.getComplaintText());
        createdAtField.setText(formatTimestamp(selectedComplaint.getCreatedAt()));
        respondedAtField.setText(formatTimestamp(selectedComplaint.getRespondedAt()));
        slaStatusField.setText(selectedComplaint.getSlaStatus());
        compensationDecisionField.setText(selectedComplaint.getCompensationDecision());
    }

    private int parseComplaintId(String selectedEntry) {
        int spaceIndex = selectedEntry.indexOf(' ');
        if (spaceIndex <= 1) {
            return -1;
        }
        String idPart = selectedEntry.substring(1, spaceIndex);
        try {
            return Integer.parseInt(idPart);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

}
