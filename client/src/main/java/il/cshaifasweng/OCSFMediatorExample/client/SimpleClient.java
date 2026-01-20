package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;
import java.util.List;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;

	// Global current user (Account / Manager / Worker)
	private static Account currentUser = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println("handle start");

		// =========================
		// REGISTRATION RESULT
		// =========================
		if (msg instanceof RegistrationResultEvent) {
			RegistrationResultEvent resultEvent = (RegistrationResultEvent) msg;
			EventBus.getDefault().post(resultEvent);
			return;
		}

		// =========================
		// PRODUCT LIST (UPDATE GUI)
		// =========================
		if (msg instanceof List) {
			System.out.println("arrived to msg instanceof LIST in simple client");
			List<Product> listt = (List<Product>) msg;

			for (Product p : listt) {
				System.out.println(p.getName());
			}

			UpdateGuiEvent updateEvent = new UpdateGuiEvent(listt);
			EventBus.getDefault().post(updateEvent);
		}

		if (msg instanceof AddProductResponse) {
			AddProductResponse response = (AddProductResponse) msg;
			EventBus.getDefault().post(response);
			return;
		}

		if (msg instanceof UserUpdateResponse) {
			UserUpdateResponse response = (UserUpdateResponse) msg;
			EventBus.getDefault().post(response);
			return;
		}

		// =========================
		// STRING MESSAGES
		// =========================
		if (msg instanceof String) {

			String recievedStr = (String) msg;

			if (recievedStr.equals("not found")) {
				// products table not found - need to init DB
				System.out.println("didnt find a table");
				InitDatabaseEvent event = new InitDatabaseEvent(true);
				EventBus.getDefault().post(event);
			}

			if (recievedStr.equals("mail not found")) {
				MailChecker mailCheckEvent = new MailChecker(false);
				EventBus.getDefault().post(mailCheckEvent);
			}

			if (recievedStr.equals("wrong password")) {
				MailChecker mailCheckEvent = new MailChecker(true);
				mailCheckEvent.setPasswordExists(false);
				EventBus.getDefault().post(mailCheckEvent);
			}

			if (recievedStr.equals("found mail and password")) {
				MailChecker mailCheckEvent = new MailChecker(true);
				mailCheckEvent.setPasswordExists(true);
				mailCheckEvent.setLoggedIn(false);
				EventBus.getDefault().post(mailCheckEvent);
			}

			if (recievedStr.equals("already logged")) {
				MailChecker mailCheckEvent = new MailChecker(true);
				mailCheckEvent.setPasswordExists(true);
				mailCheckEvent.setLoggedIn(true);
				EventBus.getDefault().post(mailCheckEvent);
			}

			if (recievedStr.equals("account frozen")) {
				MailChecker mailCheckEvent = new MailChecker(true);
				mailCheckEvent.setPasswordExists(true);
				mailCheckEvent.setLoggedIn(false);
				mailCheckEvent.setFrozen(true);
				EventBus.getDefault().post(mailCheckEvent);
			}

			if (recievedStr.startsWith("registration_failed:")) {
				String reason = recievedStr.substring("registration_failed:".length());
				String message = "Unable to complete registration.";
				if (reason.equals("email_exists")) {
					message = "An account with this email already exists.";
				}
				EventBus.getDefault().post(new RegistrationResultEvent(false, message));
			}

			// Important: بعد ما نخلص حالات الـ String نرجع من الفنكشن
			// عشان ما يفوت على الـ else-if اللي تحت
			return;
		}

		// =========================
		// TABLE FOUND (PRODUCTS / MANAGERS / WORKERS)
		// =========================
		if (msg instanceof FoundTable) {
			FoundTable ft = (FoundTable) msg;
			if (ft.getMessage().equals("managers table found") ||
					ft.getMessage().equals("workers table found")) {
				EventBus.getDefault().post(ft);
			} else {
				List<Product> ftList = ft.getRecievedProducts();
				RetrieveDataBaseEvent retEvent = new RetrieveDataBaseEvent(ftList);
				EventBus.getDefault().post(retEvent);
			}
		}

		// =========================
		// ACCOUNT / MANAGER / WORKER (LOGIN RESULT)
		// =========================
		else if (msg instanceof Account) {
			System.out.println("the server sent me the account , NICE !!");
			Account recAcc = (Account) msg;
			System.out.println("the server sent me the account , NICE 2 !!");

			// Save globally
			setCurrentUser(recAcc);

			PassAccountEvent recievedAcc = new PassAccountEvent(recAcc);
			System.out.println("the server sent me the account , NICE 3 !!");
			EventBus.getDefault().post(recievedAcc);
			System.out.println("the server sent me the account , NICE 4 !!");
		}

		else if (msg instanceof Manager) {
			System.out.println("the server sent me the account (Manager) , NICE !!");
			Manager recAcc = (Manager) msg;
			System.out.println("the server sent me the account , NICE 2 !!");

			if (recAcc.getPrivilegeLevel() < 3) {
				int desiredPrivilege = recAcc.getShopID() == 0 ? 4 : 3;
				recAcc.setPrivialge(desiredPrivilege);
				System.out.println("Normalized manager privilege to " + desiredPrivilege);
			}

			setCurrentUser(recAcc);

			PassAccountEvent recievedAcc = new PassAccountEvent(recAcc);
			System.out.println("the server sent me the account , NICE 3 !!");
			EventBus.getDefault().post(recievedAcc);
			System.out.println("the server sent me the account , NICE 4 !!");
		}

		else if (msg instanceof Worker) {
			System.out.println("the server sent me the account (Worker) , NICE !!");
			Worker recWorker = (Worker) msg;
			System.out.println("the server sent me the account , NICE 2 !!");

			if (recWorker.getPrivilegeLevel() < 2) {
				recWorker.setPrivialge(2);
				System.out.println("Normalized worker privilege to 2");
			}

			setCurrentUser(recWorker);

			PassAccountEvent recievedAcc = new PassAccountEvent(recWorker);
			System.out.println("the server sent me the account , NICE 3 !!");
			EventBus.getDefault().post(recievedAcc);
			System.out.println("the server sent me the account , NICE 4 !!");
		}

		// =========================
		// ORDERS
		// =========================
		else if (msg instanceof getAllOrdersMessage) {
			getAllOrdersMessage recievedOrders = (getAllOrdersMessage) msg;
			System.out.println("the server sent me orders");
			PassOrdersFromServer passOrders = new PassOrdersFromServer();
			passOrders.setRecievedOrders(recievedOrders.getOrderList());

			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							EventBus.getDefault().post(passOrders);
							System.out.println("posted orders to EventBus");
						}
					}, 4000
			);
		}

		// =========================
		// COMPLAINTS
		// =========================
		else if (msg instanceof GetAllComplaints) {
			System.out.println("Get Complaints Test 1");
			GetAllComplaints recievedComps = (GetAllComplaints) msg;
			System.out.println("Get Complaints Test 2");
			PassAllComplaintsEvent complaintsEvent = new PassAllComplaintsEvent();
			System.out.println("Get Complaints Test 3");
			complaintsEvent.setComplaintsToPass(recievedComps.getComplaintsList());

			System.out.println("Comp List Size = " + recievedComps.getComplaintsList().size());
			System.out.println("Get Complaints Test 4");
			EventBus.getDefault().post(complaintsEvent);
			System.out.println("Get Complaints Test 5");
		}

		else if (msg instanceof NextComplaintIdMessage) {
			NextComplaintIdMessage response = (NextComplaintIdMessage) msg;
			EventBus.getDefault().post(new NextComplaintIdEvent(response.getNextComplaintId()));
		}

		// =========================
		// MESSAGES (INBOX)
		// =========================
		else if (msg instanceof GetAllMessages) {
			System.out.println("Get Messages Test 1");

			GetAllMessages recievedMessages = (GetAllMessages) msg;
			System.out.println("Get Messages Test 2");
			passAllMessagesEvent messagesEvent = new passAllMessagesEvent();
			System.out.println("Get Messages Test 3");
			messagesEvent.setMessagesToPasssToPass(recievedMessages.getMessageList());

			System.out.println("msg List Size = " + recievedMessages.getMessageList().size());
			System.out.println("Get messages Test 4");
			EventBus.getDefault().post(messagesEvent);
			System.out.println("Get messages Test 5");
		}

		// =========================
		// ACCOUNTS LIST (ADMIN)
		// =========================
		else if (msg instanceof GetAllAccounts) {
			System.out.println("Get Accounts Test 1");
			GetAllAccounts recievedAccounts = (GetAllAccounts) msg;
			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							EventBus.getDefault().post(recievedAccounts.getAll_accounts());
						}
					}, 4000
			);
		}
	}

	// =====================================================
	// CLIENT SINGLETON
	// =====================================================

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

	// =====================================================
	// GLOBAL USER ACCESSORS
	// =====================================================

	/**
	 * Alias for backward compatibility (old code يستخدم getUser()).
	 * يفضّل استخدام getAccount() في الكود الجديد.
	 */
	public static Account getUser() {
		return currentUser;
	}

	/**
	 * Get the current logged-in account (recommended).
	 */
	public static Account getAccount() {
		return currentUser;
	}

	/**
	 * Set the current logged-in account (used by controllers مثل PrimaryController).
	 */
	public static void setAccount(Account user) {
		currentUser = user;
	}

	/**
	 * Internal helper to update current user when login result يصل من السيرفر.
	 */
	static void setCurrentUser(Account user) {
		currentUser = user;
	}

	/**
	 * Logs out the current user and clears the local session.
	 * This should only be invoked from explicit logout actions or
	 * when the application is closing.
	 */
	public static void logoutCurrentUser() {
		Account account = currentUser;
		if (account != null) {
			LogOut logOut = new LogOut();
			logOut.setMail(account.getEmail());
			try {
				getClient().sendToServer(logOut);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		currentUser = null;
	}
}
