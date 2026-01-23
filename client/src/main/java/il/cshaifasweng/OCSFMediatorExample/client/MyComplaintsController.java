package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.GetAllComplaints;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.NextComplaintIdMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MyComplaintsController {


    Account currentUser;
    @FXML // fx:id="answerBool"
    private TextField answerBool; // Value injected by FXMLLoader

    @FXML // fx:id="complaintID"
    private TextField complaintID; // Value injected by FXMLLoader

    @FXML // fx:id="complaintList"
    private ListView<String> complaintList; // Value injected by FXMLLoader

    @FXML // fx:id="complaintText"
    private TextArea complaintText; // Value injected by FXMLLoader

    @FXML // fx:id="loadButton"
    private Button loadButton; // Value injected by FXMLLoader

    @FXML // fx:id="orderID"
    private TextField orderID; // Value injected by FXMLLoader

    @FXML
    private Button submitComplaint; // Value injected by FXMLLoader

    @FXML // fx:id="refundMoney"
    private TextField refundMoney; // Value injected by FXMLLoader

    @FXML
    private TextField respondedAt;

    @FXML
    private TextField compensationDecision;


    private Integer nextComplaintId;
    private Integer selectedOrderId;


    List<Complaint> allComplaints ;

    @FXML
    void loadComplaints(ActionEvent event) {
        requestAllComplaints();
        refreshComplaintList();
    }

    @FXML
    void submitComplaint(ActionEvent event) {
        resolveCurrentUser();
        if (currentUser == null) {
            showAlert("You must be logged in to submit a complaint.");
            return;
        }

        String complaintBody = complaintText.getText() == null ? "" : complaintText.getText().trim();
        if (complaintBody.isEmpty()) {
            showAlert("Please enter a complaint before submitting.");
            return;
        }

        if (selectedOrderId == null || selectedOrderId <= 0) {
            showAlert("A related order must be selected before submitting a complaint.");
            return;
        }
        Calendar cal = Calendar.getInstance();
        Complaint newComplaint = new Complaint();
        if (nextComplaintId != null) {
            newComplaint.setComplaintID(nextComplaintId);
        }

        newComplaint.setCustomerID(currentUser.getAccountID());
        newComplaint.setOrderID(selectedOrderId);
        newComplaint.setAccepted(false);
        newComplaint.setIn24Hours(false);
        newComplaint.setComplaintText(complaintBody);
        newComplaint.setShopID(0);
        newComplaint.setAnswerworkerID(0);
        newComplaint.setReturnedMoney(false);
        newComplaint.setReturnedmoneyvalue(0);
        newComplaint.setDay(cal.get(Calendar.DAY_OF_MONTH));
        newComplaint.setMonth(cal.get(Calendar.MONTH) + 1);
        newComplaint.setYear(cal.get(Calendar.YEAR));
        newComplaint.setReplyText("");

        UpdateMessage msg = new UpdateMessage("complaint", "add");
        msg.setComplaint(newComplaint);
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        if (allComplaints == null) {
            allComplaints = new ArrayList<>();
        }
        if (newComplaint.getComplaintID() == 0 && nextComplaintId != null) {
            newComplaint.setComplaintID(nextComplaintId);
        }
        allComplaints.add(newComplaint);

        refreshComplaintList();
        selectComplaint(newComplaint.getComplaintID());
        requestNextComplaintId();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        EventBus.getDefault().register(this);
        assert answerBool != null : "fx:id=\"answerBool\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert complaintID != null : "fx:id=\"complaintID\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert complaintList != null : "fx:id=\"complaintList\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert complaintText != null : "fx:id=\"complaintText\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert loadButton != null : "fx:id=\"loadButton\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert orderID != null : "fx:id=\"orderID\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert submitComplaint != null : "fx:id=\"submitComplaint\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert refundMoney != null : "fx:id=\"refundMoney\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert respondedAt != null : "fx:id=\"respondedAt\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert compensationDecision != null : "fx:id=\"compensationDecision\" was not injected: check your FXML file 'mycomplaints.fxml'.";

        loadButton.setDisable(true);
        resolveCurrentUser();
        complaintList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Complaint selected = findComplaintByListEntry(newValue);
                if (selected != null) {
                    showComplaintDetails(selected);
                }
            }
        });


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        loadButton.setDisable(false);
                    }
                },4500
        );
        requestAllComplaints();
        requestNextComplaintId();
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
        selectedOrderId = passAcc.getOrderId();
        if (selectedOrderId != null && complaintList.getSelectionModel().getSelectedItem() == null) {
            orderID.setText(Integer.toString(selectedOrderId));
        }
        refreshComplaintList();
        requestAllComplaints();
        requestNextComplaintId();
    }
    @Subscribe
    public void complaintEvent(PassAllComplaintsEvent allComps){ // added new 30/7
        System.out.println("arrived to complaintEvent Subscriber in my complaints!!!!!");
        List<Complaint> recievedComplaints = allComps.getComplaintsToPass();
        allComplaints = allComps.getComplaintsToPass();
        for(int i=0;i<recievedComplaints.size();i++){
            System.out.println(recievedComplaints.get(i).getDay());
        }
        refreshComplaintList();
    }

    @Subscribe
    public void handleNextComplaintId(NextComplaintIdEvent event) {
        nextComplaintId = event.getComplaintId();
        if (complaintList.getSelectionModel().getSelectedItem() == null && complaintID != null) {
            complaintID.setText(Integer.toString(nextComplaintId));
            if (selectedOrderId != null) {
                orderID.setText(Integer.toString(selectedOrderId));
            }
        }
    }

    private void requestNextComplaintId() {
        try {
            SimpleClient.getClient().sendToServer(new NextComplaintIdMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestAllComplaints() {
        try {
            SimpleClient.getClient().sendToServer(new GetAllComplaints());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshComplaintList() {
        Platform.runLater(() -> {
            resolveCurrentUser();
            complaintList.getItems().clear();
            if (allComplaints == null || currentUser == null) {
                return;
            }

            allComplaints.stream()
                    .filter(c -> c.getCustomerID() == currentUser.getAccountID())
                    .sorted((a, b) -> Integer.compare(a.getComplaintID(), b.getComplaintID()))
                    .forEach(c -> complaintList.getItems().add(formatListEntry(c)));
        });
    }

    private void selectComplaint(int complaintId) {
        String entry = null;
        for (String item : complaintList.getItems()) {
            if (parseComplaintId(item) == complaintId) {
                entry = item;
                break;
            }
        }
        if (entry != null) {
            complaintList.getSelectionModel().select(entry);
            Complaint selected = findComplaintByListEntry(entry);
            if (selected != null) {
                showComplaintDetails(selected);
            }
        }
    }

    private Complaint findComplaintByListEntry(String listEntry) {
        int id = parseComplaintId(listEntry);
        if (id == -1 || allComplaints == null) {
            return null;
        }
        for (Complaint complaint : allComplaints) {
            if (complaint.getComplaintID() == id) {
                return complaint;
            }
        }
        return null;
    }

    private int parseComplaintId(String listEntry) {
        if (listEntry == null || listEntry.length() < 2) {
            return -1;
        }
        StringBuilder idBuilder = new StringBuilder();
        for (int i = 1; i < listEntry.length(); i++) {
            char c = listEntry.charAt(i);
            if (Character.isDigit(c)) {
                idBuilder.append(c);
            } else {
                break;
            }
        }
        try {
            return Integer.parseInt(idBuilder.toString());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private String formatListEntry(Complaint complaint) {
        String entry = "#" + complaint.getComplaintID() + " - " + complaint.getDay() + "/" + complaint.getMonth() + "/" + complaint.getYear();
        if (isLateStatus(complaint.getSlaStatus())) {
            entry += " (Late)";
        }
        return entry;
    }

    private void showComplaintDetails(Complaint selectedComplaint) {
        if (selectedComplaint == null) {
            return;
        }
        complaintID.setText(Integer.toString(selectedComplaint.getComplaintID()));
        orderID.setText(Integer.toString(selectedComplaint.getOrderID()));
        if(selectedComplaint.isAccepted()) {
            answerBool.setText("Yes");
            refundMoney.setText(Integer.toString(selectedComplaint.getReturnedmoneyvalue()));
        }
        else {
            answerBool.setText("No");
            refundMoney.setText("0");
        }
        complaintText.setText(selectedComplaint.getComplaintText());
        respondedAt.setText(formatDate(selectedComplaint.getRespondedAt()));
        compensationDecision.setText(defaultIfBlank(selectedComplaint.getCompensationDecision()));
        selectedOrderId = selectedComplaint.getOrderID();
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "—";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    private String defaultIfBlank(String value) {
        if (value == null || value.isBlank()) {
            return "—";
        }
        return value;
    }

    private boolean isLateStatus(String status) {
        if (status == null) {
            return false;
        }
        return "RESOLVED_LATE".equalsIgnoreCase(status)
                || "OVERDUE".equalsIgnoreCase(status)
                || "LATE".equalsIgnoreCase(status);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resolveCurrentUser() {
        if (currentUser == null) {
            currentUser = SimpleClient.getAccount();
        }
    }

}
