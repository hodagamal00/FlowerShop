package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;


import il.cshaifasweng.OCSFMediatorExample.entities.*;
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

public class MyOrdersController {



    Account currentUser;
    @FXML // fx:id="RecepAddress"
    private TextField RecepAddress; // Value injected by FXMLLoader

    @FXML // fx:id="RecepName"
    private TextField RecepName; // Value injected by FXMLLoader

    @FXML // fx:id="RecepNumber"
    private TextField RecepNumber; // Value injected by FXMLLoader

    @FXML // fx:id="accountID"
    private TextField accountID; // Value injected by FXMLLoader

    @FXML // fx:id="backToCatalog"
    private Button backToCatalog; // Value injected by FXMLLoader

    @FXML // fx:id="complaintText"
    private TextField complaintText; // Value injected by FXMLLoader

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

    @FXML // fx:id="openComplaint"
    private Button openComplaint; // Value injected by FXMLLoader

    @FXML // fx:id="orderID"
    private TextField orderID; // Value injected by FXMLLoader

    @FXML // fx:id="orderList"
    private ListView<String> orderList; // Value injected by FXMLLoader

    @FXML // fx:id="sendComplaint"
    private Button sendComplaint; // Value injected by FXMLLoader

    @FXML // fx:id="shopID"
    private TextField shopID; // Value injected by FXMLLoader

    @FXML // fx:id="totalPrice"
    private TextField totalPrice; // Value injected by FXMLLoader

    @FXML // fx:id="viewOrder"
    private Button viewOrder; // Value injected by FXMLLoader


    @FXML // fx:id="orderProducts"
    private ListView<String> orderProducts; // Value injected by FXMLLoader

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader


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
        LocalDateTime now = LocalDateTime.now();
        double refundPercent = SelectedOrder.calculateRefund(
                now.getDayOfMonth(),
                now.getMonthValue(),
                now.getYear(),
                now.getHour(),
                now.getMinute()
        );
        boolean returned = refundPercent > 0;
        int refundPercentDisplay = (int) Math.round(refundPercent * 100);
        double refundAmount = refundPercent * SelectedOrder.getTotalPrice();

        Complaint cancelComplaint = new Complaint(0,currentUser.getAccountID(),SelectedOrder.getOrderID(),true,true,"Cancel Order",SelectedOrder.getShopID(),0,returned,refundAmount,now.getDayOfMonth(),now.getMonthValue(),now.getYear(),"Automated Reply");
        cancelComplaint.setCreatedAt(new Date());
        cancelComplaint.setRespondedAt(new Date());
        cancelComplaint.setSlaStatus("RESOLVED_ON_TIME");
        cancelComplaint.setCompensationDecision(refundPercent > 0 ? "Automatic refund " + refundPercentDisplay + "%" : "No compensation" );
        UpdateMessage new_msg=new UpdateMessage("complaint","add");
        new_msg.setComplaint(cancelComplaint);
        try {
            System.out.println("before sending updateMessage to server ");
            SimpleClient.getClient().sendToServer(new_msg); // sends the updated product to the server class
            System.out.println("afater sending updateMessage to server ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    int complaint_num = 0;
    @FXML
    void GoToCatalog(ActionEvent event) throws IOException {
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
                },4000
        );

    }


    @FXML
    void openOrderComplaint(ActionEvent event)
    {
        complaintText.setVisible(true);
        sendComplaint.setVisible(true);
    }

    @FXML
    void sendOrderComplaint(ActionEvent event)
    {
        Calendar calle = Calendar.getInstance();
        int currentYear = calle.get(Calendar.YEAR);
        int currentMonth = calle.get(Calendar.MONTH);
        currentMonth++;
        int currentHour = calle.get(Calendar.HOUR_OF_DAY);
        int currentMintue = calle.get(Calendar.MINUTE);
        int currentDay = calle.get(Calendar.DAY_OF_MONTH);

        Complaint newComplaint = new Complaint();
        newComplaint.setComplaintID(0); // 0 - Will be changed later
        newComplaint.setCustomerID(currentUser.getAccountID());
        newComplaint.setOrderID(SelectedOrder.getOrderID());
        newComplaint.setAccepted(false);
        newComplaint.setIn24Hours(false);
        newComplaint.setComplaintText(complaintText.getText());
        newComplaint.setShopID(SelectedOrder.getShopID());
        newComplaint.setAnswerworkerID(0);
        newComplaint.setReturnedMoney(false);
        newComplaint.setReturnedmoneyvalue(0);
        newComplaint.setDay(currentDay);
        newComplaint.setMonth(currentMonth);
        newComplaint.setYear(currentYear);
        newComplaint.setReplyText("");
        newComplaint.setCreatedAt(new Date());
        newComplaint.setSlaStatus("IN_PROGRESS");
        newComplaint.setCompensationDecision("Pending review");
        sendComplaint.setVisible(false);
        complaintText.setVisible(false);
        UpdateMessage new_msg=new UpdateMessage("complaint","add");
        new_msg.setComplaint(newComplaint);
        try {
            System.out.println("before sending updateMessage to server ");
            SimpleClient.getClient().sendToServer(new_msg); // sends the updated product to the server class
            System.out.println("afater sending updateMessage to server ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
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

            openComplaint.setVisible(true);  // Value injected by FXMLLoader

            orderProducts.setVisible(true);

        }
        else
        {
            cancelButton.setVisible(true);

            int selected = parseSelectedOrderId();
            System.out.println("Selected is " + selected);
            if (selected == -1) {
                return;
            }
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
            openComplaint.setVisible(true);
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
                deliverStatus.setText("Delivered/Picked Up");
            else
                deliverStatus.setText("Not Delivered/Picked Up");
            RecepName.setText(retrievedOrder.getRecepName());
            RecepAddress.setText(retrievedOrder.getRecepAddress());
            RecepNumber.setText(String.valueOf(retrievedOrder.getRecepPhone()));
            if (retrievedOrder.getGreeting() != null && !retrievedOrder.getGreeting().isEmpty())
                greetingText.setText(retrievedOrder.getGreeting());
            else
                greetingText.setText("No Greeting");
            currentOrderShopID = retrievedOrder.getShopID();
        }
    }
    int currentOrderShopID;
    List<Order> allOrders = new ArrayList<Order>();



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        System.out.println("before sending getAllOrders message !");
        boolean requestedOrders = false;
        try {
            getAllOrdersMessage getOrdersMsg = new getAllOrdersMessage();
            SimpleClient.getClient().sendToServer(getOrdersMsg);
            requestedOrders = true;
            System.out.println("after sending getAllOrders message !");
        } catch (Exception ex) {
            System.out.println("Failed to request orders: " + ex.getMessage());
        }

        assert RecepAddress != null : "fx:id=\"RecepAddress\" was not injected: check your FXML file 'myorders.fxml'.";
        assert RecepName != null : "fx:id=\"RecepName\" was not injected: check your FXML file 'myorders.fxml'.";
        assert RecepNumber != null : "fx:id=\"RecepNumber\" was not injected: check your FXML file 'myorders.fxml'.";
        assert accountID != null : "fx:id=\"accountID\" was not injected: check your FXML file 'myorders.fxml'.";
        assert backToCatalog != null : "fx:id=\"backToCatalog\" was not injected: check your FXML file 'myorders.fxml'.";
        assert complaintText != null : "fx:id=\"complaintText\" was not injected: check your FXML file 'myorders.fxml'.";
        assert creditCVV != null : "fx:id=\"creditCVV\" was not injected: check your FXML file 'myorders.fxml'.";
        assert creditExpire != null : "fx:id=\"creditExpire\" was not injected: check your FXML file 'myorders.fxml'.";
        assert creditNumber != null : "fx:id=\"creditNumber\" was not injected: check your FXML file 'myorders.fxml'.";
        assert dateOrder != null : "fx:id=\"dateOrder\" was not injected: check your FXML file 'myorders.fxml'.";
        assert datePrepare != null : "fx:id=\"datePrepare\" was not injected: check your FXML file 'myorders.fxml'.";
        assert deliverService != null : "fx:id=\"deliverService\" was not injected: check your FXML file 'myorders.fxml'.";
        assert deliverStatus != null : "fx:id=\"deliverStatus\" was not injected: check your FXML file 'myorders.fxml'.";
        assert gift != null : "fx:id=\"gift\" was not injected: check your FXML file 'myorders.fxml'.";
        assert greetingText != null : "fx:id=\"greetingText\" was not injected: check your FXML file 'myorders.fxml'.";
        assert openComplaint != null : "fx:id=\"openComplaint\" was not injected: check your FXML file 'myorders.fxml'.";
        assert orderID != null : "fx:id=\"orderID\" was not injected: check your FXML file 'myorders.fxml'.";
        assert orderList != null : "fx:id=\"orderList\" was not injected: check your FXML file 'myorders.fxml'.";
        assert sendComplaint != null : "fx:id=\"sendComplaint\" was not injected: check your FXML file 'myorders.fxml'.";
        assert shopID != null : "fx:id=\"shopID\" was not injected: check your FXML file 'myorders.fxml'.";
        assert totalPrice != null : "fx:id=\"totalPrice\" was not injected: check your FXML file 'myorders.fxml'.";
        assert viewOrder != null : "fx:id=\"viewOrder\" was not injected: check your FXML file 'myorders.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'myorders.fxml'.";


        wait.setVisible(true);
        complaintText.setVisible(false);
        sendComplaint.setVisible(false);
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
        complaintText.setVisible(false);  // Value injected by FXMLLoader
        openComplaint.setVisible(false);  // Value injected by FXMLLoader
        orderProducts.setVisible(false);

        viewOrder.setDisable(true);
        backToCatalog.setDisable(true);

        if (!requestedOrders) {
            wait.setText("Offline / Not connected to server.");
            wait.setVisible(true);
            viewOrder.setDisable(true);
            backToCatalog.setDisable(false);
            return;
        }


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        viewOrder.setDisable(false);
                        backToCatalog.setDisable(false);
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

    private int parseSelectedOrderId() {
        String selectedItem = orderList.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.isBlank()) {
            return -1;
        }
        String[] parts = selectedItem.split(" - ", 2);
        if (parts.length == 0) {
            return -1;
        }
        try {
            return Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }



}
