package il.cshaifasweng.OCSFMediatorExample.client; /**
 * Sample Skeleton for 'log_manager.fxml' Controller Class
 */

import java.net.URL;
import java.util.*;


import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.getAllOrdersMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LogManagerController {

    @FXML // fx:id="fieldsError"
    private Label fieldsError; // Value injected by FXMLLoader

    @FXML // fx:id="logError"
    private Label logError; // Value injected by FXMLLoader

    @FXML // fx:id="shopError"
    private Label shopError; // Value injected by FXMLLoader

    @FXML // fx:id="shopError2"
    private Label shopError2; // Value injected by FXMLLoader



    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Day"
    private CategoryAxis Day; // Value injected by FXMLLoader

    @FXML // fx:id="FromDateText"
    private Text FromDateText; // Value injected by FXMLLoader

    @FXML // fx:id="FromDay"
    private ComboBox<Integer> FromDay; // Value injected by FXMLLoader

    @FXML // fx:id="FromMonth"
    private ComboBox<Integer> FromMonth; // Value injected by FXMLLoader

    @FXML // fx:id="FromYear"
    private ComboBox<Integer> FromYear; // Value injected by FXMLLoader

    @FXML // fx:id="LoadLogButton"
    private Button LoadLogButton; // Value injected by FXMLLoader

    @FXML // fx:id="LogChart"
    private BarChart<String, Integer> LogChart; // Value injected by FXMLLoader

    @FXML // fx:id="LogType"
    private ComboBox<String> LogType; // Value injected by FXMLLoader

    @FXML // fx:id="Number"
    private NumberAxis Number; // Value injected by FXMLLoader

    @FXML // fx:id="UntilDateText"
    private Text UntilDateText; // Value injected by FXMLLoader

    @FXML // fx:id="UntilDay"
    private ComboBox<Integer> UntilDay; // Value injected by FXMLLoader

    @FXML // fx:id="UntilMonth"
    private ComboBox<Integer> UntilMonth; // Value injected by FXMLLoader

    @FXML // fx:id="UntilYear"
    private ComboBox<Integer> UntilYear; // Value injected by FXMLLoader

    @FXML // fx:id="chooseShop"
    private ComboBox<String> chooseShop; // Value injected by FXMLLoader

    @FXML
    private Button branchOrdersBtn;

    @FXML
    private ComboBox<String> CompareShops;

    @FXML
    private CheckBox compareButton;

    @FXML // fx:id="wait"
    private Label wait; // Value injected by FXMLLoader

    @FXML
    void goToBranchOrders(ActionEvent event) {
        NavigationService.getInstance().navigate("BranchOrders");
    }
    static List<Order> orders = new ArrayList<Order>();
    static List<Complaint> complaints = new ArrayList<>();


    int selectedShopID = -1;
    int selectCompareShopID = -1;
    @FXML
    void loadLog(ActionEvent event) {
        logError.setVisible(false);
        shopError.setVisible(false);
        shopError2.setVisible(false);
        fieldsError.setVisible(false);
        if(chooseShop.getSelectionModel().getSelectedIndex() == -1)
        {
            shopError.setVisible(true);
        }
        else if(LogType.getSelectionModel().getSelectedIndex() == -1)
        {
            logError.setVisible(true);
        }
        else if(FromDay.getSelectionModel().getSelectedIndex() == -1 || FromMonth.getSelectionModel().getSelectedIndex() == -1 || FromYear.getSelectionModel().getSelectedIndex() == -1 || UntilDay.getSelectionModel().getSelectedIndex() == -1 || UntilMonth.getSelectionModel().getSelectedIndex() == -1 || UntilYear.getSelectionModel().getSelectedIndex() == -1)
        {
            fieldsError.setVisible(true);
        }
        else if(compareButton.isSelected() && CompareShops.getSelectionModel().getSelectedIndex() == -1)
        {
            shopError2.setVisible(true);
        }
        else {
            switch (chooseShop.getValue().toString()) {
                case "ID 0: - Chain":
                    selectedShopID = 0;
                    break;
                case "ID 1: Tiberias, Big Danilof":
                    selectedShopID = 1;
                    break;
                case "ID 2: Haifa, Merkaz Zeiv":
                    selectedShopID = 2;
                    break;
                case "ID 3: Tel Aviv, Ramat Aviv":
                    selectedShopID = 3;
                    break;
                case "ID 4: Eilat, Ice mall":
                    selectedShopID = 4;
                    break;
                case "ID 5: Be'er Sheva, Big Beer Sheva":
                    selectedShopID = 5;
                    break;
            }
            if (compareButton.isSelected()) {
                switch (CompareShops.getValue().toString()) {
                    case "ID 0: - Chain":
                        selectCompareShopID = 0;
                        break;
                    case "ID 1: Tiberias, Big Danilof":
                        selectCompareShopID = 1;
                        break;
                    case "ID 2: Haifa, Merkaz Zeiv":
                        selectCompareShopID = 2;
                        break;
                    case "ID 3: Tel Aviv, Ramat Aviv":
                        selectCompareShopID = 3;
                        break;
                    case "ID 4: Eilat, Ice mall":
                        selectCompareShopID = 4;
                        break;
                    case "ID 5: Be'er Sheva, Big Beer Sheva":
                        selectCompareShopID = 5;
                        break;
                }
            }
                String LogTypeString = LogType.getSelectionModel().getSelectedItem();
                List<Order> searchResultOrder = new ArrayList<Order>();
                List<Complaint> searchResultComplaint = new ArrayList<Complaint>();
                int firstWorks = 0, LastWorks = 0, totalIncome = 0, i = 0;
                XYChart.Series set = new XYChart.Series();
                if (LogTypeString == "Income Log") {
                    searchResultOrder = searchOrderResult(selectedShopID);
                    System.out.println("First Shop Orders");
                    printList(searchResultOrder);
                    while (LastWorks < searchResultOrder.size()) {
                        if (searchResultOrder.get(firstWorks).sameDate(searchResultOrder.get(LastWorks))) {
                            totalIncome = totalIncome + searchResultOrder.get(LastWorks).getTotalPrice();
                            LastWorks++;
                            if (LastWorks == searchResultOrder.size()) {
                                set.getData().add(new XYChart.Data(searchResultOrder.get(LastWorks - 1).getDate(), totalIncome));
                                totalIncome = 0;
                            }
                        } else {
                            set.getData().add(new XYChart.Data(searchResultOrder.get(firstWorks).getDate(), totalIncome));
                            firstWorks = LastWorks;
                            totalIncome = 0;
                        }
                    }
                    LogChart.getData().addAll(set);

                } else if (LogTypeString == "Orders Log") {
                    searchResultOrder = searchOrderResult(selectedShopID);
                    int totalOrders = 0;
                    firstWorks = 0;
                    LastWorks = 0;
                    while (LastWorks < searchResultOrder.size()) {
                        if (searchResultOrder.get(firstWorks).sameDate(searchResultOrder.get(LastWorks))) {
                            totalOrders++;
                            LastWorks++;
                            if (LastWorks == searchResultOrder.size()) {
                                set.getData().add(new XYChart.Data(searchResultOrder.get(LastWorks - 1).getDate(), totalOrders));
                                System.out.println("Insert " + totalOrders);
                                totalOrders = 0;
                            }
                        } else {
                            set.getData().add(new XYChart.Data(searchResultOrder.get(firstWorks).getDate(), totalOrders));
                            System.out.println("Insert " + totalOrders);
                            firstWorks = LastWorks;
                            totalOrders = 0;
                        }
                    }
                    LogChart.getData().addAll(set);
                } else {
                    searchResultComplaint = searchComplaintResult(selectedShopID);
                    int totalComplaints = 0;
                    firstWorks = 0;
                    LastWorks = 0;
                    while (LastWorks < searchResultComplaint.size()) {
                        if (searchResultComplaint.get(firstWorks).sameDate(searchResultComplaint.get(LastWorks))) {
                            totalComplaints++;
                            LastWorks++;
                            if (LastWorks == searchResultComplaint.size()) {
                                set.getData().add(new XYChart.Data(searchResultComplaint.get(LastWorks - 1).getDate(), totalComplaints));
                                totalComplaints = 0;
                            }
                        } else {
                            set.getData().add(new XYChart.Data(searchResultComplaint.get(firstWorks).getDate(), totalComplaints));
                            firstWorks = LastWorks;
                            totalComplaints = 0;
                        }
                    }
                    LogChart.getData().addAll(set);
                }
                if (compareButton.isSelected()) {
                    firstWorks = 0;
                    LastWorks = 0;
                    totalIncome = 0;
                    i = 0;
                    XYChart.Series set2 = new XYChart.Series();
                    searchResultOrder.clear();
                    searchResultComplaint.clear();
                    if (LogTypeString == "Income Log") {
                        searchResultOrder = searchOrderResult(selectCompareShopID);
                        System.out.println("Second Shop Orders");
                        printList(searchResultOrder);
                        while (LastWorks < searchResultOrder.size()) {
                            if (searchResultOrder.get(firstWorks).sameDate(searchResultOrder.get(LastWorks))) {
                                totalIncome = totalIncome + searchResultOrder.get(LastWorks).getTotalPrice();
                                LastWorks++;
                                if (LastWorks == searchResultOrder.size()) {
                                    set2.getData().add(new XYChart.Data(searchResultOrder.get(LastWorks - 1).getDate(), totalIncome));
                                    totalIncome = 0;
                                }
                            } else {
                                set2.getData().add(new XYChart.Data(searchResultOrder.get(firstWorks).getDate(), totalIncome));
                                firstWorks = LastWorks;
                                totalIncome = 0;
                            }
                        }
                        LogChart.getData().addAll(set2);

                    } else if (LogTypeString == "Orders Log") {
                        searchResultOrder = searchOrderResult(selectCompareShopID);
                        int totalOrders = 0;
                        firstWorks = 0;
                        LastWorks = 0;
                        while (LastWorks < searchResultOrder.size()) {
                            if (searchResultOrder.get(firstWorks).sameDate(searchResultOrder.get(LastWorks))) {
                                totalOrders++;
                                LastWorks++;
                                if (LastWorks == searchResultOrder.size()) {
                                    set2.getData().add(new XYChart.Data(searchResultOrder.get(LastWorks - 1).getDate(), totalOrders));
                                    System.out.println("Insert " + totalOrders);
                                    totalOrders = 0;
                                }
                            } else {
                                set2.getData().add(new XYChart.Data(searchResultOrder.get(firstWorks).getDate(), totalOrders));
                                System.out.println("Insert " + totalOrders);
                                firstWorks = LastWorks;
                                totalOrders = 0;
                            }
                        }
                        LogChart.getData().addAll(set);
                    } else {
                        searchResultComplaint = searchComplaintResult(selectCompareShopID);
                        int totalComplaints = 0;
                        firstWorks = 0;
                        LastWorks = 0;
                        while (LastWorks < searchResultComplaint.size()) {
                            if (searchResultComplaint.get(firstWorks).sameDate(searchResultComplaint.get(LastWorks))) {
                                totalComplaints++;
                                LastWorks++;
                                if (LastWorks == searchResultComplaint.size()) {
                                    set2.getData().add(new XYChart.Data(searchResultComplaint.get(LastWorks - 1).getDate(), totalComplaints));
                                    totalComplaints = 0;
                                }
                            } else {
                                set2.getData().add(new XYChart.Data(searchResultComplaint.get(firstWorks).getDate(), totalComplaints));
                                firstWorks = LastWorks;
                                totalComplaints = 0;
                            }
                        }

                    }
                }
        }
    }
    void printList(List<Order> orders)
    {
        for(int i = 0 ; i < orders.size() ; i++)
        {
            System.out.println(orders.get(i).getGreeting());
        }
    }

    List<Complaint> searchComplaintResult(int shopID)
    {
        int selectedShopID = 0;
        int selectCompareShopID = 0;
        int FromDayInt = FromDay.getValue().intValue();
        int UntilDayInt = UntilDay.getValue().intValue();
        int FromMonthInt = FromMonth.getValue().intValue();
        int UntilMonthInt = UntilMonth.getValue().intValue();
        int FromYearInt = FromYear.getValue().intValue();
        int UntilYearInt = UntilYear.getValue().intValue();
        String LogTypeString = LogType.getSelectionModel().getSelectedItem();
        List<Complaint> searchResultComplaint = new ArrayList<Complaint>();
        int firstWorks = 0, LastWorks = 0, totalIncome = 0, i = 0;
        XYChart.Series set = new XYChart.Series();
        for (i = 0; i < complaints.size(); i++) {
            System.out.println("Order Date = " + complaints.get(i).getDate());
            System.out.println("From: " + FromDayInt + "/" + FromMonthInt + "/" + FromYearInt);
            System.out.println("Until: " + UntilDayInt + "/" + UntilMonthInt + "/" + UntilYearInt);
            if (complaints.get(i).getYear() <= UntilYearInt && complaints.get(i).getYear() >= FromYearInt)
            {
                System.out.println("Year Test 1");
                if(complaints.get(i).getYear() == UntilYearInt || complaints.get(i).getYear() == FromYearInt)
                {
                    System.out.println("Year Equal Test");
                    if(complaints.get(i).getYear() == UntilYearInt)
                    {
                        System.out.println("Year == Until Test");
                        if(complaints.get(i).getMonth() < UntilMonthInt)
                        {
                            System.out.println(" Month < Until Test");
                            searchResultComplaint.add(complaints.get(i));
                        }
                        if(complaints.get(i).getMonth() == UntilMonthInt)
                        {
                            System.out.println(" Month  == Until Test");
                            if(complaints.get(i).getDay() <= UntilDayInt)
                            {
                                System.out.println(" Day <= Until Test");
                                searchResultComplaint.add(complaints.get(i));
                            }
                        }
                    }
                    if(complaints.get(i).getYear() == FromYearInt)
                    {
                        System.out.println("Year == From Test");
                        if(complaints.get(i).getMonth() > FromMonthInt)
                        {
                            System.out.println("Month > From Test");
                            searchResultComplaint.add(complaints.get(i));
                        }
                        if(complaints.get(i).getMonth() == FromMonthInt)
                        {
                            System.out.println("Month == From Test");
                            if(complaints.get(i).getDay() >= FromDayInt)
                            {
                                System.out.println("Day >= From Test");
                                searchResultComplaint.add(complaints.get(i));
                            }
                        }
                    }
                }
                else
                {
                    System.out.println("Added - Year Between Test");
                    searchResultComplaint.add(complaints.get(i));
                }
            }
        }
        for(int z = 0 ; z < searchResultComplaint.size() ; z++)
            System.out.println("ID: " + searchResultComplaint.get(z).getComplaintID());
        return searchResultComplaint;

    }
    List<Order> searchOrderResult(int shopID)
    {
        int selectedShopID = 0;
        int selectCompareShopID = 0;
        int FromDayInt = FromDay.getValue().intValue();
        int UntilDayInt = UntilDay.getValue().intValue();
        int FromMonthInt = FromMonth.getValue().intValue();
        int UntilMonthInt = UntilMonth.getValue().intValue();
        int FromYearInt = FromYear.getValue().intValue();
        int UntilYearInt = UntilYear.getValue().intValue();
        String LogTypeString = LogType.getSelectionModel().getSelectedItem();
        List<Order> searchResultOrder = new ArrayList<Order>();
        int firstWorks = 0, LastWorks = 0, totalIncome = 0, i = 0;
        XYChart.Series set = new XYChart.Series();
        for (i = 0; i < orders.size(); i++) {
            System.out.println("Order Date = " + orders.get(i).getDate());
            System.out.println("From: " + FromDayInt + "/" + FromMonthInt + "/" + FromYearInt);
            System.out.println("Until: " + UntilDayInt + "/" + UntilMonthInt + "/" + UntilYearInt);
            if (orders.get(i).getShopID() == shopID) {
                if (orders.get(i).getOrderYear() <= UntilYearInt && orders.get(i).getOrderYear() >= FromYearInt) {
                    System.out.println("Year Test 1");
                    if (orders.get(i).getOrderYear() == UntilYearInt || orders.get(i).getOrderYear() == FromYearInt) {
                        System.out.println("Year Equal Test");
                        if (orders.get(i).getOrderYear() == UntilYearInt) {
                            System.out.println("Year == Until Test");
                            if (orders.get(i).getOrderMonth() < UntilMonthInt) {
                                System.out.println(" Month < Until Test");
                                searchResultOrder.add(orders.get(i));
                            }
                            if (orders.get(i).getOrderMonth() == UntilMonthInt) {
                                System.out.println(" Month  == Until Test");
                                if (orders.get(i).getOrderDay() <= UntilDayInt) {
                                    System.out.println(" Day <= Until Test");
                                    searchResultOrder.add(orders.get(i));
                                }
                            }
                        }
                        if (orders.get(i).getOrderYear() == FromYearInt) {
                            System.out.println("Year == From Test");
                            if (orders.get(i).getOrderMonth() > FromMonthInt) {
                                System.out.println("Month > From Test");
                                searchResultOrder.add(orders.get(i));
                            }
                            if (orders.get(i).getOrderMonth() == FromMonthInt) {
                                System.out.println("Month == From Test");
                                if (orders.get(i).getOrderDay() >= FromDayInt) {
                                    System.out.println("Day >= From Test");
                                    searchResultOrder.add(orders.get(i));
                                }
                            }
                        }
                    } else {
                        System.out.println("Added - Year Between Test");
                        searchResultOrder.add(orders.get(i));
                    }
                }
            }
        }
        System.out.println("---");
        for(int z = 0 ; z < searchResultOrder.size() ; z++)
            System.out.println("ID: " + searchResultOrder.get(z).getOrderID());
        return searchResultOrder;
    }

    @FXML
    void clickedCompareButton(ActionEvent event)
    {
        if(compareButton.isSelected())
        {
            CompareShops.setVisible(true);
        }
        else
        {
            CompareShops.setVisible(false);
        }


    }
    Account currentUser;
    List<Complaint> allComplaints ;
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
        if (!AccessGuard.requireMinPrivilege(3)) {
            return;
        }
        EventBus.getDefault().register(this);
        getAllOrdersMessage getOrdersMsg = new getAllOrdersMessage();
        SimpleClient.getClient().sendToServer(getOrdersMsg);
        System.out.println("after sending getAllOrders message !");
        assert CompareShops != null : "fx:id=\"CompareShops\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert Day != null : "fx:id=\"Day\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert FromDateText != null : "fx:id=\"FromDateText\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert FromDay != null : "fx:id=\"FromDay\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert FromMonth != null : "fx:id=\"FromMonth\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert FromYear != null : "fx:id=\"FromYear\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert LoadLogButton != null : "fx:id=\"LoadLogButton\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert LogChart != null : "fx:id=\"LogChart\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert LogType != null : "fx:id=\"LogType\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert Number != null : "fx:id=\"Number\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert UntilDateText != null : "fx:id=\"UntilDateText\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert UntilDay != null : "fx:id=\"UntilDay\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert UntilMonth != null : "fx:id=\"UntilMonth\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert UntilYear != null : "fx:id=\"UntilYear\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert chooseShop != null : "fx:id=\"chooseShop\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert compareButton != null : "fx:id=\"compareButton\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert fieldsError != null : "fx:id=\"fieldsError\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert branchOrdersBtn != null : "fx:id=\"branchOrdersBtn\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert logError != null : "fx:id=\"logError\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert shopError != null : "fx:id=\"shopError\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert shopError2 != null : "fx:id=\"shopError2\" was not injected: check your FXML file 'log_manager.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'log_manager.fxml'.";
        CompareShops.setVisible(false);
        fieldsError.setVisible(false);
        logError.setVisible(false);
        shopError.setVisible(false);
        shopError2.setVisible(false);
        compareButton.setVisible(false);

        int i;
        for(i = 1 ; i < 31 ; i++)
        {
            FromDay.getItems().add(i);
            UntilDay.getItems().add(i);
        }
        for(i = 1 ; i < 13 ; i++)
        {
            FromMonth.getItems().add(i);
            UntilMonth.getItems().add(i);
        }
        for(i = 2022 ; i < 2030 ; i++)
        {
            FromYear.getItems().add(i);
            UntilYear.getItems().add(i);
        }
            //chooseShop.getItems().add("ID 0: - Chain");
            //chooseShop.getItems().add("ID 1: Tiberias, Big Danilof");
           // chooseShop.getItems().add("ID 2: Haifa, Merkaz Zeiv");
            //chooseShop.getItems().add("ID 3: Tel Aviv, Ramat Aviv");
            //chooseShop.getItems().add("ID 4: Eilat, Ice mall");
            //chooseShop.getItems().add("ID 5: Be'er Sheva, Big Beer Sheva");

        LogType.getItems().add("Income Log");
        LogType.getItems().add("Orders Log");
        LogType.getItems().add("Complaint Log");
        Day.setAnimated(false);

        branchOrdersBtn.setDisable(true);
        LoadLogButton.setDisable(true);
        wait.setVisible(true);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        branchOrdersBtn.setDisable(false);
                        LoadLogButton.setDisable(false);
                        wait.setVisible(false);
                        switch (currentUser.getBelongShop())
                        {
                            case 0:
                                chooseShop.getItems().add("ID 0: - Chain");
                                chooseShop.getItems().add("ID 1: Tiberias, Big Danilof");
                                chooseShop.getItems().add("ID 2: Haifa, Merkaz Zeiv");
                                chooseShop.getItems().add("ID 3: Tel Aviv, Ramat Aviv");
                                chooseShop.getItems().add("ID 4: Eilat, Ice mall");
                                chooseShop.getItems().add("ID 5: Be'er Sheva, Big Beer Sheva");
                                compareButton.setVisible(true);
                                break;
                            case 1:
                                chooseShop.getItems().add("ID 1: Tiberias, Big Danilof");
                                break;
                            case 2:
                                chooseShop.getItems().add("ID 2: Haifa, Merkaz Zeiv");
                                break;
                            case 3:
                                chooseShop.getItems().add("ID 3: Tel Aviv, Ramat Aviv");
                                break;
                            case 4:
                                chooseShop.getItems().add("ID 4: Eilat, Ice mall");
                                break;
                            case 5:
                                chooseShop.getItems().add("ID 5: Be'er Sheva, Big Beer Sheva");
                                break;
                        }
                    }
                },4500
        );
        CompareShops.getItems().add("ID 0: - Chain");
        CompareShops.getItems().add("ID 1: Tiberias, Big Danilof");
        CompareShops.getItems().add("ID 2: Haifa, Merkaz Zeiv");
        CompareShops.getItems().add("ID 3: Tel Aviv, Ramat Aviv");
        CompareShops.getItems().add("ID 4: Eilat, Ice mall");
        CompareShops.getItems().add("ID 5: Be'er Sheva, Big Beer Sheva");
    }
    @Subscribe
    public void complaintEvent(PassAllComplaintsEvent allComps){ // added new 30/7
        List<Complaint> recievedComplaints = allComps.getComplaintsToPass();
        allComplaints = allComps.getComplaintsToPass();
        complaints = allComplaints;
    }

    @Subscribe
    public void passOrders(PassOrdersFromServer passOrders){ // added 30/7
        List<Order> recievedOrders = passOrders.getRecievedOrders();
        orders = recievedOrders;
    }

    @Subscribe
    public void PassAccountEvent(PassAccountEventLogManager passAcc){ // added 30/7
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
