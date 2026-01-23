package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.time.LocalDate;
import java.util.Random;

public class NetworkPromotionsController {

    @FXML private Label formTitleLabel;
    @FXML private TextField promotionNameField;
    @FXML private TextField discountField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> applyToCombo;
    @FXML private CheckBox allBranchesCheckbox;
    @FXML private CheckBox activeCheckbox;
    @FXML private Button saveButton;
    @FXML private Button clearButton;
    @FXML private TableView<PromotionData> promotionsTable;
    @FXML private TableColumn<PromotionData, Integer> idCol;
    @FXML private TableColumn<PromotionData, String> nameCol;
    @FXML private TableColumn<PromotionData, Double> discountCol;
    @FXML private TableColumn<PromotionData, String> startDateCol;
    @FXML private TableColumn<PromotionData, String> endDateCol;
    @FXML private TableColumn<PromotionData, String> applyToCol;
    @FXML private TableColumn<PromotionData, String> branchesCol;
    @FXML private TableColumn<PromotionData, String> statusCol;
    @FXML private TableColumn<PromotionData, Void> actionsCol;
    @FXML private Button refreshButton;
    @FXML private Label activeCampaignsLabel;
    @FXML private Label productsOnSaleLabel;
    @FXML private Label salesIncreaseLabel;
    
    private ObservableList<PromotionData> promotionsList = FXCollections.observableArrayList();
    private PromotionData editingPromotion = null;

    @FXML
    public void initialize() {
        if (!AccessGuard.requireMinPrivilege(4)) {
            return;
        }
        setupApplyToCombo();
        setupTable();
        loadPromotions();
        updateStatistics();
    }

    private void setupApplyToCombo() {
        ObservableList<String> options = FXCollections.observableArrayList(
            "All Products", "Category: Roses", "Category: Tulips", 
            "Category: Orchids", "Category: Mixed Bouquets"
        );
        applyToCombo.setItems(options);
        applyToCombo.setValue("All Products");
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        applyToCol.setCellValueFactory(new PropertyValueFactory<>("applyTo"));
        branchesCol.setCellValueFactory(new PropertyValueFactory<>("branches"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        addActionButtons();
        promotionsTable.setItems(promotionsList);
    }

    private void addActionButtons() {
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);
            {
                editBtn.setStyle("-fx-background-color: #ba68c8; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                editBtn.setOnAction(e -> handleEdit(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(e -> handleDelete(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadPromotions() {
        promotionsList.clear();
        promotionsList.add(new PromotionData(1, "Valentine's Day Network Sale", 25.0, 
            "2025-02-10", "2025-02-14", "Category: Roses", "All (5)", "Active"));
        promotionsList.add(new PromotionData(2, "Spring Collection Discount", 15.0, 
            "2025-03-01", "2025-03-31", "All Products", "All (5)", "Active"));
    }

    @FXML
    private void handleSavePromotion() {
        if (!validateForm()) return;
        
        // TODO: Send to server
        showSuccess("Network promotion created successfully!");
        handleClear();
        loadPromotions();
        updateStatistics();
    }

    private boolean validateForm() {
        if (promotionNameField.getText().trim().isEmpty()) {
            showError("Please enter a campaign name.");
            return false;
        }
        try {
            double discount = Double.parseDouble(discountField.getText().trim());
            if (discount <= 0 || discount > 100) {
                showError("Discount must be between 0 and 100.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid discount percentage.");
            return false;
        }
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showError("Please select both dates.");
            return false;
        }
        return true;
    }

    @FXML
    private void handleClear() {
        editingPromotion = null;
        formTitleLabel.setText("Create Network-Wide Promotion");
        promotionNameField.clear();
        discountField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        applyToCombo.setValue("All Products");
        allBranchesCheckbox.setSelected(true);
        activeCheckbox.setSelected(true);
    }

    private void handleEdit(PromotionData promo) {
        editingPromotion = promo;
        formTitleLabel.setText("Edit Promotion (ID: " + promo.getId() + ")");
        // Use getName() and getDiscount() as PromotionData does not define getPromotionName().
        promotionNameField.setText(promo.getName());
        discountField.setText(String.valueOf(promo.getDiscount()));
    }

    private void handleDelete(PromotionData promo) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Promotion");
        // PromotionData does not define getPromotionName(); use getName()
        confirm.setContentText("Delete " + promo.getName() + "?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                promotionsList.remove(promo);
                updateStatistics();
                showSuccess("Promotion deleted.");
            }
        });
    }

    @FXML
    private void handleRefresh() {
        loadPromotions();
        updateStatistics();
        showInfo("Promotions refreshed.");
    }

    private void updateStatistics() {
        long active = promotionsList.stream().filter(p -> "Active".equals(p.getStatus())).count();
        activeCampaignsLabel.setText(String.valueOf(active));
        productsOnSaleLabel.setText(String.valueOf(active * 50));
        Random r = new Random();
        salesIncreaseLabel.setText(String.format("+%.1f%%", 10 + r.nextDouble() * 15));
    }

    @FXML
    private void handleBackToDashboard() {
        NavigationService.getInstance().navigate("NetworkDashboard");
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

    public static class PromotionData {
        private final int id;
        private final String name, startDate, endDate, applyTo, branches, status;
        private final double discount;

        public PromotionData(int id, String name, double discount, String startDate,
                            String endDate, String applyTo, String branches, String status) {
            this.id = id; this.name = name; this.discount = discount;
            this.startDate = startDate; this.endDate = endDate;
            this.applyTo = applyTo; this.branches = branches; this.status = status;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public double getDiscount() { return discount; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getApplyTo() { return applyTo; }
        public String getBranches() { return branches; }
        public String getStatus() { return status; }
    }
}
