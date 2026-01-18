package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.*;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutController {

    @FXML
    private CheckBox anotherMethodBox;

    @FXML
    private TextField creditNumberField;

    @FXML
    private Text creditNumberText;

    @FXML
    private TextField cvvField;

    @FXML
    private Text cvvText;

    @FXML
    private ComboBox<Integer> dayCheckout;

    @FXML
    private CheckBox deliveryBox;

    @FXML
    private Text deliveryDateCheckout;

    @FXML
    private ComboBox<Integer> expiryMonth;

    @FXML
    private Text expiryText;

    @FXML
    private ComboBox<Integer> expiryYear;

    @FXML
    private ComboBox<String> hourCheckout;

    @FXML
    private CheckBox greetingBoxCheckout;

    @FXML
    private TextField greetingTextCheckout;

    @FXML
    private ComboBox<Integer> monthCheckout;

    @FXML
    private Button placeOrderButton;

    @FXML
    private TextField recepAddressField;

    @FXML
    private Text recepAddressText;

    @FXML
    private TextField recepNameField;

    @FXML
    private Text recepNameText;

    @FXML
    private TextField recepPhoneField;

    @FXML
    private Text recepPhoneText;

    @FXML
    private ComboBox<Integer> yearCheckout;

    @FXML
    private Button back;

    @FXML // fx:id="noDate"
    private Text noDate; // Value injected by FXMLLoader


    @FXML
    private CheckBox deliverToHome;

    @FXML // fx:id="credit_regex"
    private Label credit_regex; // Value injected by FXMLLoader

    @FXML // fx:id="cvv_regex"
    private Label cvv_regex; // Value injected by FXMLLoader


    @FXML // fx:id="phone_regex"
    private Label phone_regex; // Value injected by FXMLLoader

    @FXML // fx:id="noShop"
    private Text noShop; // Value injected by FXMLLoader


    @FXML // fx:id="noDateTwo"
    private Text noDateTwo; // Value injected by FXMLLoader



    @FXML
    void openCatalog(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Catalog.fxml"));
        Parent roott = loader.load();
        CatalogController cc = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(roott));
        stage.setTitle("Catalog");
        stage.show();
        Stage stagee = (Stage)back.getScene().getWindow();
        // do what you have to do
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
    //String email_regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";
    String creditCard_regex = "^\\d{16}$";
    String CVV_regex = "^\\d{3}$";
    String phoneNum_regex = "^\\d{10}$";

    String ID_regex = "^\\d{9}$";

    @FXML
    void PlaceOrder(ActionEvent event) {

        phone_regex.setVisible(false);
        credit_regex.setVisible(false);
        cvv_regex.setVisible(false);
        noDate.setVisible(false);
        noShop.setVisible(false);
        noDateTwo.setVisible(false);

        boolean fail;
        fail = false;
        int shopID = 0;
        Calendar calle = Calendar.getInstance();
        int currentYear = calle.get(Calendar.YEAR);
        int currentMonth = calle.get(Calendar.MONTH);
        currentMonth++;
        int currentHour = calle.get(Calendar.HOUR_OF_DAY);
        int currentMintue = calle.get(Calendar.MINUTE);
        int currentDay = calle.get(Calendar.DAY_OF_MONTH);
        String chainShop = chooseShopID.getSelectionModel().toString();
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(creditCard_regex);
        matcher = pattern.matcher(creditNumberField.getText());
        if(anotherMethodBox.isSelected() == true) {
            if (!matcher.matches()) {
                credit_regex.setVisible(true);
                fail = true;
            }
            pattern = Pattern.compile(CVV_regex);
            matcher = pattern.matcher(cvvField.getText());
            if (!matcher.matches()) {
                cvv_regex.setVisible(true);
                fail = true;
            }
        }
        if(deliveryBox.isSelected() == true) {
            pattern = Pattern.compile(phoneNum_regex);
            matcher = pattern.matcher(recepPhoneField.getText());
            if (!matcher.matches()) {
                phone_regex.setVisible(true);
                fail = true;
            }
        }
        if(dayCheckout.getSelectionModel().getSelectedIndex() == -1 || monthCheckout.getSelectionModel().getSelectedIndex() == -1 || yearCheckout.getSelectionModel().getSelectedIndex() == -1 || hourCheckout.getSelectionModel().getSelectedIndex() == -1)
        {
            noDate.setVisible(true);
            fail = true;
        }
        if(chooseShopID.isVisible() == true) {
            if (chooseShopID.getSelectionModel().getSelectedIndex() == -1) {
                noShop.setVisible(true);
                fail = true;
            }
        }
        if(anotherMethodBox.isSelected())
        {
            if(expiryMonth.getSelectionModel().getSelectedIndex() == -1 || expiryYear.getSelectionModel().getSelectedIndex() == -1) {
                noDateTwo.setVisible(true);
                fail = true;
            }


        }
        if(fail == false)
        {
            if(currentUser.getBelongShop() == 0)
            {
                switch (chooseShopID.getValue().toString()) {
                    case "ID 0: - Chain":
                        shopID = 0;
                        break;
                    case "ID 1: Tiberias, Big Danilof":
                        shopID = 1;
                        break;
                    case "ID 2: Haifa, Merkaz Zeiv":
                        shopID = 2;
                        break;
                    case "ID 3: Tel Aviv, Ramat Aviv":
                        shopID = 3;
                        break;
                    case "ID 4: Eilat, Ice mall":
                        shopID = 4;
                        break;
                    case "ID 5: Be'er Sheva, Big Beer Sheva":
                        shopID = 5;
                        break;
                }
            }
            else
                shopID = currentUser.getBelongShop();

            boolean pickUp = true;
            boolean gift = false;
            String deliveredAddress = "";
            String recepName = "";
            long recepPhone = 0;
            double deliveryFee = 0.0;
            if (deliverToHome.isSelected()) {
                pickUp = true;
                gift = false;
                recepName = currentUser.getFullName();
                recepPhone = currentUser.getPhoneNumber();
                deliveredAddress = currentUser.getAddress();
            }
            if (deliveryBox.isSelected()) {
                recepPhone = Integer.parseInt(recepPhoneField.getText());
                recepName = recepNameField.getText();
                deliveredAddress = recepAddressField.getText();
                gift = true;
                pickUp = false;
                deliveryFee = 15.0;



            }
            String greeting = "";
            if (greetingBoxCheckout.isSelected()) {
                greeting = greetingTextCheckout.getText();
            } else {
                greeting = "none";
            }
            long creditCardNumber;
            int creditCardMonth;
            int creditCardYear;
            int creditCardCVV;
            if (anotherMethodBox.isSelected() == true) {
                creditCardNumber = Long.parseLong(creditNumberField.getText());
                creditCardMonth = expiryMonth.getSelectionModel().getSelectedItem();
                creditCardYear = expiryYear.getSelectionModel().getSelectedItem();
                creditCardCVV = Integer.parseInt(cvvField.getText());
            } else {
                creditCardNumber = currentUser.getCreditCardNumber();
                creditCardMonth = currentUser.getCreditMonthExpire();
                creditCardYear = currentUser.getCreditYearExpire();
                creditCardCVV = currentUser.getCcv();
            }
            int dayCheckoutInt = dayCheckout.getSelectionModel().getSelectedItem();
            int monthCheckoutInt = monthCheckout.getSelectionModel().getSelectedItem();
            int yearCheckoutInt = yearCheckout.getSelectionModel().getSelectedItem();
            String OrderedProducts = "";
            for (int i = 0; i < cart.size(); i++) {
                OrderedProducts = OrderedProducts + "%" + cart.get(i).getName() + " - " + String.valueOf(cart.get(i).getPrice()) + "%";
            }
            int prepareHour = 0;
            int prepareMinute = 0;
            String TempString = "";
            String prepareSelect = hourCheckout.getSelectionModel().getSelectedItem();
            for (int x = 0; x < prepareSelect.length(); x++) {
                if (prepareSelect.charAt(x) == ':') {
                    prepareHour = Integer.parseInt(TempString);
                    TempString = "";
                } else {
                    TempString = TempString + Character.toString(prepareSelect.charAt(x));
                }
            }
            prepareMinute = Integer.parseInt(TempString);

            int totalPrice = 0;
            for (int z = 0; z < cart.size(); z++) {
                totalPrice = totalPrice + (int) Math.round(cart.get(z).getPrice());
            }
            if (deliveryBox.isSelected())
                totalPrice = totalPrice + (int) deliveryFee;
            if (currentUser.isSubscription() == true) {
                if (totalPrice > 50)
                    totalPrice = (int) (totalPrice * 0.9);
            }

            String paymentMethod = "CREDIT_CARD";

            Order newOrder = new Order(0, pickUp, shopID, greeting, totalPrice, deliveredAddress, currentUser.getAccountID(), gift, false, dayCheckoutInt, monthCheckoutInt, yearCheckoutInt, currentDay, currentMonth, currentYear, creditCardNumber, creditCardMonth, creditCardYear, creditCardCVV, recepName, recepPhone, deliveredAddress, OrderedProducts, currentHour, currentMintue, prepareHour, prepareMinute, deliveryFee, paymentMethod);
            System.out.println(newOrder);
            UpdateMessage new_msg2 = new UpdateMessage("order", "add");
            new_msg2.setOrder(newOrder);
            try {
                System.out.println("before sending updateMessage to server ");
                SimpleClient.getClient().sendToServer(new_msg2); // sends the updated product to the server class
                System.out.println("afater sending updateMessage to server ");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            Message confirm = new Message();
            confirm.setCustomerID(currentUser.getAccountID());
            confirm.setMsgText(currentYear + "/" + currentMonth + "/" + currentDay + " - " + currentHour + ":" + currentMintue + ":" + "\n" +  "Your Order Has Been Placed!");


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
            placeOrderButton.setVisible(false);
        }
    }

    @FXML
    void DeliverToMyHome(ActionEvent event)
    {
        if(deliverToHome.isSelected())
        {
            deliveryBox.setSelected(false);
            recepNameText.setVisible(false);
            recepNameField.setVisible(false);
            recepPhoneText.setVisible(false);
            recepPhoneField.setVisible(false);
            recepAddressText.setVisible(false);
            recepAddressField.setVisible(false);
        }
    }

    @FXML
    void addGreeting(ActionEvent event) {

        if(greetingBoxCheckout.isSelected())
            greetingTextCheckout.setVisible(true);
        else
            greetingTextCheckout.setVisible(false);
    }

    @FXML
    void deliverySomeone(ActionEvent event)
    {
        if(deliveryBox.isSelected())
        {
            recepNameText.setVisible(true);
            recepNameField.setVisible(true);
            recepPhoneText.setVisible(true);
            recepPhoneField.setVisible(true);
            recepAddressText.setVisible(true);
            recepAddressField.setVisible(true);
            deliverToHome.setSelected(false);
        }
        else
        {
            recepNameText.setVisible(false);
            recepNameField.setVisible(false);
            recepPhoneText.setVisible(false);
            recepPhoneField.setVisible(false);
            recepAddressText.setVisible(false);
            recepAddressField.setVisible(false);
            deliverToHome.setSelected(true);

        }
    }

    @FXML
    void useAnotherMethod(ActionEvent event)
    {
        boolean mode;
        if(anotherMethodBox.isSelected())
            mode = true;
        else
            mode = false;
        creditNumberField.setVisible(mode);
        creditNumberText.setVisible(mode);
        expiryText.setVisible(mode);
        expiryYear.setVisible(mode);
        expiryMonth.setVisible(mode);
        cvvText.setVisible(mode);
        cvvField.setVisible(mode);
    }


    @FXML
    private ComboBox<String> chooseShopID;

    Account currentUser;
    @Subscribe
    public void PassAccountEvent(PassAccountEventCheckout passAcc){ // added today
        System.out.println("Arrived To Pass Account - CheckoutController");
        Account recvAccount = passAcc.getRecievedAccount();
        System.out.println(recvAccount.getPassword());
        System.out.println(recvAccount.getAccountID());
        System.out.println(recvAccount.getEmail());
        System.out.println(recvAccount.getFullName());
        System.out.println(recvAccount.getAddress());
        System.out.println(recvAccount.getCreditCardNumber());
        System.out.println(recvAccount.getCreditMonthExpire());
        currentUser = recvAccount;
        cart = passAcc.getProductsToCheckout();

    }
    List<Product> cart = new ArrayList<>();
    @FXML
    void initialize() throws MalformedURLException
    {
        EventBus.getDefault().register(this);
        assert anotherMethodBox != null : "fx:id=\"anotherMethodBox\" was not injected: check your FXML file 'checkout.fxml'.";
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'checkout.fxml'.";
        assert chooseShopID != null : "fx:id=\"chooseShopID\" was not injected: check your FXML file 'checkout.fxml'.";
        assert creditNumberField != null : "fx:id=\"creditNumberField\" was not injected: check your FXML file 'checkout.fxml'.";
        assert creditNumberText != null : "fx:id=\"creditNumberText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert credit_regex != null : "fx:id=\"credit_regex\" was not injected: check your FXML file 'checkout.fxml'.";
        assert cvvField != null : "fx:id=\"cvvField\" was not injected: check your FXML file 'checkout.fxml'.";
        assert cvvText != null : "fx:id=\"cvvText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert cvv_regex != null : "fx:id=\"cvv_regex\" was not injected: check your FXML file 'checkout.fxml'.";
        assert dayCheckout != null : "fx:id=\"dayCheckout\" was not injected: check your FXML file 'checkout.fxml'.";
        assert deliverToHome != null : "fx:id=\"deliverToHome\" was not injected: check your FXML file 'checkout.fxml'.";
        assert deliveryBox != null : "fx:id=\"deliveryBox\" was not injected: check your FXML file 'checkout.fxml'.";
        assert deliveryDateCheckout != null : "fx:id=\"deliveryDateCheckout\" was not injected: check your FXML file 'checkout.fxml'.";
        assert expiryMonth != null : "fx:id=\"expiryMonth\" was not injected: check your FXML file 'checkout.fxml'.";
        assert expiryText != null : "fx:id=\"expiryText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert expiryYear != null : "fx:id=\"expiryYear\" was not injected: check your FXML file 'checkout.fxml'.";
        assert greetingBoxCheckout != null : "fx:id=\"greetingBoxCheckout\" was not injected: check your FXML file 'checkout.fxml'.";
        assert greetingTextCheckout != null : "fx:id=\"greetingTextCheckout\" was not injected: check your FXML file 'checkout.fxml'.";
        assert hourCheckout != null : "fx:id=\"hourCheckout\" was not injected: check your FXML file 'checkout.fxml'.";
        assert monthCheckout != null : "fx:id=\"monthCheckout\" was not injected: check your FXML file 'checkout.fxml'.";
        assert noDate != null : "fx:id=\"noDate\" was not injected: check your FXML file 'checkout.fxml'.";
        assert noDateTwo != null : "fx:id=\"noDateTwo\" was not injected: check your FXML file 'checkout.fxml'.";
        assert noShop != null : "fx:id=\"noShop\" was not injected: check your FXML file 'checkout.fxml'.";
        assert phone_regex != null : "fx:id=\"phone_regex\" was not injected: check your FXML file 'checkout.fxml'.";
        assert placeOrderButton != null : "fx:id=\"placeOrderButton\" was not injected: check your FXML file 'checkout.fxml'.";
        assert recepAddressField != null : "fx:id=\"recepAddressField\" was not injected: check your FXML file 'checkout.fxml'.";
        assert recepAddressText != null : "fx:id=\"recepAddressText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert recepNameField != null : "fx:id=\"recepNameField\" was not injected: check your FXML file 'checkout.fxml'.";
        assert recepNameText != null : "fx:id=\"recepNameText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert recepPhoneField != null : "fx:id=\"recepPhoneField\" was not injected: check your FXML file 'checkout.fxml'.";
        assert recepPhoneText != null : "fx:id=\"recepPhoneText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert yearCheckout != null : "fx:id=\"yearCheckout\" was not injected: check your FXML file 'checkout.fxml'.";
        phone_regex.setVisible(false);
        credit_regex.setVisible(false);
        cvv_regex.setVisible(false);
        noDate.setVisible(false);
        noShop.setVisible(false);
        noDateTwo.setVisible(false);

        int i;
        System.out.println("Here");
        for(i = 1 ; i < 31 ; i++)
        {
            dayCheckout.getItems().add(i);
        }
        for(i = 1 ; i < 13 ; i++)
        {
            expiryMonth.getItems().add(i);
            monthCheckout.getItems().add(i);
        }
        for(i = 2022 ; i < 2030 ; i++)
        {
            yearCheckout.getItems().add(i);
            expiryYear.getItems().add(i);
        }
        int startHour = 7;
        String FullHour;
        for(i = 0 ; i < 15 ; i++)
        {
            FullHour = startHour + ":" + "00";
            hourCheckout.getItems().add(FullHour);
            FullHour = startHour + ":" + "15";
            hourCheckout.getItems().add(FullHour);
            FullHour = startHour + ":" + "30";
            hourCheckout.getItems().add(FullHour);
            FullHour = startHour + ":" + "45";
            hourCheckout.getItems().add(FullHour);
            startHour++;
        }
        deliverToHome.setSelected(true);
        deliveryBox.setSelected(false);


        recepNameText.setVisible(false);
        recepNameField.setVisible(false);
        recepPhoneText.setVisible(false);
        recepPhoneField.setVisible(false);
        recepAddressText.setVisible(false);
        recepAddressField.setVisible(false);

        creditNumberField.setVisible(false);
        creditNumberText.setVisible(false);
        expiryText.setVisible(false);
        expiryYear.setVisible(false);
        expiryMonth.setVisible(false);

        cvvField.setVisible(false);
        cvvText.setVisible(false);


        greetingTextCheckout.setVisible(false);
        deliveryBox.setVisible(true);

        placeOrderButton.setDisable(true);
        back.setDisable(true);
        chooseShopID.setVisible(false);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        placeOrderButton.setDisable(false);
                        back.setDisable(false);

                        if(currentUser.getBelongShop() == 0) {
                            chooseShopID.getItems().add("ID 0: - Chain");
                            chooseShopID.getItems().add("ID 1: Tiberias, Big Danilof");
                            chooseShopID.getItems().add("ID 2: Haifa, Merkaz Zeiv");
                            chooseShopID.getItems().add("ID 3: Tel Aviv, Ramat Aviv");
                            chooseShopID.getItems().add("ID 4: Eilat, Ice mall");
                            chooseShopID.getItems().add("ID 5: Be'er Sheva, Big Beer Sheva");
                            chooseShopID.setVisible(true);
                        }
                        else
                        {
                            chooseShopID.setVisible(false);
                        }

                    }
                },4500
        );
    }
}
