package il.cshaifasweng.OCSFMediatorExample.client;


// Removed unused AWT imports.  Using AWT classes like java.awt.Button
// alongside JavaFX controls causes "reference is ambiguous" compilation
// errors because both frameworks define classes such as Button and List.
// This controller uses JavaFX exclusively, so these imports are unnecessary.
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.NextComplaintIdMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;





public class  ComplaintController{

    @FXML
    private Button cancelcomp;

    @FXML
    private TextField comptxt;

    @FXML
    private TextField emailtxt;

    @FXML
    private TextField nametxt;

    @FXML
    private Button submitcomp;

    @FXML
    private TextField complaintIdField;

    @FXML
    private Label submissionStatusLabel;

    @FXML
    private TextField topictxt;

    @FXML
    void cancelcmp(ActionEvent event){
        Stage stage = (Stage) cancelcomp.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void subcomp(ActionEvent event){
        /*
         * Gather the complaint details and send a new Complaint entity to the server.
         * We require a logged-in account in order to associate the complaint with a customer.
         * If no account is available, simply close the form without sending anything.
         */
        if (currentUser == null) {
            // No logged-in user; block submission
            showStatus("Please log in to submit a complaint.", true);
            return;
        }
        // Build a new Complaint object.  We set default values for fields not captured in the form
        // such as order ID (0 by default) and shop ID (0).  If we already reserved an ID when the
        // form opened, reuse it so that the number shown to the user matches the stored complaint.
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        int month = cal.get(java.util.Calendar.MONTH) + 1; // Calendar months are 0-based
        int year = cal.get(java.util.Calendar.YEAR);
        Complaint newComplaint = new Complaint();
        if (reservedComplaintId != null) {
            newComplaint.setComplaintID(reservedComplaintId);
        }

        newComplaint.setCustomerID(currentUser.getAccountID());
        newComplaint.setOrderID(0);
        newComplaint.setAccepted(false);
        newComplaint.setIn24Hours(false);
        // Combine the topic and detailed description into the complaint text
        String topic = topictxt.getText() != null ? topictxt.getText().trim() : "";
        String details = comptxt.getText() != null ? comptxt.getText().trim() : "";
        newComplaint.setComplaintText(topic + "\n" + details);
        newComplaint.setShopID(currentUser.getBelongShop());
        newComplaint.setAnswerworkerID(0);
        newComplaint.setReturnedMoney(false);
        newComplaint.setReturnedmoneyvalue(0);
        newComplaint.setDay(day);
        newComplaint.setMonth(month);
        newComplaint.setYear(year);
        newComplaint.setReplyText("");
        newComplaint.setCreatedAt(new Date());
        newComplaint.setSlaStatus("IN_PROGRESS");
        newComplaint.setCompensationDecision("Pending review");
        // Prepare update message to add the complaint
        UpdateMessage msg = new UpdateMessage("complaint", "add");
        msg.setComplaint(newComplaint);
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Optionally send a confirmation message to the user inbox
        Message confirm = new Message();
        confirm.setCustomerID(currentUser.getAccountID());
        confirm.setMsgText("We have received your complaint and will respond within 24 hours.");
        UpdateMessage messageUpdate = new UpdateMessage("message", "add");
        messageUpdate.setMessage(confirm);
        try {
            SimpleClient.getClient().sendToServer(messageUpdate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        showStatus("Complaint submitted. Response within 24 hours.", false);
        submitcomp.setDisable(true);
    }

    @FXML
    void initialize() throws MalformedURLException
    {
        // Register this controller to receive EventBus events
        EventBus.getDefault().register(this);
        if (submissionStatusLabel != null) {
            submissionStatusLabel.setVisible(false);
        }
        if (submitcomp != null) {
            submitcomp.setDisable(true);
        }
        requestNextComplaintId();

    }

    /**
     * The logged-in user who is submitting the complaint.  This is provided via
     * a PassAccountEventComplaints event when the complaint form is opened.
     */
    private Account currentUser;
    private Integer reservedComplaintId;

    @Subscribe
    public void handlePassAccountEvent(PassAccountEventComplaints passAcc) {
        // Assign the received account to currentUser
        this.currentUser = passAcc.getRecievedAccount();
        if (submitcomp != null) {
            submitcomp.setDisable(currentUser == null);
        }
    }
    @Subscribe
    public void handleNextComplaintId(NextComplaintIdEvent event) {
        reservedComplaintId = event.getComplaintId();
        if (complaintIdField != null) {
            complaintIdField.setText(Integer.toString(reservedComplaintId));
        }
    }

    private void requestNextComplaintId() {
        try {
            SimpleClient.getClient().sendToServer(new NextComplaintIdMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showStatus(String message, boolean isError) {
        if (submissionStatusLabel == null) {
            return;
        }
        submissionStatusLabel.setText(message);
        submissionStatusLabel.setVisible(true);
        submissionStatusLabel.setStyle(isError
                ? "-fx-text-fill: #c0392b; -fx-font-style: italic;"
                : "-fx-text-fill: #27ae60; -fx-font-style: italic;");
    }


}
