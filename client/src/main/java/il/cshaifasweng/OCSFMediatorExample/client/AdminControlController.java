package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AdminControlController {

    @FXML // fx:id="accID"
    private TextField accID; // Value injected by FXMLLoader

    @FXML // fx:id="usersTable"
    private TableView<UserRow> usersTable; // Value injected by FXMLLoader

    @FXML // fx:id="address"
    private TextArea address; // Value injected by FXMLLoader


    @FXML
    private Button changeDetailsButton;

    @FXML
    private CheckBox frozenToggle;

    @FXML // fx:id="creditexpmonth"
    private TextField creditexpmonth; // Value injected by FXMLLoader

    @FXML // fx:id="creditexpyear"
    private TextField creditexpyear; // Value injected by FXMLLoader

    @FXML // fx:id="creditnum"
    private TextField creditnum; // Value injected by FXMLLoader

    @FXML // fx:id="customerID"
    private TextField customerID; // Value injected by FXMLLoader

    @FXML // fx:id="cvv"
    private TextField cvv; // Value injected by FXMLLoader

    @FXML // fx:id="email"
    private TextField email; // Value injected by FXMLLoader

    @FXML // fx:id="loadProfile"
    private Button loadProfile; // Value injected by FXMLLoader

    @FXML // fx:id="name"
    private TextField name; // Value injected by FXMLLoader

    @FXML // fx:id="password"
    private TextField password; // Value injected by FXMLLoader

    @FXML // fx:id="phone"
    private TextField phone; // Value injected by FXMLLoader

    @FXML // fx:id="profileType"
    private ComboBox<String> profileType; // Value injected by FXMLLoader

    @FXML
    private Button refreshButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML // fx:id="shop"
    private TextField shop; // Value injected by FXMLLoader

    @FXML // fx:id="sub"
    private TextField sub; // Value injected by FXMLLoader

    @FXML
    private TableColumn<UserRow, String> userEmailCol;

    @FXML
    private TableColumn<UserRow, String> userIdCol;

    @FXML
    private TableColumn<UserRow, String> userNameCol;

    @FXML
    private TableColumn<UserRow, Integer> userPrivilegeCol;

    @FXML
    private TableColumn<UserRow, String> userRoleCol;

    @FXML
    private TableColumn<UserRow, String> userStatusCol;

    @FXML
    private Label statusMessage;

    @FXML // fx:id="wait"
    private Label wait; // Value injected by FXMLLoader

    @FXML // fx:id="Save"
    private Button Save; // Value injected by FXMLLoader

    @FXML // fx:id="privilageField"
    private TextField privilageField; // Value injected by FXMLLoader

    @FXML
    private VBox createAccountSection;

    @FXML
    private TextField createName;

    @FXML
    private TextField createEmail;

    @FXML
    private TextField createPassword;

    @FXML
    private ComboBox<String> createPrivilegeCombo;

    @FXML
    private TextField createPersonId;

    @FXML
    private TextField createPhone;

    @FXML
    private TextArea createAddress;

    @FXML
    private CheckBox createSubscription;

    @FXML
    private TextField createShop;

    @FXML
    private TextField createCardNumber;

    @FXML
    private TextField createCardMonth;

    @FXML
    private TextField createCardYear;

    @FXML
    private TextField createCvv;

    @FXML
    private Button createAccountButton;

    @FXML
    private Label createStatusLabel;

    private final ObservableList<UserRow> allUsers = FXCollections.observableArrayList();
    private final ObservableList<UserRow> filteredUsers = FXCollections.observableArrayList();
    private UserRow selectedUser;
    private boolean createPending;

    @FXML
    void SaveChanges(ActionEvent event) {
        if (!isManager()) {
            navigateToAccessDenied();
            return;
        }

        if (selectedUser == null) {
            showStatus("Please select a user to update.", true);
            return;
        }

        String updateClass = selectedUser.getUpdateClass();
        if ("account".equals(updateClass)) {
            Optional<Account> updatedAccount = buildAccountFromForm();
            if (updatedAccount.isEmpty()) {
                return;
            }
            UpdateMessage updateAcc = new UpdateMessage("account", "edit");
            updateAcc.setAccount(updatedAccount.get());
            pendingUpdatedAccount = updatedAccount.get();
            sendUpdate(updateAcc);
        } else if ("worker".equals(updateClass)) {
            Optional<Worker> updatedWorker = buildWorkerFromForm();
            if (updatedWorker.isEmpty()) {
                return;
            }
            UpdateMessage updateWorker = new UpdateMessage("worker", "edit");
            updateWorker.setWorker(updatedWorker.get());
            pendingUpdatedAccount = updatedWorker.get();
            sendUpdate(updateWorker);
        } else if ("manager".equals(updateClass)) {
            Optional<Manager> updatedManager = buildManagerFromForm();
            if (updatedManager.isEmpty()) {
                return;
            }
            UpdateMessage updateManager = new UpdateMessage("manager", "edit");
            updateManager.setManager(updatedManager.get());
            pendingUpdatedAccount = updatedManager.get();
            sendUpdate(updateManager);
        }
    }

    boolean thisSub = false;
    int thisShop = 0;
    @FXML
    void loadSelectProfile(ActionEvent event) {
        UserRow selection = usersTable.getSelectionModel().getSelectedItem();
        if (selection == null) {
            showStatus("Select a user from the table first.", true);
            return;
        }
        selectedUser = selection;
        loadSelectedUserDetails(selection);
    }

    @FXML
    void selectType(ActionEvent event) {
        applyFilters();
        loadProfile.setVisible(true);
    }

    public List<Account> all_accounts = new ArrayList<>();
    public List<Manager> all_managers = new ArrayList<>();
    public List<Worker> all_workers = new ArrayList<>();
    private Account pendingUpdatedAccount;
    Account currentUser;
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        EventBus.getDefault().register(this);
        assert accID != null : "fx:id=\"accID\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert usersTable != null : "fx:id=\"usersTable\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert address != null : "fx:id=\"address\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert changeDetailsButton != null : "fx:id=\"changeDetailsButton\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert creditexpmonth != null : "fx:id=\"creditexpmonth\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert creditexpyear != null : "fx:id=\"creditexpyear\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert creditnum != null : "fx:id=\"creditnum\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert customerID != null : "fx:id=\"customerID\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert cvv != null : "fx:id=\"cvv\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert frozenToggle != null : "fx:id=\"frozenToggle\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert loadProfile != null : "fx:id=\"loadProfile\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert phone != null : "fx:id=\"phone\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert profileType != null : "fx:id=\"profileType\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert refreshButton != null : "fx:id=\"refreshButton\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert shop != null : "fx:id=\"shop\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert sub != null : "fx:id=\"sub\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert userEmailCol != null : "fx:id=\"userEmailCol\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert userIdCol != null : "fx:id=\"userIdCol\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert userNameCol != null : "fx:id=\"userNameCol\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert userPrivilegeCol != null : "fx:id=\"userPrivilegeCol\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert userRoleCol != null : "fx:id=\"userRoleCol\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert userStatusCol != null : "fx:id=\"userStatusCol\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert statusMessage != null : "fx:id=\"statusMessage\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert wait != null : "fx:id=\"wait\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert Save != null : "fx:id=\"Save\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert privilageField != null : "fx:id=\"privilageField\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createAccountSection != null : "fx:id=\"createAccountSection\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createName != null : "fx:id=\"createName\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createEmail != null : "fx:id=\"createEmail\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createPassword != null : "fx:id=\"createPassword\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createPrivilegeCombo != null : "fx:id=\"createPrivilegeCombo\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createPersonId != null : "fx:id=\"createPersonId\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createPhone != null : "fx:id=\"createPhone\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createAddress != null : "fx:id=\"createAddress\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createSubscription != null : "fx:id=\"createSubscription\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createShop != null : "fx:id=\"createShop\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createCardNumber != null : "fx:id=\"createCardNumber\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createCardMonth != null : "fx:id=\"createCardMonth\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createCardYear != null : "fx:id=\"createCardYear\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createCvv != null : "fx:id=\"createCvv\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createAccountButton != null : "fx:id=\"createAccountButton\" was not injected: check your FXML file 'admincontrol.fxml'.";
        assert createStatusLabel != null : "fx:id=\"createStatusLabel\" was not injected: check your FXML file 'admincontrol.fxml'.";

        if (!isManager()) {
            navigateToAccessDenied();
            return;
        }

        changeDetailsButton.setVisible(true);

        setupTable();

        sub.setEditable(false);

        try {
            SimpleClient.getClient().sendToServer("get Managers");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SimpleClient.getClient().sendToServer("get Workers");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SimpleClient.getClient().sendToServer("get Accounts");
        } catch (IOException e) {
            e.printStackTrace();
        }

        profileType.getItems().add("Customers");
        profileType.getItems().add("Workers");
        profileType.getItems().add("Managers");
        profileType.getItems().add(0, "All");
        profileType.getSelectionModel().selectFirst();
        loadProfile.setVisible(false);

        profileType.setDisable(true);
        loadProfile.setDisable(true);
        Save.setDisable(true);
        refreshButton.setDisable(true);
        statusMessage.setVisible(false);

        setupCreateAccountSection();


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        profileType.setDisable(false);
                        loadProfile.setDisable(false);
                        Save.setDisable(false);
                        refreshButton.setDisable(false);
                        wait.setVisible(false);
                    }
                },4500
        );
    }

    @Subscribe
    public void getManagersOrWorkersFromDB(FoundTable foundTable){
        if(foundTable.getMessage().equals("managers table found")){
            all_managers = foundTable.getRecievedManagers();
            System.out.println("all managers size is " + all_managers.size());
            refreshUsersTable();
        }
        else{
            all_workers = foundTable.getRecievedWokers();
            System.out.println("all workers size is " + all_workers.size());
            refreshUsersTable();
        }
    }
    @Subscribe
    public void getAllAccountFromDB(List<Account> receivedAccounts){
        System.out.println("in getAllAccountFromDB");
        all_accounts = receivedAccounts;
        System.out.println("all accounts size is " + all_accounts.size());
        refreshUsersTable();
    }

    @Subscribe
    public void handleUserUpdateResponse(UserUpdateResponse response) {
        if (createPending) {
            createPending = false;
            if (response.isSuccess()) {
                showCreateStatus("Account created successfully.", false);
                clearCreateForm();
                requestUserRefresh();
            } else {
                showCreateStatus(response.getErrorMessage() != null ? response.getErrorMessage() : "Failed to create account.", true);
            }
            return;
        }

        if (response.isSuccess()) {
            showStatus("User details updated successfully.", false);
            requestUserRefresh();
            syncSessionAccount(pendingUpdatedAccount);
            pendingUpdatedAccount = null;
            EventBus.getDefault().post(new CatalogRefreshEvent("privilege-updated"));
        } else {
            showStatus(response.getErrorMessage() != null ? response.getErrorMessage() : "Failed to update user.", true);
            pendingUpdatedAccount = null;
        }
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        if (!isChainManager()) {
            showCreateStatus("Only chain managers can create new accounts.", true);
            return;
        }
        String privilegeSelection = createPrivilegeCombo.getValue();
        Integer privilege = parsePrivilegeSelection(privilegeSelection);
        if (privilege == null) {
            showCreateStatus("Please select a privilege level.", true);
            return;
        }
        if (createName.getText().isBlank() || createEmail.getText().isBlank() || createPassword.getText().isBlank()) {
            showCreateStatus("Please fill in name, email, and password.", true);
            return;
        }
        UpdateMessage message;
        if (privilege == 1) {
            Long personId = parseCreateLong(createPersonId.getText(), "Government ID", true);
            Long phoneValue = parseCreateLong(createPhone.getText(), "Phone", false);
            Long cardNumber = parseCreateLong(createCardNumber.getText(), "Card Number", false);
            Integer cardMonth = parseCreateInt(createCardMonth.getText(), "Expiry Month", false);
            Integer cardYear = parseCreateInt(createCardYear.getText(), "Expiry Year", false);
            Integer cvvValue = parseCreateInt(createCvv.getText(), "CVV", false);
            if (personId == null) {
                return;
            }
            Account account = new Account(0, createName.getText(), personId,
                    createAddress.getText(), createEmail.getText(), createPassword.getText(),
                    phoneValue != null ? phoneValue : 0L,
                    cardNumber != null ? cardNumber : 0L,
                    cardMonth != null ? cardMonth : 0,
                    cardYear != null ? cardYear : 0,
                    cvvValue != null ? cvvValue : 0,
                    false,
                    parseShopId(createShop.getText(), 0),
                    createSubscription.isSelected());
            account.setPrivialge(privilege);
            message = new UpdateMessage("account", "admin_add");
            message.setAccount(account);
        } else if (privilege == 2) {
            Worker worker = new Worker(createName.getText(), createEmail.getText(), createPassword.getText(), 0);
            worker.setPrivialge(privilege);
            message = new UpdateMessage("worker", "add");
            message.setWorker(worker);
        } else {
            Manager manager = new Manager(createName.getText(), createEmail.getText(), createPassword.getText(), 0);
            manager.setPrivialge(privilege);
            manager.setShopID(parseShopId(createShop.getText(), privilege >= 4 ? 0 : 0));
            message = new UpdateMessage("manager", "add");
            message.setManager(manager);
        }

        try {
            createPending = true;
            SimpleClient.getClient().sendToServer(message);
            showCreateStatus("Submitting account creation...", false);
        } catch (IOException e) {
            createPending = false;
            showCreateStatus("Failed to send create request.", true);
        }
    }

    private void syncSessionAccount(Account updatedAccount) {
        if (updatedAccount == null) {
            return;
        }
        Account session = SimpleClient.getAccount();
        if (session == null) {
            return;
        }
        boolean matches = session.getAccountID() == updatedAccount.getAccountID();
        if (!matches && session.getPersonID() == updatedAccount.getPersonID()) {
            matches = true;
        }
        if (!matches && session.getEmail() != null && updatedAccount.getEmail() != null) {
            matches = session.getEmail().equalsIgnoreCase(updatedAccount.getEmail());
        }
        if (matches) {
            session.setPrivialge(updatedAccount.getPrivialge());
            session.setFrozen(updatedAccount.isFrozen());
            SimpleClient.setAccount(session);
        }
    }

    @Subscribe
    public void PassAccountEvent(PassAccountEventAdmin passAcc){ // added 30/7
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

    @FXML
    void handleSearch(ActionEvent event) {
        applyFilters();
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        requestUserRefresh();
        showStatus("Refreshing user list...", false);
    }

    private void setupTable() {
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        userPrivilegeCol.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        userStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        usersTable.setItems(filteredUsers);
        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedUser = newSelection;
                loadSelectedUserDetails(newSelection);
            }
        });
    }

    private void refreshUsersTable() {
        allUsers.clear();
        List<String> workerEmails = all_workers.stream()
                .map(worker -> normalizeEmail(worker.getEmail()))
                .filter(email -> !email.isBlank())
                .toList();
        List<String> managerEmails = all_managers.stream()
                .map(manager -> normalizeEmail(manager.getEmail()))
                .filter(email -> !email.isBlank())
                .toList();
        java.util.Map<String, UserRow> rowsByKey = new java.util.LinkedHashMap<>();
        for (Account account : all_accounts) {
            if (!isCustomerAccount(account, workerEmails, managerEmails)) {
                continue;
            }
            String key = resolveRowKey(account.getEmail(), String.valueOf(account.getAccountID()));
            UserRow row = UserRow.fromAccountWithCategory(account, resolveCategoryFromPrivilege(account.getPrivialge()));
            rowsByKey.put(key, row);
        }
        for (Worker worker : all_workers) {
            String key = resolveRowKey(worker.getEmail(), String.valueOf(worker.getPersonID()));
            rowsByKey.put(key, UserRow.fromWorker(worker));
        }
        for (Manager manager : all_managers) {
            String key = resolveRowKey(manager.getEmail(), String.valueOf(manager.getPersonID()));
            rowsByKey.put(key, UserRow.fromManager(manager));
        }
        allUsers.addAll(rowsByKey.values());
        applyFilters();
    }

    private void applyFilters() {
        filteredUsers.clear();
        String searchText = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        String roleFilter = profileType.getSelectionModel().getSelectedItem();

        for (UserRow row : allUsers) {
            if (roleFilter != null && !"All".equals(roleFilter) && !roleFilter.equals(row.getCategory())) {
                continue;
            }
            if (!searchText.isBlank() && !row.matches(searchText)) {
                continue;
            }
            filteredUsers.add(row);
        }
        loadProfile.setVisible(true);
    }

    private boolean isCustomerAccount(Account account, List<String> workerEmails, List<String> managerEmails) {
        if (account == null) {
            return false;
        }
        String email = normalizeEmail(account.getEmail());
        if (!email.isBlank() && (workerEmails.contains(email) || managerEmails.contains(email))) {
            return false;
        }
        return account.getPrivialge() < 2 || !email.isBlank();
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private String resolveCategoryFromPrivilege(int privilege) {
        if (privilege >= 3) {
            return "Managers";
        }
        if (privilege >= 2) {
            return "Workers";
        }
        return "Customers";
    }

    private String resolveRowKey(String email, String fallbackId) {
        String normalized = normalizeEmail(email);
        if (!normalized.isBlank()) {
            return "email:" + normalized;
        }
        return "id:" + fallbackId;
    }

    private void loadCustomer(Account selectedAcc) {
        if (selectedAcc == null) {
            return;
        }
        selectedUser.setAccount(selectedAcc);
        accID.setText(Integer.toString(selectedAcc.getAccountID()));
        customerID.setText(Long.toString(selectedAcc.getID()));
        name.setText(selectedAcc.getFullName());
        address.setText(selectedAcc.getAddress());
        email.setText(selectedAcc.getEmail());
        password.setText(selectedAcc.getPassword());
        phone.setText(Long.toString(selectedAcc.getPhoneNumber()));
        creditnum.setText(Long.toString(selectedAcc.getCreditCardNumber()));
        creditexpmonth.setText(Integer.toString(selectedAcc.getCreditMonthExpire()));
        creditexpyear.setText(Integer.toString(selectedAcc.getCreditYearExpire()));
        cvv.setText(Integer.toString(selectedAcc.getCcv()));
        privilageField.setText(Integer.toString(selectedAcc.getPrivialge()));
        frozenToggle.setSelected(selectedAcc.isFrozen());
        shop.setText(resolveShopName(selectedAcc.getBelongShop()));
        thisShop = selectedAcc.getBelongShop();
        if (selectedAcc.isSubscription()) {
            thisSub = true;
            sub.setText("Yes");
        } else {
            thisSub = false;
            sub.setText("No");
        }
        setCustomerFieldsEditable(true);
    }

    private void loadWorker(Worker selectedWork) {
        if (selectedWork == null) {
            return;
        }
        selectedUser.setWorker(selectedWork);
        accID.setText(String.valueOf(selectedWork.getPersonID()));
        email.setText(selectedWork.getEmail());
        name.setText(selectedWork.getFullName());
        password.setText(selectedWork.getPassword());
        customerID.setText(String.valueOf(selectedWork.getPersonID()));
        privilageField.setText(Integer.toString(selectedWork.getPrivialge()));
        frozenToggle.setSelected(selectedWork.isFrozen());
        address.clear();
        phone.clear();
        creditnum.clear();
        creditexpmonth.clear();
        creditexpyear.clear();
        cvv.clear();
        shop.setText("");
        sub.setText("—");
        setCustomerFieldsEditable(false);
    }

    private void loadManager(Manager selectedMan) {
        if (selectedMan == null) {
            return;
        }
        selectedUser.setManager(selectedMan);
        customerID.setText(String.valueOf(selectedMan.getPersonID()));
        email.setText(selectedMan.getEmail());
        name.setText(selectedMan.getFullName());
        password.setText(selectedMan.getPassword());
        privilageField.setText(Integer.toString(selectedMan.getPrivialge()));
        frozenToggle.setSelected(selectedMan.isFrozen());
        address.clear();
        phone.clear();
        creditnum.clear();
        creditexpmonth.clear();
        creditexpyear.clear();
        cvv.clear();
        shop.setText(resolveShopName(selectedMan.getShopID()));
        thisShop = selectedMan.getShopID();
        sub.setText("—");
        setCustomerFieldsEditable(false);
        shop.setDisable(false);
    }

    private void setCustomerFieldsEditable(boolean enabled) {
        address.setDisable(!enabled);
        phone.setDisable(!enabled);
        creditnum.setDisable(!enabled);
        creditexpmonth.setDisable(!enabled);
        creditexpyear.setDisable(!enabled);
        cvv.setDisable(!enabled);
        sub.setDisable(true);
        shop.setDisable(!enabled);
    }

    private Optional<Account> buildAccountFromForm() {
        String validationError = validateRequiredFields();
        if (validationError != null) {
            showStatus(validationError, true);
            return Optional.empty();
        }
        Integer accountId = parseIntField(accID.getText(), "Account ID");
        Long userId = parseLongField(customerID.getText(), "Customer ID");
        Long phoneValue = parseLongField(phone.getText(), "Phone");
        Long creditNumberValue = parseLongField(creditnum.getText(), "Credit Card #");
        Integer expMonthValue = parseIntField(creditexpmonth.getText(), "Expiry Month");
        Integer expYearValue = parseIntField(creditexpyear.getText(), "Expiry Year");
        Integer cvvValue = parseIntField(cvv.getText(), "CVV");
        Integer privilegeValue = parseIntField(privilageField.getText(), "Privilege");
        if (accountId == null || userId == null || phoneValue == null || creditNumberValue == null
                || expMonthValue == null || expYearValue == null || cvvValue == null || privilegeValue == null) {
            return Optional.empty();
        }
        boolean loggedValue = selectedUser != null && selectedUser.getAccount() != null
                ? Boolean.TRUE.equals(selectedUser.getAccount().getLoggedIn())
                : false;
        Account account = new Account(accountId, name.getText(), userId, address.getText(),
                email.getText(), password.getText(), phoneValue, creditNumberValue, expMonthValue,
                expYearValue, cvvValue, loggedValue, parseShopId(shop.getText(), thisShop), thisSub);
        account.setPrivialge(privilegeValue);
        account.setFrozen(frozenToggle.isSelected());
        return Optional.of(account);
    }

    private Optional<Worker> buildWorkerFromForm() {
        String validationError = validateWorkerManagerFields();
        if (validationError != null) {
            showStatus(validationError, true);
            return Optional.empty();
        }
        Integer workerId = parseIntField(customerID.getText(), "Worker ID");
        Integer privilegeValue = parseIntField(privilageField.getText(), "Privilege");
        if (workerId == null || privilegeValue == null) {
            return Optional.empty();
        }
        Worker worker = new Worker(name.getText(), email.getText(), password.getText(), workerId);
        worker.setPersonID(workerId);
        worker.setPrivialge(privilegeValue);
        worker.setFrozen(frozenToggle.isSelected());
        if (selectedUser != null && selectedUser.getWorker() != null) {
            worker.setLoggedIn(selectedUser.getWorker().getLoggedIn());
        }
        return Optional.of(worker);
    }

    private Optional<Manager> buildManagerFromForm() {
        String validationError = validateWorkerManagerFields();
        if (validationError != null) {
            showStatus(validationError, true);
            return Optional.empty();
        }
        Integer managerId = parseIntField(customerID.getText(), "Manager ID");
        Integer privilegeValue = parseIntField(privilageField.getText(), "Privilege");
        if (managerId == null || privilegeValue == null) {
            return Optional.empty();
        }
        Manager manager = new Manager(name.getText(), email.getText(), password.getText(), managerId);
        manager.setPersonID(managerId);
        manager.setPrivialge(privilegeValue);
        manager.setFrozen(frozenToggle.isSelected());
        if (selectedUser != null && selectedUser.getManager() != null) {
            manager.setLoggedIn(selectedUser.getManager().getLoggedIn());
        }
        manager.setShopID(parseShopId(shop.getText(), thisShop));
        return Optional.of(manager);
    }

    private void loadSelectedUserDetails(UserRow selection) {
        if ("account".equals(selection.getUpdateClass())) {
            loadCustomer(selection.getAccount());
        } else if ("worker".equals(selection.getUpdateClass())) {
            loadWorker(selection.getWorker());
        } else if ("manager".equals(selection.getUpdateClass())) {
            loadManager(selection.getManager());
        }
    }

    private String validateRequiredFields() {
        if (accID.getText().isBlank() || customerID.getText().isBlank() || name.getText().isBlank()
                || email.getText().isBlank() || password.getText().isBlank()) {
            return "Please fill in all required fields (ID, name, email, password).";
        }
        return null;
    }

    private String validateWorkerManagerFields() {
        if (customerID.getText().isBlank() || name.getText().isBlank() || email.getText().isBlank()
                || password.getText().isBlank()) {
            return "Please fill in all required fields (ID, name, email, password).";
        }
        return null;
    }

    private Integer parseIntField(String value, String label) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ex) {
            showStatus("Invalid " + label + " value.", true);
            return null;
        }
    }

    private Long parseLongField(String value, String label) {
        try {
            return Long.parseLong(value.trim());
        } catch (Exception ex) {
            showStatus("Invalid " + label + " value.", true);
            return null;
        }
    }

    private int parseShopId(String text, int fallback) {
        if (text == null || text.isBlank()) {
            return fallback;
        }
        String digits = text.replaceAll("\\D+", "");
        if (digits.isBlank()) {
            return fallback;
        }
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private String resolveShopName(int shopId) {
        switch (shopId) {
            case 0:
                return "ID 0: - Chain";
            case 1:
                return "Tiberias, Big Danilof";
            case 2:
                return "Haifa, Merkaz Zeiv";
            case 3:
                return "Tel Aviv, Ramat Aviv";
            case 4:
                return "Eilat, Ice mall";
            case 5:
                return "Be'er Sheva, Big Beer Sheva";
            default:
                return String.valueOf(shopId);
        }
    }

    private void sendUpdate(UpdateMessage message) {
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            showStatus("Unable to send update to server.", true);
        }
    }

    private void showStatus(String message, boolean isError) {
        statusMessage.setText(message);
        statusMessage.setStyle(isError ? "-fx-text-fill: #c62828;" : "-fx-text-fill: #2e7d32;");
        statusMessage.setVisible(true);
    }

    private boolean isManager() {
        Account account = SimpleClient.getAccount();
        return account != null && account.getPrivilegeLevel() >= 3;
    }

    private boolean isChainManager() {
        Account account = SimpleClient.getAccount();
        return account != null && account.getPrivilegeLevel() >= 4;
    }

    private void setupCreateAccountSection() {
        boolean showCreate = isChainManager();
        createAccountSection.setVisible(showCreate);
        createAccountSection.setManaged(showCreate);
        createPrivilegeCombo.getItems().setAll(
                "Customer (1)",
                "Worker (2)",
                "Manager (3)",
                "Chain Manager (4)"
        );
        createPrivilegeCombo.setValue("Customer (1)");
        createStatusLabel.setVisible(false);
    }

    private Integer parsePrivilegeSelection(String selection) {
        if (selection == null) {
            return null;
        }
        String digits = selection.replaceAll("\\D+", "");
        if (digits.isBlank()) {
            return null;
        }
        return Integer.parseInt(digits);
    }

    private Long parseCreateLong(String value, String label, boolean required) {
        if (value == null || value.isBlank()) {
            if (required) {
                showCreateStatus("Please enter a valid " + label + ".", true);
            }
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException ex) {
            showCreateStatus("Invalid " + label + " value.", true);
            return null;
        }
    }

    private Integer parseCreateInt(String value, String label, boolean required) {
        if (value == null || value.isBlank()) {
            if (required) {
                showCreateStatus("Please enter a valid " + label + ".", true);
            }
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            showCreateStatus("Invalid " + label + " value.", true);
            return null;
        }
    }

    private void showCreateStatus(String message, boolean isError) {
        createStatusLabel.setText(message);
        createStatusLabel.setStyle(isError ? "-fx-text-fill: #c62828;" : "-fx-text-fill: #2e7d32;");
        createStatusLabel.setVisible(true);
    }

    private void clearCreateForm() {
        createName.clear();
        createEmail.clear();
        createPassword.clear();
        createPersonId.clear();
        createPhone.clear();
        createAddress.clear();
        createShop.clear();
        createCardNumber.clear();
        createCardMonth.clear();
        createCardYear.clear();
        createCvv.clear();
        createSubscription.setSelected(false);
        createPrivilegeCombo.setValue("Customer (1)");
    }

    private void navigateToAccessDenied() {
        int currentPrivilege = 0;
        Account account = SimpleClient.getAccount();
        if (account != null) {
            currentPrivilege = account.getPrivilegeLevel();
        }
        AccessDeniedController.setAccessInfo(currentPrivilege, 3, "Change Details");
        NavigationService.getInstance().navigate("AccessDenied");
    }

    private void requestUserRefresh() {
        try {
            SimpleClient.getClient().sendToServer("get Managers");
            SimpleClient.getClient().sendToServer("get Workers");
            SimpleClient.getClient().sendToServer("get Accounts");
        } catch (IOException e) {
            showStatus("Failed to refresh user list from server.", true);
        }
    }

    public static class UserRow {
        private final String userId;
        private final String name;
        private final String email;
        private final String role;
        private final String status;
        private final int privilege;
        private final String category;
        private final String updateClass;
        private Account account;
        private Worker worker;
        private Manager manager;

        private UserRow(String userId, String name, String email, String role, String status, int privilege, String category, String updateClass) {
            this.userId = userId;
            this.name = name == null ? "" : name;
            this.email = email == null ? "" : email;
            this.role = role;
            this.status = status;
            this.privilege = privilege;
            this.category = category;
            this.updateClass = updateClass;
        }

        public static UserRow fromAccountWithCategory(Account account, String category) {
            String role = "Customers".equals(category) ? "Customer" : category.substring(0, category.length() - 1);
            UserRow row = new UserRow(String.valueOf(account.getAccountID()), account.getFullName(),
                    account.getEmail(), role, account.isFrozen() ? "Frozen" : "Active",
                    account.getPrivialge(), category, "account");
            row.setAccount(account);
            return row;
        }

        public static UserRow fromWorker(Worker worker) {
            UserRow row = new UserRow(String.valueOf(worker.getPersonID()), worker.getFullName(),
                    worker.getEmail(), "Worker", worker.isFrozen() ? "Frozen" : "Active",
                    worker.getPrivialge(), "Workers", "worker");
            row.setWorker(worker);
            return row;
        }

        public static UserRow fromManager(Manager manager) {
            UserRow row = new UserRow(String.valueOf(manager.getPersonID()), manager.getFullName(),
                    manager.getEmail(), "Manager", manager.isFrozen() ? "Frozen" : "Active",
                    manager.getPrivialge(), "Managers", "manager");
            row.setManager(manager);
            return row;
        }

        public boolean matches(String query) {
            String q = query.toLowerCase(Locale.ROOT);
            return userId.toLowerCase(Locale.ROOT).contains(q)
                    || name.toLowerCase(Locale.ROOT).contains(q)
                    || email.toLowerCase(Locale.ROOT).contains(q);
        }

        public String getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getStatus() { return status; }
        public int getPrivilege() { return privilege; }
        public String getCategory() { return category; }
        public String getUpdateClass() { return updateClass; }
        public Account getAccount() { return account; }
        public Worker getWorker() { return worker; }
        public Manager getManager() { return manager; }
        public void setAccount(Account account) { this.account = account; }
        public void setWorker(Worker worker) { this.worker = worker; }
        public void setManager(Manager manager) { this.manager = manager; }
    }

}
