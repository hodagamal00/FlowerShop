package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;

// Removed unused AWT imports.  Including AWT packages alongside JavaFX
// introduces ambiguous references for classes like Button and List.  This
// controller uses JavaFX exclusively, so AWT imports are unnecessary and
// problematic.
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
// Added for detailed product navigation
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

// Event to pass current account to complaint controller


public class CatalogController {
	public int flowersnum2 = 6;
	public int workernum2 =0;
	public int managernum2 =0;
	static boolean returnedFromSecondaryController = false;
	boolean firstRun = true;
	@FXML // fx:id="worker_edit"
	private ComboBox<String> worker_edit; // Value injected by FXMLLoader

	@FXML // Filter ComboBoxes
	private ComboBox<String> categoryFilter;

	@FXML
	private ComboBox<String> colorFilter;

	@FXML
	private ComboBox<String> priceFilter;

	@FXML // fx:id="logout"
	private Button logout; // Value injected by FXMLLoader



	@FXML
	private Button checkout;


	@FXML
	private VBox container1;

	@FXML
	private VBox container2;

	@FXML
	private VBox container3;

	@FXML
	private VBox container4;

	@FXML
	private VBox container5;

	@FXML
	private VBox container6;

	@FXML
	private VBox init_container;

	@FXML
	private VBox accountToolsPanel;

	@FXML
	private TextField customid;

	@FXML
	public Button infoo;


	@FXML // fx:id="flower_button1"
	private ImageView flower_button1; // Value injected by FXMLLoader

	@FXML // fx:id="flower_button2"
	private ImageView flower_button2; // Value injected by FXMLLoader

	@FXML // fx:id="flower_button3"
	private ImageView flower_button3; // Value injected by FXMLLoader

	@FXML // fx:id="flower_button4"
	private ImageView flower_button4; // Value injected by FXMLLoader

	@FXML // fx:id="flower_button5"
	private ImageView flower_button5; // Value injected by FXMLLoader

	@FXML // fx:id="flower_button6"
	private ImageView flower_button6; // Value injected by FXMLLoader

	@FXML // fx:id="flower_name1"
	private javafx.scene.control.Label flower_name1; // Value injected by FXMLLoader

	@FXML // fx:id="flower_name2"
	private javafx.scene.control.Label flower_name2; // Value injected by FXMLLoader

	@FXML // fx:id="flower_name3"
	private javafx.scene.control.Label flower_name3; // Value injected by FXMLLoader

	@FXML // fx:id="flower_name4"
	private javafx.scene.control.Label flower_name4; // Value injected by FXMLLoader

	@FXML // fx:id="flower_name5"
	private javafx.scene.control.Label flower_name5; // Value injected by FXMLLoader

	@FXML // fx:id="flower_name6"
	private javafx.scene.control.Label flower_name6; // Value injected by FXMLLoader

	@FXML
	private javafx.scene.control.Label flower_sku1;

	@FXML
	private javafx.scene.control.Label flower_sku2;

	@FXML
	private javafx.scene.control.Label flower_sku3;

	@FXML
	private javafx.scene.control.Label flower_sku4;

	@FXML
	private javafx.scene.control.Label flower_sku5;

	@FXML
	private javafx.scene.control.Label flower_sku6;

	@FXML
	private javafx.scene.control.Label flower_category1;

	@FXML
	private javafx.scene.control.Label flower_category2;

	@FXML
	private javafx.scene.control.Label flower_category3;

	@FXML
	private javafx.scene.control.Label flower_category4;

	@FXML
	private javafx.scene.control.Label flower_category5;

	@FXML
	private javafx.scene.control.Label flower_category6;

	@FXML
	private javafx.scene.control.Label flower_color1;

	@FXML
	private javafx.scene.control.Label flower_color2;

	@FXML
	private javafx.scene.control.Label flower_color3;

	@FXML
	private javafx.scene.control.Label flower_color4;

	@FXML
	private javafx.scene.control.Label flower_color5;

	@FXML
	private javafx.scene.control.Label flower_color6;

	@FXML // fx:id="flower_price1"
	private javafx.scene.control.Label flower_price1; // Value injected by FXMLLoader

	@FXML // fx:id="flower_price2"
	private javafx.scene.control.Label flower_price2; // Value injected by FXMLLoader

	@FXML // fx:id="flower_price3"
	private javafx.scene.control.Label flower_price3; // Value injected by FXMLLoader

	@FXML // fx:id="flower_price4"
	private javafx.scene.control.Label flower_price4; // Value injected by FXMLLoader

	@FXML // fx:id="flower_price5"
	private javafx.scene.control.Label flower_price5; // Value injected by FXMLLoader

	@FXML // fx:id="flower_price6"
	private javafx.scene.control.Label flower_price6; // Value injected by FXMLLoader

	@FXML
	private Label flower_promo1;

	@FXML
	private Label flower_promo2;

	@FXML
	private Label flower_promo3;

	@FXML
	private Label flower_promo4;

	@FXML
	private Label flower_promo5;

	@FXML
	private Label flower_promo6;

	@FXML
	private Label flower_price_before1;

	@FXML
	private Label flower_price_before2;

	@FXML
	private Label flower_price_before3;

	@FXML
	private Label flower_price_before4;

	@FXML
	private Label flower_price_before5;

	@FXML
	private Label flower_price_before6;

	@FXML
	private Label flower_price_after1;

	@FXML
	private Label flower_price_after2;

	@FXML
	private Label flower_price_after3;

	@FXML
	private Label flower_price_after4;

	@FXML
	private Label flower_price_after5;

	@FXML
	private Label flower_price_after6;

	@FXML
	private StackPane flower_price_before_container1;

	@FXML
	private StackPane flower_price_before_container2;

	@FXML
	private StackPane flower_price_before_container3;

	@FXML
	private StackPane flower_price_before_container4;

	@FXML
	private StackPane flower_price_before_container5;

	@FXML
	private StackPane flower_price_before_container6;

	@FXML
	private Line flower_price_before_line1;

	@FXML
	private Line flower_price_before_line2;

	@FXML
	private Line flower_price_before_line3;

	@FXML
	private Line flower_price_before_line4;

	@FXML
	private Line flower_price_before_line5;

	@FXML
	private Line flower_price_before_line6;


	@FXML
	private Button printProd;

	@FXML
	private Text justText;

	@FXML
	private Button nextPage;

	@FXML
	private Button prevPage;


	@FXML
	private Button flower1_addCart;

	@FXML
	private Button flower2_addCart;

	@FXML
	private Button flower3_addCart;

	@FXML
	private Button flower4_addCart;

	@FXML
	private Button flower5_addCart;

	@FXML
	private Button flower6_addCart;

	@FXML
	private Button btnClearCart;

	@FXML
	private TextField cartTextPrice;

	@FXML
	private Text cartTopText;

	@FXML
	private Button viewMyComplaints;

	@FXML
	private Button viewMyOrders;


	@FXML
	private ListView<String> CartItemsList;


	@FXML // fx:id="adminControlButtton"
	private Button adminControlButtton; // Value injected by FXMLLoader

	@FXML // fx:id="openComplaints"
	private Button openComplaints; // Value injected by FXMLLoader


	@FXML // fx:id="inboxList"
	private ListView<String> inboxList; // Value injected by FXMLLoader

	@FXML // fx:id="openMessage"
	private Button openMessage; // Value injected by FXMLLoader

	@FXML // fx:id="viewInboxPlz"
	private Button viewInboxPlz; // Value injected by FXMLLoader

	@FXML // fx:id="deliveryButton"
	private Button deliveryButton; // Value injected by FXMLLoader

	@FXML // fx:id="catalogAddProductBtn"
	private Button catalogAddProductBtn; // Value injected by FXMLLoader

	@FXML // fx:id="messageField"
	private TextField messageField; // Value injected by FXMLLoader

	@FXML
	void viewMessage(ActionEvent event)
	{
		System.out.println("viewMessage 1");
		messageField.setVisible(true);
		int i;
		String bb = "";
		String aa = inboxList.getSelectionModel().getSelectedItem();
		int selected = 0;
		for(i = 0 ; aa.charAt(i) != '-'; i++)
		{
		}
		i = i + 2;
		for(i = i ; i < aa.length() ; i++)
		{
			bb = bb + Character.toString(aa.charAt(i));
		}
		selected = Integer.parseInt(bb);
		System.out.println("Selected =  " + selected);
		for(i = 0 ; i < MessageList.size() ; i++)
		{
			if(MessageList.get(i).getMessageID() == selected)
			{
				messageField.setText(MessageList.get(i).getMsgText());
				System.out.println("Setting Text : " +MessageList.get(i).getMsgText());
				break;
			}
		}

	}

	@FXML
	void openInbox(ActionEvent event) {

		inboxList.getItems().clear();
		if(viewInboxPlz.getText().equals("Open Inbox"))
		{
			String MsgTemp = "";
			for (int i = 0; i < MessageList.size(); i++)
			{
				if (MessageList.get(i).getCustomerID() == currentLoggedAccount.getAccountID())
				{
					MsgTemp = "";
					int j = 0;
					while (MessageList.get(i).getMsgText().charAt(j) != ':') {
						MsgTemp = MsgTemp + MessageList.get(i).getMsgText().charAt(j);
						j++;
					}
					inboxList.getItems().add(MsgTemp);
				}
			}
			viewInboxPlz.setText("Close Inbox");
			inboxList.setVisible(true);
			openMessage.setVisible(true);

		}
		else
		{
			viewInboxPlz.setText("Open Inbox");
			inboxList.setVisible(false);
			openMessage.setVisible(false);
		}

	}

	@FXML
	void chooseAdminEditCatalog(ActionEvent event) {
		if (worker_edit == null) {
			return;
		}
		String selection = worker_edit.getSelectionModel().getSelectedItem();
		if (selection == null) {
			return;
		}
		System.out.println("Worker action selected: " + selection);
	}


	private void navigateInShell(String fxml) {
		NavigationService.getInstance().navigate(fxml);
	}

	private void navigateAfterLogin(Account account) {
		if (account == null) {
			navigateInShell("Catalog");
			return;
		}

		int privilege = account.getPrivialge();
		if (privilege >= 4) {
			navigateInShell("NetworkDashboard");
		} else if (privilege >= 3) {
			navigateInShell("log_manager");
		} else if (privilege >= 2) {
			navigateInShell("WorkerDashboard");
		} else {
			navigateInShell("Catalog");
		}
	}

	@FXML
	void goLogOut(ActionEvent event) throws IOException {
		currentLoggedAccount = null;
		SimpleClient.logoutCurrentUser();
		applyPrivilegeBasedUI();
		navigateInShell("Login");
	}

	@FXML
	void goToLogin(ActionEvent event) throws IOException {
		navigateInShell("Login");
	}

	@FXML
	void goToRegister(ActionEvent event) throws IOException {
		navigateInShell("register");
	}

	@FXML
	void ReplyToComplaints(ActionEvent event) throws IOException {

		if (!ensurePrivilege(event, 2, "Complaint Handling")) {
			return;
		}

		PassAccountEventReplyComplaint recievedAcc = new PassAccountEventReplyComplaint(currentLoggedAccount);
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						EventBus.getDefault().post(recievedAcc);
						System.out.println("the server sent me the account , NICE 4 !!");
					}
				},4000
		);

		navigateInShell("replycomplaint");

	}


	@FXML
	void openControlPanel(ActionEvent event) throws IOException {

		if (!ensurePrivilege(event, 3, "Admin Panel")) {
			return;
		}

		PassAccountEventAdmin recievedAcc = new PassAccountEventAdmin(currentLoggedAccount);
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						EventBus.getDefault().post(recievedAcc);
						System.out.println("the server sent me the account , NICE 4 !!");
					}
				},4000
		);

		navigateInShell("admincontrol");
	}

	@FXML
	void addToCartFlower1(ActionEvent event) {
		addToCart(0);
	}

	@FXML
	void addToCartFlower2(ActionEvent event) {
		addToCart(1);
	}

	@FXML
	void addToCartFlower3(ActionEvent event) {
		addToCart(2);
	}

	@FXML
	void addToCartFlower4(ActionEvent event) {
		addToCart(3);
	}

	@FXML
	void addToCartFlower5(ActionEvent event) {
		addToCart(4);
	}

	@FXML
	void addToCartFlower6(ActionEvent event) {
		addToCart(5);
	}

	/* ========================= */
	/*   الدالة المشتركة الصحيحة  */
	/* ========================= */

	private void addToCart(int offset) {
		int index = CatalogSTARTIndex + offset;
		List<Product> displayProducts = getDisplayedProducts();
		if (index < 0 || index >= displayProducts.size()) {
			return; // حماية من IndexOutOfBounds
		}

		// إضافة المنتج
		Product selectedProduct = displayProducts.get(index);
		CartService.getInstance().addProduct(selectedProduct, 1);

		refreshCartDisplay();
	}

	private void addProductToCartByIndex(int offset) {
		addToCart(offset);
	}


	private void configureProductCardActions() {
		Account account = SimpleClient.getUser();
		int privilege = account != null ? account.getPrivilegeLevel() : 0;
		boolean canEdit = privilege >= 2;

		configureSingleProductAction(flower1_addCart, 0, canEdit);
		configureSingleProductAction(flower2_addCart, 1, canEdit);
		configureSingleProductAction(flower3_addCart, 2, canEdit);
		configureSingleProductAction(flower4_addCart, 3, canEdit);
		configureSingleProductAction(flower5_addCart, 4, canEdit);
		configureSingleProductAction(flower6_addCart, 5, canEdit);
	}

	private void configureSingleProductAction(Button button, int offset, boolean canEdit) {
		List<Product> displayProducts = getDisplayedProducts();
		if (button == null || CatalogSTARTIndex + offset >= displayProducts.size()) {
			return;
		}

		Product product = displayProducts.get(CatalogSTARTIndex + offset);

		if (canEdit) {
			button.setText("Edit");
			button.setOnAction(event -> {
				setCurrent_button(product);
				try {
					App.setRoot("secondary");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else {
			button.setText("Add to Cart");
			button.setOnAction(event -> addProductToCartByIndex(offset));
		}
	}
	@FXML
	void openDelivery(ActionEvent event) throws IOException
	{
		if (!ensurePrivilege(event, 2, "Deliveries")) {
			return;
		}
		PassAccountEventDelivery recievedAcc = new PassAccountEventDelivery(currentLoggedAccount);
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						EventBus.getDefault().post(recievedAcc);
						System.out.println("the server sent me the account , NICE 4 !!");
					}
				},4000
		);

		navigateInShell("delivery");

	}


	@FXML
	private void onClearCartClicked(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
				"Clear all items from your cart?",
				ButtonType.CANCEL, ButtonType.OK);
		alert.setTitle("Clear Cart");
		alert.setHeaderText(null);
		Optional<ButtonType> response = alert.showAndWait();
		if (response.isPresent() && response.get() == ButtonType.OK) {
			CartService.getInstance().clear();
			refreshCartDisplay();
		}
	}
	@FXML
	void openComplaintManager(ActionEvent event) throws IOException {

		if (!ensurePrivilege(event, 3, "Manager Dashboard")) {
			return;
		}

		GetAllComplaints allComplaints = new GetAllComplaints();
		System.out.println("send request for complaints !!");
		try {
			System.out.println("before sending the getAllComplaints " );
			SimpleClient.getClient().sendToServer(allComplaints);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// added 30/7
		PassAccountEventLogManager recievedAcc = new PassAccountEventLogManager(currentLoggedAccount);

		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						EventBus.getDefault().post(recievedAcc);
						System.out.println("the server sent me the account , NICE 4 !!");
					}
				},4000
		);


		navigateInShell("log_manager");

	}
	@FXML
	void openCheckout(ActionEvent event) throws IOException
	{
		if (CartService.getInstance().getItems().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Cart is empty");
			alert.setHeaderText(null);
			alert.setContentText("Add items before checkout.");
			alert.showAndWait();
			return;
		}
		if (resolveCurrentPrivilegeLevel() < 1) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Login Required");
			alert.setHeaderText("Login required to checkout.");
			alert.setContentText("Login required to checkout. You can add items as a guest, but you must login/register to place an order.");
			alert.showAndWait();
			navigateInShell("Login");
			return;
		}

		System.out.println("arrived to checkout 1");
		navigateInShell("checkout");
		System.out.println("arrived to checkout 2");
		PassAccountEventCheckout recievedAcc = new PassAccountEventCheckout(currentLoggedAccount);
		recievedAcc.productsToCheckout = CartService.getInstance().getItemsCopy();

		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						EventBus.getDefault().post(recievedAcc);
						System.out.println("the server sent me the account , NICE 4 !!");
					}
				},4000
		);

		System.out.println("arrived to checkout 4 (posted the event)");
	}


	@Subscribe
	public void messageEvent(passAllMessagesEvent allMessages){ // added new 21/7
		System.out.println("arrived to MESSAGE Event Subscriber in primary!!!!!");
		List<Message> recievedMessagess = allMessages.getMessagesToPassToPass();

		for(int i=0;i<recievedMessagess.size();i++){
			System.out.println(recievedMessagess.get(i));
		}
	}

	@FXML
	void nextPageUpate(ActionEvent event)
	{
		List<Product> displayProducts = getDisplayedProducts();
		int difference = displayProducts.size() - CatalogENDIndex;
		if(difference == 0)
		{

		}
		else
		{
			CatalogSTARTIndex = CatalogENDIndex;
			if (difference < 7) {
				CatalogENDIndex = CatalogENDIndex + difference;
			} else {
				CatalogENDIndex = CatalogENDIndex + 6;
			}
			updateFields(1);
		}
	}


	@FXML
	void openMyComplaints(ActionEvent event) throws IOException {
		if (!ensurePrivilege(event, 1, "Complaints")) {
			return;
		}
		navigateInShell("mycomplaints");
		PassAccountEventComplaints recievedAcc = new PassAccountEventComplaints(currentLoggedAccount);

		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						EventBus.getDefault().post(recievedAcc);
						System.out.println("the server sent me the account , NICE 4 !!");
					}
				},4000
		);

		// added new 30/7
		GetAllComplaints allComplaints = new GetAllComplaints();
		System.out.println("send request for complaints !!");
		try {
			System.out.println("before sending the getAllComplaints " );
			SimpleClient.getClient().sendToServer(allComplaints);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void openMyOrders(ActionEvent event) throws IOException
	{
		if (!ensurePrivilege(event, 1, "My Orders")) {
			return;
		}
		navigateInShell("myorders");

		PassAccountEventOrders recievedAcc = new PassAccountEventOrders(currentLoggedAccount);

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
	void prevPageUpate(ActionEvent event)
	{
		int difference = CatalogSTARTIndex - 6;
		if(difference >= 6)
		{
			CatalogENDIndex = CatalogSTARTIndex;
			CatalogSTARTIndex = CatalogSTARTIndex - 7;
		}
		else
		{
			if(difference > -1 )
			{
				CatalogENDIndex = CatalogSTARTIndex;
				CatalogSTARTIndex = 0;
			}
		}
		updateFields(1);
	}
	@FXML
	void printProducts(ActionEvent event)
	{
		for(int i = 0 ; i < allProducts.size() ; i++)
		{
			System.out.println("ID: " + allProducts.get(i).getID());
			System.out.println("Name: " + allProducts.get(i).getName());
			System.out.println("Price: " + allProducts.get(i).getPrice());
			System.out.println("### END ###");
		}
	}

	private void requestMessages() {
		GetAllMessages allMessages = new GetAllMessages();
		System.out.println("send request for messages !!");
		try {
			System.out.println("before sending the getAllMessagaes ");
			SimpleClient.getClient().sendToServer(allMessages);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showStatusMessage(String message) {
		if (init_container == null || justText == null) {
			return;
		}
		Platform.runLater(() -> {
			init_container.setVisible(true);
			justText.setVisible(true);
			justText.setText(message);
		});
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						Platform.runLater(() -> {
							init_container.setVisible(false);
							justText.setVisible(false);
						});
					}
				}, 3000
		);
	}
	public static String current_button;

	int updateFieldsBounds = 0;

	private String formatCatalogField(String label, String value) {
		if (value == null || value.isBlank()) {
			return label + ": /";
		}
		return label + ": " + value;
	}

	private void ensureProductMetadata(List<Product> products) {
		if (products == null) {
			return;
		}
		for (int i = 0; i < products.size(); i++) {
			applyDefaultMetadata(products.get(i), i);
		}
	}

	private void applyDefaultMetadata(Product product, int index) {
		if (product == null) {
			return;
		}
		if (product.getSku() == null || product.getSku().isBlank()) {
			product.setSku(defaultSkuFor(product, index));
		}
		if (product.getCategory() == null || product.getCategory().isBlank()) {
			product.setCategory(defaultCategoryFor(index));
		}
		if (product.getColor() == null || product.getColor().isBlank()) {
			product.setColor(defaultColorFor(index));
		}
	}

	private void applyProductImage(Product product, ImageView imageView) {
		if (product == null || imageView == null) {
			return;
		}
		String imagePath = product.getImage();
		Image image = loadProductImage(imagePath);
		if (image != null) {
			imageView.setImage(image);
		}
	}

	private Image loadProductImage(String imagePath) {
		if (imagePath == null || imagePath.isBlank()) {
			return loadPlaceholderImage();
		}
		String normalizedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
		try (InputStream inputStream = getClass().getResourceAsStream(normalizedPath)) {
			if (inputStream != null) {
				return new Image(inputStream);
			}
		} catch (Exception ex) {
			System.out.println("Unable to load product image: " + ex.getMessage());
		}
		return loadPlaceholderImage();
	}

	private Image loadPlaceholderImage() {
		try (InputStream inputStream = getClass().getResourceAsStream("placeholder.png")) {
			if (inputStream != null) {
				return new Image(inputStream);
			}
		} catch (Exception ex) {
			System.out.println("Unable to load placeholder image: " + ex.getMessage());
		}
		try (InputStream inputStream = getClass().getResourceAsStream("/placeholder.png")) {
			if (inputStream != null) {
				return new Image(inputStream);
			}
		} catch (Exception ex) {
			System.out.println("Unable to load placeholder image: " + ex.getMessage());
		}
		return null;
	}

	private String defaultSkuFor(Product product, int index) {
		String[] defaultSkus = {
				"SUN-001",
				"DAI-001",
				"LIL-001",
				"TUL-001",
				"ROS-001",
				"ORC-001"
		};
		if (index >= 0 && index < defaultSkus.length) {
			return defaultSkus[index];
		}
		if (product != null && product.getID() > 0) {
			return "SKU-" + product.getID();
		}
		if (product != null && product.getName() != null && !product.getName().isBlank()) {
			return "SKU-" + product.getName().trim().toUpperCase(Locale.US).replaceAll("[^A-Z0-9]+", "-");
		}
		return "SKU-UNKNOWN";
	}

	private String defaultCategoryFor(int index) {
		String[] defaultCategories = {
				"Bouquet",
				"Bouquet",
				"Arrangement",
				"Bouquet",
				"Bouquet",
				"Arrangement"
		};
		if (index >= 0 && index < defaultCategories.length) {
			return defaultCategories[index];
		}
		return "Other";
	}

	private String defaultColorFor(int index) {
		String[] defaultColors = {
				"Yellow",
				"White",
				"White",
				"Pink",
				"Red",
				"Purple"
		};
		if (index >= 0 && index < defaultColors.length) {
			return defaultColors[index];
		}
		return "Mixed";
	}

	private String formatPrice(double price) {
		return String.format(Locale.US, "%.2f₪", price);
	}

	private void configureOldPriceStrike(Line line) {
		if (line == null) {
			return;
		}
		line.setStroke(Color.RED);
		line.setStrokeWidth(2.0);
		line.setStartX(0);
		line.setStartY(18);
		line.setEndX(60);
		line.setEndY(0);
		line.toFront();
	}

	private void updatePricingLabels(Product product, Label priceBadge, StackPane oldPriceContainer,
									 Label priceBefore, Line oldPriceStrike, Label priceAfter,
									 Label promoBadge) {
		if (product == null) {
			return;
		}

		Account account = currentLoggedAccount != null ? currentLoggedAccount : SimpleClient.getUser();
		PricingService.PricingResult pricing = PricingService.calculatePricing(product, account);
		double basePrice = pricing.getBasePrice();
		double finalPrice = pricing.getFinalPrice();
		boolean hasDiscount = basePrice > finalPrice;

		System.out.printf(Locale.US,
				"Product %d | original=%.2f | final=%.2f%n",
				product.getID(), basePrice, finalPrice);

		String formattedBase = formatPrice(basePrice);
		String formattedFinal = formatPrice(finalPrice);

		priceBadge.setText(formattedFinal);
		priceAfter.setText(formattedFinal);
		priceBefore.setText(formattedBase);
		if (oldPriceContainer != null) {
			oldPriceContainer.setVisible(hasDiscount);
			if (!oldPriceContainer.managedProperty().isBound()) {
				oldPriceContainer.setManaged(hasDiscount);
			}
		}
		if (oldPriceStrike != null) {
			oldPriceStrike.setVisible(hasDiscount);
			oldPriceStrike.toFront();
		}

		promoBadge.setText("SALE");
		promoBadge.setVisible(hasDiscount);
		promoBadge.setManaged(hasDiscount);
		priceAfter.setVisible(hasDiscount);
	}

	private void bindManagedToVisible(Node... nodes) {
		for (Node node : nodes) {
			if (node == null) {
				continue;
			}
			node.managedProperty().bind(node.visibleProperty());
		}
	}

	public void updateFields(int mode)
	{
		List<Product> displayProducts = getDisplayedProducts();
		System.out.println("START INDEX = " + CatalogSTARTIndex);
		System.out.println("END INDEX = " + CatalogENDIndex);
		System.out.println("Length = " + displayProducts.size());
		if (mode == 0) {
			flower_name1.setText(displayProducts.get(0).getName());
			flower_name2.setText(displayProducts.get(1).getName());
			flower_name3.setText(displayProducts.get(2).getName());
			flower_name4.setText(displayProducts.get(3).getName());
			flower_name5.setText(displayProducts.get(4).getName());
			flower_name6.setText(displayProducts.get(5).getName());

			flower_sku1.setText(formatCatalogField("SKU", displayProducts.get(0).getSku()));
			flower_sku2.setText(formatCatalogField("SKU", displayProducts.get(1).getSku()));
			flower_sku3.setText(formatCatalogField("SKU", displayProducts.get(2).getSku()));
			flower_sku4.setText(formatCatalogField("SKU", displayProducts.get(3).getSku()));
			flower_sku5.setText(formatCatalogField("SKU", displayProducts.get(4).getSku()));
			flower_sku6.setText(formatCatalogField("SKU", displayProducts.get(5).getSku()));

			flower_category1.setText(formatCatalogField("Type", displayProducts.get(0).getCategory()));
			flower_category2.setText(formatCatalogField("Type", displayProducts.get(1).getCategory()));
			flower_category3.setText(formatCatalogField("Type", displayProducts.get(2).getCategory()));
			flower_category4.setText(formatCatalogField("Type", displayProducts.get(3).getCategory()));
			flower_category5.setText(formatCatalogField("Type", displayProducts.get(4).getCategory()));
			flower_category6.setText(formatCatalogField("Type", displayProducts.get(5).getCategory()));

			flower_color1.setText(formatCatalogField("Color", displayProducts.get(0).getColor()));
			flower_color2.setText(formatCatalogField("Color", displayProducts.get(1).getColor()));
			flower_color3.setText(formatCatalogField("Color", displayProducts.get(2).getColor()));
			flower_color4.setText(formatCatalogField("Color", displayProducts.get(3).getColor()));
			flower_color5.setText(formatCatalogField("Color", displayProducts.get(4).getColor()));
			flower_color6.setText(formatCatalogField("Color", displayProducts.get(5).getColor()));

			updatePricingLabels(displayProducts.get(0), flower_price1, flower_price_before_container1, flower_price_before1,
					flower_price_before_line1, flower_price_after1, flower_promo1);
			updatePricingLabels(displayProducts.get(1), flower_price2, flower_price_before_container2, flower_price_before2,
					flower_price_before_line2, flower_price_after2, flower_promo2);
			updatePricingLabels(displayProducts.get(2), flower_price3, flower_price_before_container3, flower_price_before3,
					flower_price_before_line3, flower_price_after3, flower_promo3);
			updatePricingLabels(displayProducts.get(3), flower_price4, flower_price_before_container4, flower_price_before4,
					flower_price_before_line4, flower_price_after4, flower_promo4);
			updatePricingLabels(displayProducts.get(4), flower_price5, flower_price_before_container5, flower_price_before5,
					flower_price_before_line5, flower_price_after5, flower_promo5);
			updatePricingLabels(displayProducts.get(5), flower_price6, flower_price_before_container6, flower_price_before6,
					flower_price_before_line6, flower_price_after6, flower_promo6);

		}
		else
		{
			if(mode == 2)
			{
				// MODE = 1 Does Normal Updating , CatalogSTARTIndex and CatalogENDIndex
				CatalogSTARTIndex = 0;          // Were updated from outside the function.
				if (displayProducts.size() > 5)    // MODE = 2, Do not use Mode 2, Ramiz knows what this shit does, ask him
					CatalogENDIndex = 6;
				else
					CatalogENDIndex = displayProducts.size();
				System.out.println("START INDEX = " + CatalogSTARTIndex);
				System.out.println("END INDEX = " + CatalogENDIndex);
			}

			flower_price1.setText("/");
			flower_price2.setText("/");
			flower_price3.setText("/");
			flower_price4.setText("/");
			flower_price5.setText("/");
			flower_price6.setText("/");

			flower_price_before1.setText("");
			flower_price_before2.setText("");
			flower_price_before3.setText("");
			flower_price_before4.setText("");
			flower_price_before5.setText("");
			flower_price_before6.setText("");

			flower_price_after1.setText("/");
			flower_price_after2.setText("/");
			flower_price_after3.setText("/");
			flower_price_after4.setText("/");
			flower_price_after5.setText("/");
			flower_price_after6.setText("/");

			flower_promo1.setVisible(false);
			flower_promo1.setManaged(false);
			flower_promo2.setVisible(false);
			flower_promo2.setManaged(false);
			flower_promo3.setVisible(false);
			flower_promo3.setManaged(false);
			flower_promo4.setVisible(false);
			flower_promo4.setManaged(false);
			flower_promo5.setVisible(false);
			flower_promo5.setManaged(false);
			flower_promo6.setVisible(false);
			flower_promo6.setManaged(false);

			flower_name1.setText("/");
			flower_name2.setText("/");
			flower_name3.setText("/");
			flower_name4.setText("/");
			flower_name5.setText("/");
			flower_name6.setText("/");

			flower_sku1.setText("SKU: /");
			flower_sku2.setText("SKU: /");
			flower_sku3.setText("SKU: /");
			flower_sku4.setText("SKU: /");
			flower_sku5.setText("SKU: /");
			flower_sku6.setText("SKU: /");

			flower_category1.setText("Type: /");
			flower_category2.setText("Type: /");
			flower_category3.setText("Type: /");
			flower_category4.setText("Type: /");
			flower_category5.setText("Type: /");
			flower_category6.setText("Type: /");

			flower_color1.setText("Color: /");
			flower_color2.setText("Color: /");
			flower_color3.setText("Color: /");
			flower_color4.setText("Color: /");
			flower_color5.setText("Color: /");
			flower_color6.setText("Color: /");

			for(int i = 0 ; i < displayProducts.size() ; i++)
			{
				System.out.println("ID: " + displayProducts.get(i).getID());
				System.out.println("Name: " + displayProducts.get(i).getName());
				System.out.println("Price: " + displayProducts.get(i).getPrice());
				System.out.println("### END ###");
			}
			ViewItems(false);


			if(CatalogENDIndex - CatalogSTARTIndex > 0)
			{
				flower_name1.setText(displayProducts.get(CatalogSTARTIndex).getName());
				applyProductImage(displayProducts.get(CatalogSTARTIndex), flower_button1);
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex), flower_price1, flower_price_before_container1, flower_price_before1,
						flower_price_before_line1, flower_price_after1, flower_promo1);
				flower_sku1.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex).getSku()));
				flower_category1.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex).getCategory()));
				flower_color1.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex).getColor()));
				flower_button1.setVisible(true);
				flower_price1.setVisible(true);
				flower_name1.setVisible(true);
				flower_sku1.setVisible(shouldShowSku());
				flower_category1.setVisible(true);
				flower_color1.setVisible(true);
				flower1_addCart.setVisible(true);
				container1.setVisible(true);
			}

			if (CatalogENDIndex - CatalogSTARTIndex > 1)
			{
				flower_name2.setText(displayProducts.get(CatalogSTARTIndex + 1).getName());
				applyProductImage(displayProducts.get(CatalogSTARTIndex + 1), flower_button2);
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 1), flower_price2, flower_price_before_container2, flower_price_before2,
						flower_price_before_line2, flower_price_after2, flower_promo2);
				flower_sku2.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 1).getSku()));
				flower_category2.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 1).getCategory()));
				flower_color2.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 1).getColor()));
				flower_button2.setVisible(true);
				flower_price2.setVisible(true);
				flower_name2.setVisible(true);
				flower_sku2.setVisible(shouldShowSku());
				flower_category2.setVisible(true);
				flower_color2.setVisible(true);
				flower2_addCart.setVisible(true);
				container2.setVisible(true);
			}
			if (CatalogENDIndex - CatalogSTARTIndex > 2)
			{
				flower_name3.setText(displayProducts.get(CatalogSTARTIndex + 2).getName());
				applyProductImage(displayProducts.get(CatalogSTARTIndex + 2), flower_button3);
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 2), flower_price3, flower_price_before_container3, flower_price_before3,
						flower_price_before_line3, flower_price_after3, flower_promo3);
				flower_sku3.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 2).getSku()));
				flower_category3.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 2).getCategory()));
				flower_color3.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 2).getColor()));
				flower_button3.setVisible(true);
				flower_price3.setVisible(true);
				flower_name3.setVisible(true);
				flower_sku3.setVisible(shouldShowSku());
				flower_category3.setVisible(true);
				flower_color3.setVisible(true);
				flower3_addCart.setVisible(true);
				container3.setVisible(true);
			}
			if (CatalogENDIndex - CatalogSTARTIndex > 3)
			{
				flower_name4.setText(displayProducts.get(CatalogSTARTIndex + 3).getName());
				applyProductImage(displayProducts.get(CatalogSTARTIndex + 3), flower_button4);
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 3), flower_price4, flower_price_before_container4, flower_price_before4,
						flower_price_before_line4, flower_price_after4, flower_promo4);
				flower_sku4.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 3).getSku()));
				flower_category4.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 3).getCategory()));
				flower_color4.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 3).getColor()));
				flower_button4.setVisible(true);
				flower_price4.setVisible(true);
				flower_name4.setVisible(true);
				flower_sku4.setVisible(shouldShowSku());
				flower_category4.setVisible(true);
				flower_color4.setVisible(true);
				flower4_addCart.setVisible(true);
				container4.setVisible(true);
			}
			if (CatalogENDIndex - CatalogSTARTIndex > 4)
			{
				flower_name5.setText(displayProducts.get(CatalogSTARTIndex + 4).getName());
				applyProductImage(displayProducts.get(CatalogSTARTIndex + 4), flower_button5);
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 4), flower_price5, flower_price_before_container5, flower_price_before5,
						flower_price_before_line5, flower_price_after5, flower_promo5);
				flower_sku5.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 4).getSku()));
				flower_category5.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 4).getCategory()));
				flower_color5.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 4).getColor()));
				flower_button5.setVisible(true);
				flower_price5.setVisible(true);
				flower_name5.setVisible(true);
				flower_sku5.setVisible(shouldShowSku());
				flower_category5.setVisible(true);
				flower_color5.setVisible(true);
				flower5_addCart.setVisible(true);
				container5.setVisible(true);
			}
			if (CatalogENDIndex - CatalogSTARTIndex > 5)
			{
				// Display the sixth product when more than five items remain.  Use index + 5 to
				// select the sixth element in the current window and update the corresponding
				// UI components (name, price, button, cart button and container) for slot 6.
				flower_name6.setText(displayProducts.get(CatalogSTARTIndex + 5).getName());
				applyProductImage(displayProducts.get(CatalogSTARTIndex + 5), flower_button6);
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 5), flower_price6, flower_price_before_container6, flower_price_before6,
						flower_price_before_line6, flower_price_after6, flower_promo6);
				flower_sku6.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 5).getSku()));
				flower_category6.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 5).getCategory()));
				flower_color6.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 5).getColor()));
				flower_button6.setVisible(true);
				flower_price6.setVisible(true);
				flower_name6.setVisible(true);
				flower_sku6.setVisible(shouldShowSku());
				flower_category6.setVisible(true);
				flower_color6.setVisible(true);
				flower6_addCart.setVisible(true);
				container6.setVisible(true);
			}
			if (CatalogENDIndex - CatalogSTARTIndex == 6)
			{
				flower_name6.setText(displayProducts.get(CatalogSTARTIndex + 5).getName());
				applyProductImage(displayProducts.get(CatalogSTARTIndex + 5), flower_button6);
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 5), flower_price6, flower_price_before_container6, flower_price_before6,
						flower_price_before_line6, flower_price_after6, flower_promo6);
				flower_sku6.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 5).getSku()));
				flower_category6.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 5).getCategory()));
				flower_color6.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 5).getColor()));
				flower_button6.setVisible(true);
				flower_price6.setVisible(true);
				flower_name6.setVisible(true);
				flower_sku6.setVisible(shouldShowSku());
				flower_category6.setVisible(true);
				flower_color6.setVisible(true);
				flower6_addCart.setVisible(true);
				container6.setVisible(true);
			}
		}
	}
	int CatalogSTARTIndex;
	int CatalogENDIndex;

	static List<Product> allProducts = new ArrayList<>();
	private List<Product> filteredProducts = new ArrayList<>();
	private boolean filtersApplied = false;

	/**
	 * Handle clicks on a product card in the catalog.  When a user clicks a product
	 * card, this method determines which {@link Product} was clicked based on the
	 * container’s fx:id and opens a modal with detailed product information.
	 */
	@FXML
	void productCardClicked(MouseEvent event) {
		if (event.getTarget() instanceof Button) {
			return;
		}

		String containerId = ((VBox) event.getSource()).getId();
		Product selected = getProductForContainer(containerId);
		if (selected != null) {
			setCurrent_button(selected);
			openProductDetailsModal(selected);
		}
	}

	private Product getProductForContainer(String containerId) {
		int offset;
		switch (containerId) {
			case "container1":
				offset = 0;
				break;
			case "container2":
				offset = 1;
				break;
			case "container3":
				offset = 2;
				break;
			case "container4":
				offset = 3;
				break;
			case "container5":
				offset = 4;
				break;
			case "container6":
				offset = 5;
				break;
			default:
				return null;
		}

		int productIndex = CatalogSTARTIndex + offset;
		List<Product> displayProducts = getDisplayedProducts();
		if (productIndex < 0 || productIndex >= displayProducts.size()) {
			return null;
		}
		return displayProducts.get(productIndex);
	}

	private void openProductDetailsModal(Product product) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductDetailsModal.fxml"));
			Parent root = loader.load();
			ProductDetailsController controller = loader.getController();
			controller.setProduct(product);

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle(product.getName());
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void ViewItems(boolean mode)
	{
		boolean showSku = mode && shouldShowSku();
		flower_button1.setVisible(mode);
		flower_button2.setVisible(mode);
		flower_button3.setVisible(mode);
		flower_button4.setVisible(mode);
		flower_button5.setVisible(mode);
		flower_button6.setVisible(mode);

		flower_price1.setVisible(mode);
		flower_price2.setVisible(mode);
		flower_price3.setVisible(mode);
		flower_price4.setVisible(mode);
		flower_price5.setVisible(mode);
		flower_price6.setVisible(mode);

		flower_name1.setVisible(mode);
		flower_name2.setVisible(mode);
		flower_name3.setVisible(mode);
		flower_name4.setVisible(mode);
		flower_name5.setVisible(mode);
		flower_name6.setVisible(mode);

		flower_sku1.setVisible(showSku);
		flower_sku2.setVisible(showSku);
		flower_sku3.setVisible(showSku);
		flower_sku4.setVisible(showSku);
		flower_sku5.setVisible(showSku);
		flower_sku6.setVisible(showSku);

		flower_category1.setVisible(mode);
		flower_category2.setVisible(mode);
		flower_category3.setVisible(mode);
		flower_category4.setVisible(mode);
		flower_category5.setVisible(mode);
		flower_category6.setVisible(mode);

		flower_color1.setVisible(mode);
		flower_color2.setVisible(mode);
		flower_color3.setVisible(mode);
		flower_color4.setVisible(mode);
		flower_color5.setVisible(mode);
		flower_color6.setVisible(mode);

		flower1_addCart.setVisible(mode);
		flower2_addCart.setVisible(mode);
		flower3_addCart.setVisible(mode);
		flower4_addCart.setVisible(mode);
		flower5_addCart.setVisible(mode);
		flower6_addCart.setVisible(mode);

		container1.setVisible(mode);
		container2.setVisible(mode);
		container3.setVisible(mode);
		container4.setVisible(mode);
		container5.setVisible(mode);
		container6.setVisible(mode);


	}
	int cartPrice = 0;
	Account currentLoggedAccount;
	boolean availableProducts = false;

	@FXML
	void initialize() throws MalformedURLException {
		System.out.println("arrived to initialize 1");
		EventBus.getDefault().register(this);
		requestMessages();
		assert flower_button1 != null : "fx:id=\"flower_button1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_button2 != null : "fx:id=\"flower_button2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_button3 != null : "fx:id=\"flower_button3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_button4 != null : "fx:id=\"flower_button4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_button5 != null : "fx:id=\"flower_button5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_button6 != null : "fx:id=\"flower_button6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_name1 != null : "fx:id=\"flower_name1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_name2 != null : "fx:id=\"flower_name2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_name3 != null : "fx:id=\"flower_name3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_name4 != null : "fx:id=\"flower_name4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_name5 != null : "fx:id=\"flower_name5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_name6 != null : "fx:id=\"flower_name6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_sku1 != null : "fx:id=\"flower_sku1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_sku2 != null : "fx:id=\"flower_sku2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_sku3 != null : "fx:id=\"flower_sku3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_sku4 != null : "fx:id=\"flower_sku4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_sku5 != null : "fx:id=\"flower_sku5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_sku6 != null : "fx:id=\"flower_sku6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_category1 != null : "fx:id=\"flower_category1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_category2 != null : "fx:id=\"flower_category2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_category3 != null : "fx:id=\"flower_category3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_category4 != null : "fx:id=\"flower_category4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_category5 != null : "fx:id=\"flower_category5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_category6 != null : "fx:id=\"flower_category6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_color1 != null : "fx:id=\"flower_color1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_color2 != null : "fx:id=\"flower_color2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_color3 != null : "fx:id=\"flower_color3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_color4 != null : "fx:id=\"flower_color4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_color5 != null : "fx:id=\"flower_color5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_color6 != null : "fx:id=\"flower_color6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price1 != null : "fx:id=\"flower_price1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price2 != null : "fx:id=\"flower_price2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price3 != null : "fx:id=\"flower_price3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price4 != null : "fx:id=\"flower_price4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price5 != null : "fx:id=\"flower_price5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price6 != null : "fx:id=\"flower_price6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_promo1 != null : "fx:id=\"flower_promo1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_promo2 != null : "fx:id=\"flower_promo2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_promo3 != null : "fx:id=\"flower_promo3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_promo4 != null : "fx:id=\"flower_promo4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_promo5 != null : "fx:id=\"flower_promo5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_promo6 != null : "fx:id=\"flower_promo6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before1 != null : "fx:id=\"flower_price_before1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before2 != null : "fx:id=\"flower_price_before2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before3 != null : "fx:id=\"flower_price_before3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before4 != null : "fx:id=\"flower_price_before4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before5 != null : "fx:id=\"flower_price_before5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before6 != null : "fx:id=\"flower_price_before6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_container1 != null : "fx:id=\"flower_price_before_container1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_container2 != null : "fx:id=\"flower_price_before_container2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_container3 != null : "fx:id=\"flower_price_before_container3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_container4 != null : "fx:id=\"flower_price_before_container4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_container5 != null : "fx:id=\"flower_price_before_container5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_container6 != null : "fx:id=\"flower_price_before_container6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_line1 != null : "fx:id=\"flower_price_before_line1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_line2 != null : "fx:id=\"flower_price_before_line2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_line3 != null : "fx:id=\"flower_price_before_line3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_line4 != null : "fx:id=\"flower_price_before_line4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_line5 != null : "fx:id=\"flower_price_before_line5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_before_line6 != null : "fx:id=\"flower_price_before_line6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after1 != null : "fx:id=\"flower_price_after1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after2 != null : "fx:id=\"flower_price_after2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after3 != null : "fx:id=\"flower_price_after3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after4 != null : "fx:id=\"flower_price_after4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after5 != null : "fx:id=\"flower_price_after5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after6 != null : "fx:id=\"flower_price_after6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert deliveryButton != null : "fx:id=\"deliveryButton\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert catalogAddProductBtn != null : "fx:id=\"catalogAddProductBtn\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert messageField != null : "fx:id=\"messageField\" was not injected: check your FXML file 'Catalog.fxml'.";

		bindManagedToVisible(
				flower_price_before_container1, flower_price_before_container2, flower_price_before_container3,
				flower_price_before_container4, flower_price_before_container5, flower_price_before_container6,
				flower_price_after1, flower_price_after2, flower_price_after3, flower_price_after4,
				flower_price_after5, flower_price_after6
		);

		configureOldPriceStrike(flower_price_before_line1);
		configureOldPriceStrike(flower_price_before_line2);
		configureOldPriceStrike(flower_price_before_line3);
		configureOldPriceStrike(flower_price_before_line4);
		configureOldPriceStrike(flower_price_before_line5);
		configureOldPriceStrike(flower_price_before_line6);

		messageField.setVisible(false);
		if (viewMyOrders != null) viewMyOrders.setVisible(false);
		if (viewMyComplaints != null) viewMyComplaints.setVisible(false);
		if (deliveryButton != null) deliveryButton.setVisible(false);
		if (openComplaints != null) openComplaints.setVisible(false);
		if (catalogAddProductBtn != null) catalogAddProductBtn.setVisible(false);
		if (infoo != null) infoo.setVisible(false);
		if (adminControlButtton != null) adminControlButtton.setVisible(false);
		if (viewInboxPlz != null) viewInboxPlz.setVisible(false);
		if (inboxList != null) inboxList.setVisible(false);
		if (openMessage != null) openMessage.setVisible(false);
		setAccountToolsPanelVisible(false);

		// Restore persisted login so customer-specific buttons become
		// available even if the PassAccountEvent arrived before this
		// controller was initialized (after registration).
		Account persistedAccount = SimpleClient.getUser();
		if (persistedAccount != null) {
			currentLoggedAccount = persistedAccount;
			applyPrivilegeBasedUI();
		}
		checkout.setVisible(false);
		if (cartTextPrice != null) cartTextPrice.setVisible(false);
		if (CartItemsList != null) CartItemsList.setVisible(false);
		if (cartTopText != null) cartTopText.setVisible(false);
		flower1_addCart.setVisible(false);
		flower2_addCart.setVisible(false);
		flower3_addCart.setVisible(false);
		flower4_addCart.setVisible(false);
		flower5_addCart.setVisible(false);
		flower6_addCart.setVisible(false);
		compln.setVisible(false);
		customid.setVisible(false);

		switch (CatalogFlag.getFlagg()){
			case 0:{
				break;
			}
			case 1:{
				System.out.println("case 1");
				break;
			}
			case 2:{
				System.out.println("case 2");
				break;
			}
			case 3:{
				System.out.println("case 3");
				break;
			}
		}
		customid.setVisible(false);

		flower_button1.setVisible(false);
		flower_button2.setVisible(false);
		flower_button3.setVisible(false);
		flower_button4.setVisible(false);
		flower_button5.setVisible(false);
		flower_button6.setVisible(false);

		flower_price1.setVisible(false);
		flower_price2.setVisible(false);
		flower_price3.setVisible(false);
		flower_price4.setVisible(false);
		flower_price5.setVisible(false);
		flower_price6.setVisible(false);
		flower_promo1.setVisible(false);
		flower_promo1.setManaged(false);
		flower_promo2.setVisible(false);
		flower_promo2.setManaged(false);
		flower_promo3.setVisible(false);
		flower_promo3.setManaged(false);
		flower_promo4.setVisible(false);
		flower_promo4.setManaged(false);
		flower_promo5.setVisible(false);
		flower_promo5.setManaged(false);
		flower_promo6.setVisible(false);
		flower_promo6.setManaged(false);
		flower_price_before_container1.setVisible(false);
		flower_price_before_container2.setVisible(false);
		flower_price_before_container3.setVisible(false);
		flower_price_before_container4.setVisible(false);
		flower_price_before_container5.setVisible(false);
		flower_price_before_container6.setVisible(false);
		flower_price_after1.setVisible(false);
		flower_price_after2.setVisible(false);
		flower_price_after3.setVisible(false);
		flower_price_after4.setVisible(false);
		flower_price_after5.setVisible(false);
		flower_price_after6.setVisible(false);

		flower_name1.setVisible(false);
		flower_name2.setVisible(false);
		flower_name3.setVisible(false);
		flower_name4.setVisible(false);
		flower_name5.setVisible(false);
		flower_name6.setVisible(false);
		if (init_container != null) {
			init_container.setVisible(false);
		}
		if (justText != null) {
			justText.setVisible(false);
		}

		//CartItemsList.setVisible(false);

		worker_edit.getItems().add("Add worker");
		worker_edit.getItems().add("Remove worker");
		worker_edit.getItems().add("Edit worker");

		if (btnClearCart != null) btnClearCart.setVisible(false);
		//cartTopText.setVisible(false);
		//cartTextPrice.setVisible(false);

		// Populate filter combo boxes after data initialisation.  We only have six
		// products at present; categories and colours are pulled from the Product
		// objects.  Price ranges are hard coded for illustrative purposes.
		ensureCatalogDataLoaded();
		// Collect distinct categories and colours from available products
		java.util.Set<String> categories = new java.util.HashSet<>();
		java.util.Set<String> colours = new java.util.HashSet<>();
		for (Product p : allProducts) {
			if (p.getCategory() != null && !p.getCategory().isEmpty()) {
				categories.add(p.getCategory());
			}
			if (p.getColor() != null && !p.getColor().isEmpty()) {
				colours.add(p.getColor());
			}
		}
		categoryFilter.getItems().clear();
		categoryFilter.getItems().add("All");
		categoryFilter.getItems().addAll(categories);
		categoryFilter.getSelectionModel().selectFirst();

		colorFilter.getItems().clear();
		colorFilter.getItems().add("All");
		colorFilter.getItems().addAll(colours);
		colorFilter.getSelectionModel().selectFirst();

		priceFilter.getItems().clear();
		priceFilter.getItems().add("All");
		priceFilter.getItems().add("0-50");
		priceFilter.getItems().add("50-100");
		priceFilter.getItems().add("100-200");
		priceFilter.getSelectionModel().selectFirst();

		// Attach listeners to apply filters when a selection changes
		categoryFilter.setOnAction(e -> applyFilters());
		colorFilter.setOnAction(e -> applyFilters());
		priceFilter.setOnAction(e -> applyFilters());

		if (returnedFromSecondaryController) {
			updateFields(0);
		}
		if (!allProducts.isEmpty()) {
			updateFields(2);
		}
		System.out.println("PRINTING FLAG");
		System.out.println(CatalogFlag.getFlagg());
		if (cartTextPrice != null) {
			cartTextPrice.setText("0");
		}
		CartService.getInstance().getObservableItems()
				.addListener((ListChangeListener<Product>) change -> refreshCartDisplay());
		refreshCartDisplay();
		worker_edit.setVisible(false);

		inboxList.setVisible(false);
		openMessage.setVisible(false);
		applyPrivilegeBasedUI();

	}


	void initializeData() {

		try {
			SimpleClient.getClient().sendToServer("first entry"); // sends the updated product to the server class
		} catch (IOException e) {
			System.out.println("Offline mode");
		}
	}

	private void ensureCatalogDataLoaded() {
		if (!allProducts.isEmpty()) {
			ensureProductMetadata(allProducts);
			resetFilteredProducts();
			availableProducts = true;
			Platform.runLater(() -> updateFields(2));
			return;
		}
		initializeData();
	}

	@Subscribe
	public void updateGui(UpdateGuiEvent upEvent){
		System.out.println("arrived to the update GUI  event");
		allProducts = upEvent.getRecievedList();
		ensureProductMetadata(allProducts);
		resetFilteredProducts();
		availableProducts = true;
		Platform.runLater(() -> {
			updateFields(2);
			if (init_container != null) {
				init_container.setVisible(false);
			}
			if (justText != null) {
				justText.setVisible(false);
			}
		});
	}
	@Subscribe
	public void complaintEvent(PassAllComplaintsEvent allComps){ // added new 21/7
		System.out.println("arrived to complaintEvent Subscriber in primary!!!!!");
		List<Complaint> recievedComplaints = allComps.getComplaintsToPass();

		for(int i=0;i<recievedComplaints.size();i++){
			System.out.println(recievedComplaints.get(i).getDate());
		}
	}

	List<Message> MessageList = new ArrayList<>();

	@Subscribe
	public void messageEventFunction(passAllMessagesEvent allMsg){ // added new 21/7
		System.out.println("arrived to messageEvent Subscriber in primary!!!!!");
		List<Message> recievedMessages = allMsg.getMessagesToPassToPass();
		System.out.println("ReceviedMessages = " + recievedMessages.size());
		MessageList = recievedMessages;
	}

	@Subscribe
	public void PassAccountEvent(PassAccountEvent passAcc){ // added today
		Platform.runLater(() -> {
			System.out.println("arrived to passAccountToPrimary sucessfuly");
			Account recvAccount = passAcc.getRecievedAccount();
			System.out.println(recvAccount.getPassword());
			if(recvAccount.getPrivialge() == 1)
			{
				//TO:DO Adjust the buttons
			}
			System.out.println(recvAccount.getAccountID());
			System.out.println(recvAccount.getEmail());
			System.out.println(recvAccount.getFullName());
			//System.out.println(recvAccount.getAddress());
			//System.out.println(recvAccount.getCreditCardNumber());
			//System.out.println(recvAccount.getCreditMonthExpire());
			System.out.println("Acc Priv: " + recvAccount.getPrivialge());
			currentLoggedAccount = recvAccount;
			System.out.println(" Current Priv : " + currentLoggedAccount.getPrivialge());
			SimpleClient.setAccount(currentLoggedAccount);
			applyPrivilegeBasedUI();
			refreshCatalogView();
			navigateAfterLogin(currentLoggedAccount);
		});

	}

	@Subscribe
	public void handleCatalogRefresh(CatalogRefreshEvent event) {
		Platform.runLater(() -> {
			requestCatalogReload();
			applyPrivilegeBasedUI();
			refreshCatalogView();
		});
	}
	@Subscribe
	public void retRieveDatabase(RetrieveDataBaseEvent rtEvent) {

		System.out.println("arrived to the retreivedatabse event");
		System.out.println("the current table is:");
		for (int i = 0; i < rtEvent.getRecievedList().size(); i++) {
			System.out.println(rtEvent.getRecievedList().get(i).getButton());
		}
		allProducts = rtEvent.getRecievedList();
		ensureProductMetadata(allProducts);
		resetFilteredProducts();
		Platform.runLater(() -> {
			updateFields(2);
			if (init_container != null) {
				init_container.setVisible(false);
			}
			if (justText != null) {
				justText.setVisible(false);
			}
		});


	}

	@Subscribe
	public void initDatabase(InitDatabaseEvent event) {

		System.out.println("arrived to databaseInit");
		allProducts.clear();
		resetFilteredProducts();
		showStatusMessage("Catalog data is loading from the server.");
		requestCatalogReload();

	}

	static Product getCurrent_button() {
		for (int i = 0; i < allProducts.size(); i++) {
			// Compare the button references directly.  The original code attempted to
			// call a non-existent method `equal(a,b)`.  In JavaFX each product has its
			// own Button instance, so pointer comparison is sufficient to identify
			// which product matches the current_button.
			if (allProducts.get(i).getButton() == current_button) {
				return allProducts.get(i);
			}
		}
		// As a fallback, return the first product if no match is found.  Ideally,
		// this case should not occur since current_button should always refer to
		// one of the product buttons.
		return allProducts.isEmpty() ? null : allProducts.get(0);
	}

	static void setCurrent_button(Product product) {
		current_button = product != null ? product.getButton() : null;
	}


	static void setReturnedFromSecondaryController(boolean retFromSecond) {
		returnedFromSecondaryController = retFromSecond;
	}


	static boolean getReturnedFromSecondaryController() {
		return returnedFromSecondaryController;
	}

	@FXML
	private Button compln;

	@FXML
	void complinstart(ActionEvent event) throws IOException {
		navigateInShell("complaint");

		// Pass the current logged account to the complaint controller via EventBus.  The controller
		// listens for PassAccountEventComplaints events and stores the account for submission.
		if (currentLoggedAccount != null) {
			PassAccountEventComplaints recievedAcc = new PassAccountEventComplaints(currentLoggedAccount);
			// Delay posting by a short time to ensure the new controller has registered
			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							EventBus.getDefault().post(recievedAcc);
						}
					}, 500);
		}
	}

	@FXML
	void clearFilters(ActionEvent event) {
		if (categoryFilter != null) {
			categoryFilter.getSelectionModel().clearSelection();
			categoryFilter.setPromptText("Category");
		}
		if (colorFilter != null) {
			colorFilter.getSelectionModel().clearSelection();
			colorFilter.setPromptText("Color");
		}
		if (priceFilter != null) {
			priceFilter.getSelectionModel().clearSelection();
			priceFilter.setPromptText("Price Range");
		}
		// Refresh the product display
		// Reset selections to "All" and reapply filters
		if (categoryFilter != null && categoryFilter.getItems().contains("All")) {
			categoryFilter.getSelectionModel().select("All");
		}
		if (colorFilter != null && colorFilter.getItems().contains("All")) {
			colorFilter.getSelectionModel().select("All");
		}
		if (priceFilter != null && priceFilter.getItems().contains("All")) {
			priceFilter.getSelectionModel().select("All");
		}
		applyFilters();
	}

	/**
	 * Apply the selected filters to the product cards.  Each of the six
	 * containers corresponds to a product in {@code allProducts}.  If a
	 * product does not satisfy the selected category, colour or price range,
	 * its container and controls will be hidden; otherwise they are shown.
	 */
	private void applyFilters() {
		String selectedCategory = (categoryFilter != null && categoryFilter.getValue() != null) ? categoryFilter.getValue() : "All";
		String selectedColor    = (colorFilter != null && colorFilter.getValue() != null) ? colorFilter.getValue() : "All";
		String selectedPrice    = (priceFilter != null && priceFilter.getValue() != null) ? priceFilter.getValue() : "All";

		List<Product> updatedFilteredProducts = new ArrayList<>();
		for (Product p : allProducts) {
			boolean matches = true;
			// Category filter
			if (!"All".equals(selectedCategory)) {
				if (p.getCategory() == null || !selectedCategory.equalsIgnoreCase(p.getCategory())) {
					matches = false;
				}
			}
			// Colour filter
			if (!"All".equals(selectedColor)) {
				if (p.getColor() == null || !selectedColor.equalsIgnoreCase(p.getColor())) {
					matches = false;
				}
			}
			// Price filter
			if (!"All".equals(selectedPrice)) {
				double price = PricingService.calculateDisplayPrice(p, currentLoggedAccount);
				try {
					String[] parts = selectedPrice.split("-");
					double min = Double.parseDouble(parts[0]);
					double max = Double.parseDouble(parts[1]);
					if (price < min || price > max) {
						matches = false;
					}
				} catch (Exception e) {
					// Ignore malformed price range
				}
			}
			if (matches) {
				updatedFilteredProducts.add(p);
			}
		}
		filteredProducts = updatedFilteredProducts;
		filtersApplied = true;
		CatalogSTARTIndex = 0;
		CatalogENDIndex = Math.min(6, filteredProducts.size());
		updateFields(1);
	}

	private void refreshCatalogView() {
		if (filtersApplied) {
			applyFilters();
			return;
		}
		if (!allProducts.isEmpty()) {
			updateFields(2);
		}
	}

	private void requestCatalogReload() {
		try {
			SimpleClient.getClient().sendToServer("first entry");
		} catch (IOException e) {
			System.out.println("Offline mode");
		}
	}

	private List<Product> getDisplayedProducts() {
		return filtersApplied ? filteredProducts : allProducts;
	}

	private void resetFilteredProducts() {
		filteredProducts = new ArrayList<>(allProducts);
		filtersApplied = false;
	}


	@FXML // fx:id="accbtn"
	private Button accbtn; // Value injected by FXMLLoader

	@FXML
	void accbtnlogin(ActionEvent event) throws IOException {
		System.out.println(".");
	}

	// ========================================
	// PRIVILEGE-BASED UI MANAGEMENT
	// ========================================

	/**
	 * Apply UI element visibility based on user privilege level
	 * Privilege 0 (Guest): Browse catalog only
	 * Privilege 1 (Customer): + Checkout, orders, complaints
	 * Privilege 2 (Worker): + Worker panel, deliveries
	 * Privilege 3 (Manager): + Admin dashboard, reports
	 * Privilege 4 (Chain Manager): + Network-wide access
	 */
	private void applyPrivilegeBasedUI() {
		// If no account is logged in yet (e.g., user opens catalog as guest),
		// default to privilege 0 to avoid NullPointerExceptions.  This ensures
		// the catalog can still be browsed without requiring authentication.
		Account account = SimpleClient.getUser();
		if (account == null) {
			hideAllPrivilegedFeatures();
			setAccountToolsPanelVisible(false);
			configureProductCardActions();
			System.out.println("=== Applying UI for privilege level: 0 (guest) ===");
			return;
		}
		currentLoggedAccount = account;
		int privilege = account.getPrivilegeLevel();
		System.out.println("=== Applying UI for privilege level: " + privilege + " ===");

		// GUEST (0): Can only browse catalog - all interactive features hidden
		if (privilege == 0) {
			hideAllPrivilegedFeatures();
			setAccountToolsPanelVisible(false);
			System.out.println("Guest mode: Browse-only access");
			return;
		}

		// CUSTOMER (1): Can browse + checkout + manage own orders/complaints
		enableCustomerFeatures();
		enableCustomerOnlyFeatures();
		setAccountToolsPanelVisible(privilege >= 2);
		System.out.println("Customer mode: Shopping and account management enabled");

		if (privilege >= 2) {
			// WORKER (2): Customer features + worker panel
			enableWorkerFeatures();
			setAccountToolsPanelVisible(true);
			System.out.println("Worker mode: Customer + Worker panel enabled");
		}
		if (privilege >= 3) {
			// MANAGER (3): Worker features + admin dashboard + reports
			enableManagerFeatures();
			System.out.println("Manager mode: Full branch admin access enabled");
		}
		if (privilege >= 4) {
			// CHAIN MANAGER (4): All features + network-wide access
			enableChainManagerFeatures();
			System.out.println("Chain Manager mode: Network-wide admin access enabled");
		}

		configureProductCardActions();
	}

	/**
	 * Hide all privileged features (reset to guest mode)
	 */
	private void hideAllPrivilegedFeatures() {
		// Customer features
		if (viewMyOrders != null) viewMyOrders.setVisible(false);
		if (viewMyComplaints != null) viewMyComplaints.setVisible(false);
		if (viewInboxPlz != null) viewInboxPlz.setVisible(false);
		showCartPanelForGuest();
		setAddToCartButtonsVisible(true);

		// Worker features
		if (deliveryButton != null) deliveryButton.setVisible(false);
		if (openComplaints != null) openComplaints.setVisible(false);
		if (catalogAddProductBtn != null) catalogAddProductBtn.setVisible(false);

		// Manager features
		if (infoo != null) infoo.setVisible(false);
		if (adminControlButtton != null) adminControlButtton.setVisible(false);
	}

	private void setAccountToolsPanelVisible(boolean visible) {
		if (accountToolsPanel != null) {
			accountToolsPanel.setVisible(visible);
			accountToolsPanel.setManaged(visible);
		}
	}

	private void showCartPanelForGuest() {
		if (cartTopText != null) {
			cartTopText.setVisible(true);
			cartTopText.setManaged(true);
		}
		if (CartItemsList != null) {
			CartItemsList.setVisible(true);
			CartItemsList.setManaged(true);
		}
		if (cartTextPrice != null) {
			cartTextPrice.setVisible(true);
			cartTextPrice.setManaged(true);
			cartTextPrice.setText("0");
		}
		if (checkout != null) {
			checkout.setVisible(true);
			checkout.setManaged(true);
			checkout.setDisable(false);
		}
		if (btnClearCart != null) {
			btnClearCart.setVisible(true);
			btnClearCart.setManaged(true);
		}
		refreshCartDisplay();
	}

	/**
	 * Enable customer features (privilege >= 1)
	 * Allows: Shopping cart, checkout, order management, complaints
	 */
	private void enableCustomerFeatures() {
		// Shopping cart and checkout
		if (checkout != null) {
			checkout.setVisible(true);
			checkout.setDisable(false);
		}
		if (cartTextPrice != null) cartTextPrice.setVisible(true);
		if (CartItemsList != null) CartItemsList.setVisible(true);
		if (cartTopText != null) cartTopText.setVisible(true);
		if (btnClearCart != null) btnClearCart.setVisible(true);

		// Add to cart buttons
		setAddToCartButtonsVisible(true);

		// Account management
		if (viewMyOrders != null) viewMyOrders.setVisible(true);
		if (viewMyComplaints != null) viewMyComplaints.setVisible(true);
		if (viewInboxPlz != null) viewInboxPlz.setVisible(true);

		System.out.println("  \u2713 Customer features enabled");
	}

	private void enableCustomerOnlyFeatures() {
		setSkuLabelsVisible(shouldShowCustomerOnlyFeatures());
	}

	/**
	 * Enable worker features (privilege >= 2)
	 * Allows: Worker panel, delivery management, complaint handling
	 */
	private void enableWorkerFeatures() {
		// Worker panel access
		if (deliveryButton != null) deliveryButton.setVisible(true);
		if (openComplaints != null) openComplaints.setVisible(true);
		if (catalogAddProductBtn != null) catalogAddProductBtn.setVisible(true);

		System.out.println("  \u2713 Worker features enabled");
	}

	/**
	 * Enable guest features (privilege = 0)
	 * Allows: catalog browsing, temporary cart management
	 */
	private void enableGuestFeatures() {
		if (btnClearCart != null) btnClearCart.setVisible(true);
		setAddToCartButtonsVisible(true);
	}

	private void setAddToCartButtonsVisible(boolean visible) {
		if (flower1_addCart != null) flower1_addCart.setVisible(visible);
		if (flower2_addCart != null) flower2_addCart.setVisible(visible);
		if (flower3_addCart != null) flower3_addCart.setVisible(visible);
		if (flower4_addCart != null) flower4_addCart.setVisible(visible);
		if (flower5_addCart != null) flower5_addCart.setVisible(visible);
		if (flower6_addCart != null) flower6_addCart.setVisible(visible);
	}

	private void setSkuLabelsVisible(boolean visible) {
		if (flower_sku1 != null) flower_sku1.setVisible(visible);
		if (flower_sku2 != null) flower_sku2.setVisible(visible);
		if (flower_sku3 != null) flower_sku3.setVisible(visible);
		if (flower_sku4 != null) flower_sku4.setVisible(visible);
		if (flower_sku5 != null) flower_sku5.setVisible(visible);
		if (flower_sku6 != null) flower_sku6.setVisible(visible);
	}

	private int resolveCurrentPrivilegeLevel() {
		if (currentLoggedAccount != null) {
			return currentLoggedAccount.getPrivialge();
		}
		Account sessionAccount = SimpleClient.getUser();
		if (sessionAccount != null) {
			return sessionAccount.getPrivilegeLevel();
		}
		return 0;
	}

	private boolean shouldShowCustomerOnlyFeatures() {
		return resolveCurrentPrivilegeLevel() == 1;
	}

	private boolean shouldShowSku() {
		return shouldShowCustomerOnlyFeatures();
	}

	private void addProductToCart(Product product) {
		if (product == null) {
			return;
		}

		CartService.getInstance().addProduct(product, 1);
		refreshCartDisplay();
	}

	private void refreshCartDisplay() {
		List<Product> items = CartService.getInstance().getItems();
		updateCartList(items);
		recalculateCartTotals(items);
	}

	private void updateCartList(List<Product> items) {
		if (CartItemsList == null) {
			return;
		}
		CartItemsList.getItems().setAll(buildCartLineLabels(items));
	}

	private List<String> buildCartLineLabels(List<Product> items) {
		List<CartLine> lines = buildCartLines(items);
		List<String> labels = new ArrayList<>();
		for (CartLine line : lines) {
			String label = line.quantity > 1
					? String.format("%s x%d", line.product.getName(), line.quantity)
					: line.product.getName();
			labels.add(label);
		}
		return labels;
	}

	private List<CartLine> buildCartLines(List<Product> items) {
		Map<String, CartLine> lines = new LinkedHashMap<>();
		if (items != null) {
			for (Product product : items) {
				if (product == null) {
					continue;
				}
				String key = product.getID() + "|" + product.getName() + "|" + product.getPrice();
				CartLine line = lines.get(key);
				if (line == null) {
					line = new CartLine(product, 0);
					lines.put(key, line);
				}
				line.quantity += 1;
			}
		}
		return new ArrayList<>(lines.values());
	}

	private void recalculateCartTotals(List<Product> items) {
		Account account = currentLoggedAccount != null ? currentLoggedAccount : SimpleClient.getUser();
		double total = 0.0;
		for (CartLine line : buildCartLines(items)) {
			PricingService.PricingResult pricing = PricingService.calculatePricing(line.product, account);
			total += pricing.getFinalPrice() * line.quantity;
		}
		total = PricingService.roundCurrency(total);
		if (cartTextPrice != null) {
			cartTextPrice.setText(String.format(Locale.US, "%.2f", total));
		}
	}

	private void recalculateCartTotals() {
		recalculateCartTotals(CartService.getInstance().getItems());
	}

	private static class CartLine {
		private final Product product;
		private int quantity;

		private CartLine(Product product, int quantity) {
			this.product = product;
			this.quantity = quantity;
		}
	}

	private PriceRange parsePriceRange(String rawValue) {
		if (rawValue == null) {
			return null;
		}
		String sanitized = rawValue.trim();
		if (sanitized.isEmpty()) {
			return null;
		}
		String normalized = sanitized.replaceAll("\\s+", "");
		String[] parts = normalized.split("-");
		try {
			if (parts.length == 1) {
				double value = Double.parseDouble(parts[0]);
				return new PriceRange(value, value);
			}
			if (parts.length == 2) {
				double min = Double.parseDouble(parts[0]);
				double max = Double.parseDouble(parts[1]);
				if (min > max) {
					double swap = min;
					min = max;
					max = swap;
				}
				return new PriceRange(min, max);
			}
		} catch (NumberFormatException ignored) {
			return null;
		}
		return null;
	}

	private static class PriceRange {
		private final double min;
		private final double max;

		private PriceRange(double min, double max) {
			this.min = Math.max(0, min);
			this.max = Math.max(0, max);
		}

		private double getSuggestedPrice() {
			return (min + max) / 2;
		}

		private String getDisplayText() {
			if (min == max) {
				return String.format(Locale.US, "%.0f", min);
			}
			return String.format(Locale.US, "%.0f-%.0f", min, max);
		}
	}
	/**
	 * Enable manager features (privilege >= 3)
	 * Allows: Admin dashboard, user management, reports
	 */
	private void enableManagerFeatures() {
		// Admin dashboard and controls
		if (infoo != null) infoo.setVisible(true);
		if (adminControlButtton != null) adminControlButtton.setVisible(true);

		System.out.println("  \u2713 Manager features enabled (Admin Dashboard)");
	}

	/**
	 * Enable chain manager features (privilege >= 4)
	 * Allows: Network-wide dashboard, global settings
	 */
	private void enableChainManagerFeatures() {
		// Chain manager gets all features (already enabled by manager mode)
		// Additional network-wide features can be added here
		System.out.println("  \u2713 Chain Manager features enabled (Network Dashboard)");
	}

	// ===============================================================
	// NAVIGATION METHODS FOR NEW PAGES (PHASES 1-5)
	// ===============================================================

	/**
	 * Navigate to Product Details page
	 * Shows detailed information about a specific product
	 */
	@FXML
	void openProductDetails(ActionEvent event) throws IOException {
		Product selected = getCurrent_button();
		if (selected == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Product Details");
			alert.setHeaderText("No product selected");
			alert.setContentText("Please select a product from the catalog first.");
			alert.showAndWait();
			return;
		}
		openProductDetailsModal(selected);
	}

	@FXML
	void onCustomItemClicked(ActionEvent event) {
		if (resolveCurrentPrivilegeLevel() < 1) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Login Required");
			alert.setHeaderText("Login required to start a custom order.");
			alert.setContentText("Please log in or register to start a custom item.");
			alert.showAndWait();
			navigateInShell("Login");
			return;
		}

		ensureCatalogDataLoaded();
		Product customProduct = null;
		for (Product product : allProducts) {
			if (product != null && product.isCustomProduct()) {
				customProduct = product;
				break;
			}
		}

		if (customProduct == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Custom Item");
			alert.setHeaderText("No custom items available");
			alert.setContentText("Custom products are not available at the moment.");
			alert.showAndWait();
			return;
		}

		openProductDetailsModal(customProduct);
	}

	/**
	 * Navigate to Order Confirmation page
	 * Shows confirmation after placing an order
	 */
	@FXML
	void openOrderConfirmation(ActionEvent event) throws IOException {
		navigateInShell("OrderConfirmation");
	}
	/**
	 * Navigate to Profile page
	 * Shows user profile and account settings
	 */
	@FXML
	void openProfile(ActionEvent event) throws IOException {
		navigateInShell("Profile");
	}
	/**
	 * Navigate to Order Details page
	 * Shows detailed information about a specific order
	 */
	@FXML
	void openOrderDetails(ActionEvent event) throws IOException {
		navigateInShell("OrderDetails");
	}

	/**
	 * Navigate to Worker Dashboard
	 * Shows worker panel with orders and tasks (Privilege >= 2 required)
	 */
	@FXML
	void openWorkerDashboard(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 2) {
			openAccessDenied(event, 2, "Worker Dashboard");
			return;
		}

		navigateInShell("WorkerDashboard");
	}

	/**
	 * Navigate to Branch Orders page
	 * Shows all orders for the branch (Privilege >= 2 required)
	 */
	@FXML
	void openBranchOrders(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 2) {
			openAccessDenied(event, 2, "Branch Orders");
			return;
		}

		navigateInShell("BranchOrders");
	}
	/**
	 * Navigate to Catalog Management page
	 * Allows workers to manage catalog items (Privilege >= 2 required)
	 */
	@FXML
	void openCatalogManagement(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 2) {
			openAccessDenied(event, 2, "Catalog Management");
			return;
		}

		navigateInShell("CatalogManagement");
	}
	/**
	 * Navigate to Branch Reports page
	 * Shows branch analytics with charts (Privilege >= 3 required)
	 */
	@FXML
	void openBranchReports(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 3) {
			openAccessDenied(event, 3, "Branch Reports");
			return;
		}

		navigateInShell("BranchReports");

	}

	/**
	 * Navigate to Promotions Management page
	 * Manage branch promotional campaigns (Privilege >= 3 required)
	 */
	@FXML
	void openPromotionsManagement(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 3) {
			openAccessDenied(event, 3, "Promotions Management");
			return;
		}

		navigateInShell("PromotionsManagement");

	}

	/**
	 * Navigate to Branch Settings page
	 * Configure branch settings (Privilege >= 3 required)
	 */
	@FXML
	void openBranchSettings(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 3) {
			openAccessDenied(event, 3, "Branch Settings");
			return;
		}

		navigateInShell("BranchSettings");

	}

	/**
	 * Navigate to Network Dashboard
	 * Network-wide overview with charts (Privilege >= 4 required)
	 */
	@FXML
	void openNetworkDashboard(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 4) {
			openAccessDenied(event, 4, "Network Dashboard");
			return;
		}

		navigateInShell("NetworkDashboard");

	}

	/**
	 * Navigate to Cross-Branch Reports page
	 * Advanced analytics across all branches (Privilege >= 4 required)
	 */
	@FXML
	void openCrossBranchReports(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 4) {
			openAccessDenied(event, 4, "Cross-Branch Reports");
			return;
		}

		navigateInShell("CrossBranchReports");

	}

	/**
	 * Navigate to Global Settings page
	 * Network-wide configuration (Privilege >= 4 required)
	 */
	@FXML
	void openGlobalSettings(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 4) {
			openAccessDenied(event, 4, "Global Settings");
			return;
		}

		navigateInShell("GlobalSettings");

	}

	/**
	 * Navigate to Network Promotions page
	 * Chain-wide promotional campaigns (Privilege >= 4 required)
	 */
	@FXML
	void openNetworkPromotions(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 4) {
			openAccessDenied(event, 4, "Network Promotions");
			return;
		}

		navigateInShell("NetworkPromotions");

	}

	/**
	 * Navigate to Role Management page
	 * Manage user roles and privileges (Privilege >= 4 required)
	 */
	@FXML
	void openRoleManagement(ActionEvent event) throws IOException {
		// Check privilege level
		if (SimpleClient.getClient().getUser() == null || SimpleClient.getClient().getUser().getPrivilegeLevel() < 4) {
			openAccessDenied(event, 4, "Role Management");
			return;
		}

		navigateInShell("RoleManagement");

	}

	/**
	 * Navigate to Error page
	 * Generic error display page
	 */
	@FXML
	void openError(ActionEvent event) throws IOException {
		navigateInShell("Error");

	}
	private boolean ensurePrivilege(ActionEvent event, int requiredPrivilege, String pageName) throws IOException {
		if (resolveCurrentPrivilegeLevel() < requiredPrivilege) {
			openAccessDenied(event, requiredPrivilege, pageName);
			return false;
		}
		return true;
	}
	/**
	 * Navigate to Access Denied page with custom privilege information
	 */
	private void openAccessDenied(ActionEvent event, int requiredPrivilege, String pageName) throws IOException {
		int currentPrivilege = resolveCurrentPrivilegeLevel();

		AccessDeniedController.setAccessInfo(currentPrivilege, requiredPrivilege, pageName);

		navigateInShell("AccessDenied");

	}
}
