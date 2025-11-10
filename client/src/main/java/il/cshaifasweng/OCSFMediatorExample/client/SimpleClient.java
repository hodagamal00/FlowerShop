package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.util.List;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	private static Account currentUser = null; // Global current user storage

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println("handle start");

		if(msg instanceof RegistrationResultEvent) {
			RegistrationResultEvent resultEvent = (RegistrationResultEvent) msg;
			EventBus.getDefault().post(resultEvent);
			return;
		}

		if(msg instanceof List){
			System.out.println("arrived to msg instanceof LIST in simple client");
			List<Product> listt = (List<Product>) msg;
			//for(int i=0 ; i<5;i++) System.out.println(listt.get(i).getID());


			for(int i=0 ;i<listt.size();i++){
				System.out.println(listt.get(i).getName());
			}
			UpdateGuiEvent updateEvent = new UpdateGuiEvent(listt);
			EventBus.getDefault().post(updateEvent);

		}
		if(msg instanceof String){

			String recievedStr = (String)msg ;

			if(recievedStr.equals("not found")){
				System.out.println("didnt find a table"); // we didnt find the table , so we need to create 6 items at the beggings
				// now tell the primary controller that the table isnt found
				InitDatabaseEvent event = new InitDatabaseEvent(true);
				EventBus.getDefault().post(event);
			}
			if(recievedStr.equals("mail not found")){
				// post an event that the mail is not found
				MailChecker mailCheckEvent = new MailChecker(false);
				EventBus.getDefault().post(mailCheckEvent);
			}
			if(recievedStr.equals("wrong password")){
				// post an event that the password is incorrect
				MailChecker mailCheckEvent = new MailChecker(true);
				mailCheckEvent.setPasswordExists(false);
				EventBus.getDefault().post(mailCheckEvent);
			}
			if(recievedStr.equals("found mail and password"))
			{
				MailChecker mailCheckEvent = new MailChecker(true);
				mailCheckEvent.setPasswordExists(true);
				mailCheckEvent.setLoggedIn(false);
				EventBus.getDefault().post(mailCheckEvent);
			}
			if(recievedStr.equals("already logged"))
			{
				MailChecker mailCheckEvent = new MailChecker(true);
				mailCheckEvent.setPasswordExists(true);
				mailCheckEvent.setLoggedIn(true);
				EventBus.getDefault().post(mailCheckEvent);
			}
			if(recievedStr.startsWith("registration_failed:"))
			{
				String reason = recievedStr.substring("registration_failed:".length());
				String message = "Unable to complete registration.";
				if(reason.equals("email_exists")){
					message = "An account with this email already exists.";
				}
				EventBus.getDefault().post(new RegistrationResultEvent(false, message));
			}
		}
		else if(msg instanceof FoundTable){
			FoundTable ft = (FoundTable) msg ;
			if(ft.getMessage().equals("managers table found") || ft.getMessage().equals("workers table found")){
				EventBus.getDefault().post(ft);
			}
			else {
				List<Product> ftList = ft.getRecievedProducts();
				RetrieveDataBaseEvent retEvent = new RetrieveDataBaseEvent(ftList);
				EventBus.getDefault().post(retEvent);
			}
		}
		else if(msg instanceof Account){ // added today
			System.out.println("the server sent me the account , NICE !!");
			Account recAcc = (Account)msg;
			System.out.println("the server sent me the account , NICE 2 !!");
			// Set current user globally
			setCurrentUser(recAcc);
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

		else if(msg instanceof Manager){ // added today
			System.out.println("the server sent me the account , NICE !!");
			Manager recAcc = (Manager) msg;
			System.out.println("the server sent me the account , NICE 2 !!");
			// Set current user globally (Manager extends Account)
			setCurrentUser((Account)recAcc);
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

		else if(msg instanceof Worker){ // added today
			System.out.println("the server sent me the account , NICE !!");
			Worker recWorker = (Worker) msg;
			System.out.println("the server sent me the account , NICE 2 !!");
			// Set current user globally (Worker extends Account)
			setCurrentUser((Account)recWorker);
			PassAccountEvent recievedAcc = new PassAccountEvent(recWorker);
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
		else if(msg instanceof getAllOrdersMessage){ // added 18/7
			getAllOrdersMessage recievedOrders = (getAllOrdersMessage) msg;
			System.out.println("the server sent me orders");
			PassOrdersFromServer passOrders = new PassOrdersFromServer();
			passOrders.setRecievedOrders(recievedOrders.getOrderList());

			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							EventBus.getDefault().post(passOrders);
							System.out.println("the server sent me the account , NICE 4 !!");
						}
					},4000
			);

		}
		else if(msg instanceof GetAllComplaints){ // added new 21/7
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
		else if(msg instanceof GetAllMessages){ // added 16.8
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
		else if(msg instanceof GetAllAccounts){
			System.out.println("Get Accounts Test 1");
			GetAllAccounts recievedAccounts = (GetAllAccounts) msg;
			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							EventBus.getDefault().post(recievedAccounts.getAll_accounts());
						}
					},4000
			);
		}
	}


	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

	/**
	 * Get the current logged-in user
	 * @return The current user Account, Manager, or Worker object, or null if not logged in
	 */
	public static Account getUser() {
		return currentUser;
	}

	/**
	 * Get the current logged-in user (alias method for compatibility)
	 * @return The current user Account, Manager, or Worker object, or null if not logged in
	 */
	public static Account getAccount() {
		return currentUser;
	}

	/**
	 * Set the current logged-in user (called internally when account events are received)
	 */
	public static void setAccount(Account user) {
		currentUser = user;
	}

	/**
	 * Set the current logged-in user (internal method)
	 */
	static void setCurrentUser(Account user) {
		currentUser = user;
	}

}
