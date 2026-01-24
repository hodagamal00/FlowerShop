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
import il.cshaifasweng.OCSFMediatorExample.entities.ComplaintUpdateResponse;
import il.cshaifasweng.OCSFMediatorExample.entities.GetAllComplaints;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.UserUpdateResponse;
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

    @FXML
    private TextField createdAtField;

    @FXML
    private TextField respondedAtField;

    @FXML
    private TextField compensationDecisionField;
    @FXML // fx:id="refundCheck"
    private CheckBox refundCheck; // Value injected by FXMLLoader

    @FXML
    private TextField refundAmountField;

    @FXML // fx:id="sendButton"
    private Button sendButton; // Value injected by FXMLLoader

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader


    @FXML // fx:id="other"
    private Text other; // Value injected by FXMLLoader


    @FXML // fx:id="wait"
    private Label wait; // Value injected by FXMLLoader

    @FXML
    private TextField replyText;

    @FXML
    void SendReply(ActionEvent event)
    {
        if (currentUser == null) {
            currentUser = SimpleClient.getAccount();
        }
        if (currentUser == null) {
            showError("You must be logged in to send a response.");
            return;
        }
        if (selectedComplaint == null || selectedComplaint.getComplaintID() <= 0) {
            showAlert("Please select a complaint to respond to.");
            return;
        }
        String replyBody = replyText.getText() == null ? "" : replyText.getText().trim();
        if (replyBody.isEmpty()) {
            showAlert("Please enter a response before sending.");
            return;
        }
        int compensationAmount = 0;
        boolean willReturnMoney = false;
        selectedComplaint.setAccepted(true);
        selectedComplaint.setAnswerworkerID(currentUser.getAccountID());
        selectedComplaint.setReplyText(replyBody);
        selectedComplaint.setRespondedAt(new Date());
        if(refundCheck.isSelected())
        {
            willReturnMoney = true;
            String amountText = refundAmountField.getText() == null ? "" : refundAmountField.getText().trim();
            if (amountText.isEmpty()) {
                showAlert("Please enter a compensation amount.");
                return;
            }
            compensationAmount = parseCompensationAmount(amountText);
            if (compensationAmount <= 0) {
                showAlert("Compensation amount must be greater than zero.");
                return;
            }
            selectedComplaint.setCompensationDecision("Compensation approved");
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
            awaitingComplaintUpdate = true;
            sendButton.setDisable(true);
            SimpleClient.getClient().sendToServer(update_complaint);

        } catch (IOException e) {
            awaitingComplaintUpdate = false;
            sendButton.setDisable(false);
            e.printStackTrace();
            showError("Failed to send response. Please try again.");
        }
        System.out.println("ReplyComplaintController before updating complaint");
        System.out.println("ReplyComplaintController after updating complaint");

    }

    @FXML
    void addRefund(ActionEvent event)
    {
        if (refundCheck.isSelected()) {
            refundAmountField.setVisible(true);
        } else {
            refundAmountField.setVisible(false);
            refundAmountField.clear();
        }
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

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Complaint Response");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Complaint Response");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    Complaint selectedComplaint = new Complaint();
    private boolean awaitingComplaintUpdate = false;
    @FXML
    void loadComplaints(ActionEvent event)
    {
        requestAllComplaints();
    }
    @FXML
    void backToDashboard(ActionEvent event) throws IOException {

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("WorkerDashboard.fxml"));
        Parent roott = loader.load();
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(roott));
        stage.setTitle("Worker Dashboard");
        stage.setMaximized(true);
        stage.show();

    }

    Account currentUser;
    List<Complaint> retrievedComplaints = new ArrayList<>();
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
        if (!AccessGuard.requireMinPrivilege(2)) {
            return;
        }
        EventBus.getDefault().register(this);
        currentUser = SimpleClient.getAccount();
        assert accountID != null : "fx:id=\"accountID\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert complaintDate != null : "fx:id=\"complaintDate\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert complaintID != null : "fx:id=\"complaintID\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert complaintList != null : "fx:id=\"complaintList\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert complaintText != null : "fx:id=\"complaintText\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert loadButton != null : "fx:id=\"loadButton\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert orderID != null : "fx:id=\"orderID\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert refundCheck != null : "fx:id=\"refundCheck\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert refundAmountField != null : "fx:id=\"refundAmountField\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert sendButton != null : "fx:id=\"sendButton\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert other != null : "fx:id=\"other\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert createdAtField != null : "fx:id=\"createdAtField\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert respondedAtField != null : "fx:id=\"respondedAtField\" was not injected: check your FXML file 'replycomplaint.fxml'.";
        assert compensationDecisionField != null : "fx:id=\"compensationDecisionField\" was not injected: check your FXML file 'replycomplaint.fxml'.";

        complaintList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            loadSelectedComplaint(newValue);
        });

        //Complaint a = new Complaint(0,23,22,false,false,"Fuck you",2,0,false,0,23,2,2004,"");
        refundAmountField.setVisible(false);
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

    @Subscribe
    public void handleComplaintUpdateResponse(ComplaintUpdateResponse response) {
        if (!awaitingComplaintUpdate) {
            return;
        }
        awaitingComplaintUpdate = false;
        sendButton.setDisable(false);
        if (response == null || !response.isSuccess()) {
            String message = response != null && response.getMessage() != null
                    ? response.getMessage()
                    : "Failed to update complaint response.";
            showError(message);
            return;
        }
        if (response.getComplaint() != null) {
            selectedComplaint = response.getComplaint();
            respondedAtField.setText(formatTimestamp(selectedComplaint.getRespondedAt()));
            compensationDecisionField.setText(selectedComplaint.getCompensationDecision());
        }
        showSuccess(response.getMessage() != null ? response.getMessage() : "Response sent.");
        sendComplaintNotification();
        requestAllComplaints();
    }

    @Subscribe
    public void handleComplaintUpdateAuthFailure(UserUpdateResponse response) {
        if (!awaitingComplaintUpdate || response == null || response.isSuccess()) {
            return;
        }
        awaitingComplaintUpdate = false;
        sendButton.setDisable(false);
        showError(response.getMessage() != null ? response.getMessage() : "Unable to send response.");
    }

    private void sendComplaintNotification() {
        if (selectedComplaint == null) {
            return;
        }
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
        try {
            SimpleClient.getClient().sendToServer(updateMessage1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String formatTimestamp(Date date) {
        if (date == null) {
            return "-";
        }
        return new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(date);
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
        displayedComplaints.clear();
        for (Complaint complaint : retrievedComplaints) {
            displayedComplaints.add(complaint);
            complaintList.getItems().add(formatListEntry(complaint));
        }
    }

    private void loadSelectedComplaint(String selectedEntry) {
        int selectedIndex = complaintList.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= displayedComplaints.size()) {
            return;
        }
        selectedComplaint = displayedComplaints.get(selectedIndex);
        sendButton.setVisible(true);
        complaintID.setText(String.valueOf(selectedComplaint.getComplaintID()));
        accountID.setText(String.valueOf(selectedComplaint.getCustomerID()));
        orderID.setText(String.valueOf(selectedComplaint.getOrderID()));
        complaintDate.setText(selectedComplaint.getDate());
        complaintText.setText(selectedComplaint.getComplaintText());
        createdAtField.setText(formatTimestamp(selectedComplaint.getCreatedAt()));
        respondedAtField.setText(formatTimestamp(selectedComplaint.getRespondedAt()));
        compensationDecisionField.setText(selectedComplaint.getCompensationDecision());
    }

    private String formatListEntry(Complaint complaint) {
        String entry = "Complaint – " + formatListTimestamp(complaint);
        if (complaint.getRespondedAt() != null || complaint.isAccepted()) {
            entry += " (Resolved)";
        }
        return entry;
    }

    private String formatListTimestamp(Complaint complaint) {
        Date createdAt = complaint.getCreatedAt();
        if (createdAt != null) {
            return formatTimestamp(createdAt);
        }
        return String.format("%02d/%02d/%04d - --:--", complaint.getDay(), complaint.getMonth(), complaint.getYear());
    }

    private final List<Complaint> displayedComplaints = new ArrayList<>();

}
