/**
 * Sample Skeleton for 'replycomplaint.fxml' Controller Class
 */


package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.*;
import javafx.scene.text.Text;
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
    private TextArea complaintText; // Value injected by FXMLLoader

    @FXML // fx:id="loadButton"
    private Button loadButton; // Value injected by FXMLLoader

    @FXML // fx:id="orderID"
    private TextField orderID; // Value injected by FXMLLoader

    @FXML
    private ComboBox<String> branchFilterCombo;

    @FXML
    private ComboBox<String> statusFilterCombo;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

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
        if (selectedComplaint == null || selectedComplaint.getComplaintID() == 0) {
            showAlert("Please select a complaint to respond to.");
            return;
        }
        if (currentUser == null) {
            showAlert("You must be logged in to respond to complaints.");
            return;
        }
        if (replyText.getText() == null || replyText.getText().trim().isEmpty()) {
            showAlert("Please enter a response message.");
            return;
        }
        int compensationAmount = 0;
        boolean willReturnMoney=  false;
        selectedComplaint.setAccepted(true);
        selectedComplaint.setAnswerworkerID(currentUser.getAccountID());
        selectedComplaint.setReplyText(replyText.getText().trim());
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
        requestComplaints();
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

        refundPercent.getItems().add("25₪");
        refundPercent.getItems().add("50₪");
        refundPercent.getItems().add("75₪");
        refundPercent.getItems().add("100₪");

        refundPercent.setVisible(false);
        other.setVisible(false);
        wait.setVisible(false);
        sendButton.setVisible(false);

        setupFilters();
        complaintList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Complaint selected = findComplaintByListEntry(newValue);
                if (selected != null) {
                    selectedComplaint = selected;
                    showComplaintDetails(selected);
                }
            }
        });
        requestComplaints();
    }

    @Subscribe
    public void retrieveAllComplaints(PassAllComplaintsEvent complaints){
        System.out.println("we're in retrieveAllComplaints");
        List<Complaint> allComplaints = complaints.getComplaintsToPass();
        System.out.println("SIZE = " + allComplaints.size());
        retrievedComplaints = allComplaints != null ? allComplaints : new ArrayList<>();
        applyFilters();
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
        setupBranchFilterVisibility();
    }
    private String formatTimestamp(Date date) {
        if (date == null) {
            return "-";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }

    private void setupFilters() {
        if (statusFilterCombo != null) {
            statusFilterCombo.getItems().setAll("All", "Pending", "Resolved");
            statusFilterCombo.setValue("All");
            statusFilterCombo.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        }
        if (fromDatePicker != null) {
            fromDatePicker.setValue(LocalDate.now().minusDays(30));
            fromDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        }
        if (toDatePicker != null) {
            toDatePicker.setValue(LocalDate.now());
            toDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        }
        if (branchFilterCombo != null) {
            branchFilterCombo.getItems().setAll(
                    "All Branches",
                    "Branch 1 - Tiberias, Big Danilof",
                    "Branch 2 - Haifa, Merkaz Zeiv",
                    "Branch 3 - Tel Aviv, Ramat Aviv",
                    "Branch 4 - Eilat, Ice mall",
                    "Branch 5 - Be'er Sheva, Big Beer Sheva"
            );
            branchFilterCombo.setValue("All Branches");
            branchFilterCombo.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        }
        setupBranchFilterVisibility();
    }

    private void setupBranchFilterVisibility() {
        if (branchFilterCombo == null) {
            return;
        }
        boolean isChainManager = currentUser != null && currentUser.getPrivilegeLevel() >= 4;
        branchFilterCombo.setVisible(isChainManager);
        branchFilterCombo.setManaged(isChainManager);
        if (!isChainManager) {
            branchFilterCombo.setValue("All Branches");
        }
    }

    private void requestComplaints() {
        try {
            GetAllComplaints request = new GetAllComplaints();
            int privilege = SimpleClient.getPrivilegeLevel();
            if (privilege >= 4) {
                request.setScope("NETWORK");
            } else {
                request.setScope("BRANCH");
            }
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyFilters() {
        complaintList.getItems().clear();
        if (retrievedComplaints == null) {
            return;
        }
        LocalDate from = fromDatePicker != null ? fromDatePicker.getValue() : null;
        LocalDate to = toDatePicker != null ? toDatePicker.getValue() : null;
        String status = statusFilterCombo != null ? statusFilterCombo.getValue() : "All";
        Integer branchFilter = resolveBranchFilter();

        for (Complaint complaint : retrievedComplaints) {
            if (branchFilter != null && complaint.getShopID() != branchFilter) {
                continue;
            }
            if (!matchesStatus(complaint, status)) {
                continue;
            }
            LocalDate complaintDateValue = resolveComplaintDate(complaint);
            if (from != null && complaintDateValue != null && complaintDateValue.isBefore(from)) {
                continue;
            }
            if (to != null && complaintDateValue != null && complaintDateValue.isAfter(to)) {
                continue;
            }
            complaintList.getItems().add(formatListEntry(complaint));
        }
    }

    private Integer resolveBranchFilter() {
        if (branchFilterCombo == null || branchFilterCombo.getValue() == null) {
            return null;
        }
        String selection = branchFilterCombo.getValue();
        if ("All Branches".equalsIgnoreCase(selection)) {
            return null;
        }
        if (selection.startsWith("Branch 1")) {
            return 1;
        }
        if (selection.startsWith("Branch 2")) {
            return 2;
        }
        if (selection.startsWith("Branch 3")) {
            return 3;
        }
        if (selection.startsWith("Branch 4")) {
            return 4;
        }
        if (selection.startsWith("Branch 5")) {
            return 5;
        }
        return null;
    }

    private boolean matchesStatus(Complaint complaint, String status) {
        if (status == null || "All".equalsIgnoreCase(status)) {
            return true;
        }
        boolean resolved = complaint.isAccepted();
        return resolved ? "Resolved".equalsIgnoreCase(status) : "Pending".equalsIgnoreCase(status);
    }

    private String formatListEntry(Complaint complaint) {
        String date = complaint.getDay() + "/" + complaint.getMonth() + "/" + complaint.getYear();
        String status = complaint.isAccepted() ? "Resolved" : "Pending";
        String entry = "#" + complaint.getComplaintID() + " - " + date + " (" + status + ")";
        if (isLateStatus(complaint.getSlaStatus())) {
            entry += " (Late)";
        }
        return entry;
    }

    private Complaint findComplaintByListEntry(String listEntry) {
        int id = parseComplaintId(listEntry);
        if (id == -1) {
            return null;
        }
        for (Complaint complaint : retrievedComplaints) {
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

    private void showComplaintDetails(Complaint selected) {
        complaintID.setText(String.valueOf(selected.getComplaintID()));
        accountID.setText(String.valueOf(selected.getCustomerID()));
        orderID.setText(String.valueOf(selected.getOrderID()));
        complaintDate.setText(selected.getDay() + "/" + selected.getMonth() + "/" + selected.getYear());
        complaintText.setText(selected.getComplaintText());
        createdAtField.setText(formatTimestamp(selected.getCreatedAt()));
        respondedAtField.setText(formatTimestamp(selected.getRespondedAt()));
        slaStatusField.setText(selected.getSlaStatus() != null ? selected.getSlaStatus() : "-");
        compensationDecisionField.setText(selected.getCompensationDecision() != null ? selected.getCompensationDecision() : "-");
        replyText.setText(selected.getReplyText() != null ? selected.getReplyText() : "");
        sendButton.setVisible(true);
    }

    private LocalDate resolveComplaintDate(Complaint complaint) {
        if (complaint.getCreatedAt() != null) {
            return complaint.getCreatedAt().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        }
        try {
            return LocalDate.of(complaint.getYear(), complaint.getMonth(), complaint.getDay());
        } catch (Exception ex) {
            return null;
        }
    }
}
