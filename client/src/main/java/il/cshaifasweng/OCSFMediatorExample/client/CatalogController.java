package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;

// Removed unused AWT imports.  Including AWT packages alongside JavaFX
// introduces ambiguous references for classes like Button and List.  This
// controller uses JavaFX exclusively, so AWT imports are unnecessary and
// problematic.
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
// Added for detailed product navigation
import javafx.scene.layout.AnchorPane;
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
	@FXML // fx:id="adminEditCatalog"
	private ComboBox<String> adminEditCatalog; // Value injected by FXMLLoader

	@FXML // fx:id="adminEditCatalog"
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

	@FXML // fx:id="EditItemDesc"
	private TextField EditItemDesc; // Value injected by FXMLLoader

	@FXML // fx:id="EditItemPrice"
	private TextField EditItemPrice; // Value injected by FXMLLoader

	@FXML // fx:id="EditItemType"
	private TextField EditItemType; // Value injected by FXMLLoader


	@FXML // fx:id="EditItemExtra"
	private TextField EditItemExtra; // Value injected by FXMLLoader

	@FXML // fx:id="customPrice"
	private TextField customPrice; // Value injected by FXMLLoader

	@FXML
	private TextField customid;

	@FXML // fx:id="CreateCustomItem"
	public Button CreateCustomItem; // Value injected by FXMLLoader

	@FXML
	public Button infoo;

	@FXML // fx:id="CancelCustomItem"
	private Button CancelCustomItem; // Value injected by FXMLLoader

	@FXML // fx:id="FinishCustomItem"
	private Button FinishCustomItem; // Value injected by FXMLLoader


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


	@FXML // fx:id="AddItem"
	private Button AddItem; // Value injected by FXMLLoader

	//@FXML // fx:id="AddItem"
	//private Button remID;

	@FXML // fx:id="RemoveItem"
	private Button RemoveItem; // Value injected by FXMLLoader

	@FXML // fx:id="UpdateItem"
	private Button UpdateItem; // Value injected by FXMLLoader

	@FXML
	private Button printProd;

	@FXML
	private Text justText;

	@FXML
	private Button nextPage;

	@FXML
	private Button prevPage;


	@FXML
	private ComboBox<String> chooseCustomColor;

	@FXML
	private ComboBox<String> chooseCustomType;

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
	private Button viewCart;

	@FXML
	private TextField cartTextDiscount;

	@FXML
	private TextField cartTextPrice;

	@FXML
	private Text cartTextPriceDiscount;

	@FXML
	private Text cartTextPriceFinal;


	@FXML // fx:id="customError"
	private Text customError; // Value injected by FXMLLoader

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
		LogOut logOutObject = new LogOut();
		if (currentLoggedAccount != null) {
			logOutObject.setMail(currentLoggedAccount.getEmail());
		}

		try {
			System.out.println("before sending the logout object" );
			SimpleClient.getClient().sendToServer(logOutObject);
		} catch (IOException e) {
			e.printStackTrace();
		}

		currentLoggedAccount = null;
		SimpleClient.setAccount(null);
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
		if (!ensureLoggedInForCart()) {
			return;
		}

		int index = CatalogSTARTIndex + offset;
		List<Product> displayProducts = getDisplayedProducts();
		if (index < 0 || index >= displayProducts.size()) {
			return; // حماية من IndexOutOfBounds
		}

		// السعر الحالي (إذا الحقل فاضي يبدأ من 0)
		int basePrice = 0;
		if (!cartTextPrice.getText().isEmpty()) {
			basePrice = Integer.parseInt(cartTextPrice.getText());
		}

		int addedPrice = (int) Math.round(displayProducts.get(index).getPrice());
		basePrice += addedPrice;

		// إضافة المنتج
		Product selectedProduct = displayProducts.get(index);
		CartItemsList.getItems().add(selectedProduct.getName());
		CartService.getInstance().addProduct(selectedProduct, 1);

		// تحديث السعر قبل الخصم
		cartTextPrice.setText(String.valueOf(basePrice));

		// الخصم: 10% فقط إذا مشترك والمجموع أكبر من 50₪
		if (currentLoggedAccount != null
				&& currentLoggedAccount.isSubscription()
				&& basePrice > 50) {
			cartTextDiscount.setText(String.valueOf((int)(basePrice * 0.9)));
		} else {
			cartTextDiscount.setText(String.valueOf(basePrice));
		}
	}

	private boolean ensureLoggedInForCart() {
		if (resolveCurrentPrivilegeLevel() >= 1) {
			return true;
		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Login Required");
		alert.setHeaderText("Please log in to add items to your cart.");
		alert.setContentText("Guests can browse the catalog, but checkout and ordering require a user account.");
		alert.showAndWait();

		NavigationService.getInstance().navigate("Login");
		return false;
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
				Alert chooser = new Alert(Alert.AlertType.CONFIRMATION);
				chooser.setTitle("Choose Action");
				chooser.setHeaderText("What would you like to do?");
				chooser.setContentText("You can add this product to the cart or edit its details.");

				ButtonType addToCart = new ButtonType("Add to Cart", ButtonBar.ButtonData.OK_DONE);
				ButtonType editProduct = new ButtonType("Edit Product", ButtonBar.ButtonData.APPLY);
				ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

				chooser.getButtonTypes().setAll(addToCart, editProduct, cancel);

				Optional<ButtonType> result = chooser.showAndWait();

				if (result.isPresent()) {
					if (result.get() == addToCart) {
						addProductToCartByIndex(offset);
					} else if (result.get() == editProduct) {
						setCurrent_button(product);
						try {
							App.setRoot("secondary");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
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


	int cartViewBinary = 0;
	@FXML
	void viewUserCart(ActionEvent event)
	{
		if (!ensureLoggedInForCart()) {
			return;
		}
		syncCartFromService();
		boolean mode;
		if(cartViewBinary == 0)
		{
			nextPage.setVisible(false);
			prevPage.setVisible(false);
			mode = false;
			cartViewBinary++;
			viewCart.setText("Close Cart");

			CartItemsList.setVisible(true);
			cartTopText.setVisible(true);
			cartTextPrice.setVisible(true);
			cartTextDiscount.setVisible(true);
			cartTextPriceDiscount.setVisible(true);
			cartTextPriceFinal.setVisible(true);
		}
		else
		{
			nextPage.setVisible(true);
			prevPage.setVisible(true);
			mode = true;
			cartViewBinary--;
			viewCart.setText("View Cart");

			CartItemsList.setVisible(false);
			cartTopText.setVisible(false);
			cartTextPrice.setVisible(false);
			cartTextDiscount.setVisible(false);
			cartTextPriceDiscount.setVisible(false);
			cartTextPriceFinal.setVisible(false);
		}
		ViewItems(mode);
		viewAdminGUI(mode);
		CartItemsList.setVisible(!mode);
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
		if (!ensurePrivilege(event, 1, "Checkout")) {
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

	@FXML
	Product createCustomitem(ActionEvent event) {
		if (!ensureLoggedInForCart()) {
			return null;
		}

		FinishCustomItem.setText("Add Custom Item To Cart");
		CancelCustomItem.setText("Cancel Custom Item Designer");
		flower_button1.setVisible(true);
		flower_button2.setVisible(true);
		flower_button3.setVisible(true);
		flower_button4.setVisible(true);
		flower_button5.setVisible(true);
		flower_button6.setVisible(true);

		flower_price1.setVisible(true);
		flower_price2.setVisible(true);
		flower_price3.setVisible(true);
		flower_price4.setVisible(true);
		flower_price5.setVisible(true);
		flower_price6.setVisible(true);

		flower_name1.setVisible(true);
		flower_name2.setVisible(true);
		flower_name3.setVisible(true);
		flower_name4.setVisible(true);
		flower_name5.setVisible(true);
		flower_name6.setVisible(true);

		CreateCustomItem.setVisible(false);
		adminEditCatalog.setVisible(false);
		//	remID.setVisible(false);

		EditItemExtra.setVisible(false);
		EditItemDesc.setVisible(false);
		EditItemType.setVisible(false);
		EditItemPrice.setVisible(false);
		AddItem.setVisible(false);
		RemoveItem.setVisible(false);
		UpdateItem.setVisible(false);

		chooseCustomColor.setVisible(true);
		customPrice.setVisible(true);
		customid.setVisible(true);
		chooseCustomType.setVisible(true);

		CancelCustomItem.setVisible(true);
		FinishCustomItem.setVisible(true);

		// When creating a placeholder product, provide a numeric price (e.g., 0.0)
		// instead of a string to match the Product constructor signature.
		Product newProduct = new Product(0, "test", "test", "test", 0.0);  // Please insert real values
		//CREATE A NEW PRODUCT DYNAMICALLY
		// Tips: A global variable called ProductID which is incremented after each product created.
		// A function GetNextProductID that returns a fresh ID for the new product to be added.
		// Static fields and functions.
		return newProduct;
	}

	@FXML
	void cancelCustomitem(ActionEvent event) {
		//.setVisible(false);
		flower_button1.setVisible(true);
		flower_button2.setVisible(true);
		flower_button3.setVisible(true);
		flower_button4.setVisible(true);
		flower_button5.setVisible(true);
		flower_button6.setVisible(true);

		flower_price1.setVisible(true);
		flower_price2.setVisible(true);
		flower_price3.setVisible(true);
		flower_price4.setVisible(true);
		flower_price5.setVisible(true);
		flower_price6.setVisible(true);

		flower_name1.setVisible(true);
		flower_name2.setVisible(true);
		flower_name3.setVisible(true);
		flower_name4.setVisible(true);
		flower_name5.setVisible(true);
		flower_name6.setVisible(true);

		CreateCustomItem.setVisible(true);
		adminEditCatalog.setVisible(true);

		customid.setVisible(false);
		chooseCustomType.setVisible(false);
		chooseCustomColor.setVisible(false);
		customPrice.setVisible(false);

		CancelCustomItem.setVisible(false);
		FinishCustomItem.setVisible(false);
	}

	@FXML
	void addCartCustomitem(ActionEvent event)
	{
		if (!ensureLoggedInForCart()) {
			return;
		}
		customError.setVisible(false);
		boolean fail = false;
		if(chooseCustomType.getSelectionModel().getSelectedIndex() == -1)
		{
			customError.setText("Please choose a type");
			customError.setVisible(true);
			fail = true;
		}
		if(chooseCustomColor.getSelectionModel().getSelectedIndex() == -1)
		{
			customError.setText("Please choose a color");
			customError.setVisible(true);
			fail = true;
		}
		String customPriceString = customPrice.getText();
		PriceRange desiredRange = parsePriceRange(customPriceString);
		if (desiredRange == null) {
			fail = true;
			customError.setText("Please enter a valid price range (e.g., 50-120)");
			customError.setVisible(true);
		}

		if(fail == false)
		{
			String color = chooseCustomColor.getSelectionModel().getSelectedItem();
			String Type = chooseCustomType.getSelectionModel().getSelectedItem();
			// Parse the custom price text into a double before constructing the Product.
			double priceValue = desiredRange.getSuggestedPrice();
			// Use the selected type and color strings rather than the ComboBox objects themselves.
			String selectedType = chooseCustomType.getSelectionModel().getSelectedItem();
			String selectedColor = chooseCustomColor.getSelectionModel().getSelectedItem();
			Product product = new Product(0, "btn", "Custom Item", "A " + selectedType + " With dominant color " + selectedColor + " (Price Range " + desiredRange.getDisplayText() + ")", priceValue);

			addProductToCart(product);



			customid.setVisible(false);

			CancelCustomItem.setVisible(false);
			FinishCustomItem.setVisible(false);

			chooseCustomType.setVisible(false);
			chooseCustomColor.setVisible(false);
			customPrice.setVisible(false);
			CreateCustomItem.setVisible(true);
			customError.setVisible(false);
		}

	}


	@FXML
	void adminAddItemFunc(ActionEvent event) {

		String newType = EditItemType.getText();
		String newDesc = EditItemDesc.getText();
		String newPrice = EditItemPrice.getText();
		Product new_flower = new Product();
		new_flower.setPrice(Double.parseDouble(newPrice));
		new_flower.setName(newType);
		new_flower.setDetails(newDesc);
		flowersnum2++;
		new_flower.setID(flowersnum2);
		UpdateMessage updateMessage1 = new UpdateMessage("product", "add");
		updateMessage1.setProduct(new_flower);
		updateMessage1.setId(flowersnum2);
		System.out.println("before try - edit");
		try {
			System.out.println("before sending updateMessage to server ");
			SimpleClient.getClient().sendToServer(updateMessage1); // sends the updated product to the server class
			System.out.println("afater sending updateMessage to server ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CreateCustomItem.setVisible(true);
		adminEditCatalog.setVisible(true);

		//	remID.setVisible(false);
		chooseCustomColor.setVisible(false);
		customPrice.setVisible(false);
		customid.setVisible(false);
		chooseCustomType.setVisible(false);

		CancelCustomItem.setVisible(false);
		FinishCustomItem.setVisible(false);

		EditItemExtra.setVisible(false);

		if(CatalogENDIndex - CatalogSTARTIndex < 6)
		{
			CatalogENDIndex++;
		}
		showStatusMessage("Catalog Updated Successfully - 0 Errors");
		//	AddItem.setVisible(false);
	}

	@FXML
	void adminRemoveItemFunc(ActionEvent event) {

		String deleteID = EditItemExtra.getText();
		// Remove the item with the currnet ID from the catalog
		// create removeItem object
		// give the deleteID to the removeItem object
		// send the object to the server
		//System.out.println(deleteID);
		//	System.out.println(deleteID);
		UpdateMessage removeType = new UpdateMessage("product", "remove");
		removeType.setDelteId(deleteID);

		flowersnum2--;
		try {
			SimpleClient.getClient().sendToServer(removeType); // sends the updated product to the server class
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CreateCustomItem.setVisible(true);
		adminEditCatalog.setVisible(true);

		chooseCustomColor.setVisible(false);
		customPrice.setVisible(false);
		customid.setVisible(false);
		chooseCustomType.setVisible(false);

		CancelCustomItem.setVisible(false);
		FinishCustomItem.setVisible(false);

		EditItemType.setVisible(false);
		EditItemDesc.setVisible(false);
		EditItemPrice.setVisible(false);
		EditItemExtra.setVisible(false);
		//	RemoveItem.setVisible(false);
		//.setVisible(false);

		showStatusMessage("Catalog Updated Successfully - 0 Errors");
		RemoveItem.setVisible(false);

		allProducts.remove(Integer.parseInt(EditItemExtra.getText())-1);

		updateFields(2);

	}

	@FXML
	void adminUpdateItemFunc(ActionEvent event) {

		String TempType = allProducts.get(0).getName();
		String TempDesc = allProducts.get(0).getDetails();
		double TempPrice = allProducts.get(0).getPrice();

		String newType = EditItemType.getText();
		System.out.println(newType);
		String newDesc = EditItemDesc.getText();
		System.out.println(newDesc);
		String newPrice = EditItemPrice.getText();
		System.out.println(newPrice);
		String updateID = EditItemExtra.getText();
		System.out.println(updateID);

		List<Product> tempList = allProducts;
		int TargerID = Integer.parseInt(updateID) - 1;

		allProducts.get(TargerID).setName(newType);
		allProducts.get(TargerID).setPrice(Double.parseDouble(newPrice));
		allProducts.get(TargerID).setDetails(newDesc);

		Product currtProduct  = CatalogController.getCurrent_button();


		currtProduct.setPrice(Double.parseDouble(newPrice));
		currtProduct.setDetails(newDesc);
		currtProduct.setName(newType);
		int castedID = Integer.parseInt(updateID);
		currtProduct.setID(castedID);

		UpdateMessage updateMessage1 = new UpdateMessage("product","edit");
		updateMessage1.setProduct(currtProduct);


		updateMessage1.setId(castedID);
		System.out.println("arrived here before sending the updatemessage1");
		try {
			SimpleClient.getClient().sendToServer(updateMessage1); // sends the updated product to the server class
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		allProducts.get(0).setName(TempType);
		allProducts.get(0).setPrice(TempPrice);
		allProducts.get(0).setDetails(TempDesc);

		// Update the item with the current ID with the new variables
		//remID.setVisible(false);
		CreateCustomItem.setVisible(true);
		adminEditCatalog.setVisible(true);

		chooseCustomColor.setVisible(false);
		customPrice.setVisible(false);
		customid.setVisible(false);
		chooseCustomType.setVisible(false);

		CancelCustomItem.setVisible(false);
		FinishCustomItem.setVisible(false);

		EditItemType.setVisible(false);
		EditItemDesc.setVisible(false);
		EditItemPrice.setVisible(false);
		EditItemExtra.setVisible(false);
		//	UpdateItem.setVisible(false);

		updateFields(2);

		showStatusMessage("Catalog Updated Successfully - 0 Errors");
	}

	@FXML
	void chooseAdminEditCatalog(ActionEvent event) {
		//	remID.setVisible(false);
		CreateCustomItem.setVisible(false);
		adminEditCatalog.setVisible(true);

		chooseCustomColor.setVisible(false);
		customPrice.setVisible(false);
		customid.setVisible(false);
		chooseCustomType.setVisible(false);

		CancelCustomItem.setVisible(false);
		FinishCustomItem.setVisible(false);

		String chosen = adminEditCatalog.getSelectionModel().getSelectedItem();
		System.out.println(chosen);
		System.out.println("OKAY OKAY");
		if (chosen == "Add Item") {
			EditItemType.setText("New Item Type");
			EditItemDesc.setText("New Item Desc");
			EditItemPrice.setText("New Item Price");
			EditItemType.setVisible(true);
			EditItemDesc.setVisible(true);
			EditItemPrice.setVisible(true);
			EditItemExtra.setVisible(false);
			System.out.println("WENT ADD");
			RemoveItem.setVisible(false);
			UpdateItem.setVisible(false);
			AddItem.setVisible(true);
		}
		if (chosen == "Remove Item") {
			//EditItemExtra.setText("Item ID To Remove");
			//UpdateItem.setText("Remove");
			EditItemType.setVisible(false);
			EditItemDesc.setVisible(false);
			EditItemPrice.setVisible(false);
			EditItemExtra.setVisible(true);
			EditItemExtra.setText("ID to remove");
			//.setVisible(true);
			System.out.println("WENT REMOVE");
			AddItem.setVisible(false);
			UpdateItem.setVisible(false);
			RemoveItem.setVisible(true);
			//send id to server
		}
		if (chosen == "Edit Item") {
			EditItemType.setText("New Item Type");
			EditItemDesc.setText("New Item Desc");
			EditItemPrice.setText("New Item Price");
			EditItemExtra.setText("Item ID To Update");
			EditItemType.setVisible(true);
			EditItemDesc.setVisible(true);
			EditItemPrice.setVisible(true);
			EditItemExtra.setVisible(true);
			System.out.println("WENT UPDATE");
			AddItem.setVisible(false);
			RemoveItem.setVisible(false);
			UpdateItem.setVisible(true);
		}
	}


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
		if (price == Math.floor(price)) {
			return String.format(Locale.US, "%.0f₪", price);
		}
		return String.format(Locale.US, "%.2f₪", price);
	}

	private void updatePricingLabels(Product product, Label priceBadge, Label priceBefore, Label priceAfter, Label promoBadge) {
		double basePrice = product.getPrice();
		double actualPrice = product.getActualPrice();
		boolean hasPromotion = product.isPromotion() && product.getDiscountPercent() > 0 && actualPrice < basePrice;

		String formattedBase = formatPrice(basePrice);
		String formattedActual = formatPrice(actualPrice);

		priceBadge.setText(formattedActual);
		priceAfter.setText(formattedActual);
		priceBefore.setText(formattedBase);

		promoBadge.setVisible(hasPromotion);
		promoBadge.setManaged(hasPromotion);
		priceBefore.setVisible(hasPromotion);
		priceBefore.setManaged(hasPromotion);
		priceAfter.setVisible(true);
		priceAfter.setManaged(true);
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

			updatePricingLabels(displayProducts.get(0), flower_price1, flower_price_before1, flower_price_after1, flower_promo1);
			updatePricingLabels(displayProducts.get(1), flower_price2, flower_price_before2, flower_price_after2, flower_promo2);
			updatePricingLabels(displayProducts.get(2), flower_price3, flower_price_before3, flower_price_after3, flower_promo3);
			updatePricingLabels(displayProducts.get(3), flower_price4, flower_price_before4, flower_price_after4, flower_promo4);
			updatePricingLabels(displayProducts.get(4), flower_price5, flower_price_before5, flower_price_after5, flower_promo5);
			updatePricingLabels(displayProducts.get(5), flower_price6, flower_price_before6, flower_price_after6, flower_promo6);

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
			flower_promo2.setVisible(false);
			flower_promo3.setVisible(false);
			flower_promo4.setVisible(false);
			flower_promo5.setVisible(false);
			flower_promo6.setVisible(false);
			flower_promo1.setManaged(false);
			flower_promo2.setManaged(false);
			flower_promo3.setManaged(false);
			flower_promo4.setManaged(false);
			flower_promo5.setManaged(false);
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
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex), flower_price1, flower_price_before1, flower_price_after1, flower_promo1);
				flower_sku1.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex).getSku()));
				flower_category1.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex).getCategory()));
				flower_color1.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex).getColor()));
				flower_button1.setVisible(true);
				flower_price1.setVisible(true);
				flower_name1.setVisible(true);
				flower_sku1.setVisible(true);
				flower_category1.setVisible(true);
				flower_color1.setVisible(true);
				flower1_addCart.setVisible(true);
				container1.setVisible(true);
			}

			if (CatalogENDIndex - CatalogSTARTIndex > 1)
			{
				flower_name2.setText(displayProducts.get(CatalogSTARTIndex + 1).getName());
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 1), flower_price2, flower_price_before2, flower_price_after2, flower_promo2);
				flower_sku2.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 1).getSku()));
				flower_category2.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 1).getCategory()));
				flower_color2.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 1).getColor()));
				flower_button2.setVisible(true);
				flower_price2.setVisible(true);
				flower_name2.setVisible(true);
				flower_sku2.setVisible(true);
				flower_category2.setVisible(true);
				flower_color2.setVisible(true);
				flower2_addCart.setVisible(true);
				container2.setVisible(true);
			}
			if (CatalogENDIndex - CatalogSTARTIndex > 2)
			{
				flower_name3.setText(displayProducts.get(CatalogSTARTIndex + 2).getName());
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 2), flower_price3, flower_price_before3, flower_price_after3, flower_promo3);
				flower_sku3.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 2).getSku()));
				flower_category3.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 2).getCategory()));
				flower_color3.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 2).getColor()));
				flower_button3.setVisible(true);
				flower_price3.setVisible(true);
				flower_name3.setVisible(true);
				flower_sku3.setVisible(true);
				flower_category3.setVisible(true);
				flower_color3.setVisible(true);
				flower3_addCart.setVisible(true);
				container3.setVisible(true);
			}
			if (CatalogENDIndex - CatalogSTARTIndex > 3)
			{
				flower_name4.setText(displayProducts.get(CatalogSTARTIndex + 3).getName());
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 3), flower_price4, flower_price_before4, flower_price_after4, flower_promo4);
				flower_sku4.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 3).getSku()));
				flower_category4.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 3).getCategory()));
				flower_color4.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 3).getColor()));
				flower_button4.setVisible(true);
				flower_price4.setVisible(true);
				flower_name4.setVisible(true);
				flower_sku4.setVisible(true);
				flower_category4.setVisible(true);
				flower_color4.setVisible(true);
				flower4_addCart.setVisible(true);
				container4.setVisible(true);
			}
			if (CatalogENDIndex - CatalogSTARTIndex > 4)
			{
				flower_name5.setText(displayProducts.get(CatalogSTARTIndex + 4).getName());
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 4), flower_price5, flower_price_before5, flower_price_after5, flower_promo5);
				flower_sku5.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 4).getSku()));
				flower_category5.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 4).getCategory()));
				flower_color5.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 4).getColor()));
				flower_button5.setVisible(true);
				flower_price5.setVisible(true);
				flower_name5.setVisible(true);
				flower_sku5.setVisible(true);
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
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 5), flower_price6, flower_price_before6, flower_price_after6, flower_promo6);
				flower_sku6.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 5).getSku()));
				flower_category6.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 5).getCategory()));
				flower_color6.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 5).getColor()));
				flower_button6.setVisible(true);
				flower_price6.setVisible(true);
				flower_name6.setVisible(true);
				flower_sku6.setVisible(true);
				flower_category6.setVisible(true);
				flower_color6.setVisible(true);
				flower6_addCart.setVisible(true);
				container6.setVisible(true);
			}
			if (CatalogENDIndex - CatalogSTARTIndex == 6)
			{
				flower_name6.setText(displayProducts.get(CatalogSTARTIndex + 5).getName());
				updatePricingLabels(displayProducts.get(CatalogSTARTIndex + 5), flower_price6, flower_price_before6, flower_price_after6, flower_promo6);
				flower_sku6.setText(formatCatalogField("SKU", displayProducts.get(CatalogSTARTIndex + 5).getSku()));
				flower_category6.setText(formatCatalogField("Type", displayProducts.get(CatalogSTARTIndex + 5).getCategory()));
				flower_color6.setText(formatCatalogField("Color", displayProducts.get(CatalogSTARTIndex + 5).getColor()));
				flower_button6.setVisible(true);
				flower_price6.setVisible(true);
				flower_name6.setVisible(true);
				flower_sku6.setVisible(true);
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
			syncProductImageFromCatalog(selected, getImageViewForContainer(containerId));
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

		flower_sku1.setVisible(mode);
		flower_sku2.setVisible(mode);
		flower_sku3.setVisible(mode);
		flower_sku4.setVisible(mode);
		flower_sku5.setVisible(mode);
		flower_sku6.setVisible(mode);

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
	public void viewAdminGUI(boolean mode)
	{
		adminEditCatalog.setVisible(mode);
		worker_edit.setVisible(mode);
		CreateCustomItem.setVisible(mode);
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
		assert flower_price_after1 != null : "fx:id=\"flower_price_after1\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after2 != null : "fx:id=\"flower_price_after2\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after3 != null : "fx:id=\"flower_price_after3\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after4 != null : "fx:id=\"flower_price_after4\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after5 != null : "fx:id=\"flower_price_after5\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert flower_price_after6 != null : "fx:id=\"flower_price_after6\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert deliveryButton != null : "fx:id=\"deliveryButton\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert messageField != null : "fx:id=\"messageField\" was not injected: check your FXML file 'Catalog.fxml'.";
		assert customError != null : "fx:id=\"customError\" was not injected: check your FXML file 'Catalog.fxml'.";

		customError.setVisible(false);
		messageField.setVisible(false);
		if (viewMyOrders != null) viewMyOrders.setVisible(false);
		if (viewMyComplaints != null) viewMyComplaints.setVisible(false);
		if (deliveryButton != null) deliveryButton.setVisible(false);
		if (openComplaints != null) openComplaints.setVisible(false);
		if (infoo != null) infoo.setVisible(false);
		if (adminControlButtton != null) adminControlButtton.setVisible(false);
		if (viewInboxPlz != null) viewInboxPlz.setVisible(false);
		if (inboxList != null) inboxList.setVisible(false);
		if (openMessage != null) openMessage.setVisible(false);

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
		if (cartTextDiscount != null) cartTextDiscount.setVisible(false);
		if (cartTextPriceDiscount != null) cartTextPriceDiscount.setVisible(false);
		if (cartTextPriceFinal != null) cartTextPriceFinal.setVisible(false);
		if (CartItemsList != null) CartItemsList.setVisible(false);
		if (cartTopText != null) cartTopText.setVisible(false);
		flower1_addCart.setVisible(false);
		flower2_addCart.setVisible(false);
		flower3_addCart.setVisible(false);
		flower4_addCart.setVisible(false);
		flower5_addCart.setVisible(false);
		flower6_addCart.setVisible(false);
		CreateCustomItem.setVisible(false);
		adminEditCatalog.setVisible(false);

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
		chooseCustomType.setVisible(false);
		customPrice.setVisible(false);
		customid.setVisible(false);
		chooseCustomColor.setVisible(false);

		EditItemType.setVisible(false);
		EditItemDesc.setVisible(false);
		EditItemPrice.setVisible(false);
		EditItemExtra.setVisible(false);

		AddItem.setVisible(false);
		RemoveItem.setVisible(false);
		UpdateItem.setVisible(false);
		//remID.setVisible(false);
		CancelCustomItem.setVisible(false);
		FinishCustomItem.setVisible(false);

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
		flower_promo2.setVisible(false);
		flower_promo3.setVisible(false);
		flower_promo4.setVisible(false);
		flower_promo5.setVisible(false);
		flower_promo6.setVisible(false);
		flower_promo1.setManaged(false);
		flower_promo2.setManaged(false);
		flower_promo3.setManaged(false);
		flower_promo4.setManaged(false);
		flower_promo5.setManaged(false);
		flower_promo6.setManaged(false);
		flower_price_before1.setVisible(false);
		flower_price_before2.setVisible(false);
		flower_price_before3.setVisible(false);
		flower_price_before4.setVisible(false);
		flower_price_before5.setVisible(false);
		flower_price_before6.setVisible(false);
		flower_price_before1.setManaged(false);
		flower_price_before2.setManaged(false);
		flower_price_before3.setManaged(false);
		flower_price_before4.setManaged(false);
		flower_price_before5.setManaged(false);
		flower_price_before6.setManaged(false);
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

		adminEditCatalog.getItems().add("Add Item");
		adminEditCatalog.getItems().add("Remove Item");
		adminEditCatalog.getItems().add("Edit Item");

		chooseCustomColor.getItems().add("Red");
		chooseCustomColor.getItems().add("Blue");
		chooseCustomColor.getItems().add("Yellow");
		chooseCustomColor.getItems().add("White");
		chooseCustomColor.getItems().add("Purple");

		chooseCustomType.getItems().add("Arrangement");
		chooseCustomType.getItems().add("Bloom & Pot");
		chooseCustomType.getItems().add("Bouquet");
		chooseCustomType.getItems().add("Colletion");

		worker_edit.getItems().add("Add worker");
		worker_edit.getItems().add("Remove worker");
		worker_edit.getItems().add("Edit worker");

		viewCart.setVisible(false);
		//cartTopText.setVisible(false);
		//cartTextPrice.setVisible(false);
		//cartTextDiscount.setVisible(false);
		//cartTextPriceDiscount.setVisible(false);
		//cartTextPriceFinal.setVisible(false);

		// Populate filter combo boxes after data initialisation.  We only have six
		// products at present; categories and colours are pulled from the Product
		// objects.  Price ranges are hard coded for illustrative purposes.
		initializeData();
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
		cartTextPrice.setText("0");
		cartTextDiscount.setText("0");
		updateCartSummary(0);
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
			navigateAfterLogin(currentLoggedAccount);
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
		// When constructing Product instances we must pass the price as a double.
		// Label#getText() returns a String, so parse it to double before calling
		// the Product constructor.  This avoids "String cannot be converted to
		// double" compilation errors.
		double price1 = 0.0;
		double price2 = 0.0;
		double price3 = 0.0;
		double price4 = 0.0;
		double price5 = 0.0;
		double price6 = 0.0;
		try {
			// Strip any non-numeric characters (e.g. currency symbols) before parsing
			price1 = Double.parseDouble(flower_price1.getText().replaceAll("[^\\d.]", ""));
			price2 = Double.parseDouble(flower_price2.getText().replaceAll("[^\\d.]", ""));
			price3 = Double.parseDouble(flower_price3.getText().replaceAll("[^\\d.]", ""));
			price4 = Double.parseDouble(flower_price4.getText().replaceAll("[^\\d.]", ""));
			price5 = Double.parseDouble(flower_price5.getText().replaceAll("[^\\d.]", ""));
			price6 = Double.parseDouble(flower_price6.getText().replaceAll("[^\\d.]", ""));
		} catch (NumberFormatException ex) {
			// If parsing fails, leave default 0.0; you may want to handle this case
			// by showing an error to the user or skipping product creation
			ex.printStackTrace();
		}
		Product flower1 = new Product(1, flower_button1.getId(), flower_name1.getText(), "", price1);
		applyDefaultMetadata(flower1, 0);
		allProducts.add(flower1);
		Product flower2 = new Product(2, flower_button2.getId(), flower_name2.getText(), "", price2);
		applyDefaultMetadata(flower2, 1);
		allProducts.add(flower2);
		Product flower3 = new Product(3, flower_button3.getId(), flower_name3.getText(), "", price3);
		applyDefaultMetadata(flower3, 2);
		allProducts.add(flower3);
		Product flower4 = new Product(4, flower_button4.getId(), flower_name4.getText(), "", price4);
		applyDefaultMetadata(flower4, 3);
		allProducts.add(flower4);
		Product flower5 = new Product(5, flower_button5.getId(), flower_name5.getText(), "", price5);
		applyDefaultMetadata(flower5, 4);
		allProducts.add(flower5);
		Product flower6 = new Product(6, flower_button6.getId(), flower_name6.getText(), "", price6);
		applyDefaultMetadata(flower6, 5);
		allProducts.add(flower6);

		List<Product> productList = new ArrayList<Product>();
		productList.add(flower1);
		productList.add(flower2);
		productList.add(flower3);
		productList.add(flower4);
		productList.add(flower5);
		productList.add(flower6);
		try {
			SimpleClient.getClient().sendToServer(productList); // sends the updated product to the server class
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resetFilteredProducts();

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
				double price = p.getPrice();
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

	private List<Product> getDisplayedProducts() {
		return filtersApplied ? filteredProducts : allProducts;
	}

	private void resetFilteredProducts() {
		filteredProducts = new ArrayList<>(allProducts);
		filtersApplied = false;
	}


	@FXML // fx:id="RemoveItem"
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
			System.out.println("Guest mode: Browse-only access");
			return;
		}

		// CUSTOMER (1): Can browse + checkout + manage own orders/complaints
		enableCustomerFeatures();
		System.out.println("Customer mode: Shopping and account management enabled");

		if (privilege >= 2) {
			// WORKER (2): Customer features + worker panel
			enableWorkerFeatures();
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
		if (checkout != null) checkout.setVisible(false);
		if (cartTextPrice != null) cartTextPrice.setVisible(false);
		if (cartTextDiscount != null) cartTextDiscount.setVisible(false);
		if (cartTextPriceDiscount != null) cartTextPriceDiscount.setVisible(false);
		if (cartTextPriceFinal != null) cartTextPriceFinal.setVisible(false);
		if (CartItemsList != null) CartItemsList.setVisible(false);
		if (cartTopText != null) cartTopText.setVisible(false);
		if (viewCart != null) viewCart.setVisible(false);
		setAddToCartButtonsVisible(false);
		if (CreateCustomItem != null) CreateCustomItem.setVisible(false);

		// Worker features
		if (deliveryButton != null) deliveryButton.setVisible(false);
		if (openComplaints != null) openComplaints.setVisible(false);

		// Manager features
		if (infoo != null) infoo.setVisible(false);
		if (adminControlButtton != null) adminControlButtton.setVisible(false);
		if (adminEditCatalog != null) adminEditCatalog.setVisible(false);
	}

	/**
	 * Enable customer features (privilege >= 1)
	 * Allows: Shopping cart, checkout, order management, complaints
	 */
	private void enableCustomerFeatures() {
		// Shopping cart and checkout
		if (checkout != null) checkout.setVisible(true);
		if (cartTextPrice != null) cartTextPrice.setVisible(true);
		if (cartTextDiscount != null) cartTextDiscount.setVisible(true);
		if (cartTextPriceDiscount != null) cartTextPriceDiscount.setVisible(true);
		if (cartTextPriceFinal != null) cartTextPriceFinal.setVisible(true);
		if (CartItemsList != null) CartItemsList.setVisible(true);
		if (cartTopText != null) cartTopText.setVisible(true);
		if (viewCart != null) viewCart.setVisible(true);

		// Add to cart buttons
		setAddToCartButtonsVisible(true);

		// Custom products
		if (CreateCustomItem != null) CreateCustomItem.setVisible(true);

		// Account management
		if (viewMyOrders != null) viewMyOrders.setVisible(true);
		if (viewMyComplaints != null) viewMyComplaints.setVisible(true);
		if (viewInboxPlz != null) viewInboxPlz.setVisible(true);

		System.out.println("  \u2713 Customer features enabled");
	}

	/**
	 * Enable worker features (privilege >= 2)
	 * Allows: Worker panel, delivery management, complaint handling
	 */
	private void enableWorkerFeatures() {
		// Worker panel access
		if (deliveryButton != null) deliveryButton.setVisible(true);
		if (openComplaints != null) openComplaints.setVisible(true);

		System.out.println("  \u2713 Worker features enabled");
	}

	/**
	 * Enable guest features (privilege = 0)
	 * Allows: catalog browsing, temporary cart management
	 */
	private void enableGuestFeatures() {
		if (viewCart != null) viewCart.setVisible(true);
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

	private void syncCartFromService() {
		if (CartItemsList == null) {
			return;
		}
		CartItemsList.getItems().clear();
		int basePrice = 0;
		for (Product product : CartService.getInstance().getItems()) {
			if (product == null) {
				continue;
			}
			CartItemsList.getItems().add(product.getName());
			basePrice += (int) Math.round(product.getPrice());
		}
		updateCartSummary(basePrice);
	}

	private void addProductToCart(Product product) {
		if (product == null) {
			return;
		}

		if (CartItemsList != null) {
			CartItemsList.getItems().add(product.getName());
		}
		CartService.getInstance().addProduct(product, 1);

		int basePrice = parseCartTotal();
		basePrice += (int) Math.round(product.getPrice());
		updateCartSummary(basePrice);
	}

	private int parseCartTotal() {
		if (cartTextPrice == null) {
			return 0;
		}
		String value = cartTextPrice.getText();
		if (value == null || value.isBlank()) {
			return 0;
		}
		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException ignored) {
			return 0;
		}
	}

	private void updateCartSummary(int basePrice) {
		if (cartTextPrice != null) {
			cartTextPrice.setText(String.valueOf(basePrice));
		}

		boolean discountApplied = hasSubscriptionDiscount() && basePrice > 50;
		int discountedTotal = discountApplied ? (int) Math.round(basePrice * 0.9) : basePrice;

		if (cartTextDiscount != null) {
			cartTextDiscount.setText(String.valueOf(discountedTotal));
		}
		if (cartTextPriceDiscount != null) {
			cartTextPriceDiscount.setText(discountApplied ? "Subscriber discount applied" : "No discounts applied");
		}
		if (cartTextPriceFinal != null) {
			cartTextPriceFinal.setText("Final total: " + discountedTotal);
		}
	}

	private boolean hasSubscriptionDiscount() {
		if (currentLoggedAccount != null) {
			return currentLoggedAccount.isSubscription();
		}
		Account account = SimpleClient.getUser();
		return account != null && account.isSubscription();
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
		if (adminEditCatalog != null) adminEditCatalog.setVisible(true);

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
		syncProductImageFromCatalog(selected, getImageViewForProduct(selected));
		ProductDetailsController.setPendingProduct(selected);
		navigateInShell("ProductDetails");
	}

	private ImageView getImageViewForContainer(String containerId) {
		switch (containerId) {
			case "container1":
				return flower_button1;
			case "container2":
				return flower_button2;
			case "container3":
				return flower_button3;
			case "container4":
				return flower_button4;
			case "container5":
				return flower_button5;
			case "container6":
				return flower_button6;
			default:
				return null;
		}
	}

	private ImageView getImageViewForProduct(Product product) {
		if (product == null || product.getButton() == null) {
			return null;
		}
		String buttonId = product.getButton();
		if (buttonId.equals(flower_button1.getId())) {
			return flower_button1;
		}
		if (buttonId.equals(flower_button2.getId())) {
			return flower_button2;
		}
		if (buttonId.equals(flower_button3.getId())) {
			return flower_button3;
		}
		if (buttonId.equals(flower_button4.getId())) {
			return flower_button4;
		}
		if (buttonId.equals(flower_button5.getId())) {
			return flower_button5;
		}
		if (buttonId.equals(flower_button6.getId())) {
			return flower_button6;
		}
		return null;
	}

	private void syncProductImageFromCatalog(Product product, ImageView imageView) {
		if (product == null || imageView == null || imageView.getImage() == null) {
			return;
		}
		String imageUrl = imageView.getImage().getUrl();
		if (imageUrl != null && !imageUrl.isBlank()) {
			product.setImage(imageUrl);
		}
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
