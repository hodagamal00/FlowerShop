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
import java.util.Locale;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private CheckBox orderForSomeoneElseBox;

    @FXML
    private Text deliveryValidationText;

    @FXML
    private Text recipientValidationText;

    @FXML
    private Text addressValidationText;

    @FXML
    private Text subtotalText;

    @FXML
    private Text deliveryFeeText;

    @FXML
    private Text totalText;

    @FXML
    private Text orderTimingText;

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

    private static final double DELIVERY_FEE = 20.0;


    @FXML
    void openCatalog(ActionEvent event) throws IOException {
        NavigationService.getInstance().navigate("Catalog");

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
        deliveryValidationText.setVisible(false);
        recipientValidationText.setVisible(false);
        addressValidationText.setVisible(false);

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
        if (!deliverToHome.isSelected() && !deliveryBox.isSelected()) {
            deliveryValidationText.setText("Please choose pickup or delivery.");
            deliveryValidationText.setVisible(true);
            fail = true;
        }

        if (deliveryBox.isSelected() == true) {
            pattern = Pattern.compile(phoneNum_regex);
            matcher = pattern.matcher(recepPhoneField.getText());
            if (!matcher.matches()) {
                phone_regex.setVisible(true);
                fail = true;
            }
            if (recepNameField.getText() == null || recepNameField.getText().trim().isEmpty()) {
                recipientValidationText.setText("Recipient name is required for delivery.");
                recipientValidationText.setVisible(true);
                fail = true;
            }
            if (recepAddressField.getText() == null || recepAddressField.getText().trim().isEmpty()) {
                addressValidationText.setText("Delivery address is required for delivery.");
                addressValidationText.setVisible(true);
                fail = true;
            }
        } else if (orderForSomeoneElseBox.isSelected()) {
            pattern = Pattern.compile(phoneNum_regex);
            matcher = pattern.matcher(recepPhoneField.getText());
            if (!matcher.matches()) {
                phone_regex.setVisible(true);
                fail = true;
            }
            if (recepNameField.getText() == null || recepNameField.getText().trim().isEmpty()) {
                recipientValidationText.setText("Recipient name is required when ordering for someone else.");
                recipientValidationText.setVisible(true);
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
                if (orderForSomeoneElseBox.isSelected()) {
                    recepName = recepNameField.getText();
                    recepPhone = Long.parseLong(recepPhoneField.getText());
                } else {
                    recepName = currentUser.getFullName();
                    recepPhone = currentUser.getPhoneNumber();
                }
                deliveredAddress = currentUser.getAddress();
            }
            if (deliveryBox.isSelected()) {
                recepPhone = Long.parseLong(recepPhoneField.getText());
                recepName = recepNameField.getText();
                deliveredAddress = recepAddressField.getText();
                gift = true;
                pickUp = false;
                deliveryFee = DELIVERY_FEE;
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
                double itemPrice = PricingService.calculateDisplayPrice(cart.get(i), currentUser);
                OrderedProducts = OrderedProducts + "%" + cart.get(i).getName() + " - " + String.valueOf(itemPrice) + "%";
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

            LocalDateTime prepareDateTime = buildPrepareDateTime(yearCheckoutInt, monthCheckoutInt, dayCheckoutInt, prepareHour, prepareMinute);
            if (prepareDateTime == null) {
                noDate.setText("Please select a valid preparation date and time.");
                noDate.setVisible(true);
                return;
            }
            LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
            if (prepareDateTime.isBefore(now)) {
                noDate.setText("Preparation time must be in the future.");
                noDate.setVisible(true);
                return;
            }

            double totalPrice = 0.0;
            for (int z = 0; z < cart.size(); z++) {
                totalPrice += PricingService.calculateDisplayPrice(cart.get(z), currentUser);
            }
            if (deliveryBox.isSelected()) {
                totalPrice += deliveryFee;
            }
            totalPrice = PricingService.roundCurrency(totalPrice);

            String paymentMethod = "CREDIT_CARD";

            Order newOrder = new Order(0, pickUp, shopID, greeting, (int) Math.round(totalPrice), deliveredAddress, currentUser.getAccountID(), gift, false, dayCheckoutInt, monthCheckoutInt, yearCheckoutInt, currentDay, currentMonth, currentYear, creditCardNumber, creditCardMonth, creditCardYear, creditCardCVV, recepName, recepPhone, deliveredAddress, OrderedProducts, currentHour, currentMintue, prepareHour, prepareMinute, deliveryFee, paymentMethod);
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
            navigateToOrderConfirmation(newOrder, deliveryBox.isSelected());
        }
    }

    @FXML
    void DeliverToMyHome(ActionEvent event)
    {
        if(deliverToHome.isSelected())
        {
            deliveryBox.setSelected(false);
            if (!orderForSomeoneElseBox.isSelected()) {
                updateRecipientVisibility(false);
            }
            updateOrderSummary();
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
            updateRecipientVisibility(true);
            deliverToHome.setSelected(false);
            orderForSomeoneElseBox.setSelected(true);
        }
        else
        {
            updateRecipientVisibility(orderForSomeoneElseBox.isSelected());
            deliverToHome.setSelected(true);
        }
        updateOrderSummary();
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
    void toggleOrderForSomeoneElse(ActionEvent event) {
        if (orderForSomeoneElseBox.isSelected()) {
            updateRecipientVisibility(true);
            deliverToHome.setSelected(true);
            if (deliveryBox.isSelected()) {
                updateRecipientVisibility(true);
            }
        } else if (!deliveryBox.isSelected()) {
            updateRecipientVisibility(false);
        }
        updateOrderSummary();
    }


    @FXML
    private ComboBox<String> chooseShopID;

    Account currentUser;
    private java.util.Timer shopTimer;
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
        updateOrderSummary();
        applyGreetingVisibility();
        scheduleShopSelectionEnable();

    }

    @Subscribe
    public void handleUserUpdateResponse(UserUpdateResponse response) {
        if (response == null || response.isSuccess()) {
            return;
        }
        String message = response.getMessage();
        if (message == null || message.isBlank()) {
            message = "Order submission failed. Please try again.";
        }
        noDate.setText(message);
        noDate.setVisible(true);
    }
    List<Product> cart = new ArrayList<>();
    @FXML
    void initialize() throws MalformedURLException
    {
        EventBus.getDefault().register(this);
        assert orderForSomeoneElseBox != null : "fx:id=\"orderForSomeoneElseBox\" was not injected: check your FXML file 'checkout.fxml'.";
        assert deliveryValidationText != null : "fx:id=\"deliveryValidationText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert recipientValidationText != null : "fx:id=\"recipientValidationText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert addressValidationText != null : "fx:id=\"addressValidationText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert subtotalText != null : "fx:id=\"subtotalText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert deliveryFeeText != null : "fx:id=\"deliveryFeeText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert totalText != null : "fx:id=\"totalText\" was not injected: check your FXML file 'checkout.fxml'.";
        assert orderTimingText != null : "fx:id=\"orderTimingText\" was not injected: check your FXML file 'checkout.fxml'.";
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
        deliveryValidationText.setVisible(false);
        recipientValidationText.setVisible(false);
        addressValidationText.setVisible(false);

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
        orderForSomeoneElseBox.setSelected(false);

        updateRecipientVisibility(false);

        creditNumberField.setVisible(false);
        creditNumberText.setVisible(false);
        expiryText.setVisible(false);
        expiryYear.setVisible(false);
        expiryMonth.setVisible(false);

        cvvField.setVisible(false);
        cvvText.setVisible(false);


        greetingTextCheckout.setVisible(false);
        deliveryBox.setVisible(true);
        updateOrderSummary();

        applyGreetingVisibility();

        placeOrderButton.setDisable(true);
        back.setDisable(true);
        chooseShopID.setVisible(false);

        dayCheckout.setOnAction(event -> updateOrderSummary());
        monthCheckout.setOnAction(event -> updateOrderSummary());
        yearCheckout.setOnAction(event -> updateOrderSummary());
        hourCheckout.setOnAction(event -> updateOrderSummary());

    }

    private void applyGreetingVisibility() {
        Account account = currentUser != null ? currentUser : SimpleClient.getUser();
        boolean showGreeting = account != null && account.getPrivilegeLevel() == 1;

        if (greetingBoxCheckout != null) {
            greetingBoxCheckout.setVisible(showGreeting);
            greetingBoxCheckout.setManaged(showGreeting);
            if (!showGreeting) {
                greetingBoxCheckout.setSelected(false);
            }
        }
        if (greetingTextCheckout != null) {
            greetingTextCheckout.setVisible(false);
            greetingTextCheckout.setManaged(showGreeting);
            if (!showGreeting) {
                greetingTextCheckout.clear();
            }
        }
    }

    private void scheduleShopSelectionEnable() {
        if (currentUser == null) {
            return;
        }
        if (shopTimer != null) {
            shopTimer.cancel();
        }
        shopTimer = new java.util.Timer();
        shopTimer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (currentUser == null) {
                            return;
                        }
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

    private void updateRecipientVisibility(boolean visible) {
        recepNameText.setVisible(visible);
        recepNameField.setVisible(visible);
        recepPhoneText.setVisible(visible);
        recepPhoneField.setVisible(visible);
        boolean showAddress = visible && deliveryBox.isSelected();
        recepAddressText.setVisible(showAddress);
        recepAddressField.setVisible(showAddress);
        addressValidationText.setVisible(false);
        recipientValidationText.setVisible(false);
    }

    private void updateOrderSummary() {
        double subtotal = 0.0;
        for (Product product : cart) {
            subtotal += PricingService.calculateDisplayPrice(product, currentUser);
        }
        subtotal = PricingService.roundCurrency(subtotal);
        double deliveryFee = deliveryBox.isSelected() ? DELIVERY_FEE : 0.0;
        double total = PricingService.roundCurrency(subtotal + deliveryFee);

        subtotalText.setText(String.format(Locale.US, "%.2f₪", subtotal));
        deliveryFeeText.setText(deliveryBox.isSelected() ? String.format(Locale.US, "%.2f₪", deliveryFee) : "Free");
        totalText.setText(String.format(Locale.US, "%.2f₪", total));

        String timingLabel = "Select time";
        if (dayCheckout.getSelectionModel().getSelectedIndex() != -1
                && monthCheckout.getSelectionModel().getSelectedIndex() != -1
                && yearCheckout.getSelectionModel().getSelectedIndex() != -1
                && hourCheckout.getSelectionModel().getSelectedIndex() != -1) {
            int day = dayCheckout.getSelectionModel().getSelectedItem();
            int month = monthCheckout.getSelectionModel().getSelectedItem();
            int year = yearCheckout.getSelectionModel().getSelectedItem();
            String prepareSelect = hourCheckout.getSelectionModel().getSelectedItem();
            int[] timeParts = parseTime(prepareSelect);
            LocalDateTime prepareDateTime = buildPrepareDateTime(year, month, day, timeParts[0], timeParts[1]);
            if (prepareDateTime != null) {
                LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
                Duration duration = Duration.between(now, prepareDateTime);
                if (!duration.isNegative() && duration.toHours() < 3) {
                    timingLabel = "Immediate (within 3 hours)";
                } else {
                    timingLabel = "Scheduled";
                }
            }
        }
        orderTimingText.setText(timingLabel);
    }

    private int[] parseTime(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return new int[]{hour, minute};
    }

    private LocalDateTime buildPrepareDateTime(int year, int month, int day, int hour, int minute) {
        try {
            return LocalDateTime.of(year, month, day, hour, minute);
        } catch (Exception e) {
            return null;
        }
    }

    private void navigateToOrderConfirmation(Order order, boolean delivery) {
        OrderConfirmationController.setOrder(order, delivery);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderConfirmation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) placeOrderButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
