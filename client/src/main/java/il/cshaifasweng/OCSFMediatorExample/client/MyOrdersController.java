package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDateTime;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.persistence.Column;

public class MyOrdersController {

    private boolean cancelInProgress = false;
    private LocalDateTime now;
    private double refundAmount;
    private double refundPercent;
    private String refundPercentDisplay;
    private boolean returned;


    Account currentUser;
    @FXML // fx:id="RecepAddress"
    private TextField RecepAddress; // Value injected by FXMLLoader

    @FXML // fx:id="RecepName"
    private TextField RecepName; // Value injected by FXMLLoader

    @FXML // fx:id="RecepNumber"
    private TextField RecepNumber; // Value injected by FXMLLoader

    @FXML // fx:id="accountID"
    private TextField accountID; // Value injected by FXMLLoader


    @FXML // fx:id="creditCVV"
    private TextField creditCVV; // Value injected by FXMLLoader

    @FXML // fx:id="creditExpire"
    private TextField creditExpire; // Value injected by FXMLLoader

    @FXML // fx:id="creditNumber"
    private TextField creditNumber; // Value injected by FXMLLoader

    @FXML // fx:id="dateOrder"
    private TextField dateOrder; // Value injected by FXMLLoader

    @FXML // fx:id="datePrepare"
    private TextField datePrepare; // Value injected by FXMLLoader

    @FXML // fx:id="deliverService"
    private TextField deliverService; // Value injected by FXMLLoader

    @FXML // fx:id="deliverStatus"
    private TextField deliverStatus; // Value injected by FXMLLoader

    @FXML // fx:id="gift"
    private TextField gift; // Value injected by FXMLLoader

    @FXML // fx:id="greetingText"
    private TextField greetingText; // Value injected by FXMLLoader

    @FXML // fx:id="submitComplaint"
    private Button submitComplaint; // Value injected by FXMLLoader

    @FXML // fx:id="orderID"
    private TextField orderID; // Value injected by FXMLLoader

    @FXML // fx:id="orderList"
    private ListView<String> orderList; // Value injected by FXMLLoader

    @FXML // fx:id="refresh"
    private Button refresh; // Value injected by FXMLLoader

    @FXML // fx:id="shopID"
    private TextField shopID; // Value injected by FXMLLoader

    @FXML // fx:id="text1"
    private Text text1; // Value injected by FXMLLoader

    @FXML // fx:id="text10"
    private Text text10; // Value injected by FXMLLoader

    @FXML // fx:id="text11"
    private Text text11; // Value injected by FXMLLoader

    @FXML // fx:id="text12"
    private Text text12; // Value injected by FXMLLoader

    @FXML // fx:id="text13"
    private Text text13; // Value injected by FXMLLoader

    @FXML // fx:id="text14"
    private Text text14; // Value injected by FXMLLoader

    @FXML // fx:id="text15"
    private Text text15; // Value injected by FXMLLoader

    @FXML // fx:id="text16"
    private Text text16; // Value injected by FXMLLoader

    @FXML // fx:id="text2"
    private Text text2; // Value injected by FXMLLoader

    @FXML // fx:id="text3"
    private Text text3; // Value injected by FXMLLoader

    @FXML // fx:id="text4"
    private Text text4; // Value injected by FXMLLoader

    @FXML // fx:id="text5"
    private Text text5; // Value injected by FXMLLoader

    @FXML // fx:id="text6"
    private Text text6; // Value injected by FXMLLoader

    @FXML // fx:id="text7"
    private Text text7; // Value injected by FXMLLoader

    @FXML // fx:id="text8"
    private Text text8; // Value injected by FXMLLoader

    @FXML // fx:id="text9"
    private Text text9; // Value injected by FXMLLoader

    @FXML // fx:id="text99"
    private Text text99; // Value injected by FXMLLoader

    @FXML // fx:id="totalPrice"
    private TextField totalPrice; // Value injected by FXMLLoader

    @FXML // fx:id="viewOrder"
    private Button viewOrder; // Value injected by FXMLLoader


    @FXML // fx:id="orderProducts"
    private ListView<String> orderProducts; // Value injected by FXMLLoader

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML // fx:id="refundDecisionLabel"
    private Label refundDecisionLabel; // Value injected by FXMLLoader


    @FXML // fx:id="wait"
    private Label wait; // Value injected by FXMLLoader


    @FXML
    void refreshFunction(ActionEvent event) {
        String Temp = "";
        for(int i = 0 ; i < allOrders.size() ; i++)
        {
            if(allOrders.get(i).getAccountID() == currentUser.getAccountID())
            {
                Temp = Temp + "#" + allOrders.get(i).getOrderID() + " - " + allOrders.get(i).getDate();
            }
        }
    }


    @FXML
    void cancelOrder(ActionEvent event)
    {
        if (SelectedOrder == null || cancelInProgress) {
            return;
        }
        now = LocalDateTime.now();
        double refundFactor = SelectedOrder.calculateRefund(
                now.getDayOfMonth(), now.getMonthValue(), now.getYear(), now.getHour(), now.getMinute());
        refundAmount = SelectedOrder.getTotalPrice() * refundFactor;
        refundPercent = refundFactor * 100.0;
        refundPercentDisplay = String.format("%.0f%%", refundPercent);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Order");
        alert.setHeaderText("Are you sure you want to cancel this order?");
        alert.setContentText(String.format("Estimated refund: $%.2f (%s)\nThis action cannot be undone.",
                refundAmount, refundPercentDisplay));
        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        CancelOrderRequest request = new CancelOrderRequest(SelectedOrder.getOrderID());
        try {
            cancelInProgress = true;
            cancelButton.setDisable(true);
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            e.printStackTrace();
            cancelInProgress = false;
            cancelButton.setDisable(false);
        }
    }

    int complaint_num = 0;


    @FXML
    void goToMyComplaints(ActionEvent event) {
        if (SelectedOrder == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Please select an order before submitting a complaint.");
            alert.showAndWait();
            return;
        }
        NavigationService.getInstance().navigate("mycomplaints");

        PassAccountEventComplaints recievedAcc = new PassAccountEventComplaints(currentUser, SelectedOrder.getOrderID());
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(recievedAcc);
                        System.out.println("the server sent me the account , NICE 4 !!");
                    }
                },4000
        );

        GetAllComplaints allComplaints = new GetAllComplaints();
        System.out.println("send request for complaints !!");
        try {
            System.out.println("before sending the getAllComplaints " );
            SimpleClient.getClient().sendToServer(allComplaints);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Order retrievedOrder = new Order();  // This is the order with theID
    Order SelectedOrder;
    int viewOrderMode = 0;
    @FXML
    void openOrder(ActionEvent event)
    {
        if (viewOrderMode == 0)
        {
            for(int i = 0 ; i < allOrders.size(); i ++)
            {
                if (allOrders.get(i).getAccountID() == currentUser.getAccountID())
                {
                    System.out.println("We are In !!!");
                    String orderString = "";
                    orderString = allOrders.get(i).getOrderID() + " - " + allOrders.get(i).getDate();
                    System.out.println("Adding String - " + orderString);
                    orderList.getItems().add(orderString);
                }
            }
            viewOrder.setText("Load Selected Order");
            viewOrderMode = 1;


            RecepAddress.setVisible(true);
            RecepName.setVisible(true);  // Value injected by FXMLLoader
            RecepNumber.setVisible(true); // Value injected by FXMLLoader
            accountID.setVisible(true);  // Value injected by FXMLLoader
            creditCVV.setVisible(true);// Value injected by FXMLLoade
            creditExpire.setVisible(true);  // Value injected by FXMLLoader
            creditNumber.setVisible(true);  // Value injected by FXMLLoader
            dateOrder.setVisible(true);  // Value injected by FXMLLoader
            datePrepare.setVisible(true);  // Value injected by FXMLLoader
            deliverService.setVisible(true); // Value injected by FXMLLoader

            deliverStatus.setVisible(true);  // Value injected by FXMLLoader

            gift.setVisible(true);  // Value injected by FXMLLoader

            greetingText.setVisible(true);  // Value injected by FXMLLoader

            orderID.setVisible(true);  // Value injected by FXMLLoader

            orderList.setVisible(true); // Value injected by FXMLLoader

            shopID.setVisible(true);  // Value injected by FXMLLoader

            totalPrice.setVisible(true);  // Value injected by FXMLLoader

            viewOrder.setVisible(true);  // Value injected by FXMLLoader

            submitComplaint.setVisible(true);  // Value injected by FXMLLoader

            orderProducts.setVisible(true);

        }
        else
        {
            cancelButton.setVisible(true);

            int selected = orderList.getSelectionModel().getSelectedItem().charAt(0) - 48;
            System.out.println("Selected is " + selected);
            //int theID = Integer.parseInt(enterID.getText());
            for (int i = 0; i < allOrders.size(); i++) {
                if (allOrders.get(i).getOrderID() == selected) {
                    retrievedOrder = allOrders.get(i);
                    break;
                }
            }
            SelectedOrder = retrievedOrder;
            String currentProduct = "";
            String MyProducts = retrievedOrder.getProducts();
            orderProducts.getItems().clear();
            for(int i = 0 ; i < MyProducts.length() ; i++)
            {
                if(MyProducts.charAt(i) != 37)
                {
                    currentProduct = currentProduct + Character.toString(MyProducts.charAt(i));
                }
                else if(currentProduct != "")
                {
                    orderProducts.getItems().add(currentProduct);
                    currentProduct = "";
                }
                else
                    currentProduct = "";
            }
            submitComplaint.setVisible(true);
            orderID.setText(String.valueOf(retrievedOrder.getOrderID()));
            accountID.setText(String.valueOf(currentUser.getAccountID()));
            creditNumber.setText(String.valueOf(retrievedOrder.getCreditCardNumber()));
            String creditXpire = "" + retrievedOrder.getCreditCardExpMonth() + "/" + retrievedOrder.getCreditCardExpYear();
            creditExpire.setText(creditXpire);
            creditCVV.setText(String.valueOf(retrievedOrder.getCreditCardCVV()));
            totalPrice.setText(String.valueOf(retrievedOrder.getTotalPrice()));
            shopID.setText(String.valueOf(retrievedOrder.getShopID()));
            if (retrievedOrder.isGift() == true)
                gift.setText("true");
            else
                gift.setText("false");
            String orderDate = "" + retrievedOrder.getOrderDay() + "/" + retrievedOrder.getOrderMonth() + "/" + retrievedOrder.getOrderYear();
            dateOrder.setText(orderDate);
            String prepareDate = "" + retrievedOrder.getPrepareDay() + "/" + retrievedOrder.getPrepareMonth() + "/" + retrievedOrder.getPrepareYear();
            datePrepare.setText(prepareDate);
            if (retrievedOrder.isPickUp() == true)
                deliverService.setText("Pick Up");
            else
                deliverService.setText("Delivery");
            if (retrievedOrder.isDelivered() == true)
                deliverService.setText("Delivered/Picked Up");
            else
                deliverService.setText("Not Delivered/Picked Up");
            RecepName.setText(retrievedOrder.getRecepName());
            RecepAddress.setText(retrievedOrder.getRecepAddress());
            RecepNumber.setText(String.valueOf(retrievedOrder.getRecepPhone()));
            if (retrievedOrder.getGreeting() != "")
                greetingText.setText(retrievedOrder.getGreeting());
            else
                greetingText.setText("No Greeting");
            currentOrderShopID = retrievedOrder.getShopID();
            if (retrievedOrder.isCancelled()) {
                refundDecisionLabel.setText(String.format("Order cancelled. Refund: $%.2f (%s)",
                        retrievedOrder.getRefundAmount(), retrievedOrder.getRefundStatus()));
                refundDecisionLabel.setVisible(true);
                cancelButton.setVisible(false);
            } else {
                refundDecisionLabel.setVisible(false);
            }
        }
    }
    int currentOrderShopID;
    List<Order> allOrders = new ArrayList<Order>();



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        System.out.println("before sending getAllOrders message !");
        getAllOrdersMessage getOrdersMsg = new getAllOrdersMessage();
        SimpleClient.getClient().sendToServer(getOrdersMsg);
        System.out.println("after sending getAllOrders message !");

        assert RecepAddress != null : "fx:id=\"RecepAddress\" was not injected: check your FXML file 'myorders.fxml'.";
        assert RecepName != null : "fx:id=\"RecepName\" was not injected: check your FXML file 'myorders.fxml'.";
        assert RecepNumber != null : "fx:id=\"RecepNumber\" was not injected: check your FXML file 'myorders.fxml'.";
        assert accountID != null : "fx:id=\"accountID\" was not injected: check your FXML file 'myorders.fxml'.";
        assert creditCVV != null : "fx:id=\"creditCVV\" was not injected: check your FXML file 'myorders.fxml'.";
        assert creditExpire != null : "fx:id=\"creditExpire\" was not injected: check your FXML file 'myorders.fxml'.";
        assert creditNumber != null : "fx:id=\"creditNumber\" was not injected: check your FXML file 'myorders.fxml'.";
        assert dateOrder != null : "fx:id=\"dateOrder\" was not injected: check your FXML file 'myorders.fxml'.";
        assert datePrepare != null : "fx:id=\"datePrepare\" was not injected: check your FXML file 'myorders.fxml'.";
        assert deliverService != null : "fx:id=\"deliverService\" was not injected: check your FXML file 'myorders.fxml'.";
        assert deliverStatus != null : "fx:id=\"deliverStatus\" was not injected: check your FXML file 'myorders.fxml'.";
        assert gift != null : "fx:id=\"gift\" was not injected: check your FXML file 'myorders.fxml'.";
        assert greetingText != null : "fx:id=\"greetingText\" was not injected: check your FXML file 'myorders.fxml'.";
        assert orderID != null : "fx:id=\"orderID\" was not injected: check your FXML file 'myorders.fxml'.";
        assert orderList != null : "fx:id=\"orderList\" was not injected: check your FXML file 'myorders.fxml'.";
        assert refresh != null : "fx:id=\"refresh\" was not injected: check your FXML file 'myorders.fxml'.";
        assert submitComplaint != null : "fx:id=\"submitComplaint\" was not injected: check your FXML file 'myorders.fxml'.";
        assert shopID != null : "fx:id=\"shopID\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text1 != null : "fx:id=\"text1\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text10 != null : "fx:id=\"text10\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text11 != null : "fx:id=\"text11\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text12 != null : "fx:id=\"text12\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text13 != null : "fx:id=\"text13\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text14 != null : "fx:id=\"text14\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text15 != null : "fx:id=\"text15\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text16 != null : "fx:id=\"text16\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text2 != null : "fx:id=\"text2\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text3 != null : "fx:id=\"text3\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text4 != null : "fx:id=\"text4\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text5 != null : "fx:id=\"text5\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text6 != null : "fx:id=\"text6\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text7 != null : "fx:id=\"text7\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text8 != null : "fx:id=\"text8\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text9 != null : "fx:id=\"text9\" was not injected: check your FXML file 'myorders.fxml'.";
        assert text99 != null : "fx:id=\"text99\" was not injected: check your FXML file 'myorders.fxml'.";
        assert totalPrice != null : "fx:id=\"totalPrice\" was not injected: check your FXML file 'myorders.fxml'.";
        assert viewOrder != null : "fx:id=\"viewOrder\" was not injected: check your FXML file 'myorders.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'myorders.fxml'.";
        assert refundDecisionLabel != null : "fx:id=\"refundDecisionLabel\" was not injected: check your FXML file 'myorders.fxml'.";


        wait.setVisible(true);
        cancelButton.setVisible(false);

        viewOrder.setText("Load Orders");
        String orderDetails = "";

        RecepAddress.setVisible(false);
        RecepName.setVisible(false);  // Value injected by FXMLLoader
        RecepNumber.setVisible(false); // Value injected by FXMLLoader
        accountID.setVisible(false);  // Value injected by FXMLLoader
        creditCVV.setVisible(false);// Value injected by FXMLLoader
        creditExpire.setVisible(false);  // Value injected by FXMLLoader
        creditNumber.setVisible(false);  // Value injected by FXMLLoader
        dateOrder.setVisible(false);  // Value injected by FXMLLoader
        datePrepare.setVisible(false);  // Value injected by FXMLLoader
        deliverService.setVisible(false); // Value injected by FXMLLoader
        deliverStatus.setVisible(false);  // Value injected by FXMLLoader
        gift.setVisible(false);  // Value injected by FXMLLoader
        greetingText.setVisible(false);  // Value injected by FXMLLoader
        orderID.setVisible(false);  // Value injected by FXMLLoader
        shopID.setVisible(false);  // Value injected by FXMLLoader
        totalPrice.setVisible(false);  // Value injected by FXMLLoader
        submitComplaint.setVisible(false);  // Value injected by FXMLLoader
        orderProducts.setVisible(false);
        refundDecisionLabel.setVisible(false);

        viewOrder.setDisable(true);


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        viewOrder.setDisable(false);
                        wait.setVisible(false);
                    }
                },4500
        );
    }
    @Subscribe
    public void PassAccountEvent(PassAccountEventOrders passAcc){ // added today
        System.out.println("Arrived To Pass Account - order!");
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
    public void passOrders(PassOrdersFromServer passOrders){ // added 18/7
        System.out.println("arrived to subscriebr of passOrders !");
        List<Order> recievedOrders = passOrders.getRecievedOrders();
        allOrders = recievedOrders;
    }

    @Subscribe
    public void handleCancelOrderResponse(CancelOrderResponse response) {
        if (SelectedOrder == null || response.getOrderId() != SelectedOrder.getOrderID()) {
            return;
        }
        Platform.runLater(() -> {
            cancelInProgress = false;
            cancelButton.setDisable(false);
            if (response.isSuccess()) {
                now = LocalDateTime.now();
                refundAmount = response.getRefundAmount();
                refundPercent = response.getRefundPercent();
                refundPercentDisplay = String.format("%.0f%%", refundPercent);
                returned = true;
                SelectedOrder.setCancelled(true);
                SelectedOrder.setRefundAmount(response.getRefundAmount());
                SelectedOrder.setRefundStatus(response.getRefundStatus());
                cancelButton.setVisible(false);
                if (deliverStatus != null) {
                    deliverStatus.setText("Cancelled");
                }
                if (refundDecisionLabel != null) {
                    refundDecisionLabel.setText(String.format("Order cancelled. Refund: $%.2f (%s)",
                            response.getRefundAmount(), response.getRefundStatus()));
                    refundDecisionLabel.setVisible(true);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order Cancelled");
                alert.setHeaderText("Cancellation Confirmed");
                alert.setContentText(String.format("%s Refund: $%.2f (%.0f%%)",
                        response.getMessage(), response.getRefundAmount(), response.getRefundPercent()));
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Cancellation Failed");
                alert.setHeaderText("Unable to cancel order");
                alert.setContentText(response.getMessage());
                alert.showAndWait();
            }
        });
    }



}
