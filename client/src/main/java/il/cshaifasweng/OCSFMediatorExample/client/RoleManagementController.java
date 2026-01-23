package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.util.Random;

public class RoleManagementController {

    @FXML private Button backButton;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> roleFilterCombo;
    @FXML private ComboBox<String> branchFilterCombo;
    @FXML private Button searchButton;
    @FXML private TableView<UserData> usersTable;
    @FXML private TableColumn<UserData, Integer> userIdCol;
    @FXML private TableColumn<UserData, String> userNameCol;
    @FXML private TableColumn<UserData, String> userEmailCol;
    @FXML private TableColumn<UserData, String> userBranchCol;
    @FXML private TableColumn<UserData, String> userRoleCol;
    @FXML private TableColumn<UserData, Integer> userPrivilegeCol;
    @FXML private TableColumn<UserData, String> userSubscriptionCol;
    @FXML private TableColumn<UserData, String> userStatusCol;
    @FXML private TableColumn<UserData, Void> userActionsCol;
    @FXML private Button refreshButton;
    @FXML private Label totalUsersLabel;
    @FXML private Label customersLabel;
    @FXML private Label workersLabel;
    @FXML private Label managersLabel;
    
    private ObservableList<UserData> usersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (!AccessGuard.requireMinPrivilege(4)) {
            return;
        }
        setupFilters();
        setupTable();
        loadUsers();
        updateStatistics();
    }

    private void setupFilters() {
        ObservableList<String> roles = FXCollections.observableArrayList(
            "All Roles", "Guest (0)", "Customer (1)", "Worker (2)", 
            "Manager (3)", "Chain Manager (4)"
        );
        roleFilterCombo.setItems(roles);
        roleFilterCombo.setValue("All Roles");
        
        ObservableList<String> branches = FXCollections.observableArrayList(
            "All Branches", "Main Street", "Downtown", "Shopping Mall", "Airport", "University"
        );
        branchFilterCombo.setItems(branches);
        branchFilterCombo.setValue("All Branches");
    }

    private void setupTable() {
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userBranchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));
        userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        userPrivilegeCol.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        userSubscriptionCol.setCellValueFactory(new PropertyValueFactory<>("subscription"));
        userStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        addActionButtons();
        usersTable.setItems(usersList);
    }

    private void addActionButtons() {
        userActionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button changeRoleBtn = new Button("Change Role");
            private final Button toggleStatusBtn = new Button("Toggle Status");
            private final HBox pane = new HBox(5, changeRoleBtn, toggleStatusBtn);
            {
                changeRoleBtn.setStyle("-fx-background-color: #ba68c8; -fx-text-fill: white; -fx-font-size: 11px;");
                toggleStatusBtn.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-size: 11px;");
                changeRoleBtn.setOnAction(e -> handleChangeRole(getTableView().getItems().get(getIndex())));
                toggleStatusBtn.setOnAction(e -> handleToggleStatus(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadUsers() {
        usersList.clear();
        String[] names = {"John Doe", "Sarah Cohen", "David Levi", "Rachel Green", "Michael Brown", 
                         "Emma Wilson", "Daniel Miller", "Lisa Davis", "Tom Anderson", "Anna Taylor"};
        String[] branches = {"Main Street", "Downtown", "Shopping Mall", "Airport", "University"};
        String[] roles = {"Customer", "Worker", "Manager", "Customer", "Worker", 
                         "Customer", "Manager", "Worker", "Customer", "Chain Manager"};
        int[] privileges = {1, 2, 3, 1, 2, 1, 3, 2, 1, 4};
        Random r = new Random();
        
        for (int i = 0; i < 10; i++) {
            usersList.add(new UserData(
                i + 1, names[i], names[i].toLowerCase().replace(" ", ".") + "@email.com",
                branches[r.nextInt(branches.length)], roles[i], privileges[i],
                r.nextBoolean() ? "Yes" : "No", "Active"
            ));
        }
    }

    @FXML
    private void handleSearch() {
        // TODO: Implement search/filter logic
        showInfo("Search functionality will filter users based on criteria.");
    }

    private void handleChangeRole(UserData user) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Customer (1)", 
            "Guest (0)", "Customer (1)", "Worker (2)", "Manager (3)", "Chain Manager (4)");
        dialog.setTitle("Change User Role");
        // UserData defines getName(); there is no getFullName() method.
        dialog.setHeaderText("Change role for: " + user.getName());
        dialog.setContentText("Select new role:");
        
        dialog.showAndWait().ifPresent(role -> {
            // TODO: Send to server
            showSuccess("Role updated to " + role + " for " + user.getName());
            loadUsers();
            updateStatistics();
        });
    }

    private void handleToggleStatus(UserData user) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Toggle Status");
        // Use getName() instead of getFullName().
        confirm.setContentText("Toggle status for " + user.getName() + "?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                // TODO: Send to server
                showSuccess("Status toggled for " + user.getName());
                loadUsers();
            }
        });
    }

    @FXML
    private void handleRefresh() {
        loadUsers();
        updateStatistics();
        showInfo("User list refreshed.");
    }

    private void updateStatistics() {
        totalUsersLabel.setText(String.valueOf(usersList.size()));
        long customers = usersList.stream().filter(u -> u.getPrivilege() == 1).count();
        long workers = usersList.stream().filter(u -> u.getPrivilege() == 2).count();
        long managers = usersList.stream().filter(u -> u.getPrivilege() >= 3).count();
        customersLabel.setText(String.valueOf(customers));
        workersLabel.setText(String.valueOf(workers));
        managersLabel.setText(String.valueOf(managers));
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            App.setRoot("NetworkDashboard");
        } catch (IOException e) {
            showError("Failed to navigate.");
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static class UserData {
        private final int id, privilege;
        private final String name, email, branch, role, subscription, status;

        public UserData(int id, String name, String email, String branch, String role,
                       int privilege, String subscription, String status) {
            this.id = id; this.name = name; this.email = email; this.branch = branch;
            this.role = role; this.privilege = privilege;
            this.subscription = subscription; this.status = status;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getBranch() { return branch; }
        public String getRole() { return role; }
        public int getPrivilege() { return privilege; }
        public String getSubscription() { return subscription; }
        public String getStatus() { return status; }
    }
}
