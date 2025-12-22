package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.GetAllComplaints;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.NextComplaintIdMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Calendar;

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
    private ListView<Complaint> complaintList;

    @FXML // fx:id="complaintText"
    private TextArea complaintText; // Value injected by FXMLLoader

    @FXML // fx:id="loadButton"
    private Button loadButton; // Value injected by FXMLLoader

    @FXML // fx:id="orderID"
    private TextField orderID; // Value injected by FXMLLoader

    @FXML
    private TextField createdAt;

    @FXML
    private TextField respondedAt;

    @FXML
    private TextField slaStatus;

    @FXML
    private TextField compensationDecision;
    @FXML
    private Button submitComplaint; // Value injected by FXMLLoader

    @FXML // fx:id="refundMoney"
    private TextField refundMoney; // Value injected by FXMLLoader

    @FXML // fx:id="replyWorker"
    private TextField replyWorker; // Value injected by FXMLLoader

    @FXML // fx:id="wait"
    private Label wait; // Value injected by FXMLLoader

    private Integer nextComplaintId;



    @FXML
    void openCatalog(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Catalog.fxml"));
        Parent roott = loader.load();
        CatalogController cc = loader.getController();
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

        // 1) تحقق من تسجيل الدخول
        if (currentUser == null) {
            showAlert("You must be logged in to view complaints.");
            return;
        }

        // 2) تأكد أن القائمة موجودة
        if (allComplaints == null) {
            allComplaints = new ArrayList<>();
        }

        // 3) نظّف القائمة قبل إعادة التحميل
        complaintList.getItems().clear();

        // 4) فلترة الشكاوى الخاصة بالمستخدم الحالي + تعبئة القائمة
        for (Complaint c : allComplaints) {
            if (c != null && c.getCustomerID() == currentUser.getAccountID()) {
                complaintList.getItems().add(c);
            }
        }

        // 5) إذا لا يوجد شكاوى
        if (complaintList.getItems().isEmpty()) {
            showAlert("No complaints found for your account.");
            clearComplaintDetails();
            return;
        }

        // 6) طريقة العرض داخل الـListView (بدل ما نخزن String)
        configureComplaintListCellFactory();

        // 7) اعرض أول شكوى تلقائيًا (اختياري)
        complaintList.getSelectionModel().selectFirst();
        Complaint selected = complaintList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            fillComplaintDetails(selected);
        }

        // 8) عند تغيير الاختيار اعرض التفاصيل
        complaintList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                fillComplaintDetails(newV);
            }
        });
    }

    /* ============================= */
    /* Helpers: تفاصيل الشكوى بالواجهة */
    /* ============================= */

    private void fillComplaintDetails(Complaint selectedComplaint) {

        complaintID.setText(String.valueOf(selectedComplaint.getComplaintID()));
        orderID.setText(String.valueOf(selectedComplaint.getOrderID()));

        // Accepted / Answer
        if (selectedComplaint.isAccepted()) {
            answerBool.setText("Yes");
            refundMoney.setText(String.valueOf(selectedComplaint.getReturnedmoneyvalue()));
        } else {
            answerBool.setText("No");
            refundMoney.setText("0");
        }

        replyWorker.setText(String.valueOf(selectedComplaint.getAnswerworkerID()));
        complaintText.setText(selectedComplaint.getComplaintText() == null ? "" : selectedComplaint.getComplaintText());

        // إذا عندك حقول إضافية مثل SLA / respondedAt / compensationDecision:
        // slaStatus.setText(selectedComplaint.getSlaStatus());
        // compensationDecision.setText(selectedComplaint.getCompensationDecision());
        // createdAt.setText(formatTimestamp(selectedComplaint.getCreatedAt()));
        // respondedAt.setText(formatTimestamp(selectedComplaint.getRespondedAt()));
    }

    private void clearComplaintDetails() {
        complaintID.setText("");
        orderID.setText("");
        answerBool.setText("");
        refundMoney.setText("");
        replyWorker.setText("");
        complaintText.setText("");

        // لو عندك حقول إضافية:
        // slaStatus.setText("");
        // compensationDecision.setText("");
        // createdAt.setText("");
        // respondedAt.setText("");
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
        assert submitComplaint != null : "fx:id=\"submitComplaint\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert refundMoney != null : "fx:id=\"refundMoney\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert replyWorker != null : "fx:id=\"replyWorker\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert createdAt != null : "fx:id=\"createdAt\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert respondedAt != null : "fx:id=\"respondedAt\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert slaStatus != null : "fx:id=\"slaStatus\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        assert compensationDecision != null : "fx:id=\"compensationDecision\" was not injected: check your FXML file 'mycomplaints.fxml'.";
        loadButton.setDisable(true);
        backToCatalog.setDisable(true);
        wait.setVisible(true);
        configureComplaintListCellFactory();
        complaintList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showComplaintDetails(newValue);
            }
        });


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
        complaintList.getItems().clear();
        if (allComplaints == null || currentUser == null) {
            return;
        }

        allComplaints.stream()
                .filter(c -> c.getCustomerID() == currentUser.getAccountID())
                .sorted((a, b) -> Integer.compare(a.getComplaintID(), b.getComplaintID()))
                .forEach(c -> complaintList.getItems().add(c));
    }

    private String formatTimestamp(Date date) {
        if (date == null) {
            return "-";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
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
        replyWorker.setText(Integer.toString(selectedComplaint.getAnswerworkerID()));
        complaintText.setText(selectedComplaint.getComplaintText());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void configureComplaintListCellFactory() {
        complaintList.setCellFactory(lv -> new javafx.scene.control.ListCell<Complaint>() {
            @Override
            protected void updateItem(Complaint item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("#" + item.getComplaintID() + " - " +
                            item.getDay() + "/" + item.getMonth() + "/" + item.getYear());
                }
            }
        });
    }

    private int parseOrderId(String text) {
        if (text == null || text.trim().isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
