package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import il.cshaifasweng.OCSFMediatorExample.entities.getAllOrdersMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;

import static com.sun.xml.bind.v2.schemagen.Util.equal;

public class DeliveryController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="deliver"
    private Button deliver; // Value injected by FXMLLoader

    @FXML // fx:id="deliveryList"
    private ListView<String> deliveryList; // Value injected by FXMLLoader

    @FXML // fx:id="listOrders"
    private ComboBox<Integer> listOrders; // Value injected by FXMLLoader

    @FXML // fx:id="wait"
    private Label wait; // Value injected by FXMLLoader



    @FXML
    private Button back;

    @FXML
    void ApplyDelivery(ActionEvent event) {
        String aString = "";
        if (deliver.getText().equals("Load Pending Orders"))
        {
            for (int i = 0; i < Orders.size(); i++) {
                if(Orders.get(i).isDelivered() == false) {
                    aString = "#" + Orders.get(i).getOrderID() + " - Ordered On " + Orders.get(i).getOrderDay() + "/" + Orders.get(i).getOrderMonth() + "/" + Orders.get(i).getOrderYear() + " Prepare For " + Orders.get(i).getPrepareDay() + "/" + Orders.get(i).getOrderMonth() + "/" + Orders.get(i).getPrepareYear();
                    deliveryList.getItems().add(aString);
                    aString = "";
                }

            }
            deliver.setText("Deliver Selected");

        }
        else
        {
            String bb = "";
            String aa = deliveryList.getSelectionModel().getSelectedItem();
            for(int i = 1 ; aa.charAt(i) != ' '; i++)
            {
                bb = bb + Character.toString(aa.charAt(i));
            }
            System.out.println("bb = " + bb);
            int selected = Integer.parseInt(bb);
            for (int i = 0; i < Orders.size(); i++) {
                if (selected == Orders.get(i).getOrderID())
                {
                    Orders.get(i).setDelivered(true);
                    try {
                        SimpleClient.getClient().sendToServer(Orders.get(i));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            deliveryList.getItems().clear();
            deliver.setText("Load Pending Orders");
        }

    }

    @FXML
    void openCatalog(ActionEvent event) throws IOException {
        Account recAcc = currentUser;
        System.out.println("the server sent me the account , NICE 2 !!");
        PassAccountEvent recievedAcc = new PassAccountEvent(recAcc);
        System.out.println("the server sent me the account , NICE 3 !!");
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Deliver Privilage: " + recievedAcc.getRecievedAccount().getPrivialge());
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
        Stage stagee = (Stage)back.getScene().getWindow();
        // do what you have to do
        stagee.close();
    }

    Account currentUser;
    static List<Order> Orders = new ArrayList<>();
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        getAllOrdersMessage getOrdersMsg = new getAllOrdersMessage();
        SimpleClient.getClient().sendToServer(getOrdersMsg);
        assert deliver != null : "fx:id=\"deliver\" was not injected: check your FXML file 'delivery.fxml'.";
        assert deliveryList != null : "fx:id=\"deliveryList\" was not injected: check your FXML file 'delivery.fxml'.";
        assert listOrders != null : "fx:id=\"listOrders\" was not injected: check your FXML file 'delivery.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'delivery.fxml'.";

        deliver.setDisable(true);
        listOrders.setDisable(true);
        back.setDisable(true);
        wait.setVisible(true);
        		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
                        deliver.setDisable(false);
                        listOrders.setDisable(false);
                        back.setDisable(false);
                        wait.setVisible(false);
					}
				},4500
		);
        		listOrders.setVisible(false);
    }
    @Subscribe
    public void passOrders(PassOrdersFromServer passOrders){ // added 30/7
        System.out.println("arrived to subscriebr of passOrders in delivery controller !");
        List<Order> recievedOrders = passOrders.getRecievedOrders();
        Orders = recievedOrders;
        System.out.println("Orders Size : " + Orders.size());
    }


    @Subscribe
    public void PassAccountEvent(PassAccountEventDelivery passAcc){ // added 30/7
        System.out.println("Arrived To Pass Account - deliveryManager!");
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