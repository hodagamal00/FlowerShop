package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.WorkerUpdateManager;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ManagerUpdateManager;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.OrderUpdateManager;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ComplaintUpdateManager;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SimpleServer extends AbstractServer {

private static SessionFactory cachedSessionFactory;
	private static final Object sessionFactoryLock = new Object();
	private List<Product> productGeneralList = new ArrayList<>();
	private List<Account> accountGeneralList = new ArrayList<>();
	private int flowersnum = 0;

	public SimpleServer(int port) {
		super(port);
	}

	public void Saveinsess() {
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				for (int i = 0; i < productGeneralList.size(); i++) {
					session.save(productGeneralList.get(i));
					session.flush();
				}
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	public static SessionFactory getSessionFactory() throws HibernateException {
		if (cachedSessionFactory != null) {
			return cachedSessionFactory;
		}

		synchronized (sessionFactoryLock) {
			if (cachedSessionFactory != null) {
				return cachedSessionFactory;
			}

			Configuration configuration = new Configuration();

			Properties properties = new Properties();
			try (InputStream input = SimpleServer.class.getClassLoader()
					.getResourceAsStream("hibernate.properties")) {
				if (input == null) {
					throw new HibernateException(
							"Unable to locate hibernate.properties on the classpath");
				}
				properties.load(input);
			} catch (IOException ex) {
				throw new HibernateException("Failed to load Hibernate configuration", ex);
			}

			configuration.setProperties(properties);

			configuration.addAnnotatedClass(Product.class);
			configuration.addAnnotatedClass(Account.class);
			configuration.addAnnotatedClass(Worker.class);
			configuration.addAnnotatedClass(Manager.class);
			configuration.addAnnotatedClass(Order.class);
			configuration.addAnnotatedClass(Complaint.class);
			configuration.addAnnotatedClass(Message.class);
			configuration.addAnnotatedClass(Report.class);
			configuration.addAnnotatedClass(Promotion.class);
			configuration.addAnnotatedClass(BranchSettings.class);
			configuration.addAnnotatedClass(GlobalSettings.class);

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties())
					.build();

			cachedSessionFactory = configuration.buildSessionFactory(serviceRegistry);
			DemoDataInitializer.initialize(cachedSessionFactory);
			return cachedSessionFactory;
		}
	}

	public static void generateProducts() {
		System.out.println("arrived to generate products function");
		Product product = new Product(5, "btn", "flower1", "someDetails", 5000.0);
		System.out.println("finisehd creating the product");
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				session.save(product);
				session.flush();
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client)
			throws SQLException, IOException {

		System.out.println("handleeeeeeeeeeee");
		System.out.println("msg class is " + msg.getClass());

		if (msg instanceof String) {
			SessionFactory sessionFactory = getSessionFactory();
			Session localSession = null;
			Transaction tx1 = null;
			try {
				localSession = sessionFactory.openSession();
				tx1 = localSession.beginTransaction();

				String recievedStr = (String) msg;
				if (recievedStr.equals("first entry")) {
					System.out.println("entered first entry");

					List<String> list = localSession.createSQLQuery("SHOW TABLES;").list();

					int tableFoundIndex = -1;
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).equals("products_table")) {
							tableFoundIndex = i;
						}
					}
					if (tableFoundIndex != -1) {
						if (countRows(localSession) == 0) {
							System.out.println("didnt find a table (this message is from the server");
							client.sendToClient("not found");
						} else {
							List<Product> resultList = getAllProducts(localSession);
							FoundTable foundTbl = new FoundTable("found", resultList);
							client.sendToClient(foundTbl);
						}
					} else {
						client.sendToClient("not found");
					}
				}

				if (recievedStr.equals("get Managers")) {
					Account requester = getClientAccount(client);
					if (requester == null || requester.getPrivilegeLevel() < 3) {
						sendAuthError(client, "Access denied");
					} else if (requiresBranchAssignment(requester) && resolveBranchId(requester) <= 0) {
						sendAuthError(client, "Access denied");
					} else {
						ManagerUpdateManager obj = new ManagerUpdateManager();
						FoundTable foundTbl = new FoundTable("managers table found");
						foundTbl.setRecievedManagers(filterManagersByScope(obj.managerGeneralList, requester));
						client.sendToClient(foundTbl);
					}
				}

				if (recievedStr.equals("get Workers")) {
					Account requester = getClientAccount(client);
					if (requester == null || requester.getPrivilegeLevel() < 3) {
						sendAuthError(client, "Access denied");
					} else if (requiresBranchAssignment(requester) && resolveBranchId(requester) <= 0) {
						sendAuthError(client, "Access denied");
					} else {
						WorkerUpdateManager obj = new WorkerUpdateManager();
						FoundTable foundTbl = new FoundTable("workers table found");
						foundTbl.setRecievedWorkers(filterWorkersByScope(obj.workerGeneralList, requester));
						client.sendToClient(foundTbl);
					}
				}

				if (recievedStr.equals("get complaints")) {
					Account account = getClientAccount(client);
					if (account == null || account.getPrivilegeLevel() < 2) {
						sendAuthError(client, "Access denied");
					} else if (requiresBranchAssignment(account) && resolveBranchId(account) <= 0) {
						sendAuthError(client, "Access denied");
					} else {
						GetAllComplaints obj = new GetAllComplaints();
						obj.setComplaintsList(getScopedComplaints(localSession, account));
						System.out.println("Comp List Size = " + obj.getComplaintsList().size());
						client.sendToClient(obj);
					}
				}

				if (recievedStr.equals("get Accounts")) {
					Account requester = getClientAccount(client);
					if (requester == null || requester.getPrivilegeLevel() < 4) {
						sendAuthError(client, "Access denied");
					} else if (requiresBranchAssignment(requester) && resolveBranchId(requester) <= 0) {
						sendAuthError(client, "Access denied");
					} else {
						System.out.println("get accounts test 1");
						GetAllAccounts obj = new GetAllAccounts();
						System.out.println("get accounts test 2");
						obj.setAll_accounts(getScopedAccounts(localSession, requester));
						System.out.println("get accounts test 3");
						client.sendToClient(obj);
						System.out.println("get accounts test 4");
					}
				}

				tx1.commit();
			} catch (Exception ex) {
				if (tx1 != null) {
					tx1.rollback();
				}
				throw ex;
			} finally {
				if (localSession != null) {
					localSession.close();
				}
			}
		}

		if (msg instanceof AddProductRequest) {
			handleAddProductRequest((AddProductRequest) msg, client);
			return;
		}

		if (msg instanceof NextComplaintIdMessage) {
			NextComplaintIdMessage request = (NextComplaintIdMessage) msg;
			int nextId = ComplaintUpdateManager.previewNextComplaintId();
			request.setNextComplaintId(nextId);
			client.sendToClient(request);
			return;
		}

		if (msg instanceof ReportDataRequest) {
			handleReportDataRequest((ReportDataRequest) msg, client);
			return;
		}

		if (msg instanceof UpdateMessage) {
			System.out.println("Arrived At UpdateMessage 1");
			UpdateMessage recievedMessage = (UpdateMessage) msg;
			String updateClassName = recievedMessage.getUpdateClass();
			String updateClassFunction = recievedMessage.getUpdateFunction();
			System.out.println("Arrived At UpdateMessage 2");

			if (requiresSystemManagerPrivileges(recievedMessage) && !isSystemManager(client)) {
				client.sendToClient(new UserUpdateResponse(false, "Unauthorized: system manager access required."));
				return;
			}

			switch (updateClassName) {
				case "product":
					if (!requirePrivilegeAtLeast(client, 3)) {
						break;
					}
					if (updateClassFunction.equals("add")) {
						System.out.println("arrived to here inside add");
						Product recievedProd = recievedMessage.getProduct();
						addItemToCatalog(recievedProd);
						sendToAllClients(loadAllProducts());
						System.out.println("send the updated list to the client !");
					} else if (updateClassFunction.equals("remove")) {
						String idToRemove = recievedMessage.getDelteId();
						removeItemFromCatalog(idToRemove, client);
						sendToAllClients(loadAllProducts());
					} else if (updateClassFunction.equals("edit")) {
						System.out.println("Arrived edit case in the switch !");
						Product recievedProd = recievedMessage.getProduct();
						editCatalogProduct(recievedProd);
						sendToAllClients(loadAllProducts());
					}
					break;

				case "account":
					if (!"add".equals(updateClassFunction) && !requirePrivilegeAtLeast(client, 4)) {
						break;
					}
					if (updateClassFunction.equals("add")) {
						System.out.println("arrived to here inside add");
						Account NewAcc = recievedMessage.getAccount();

						try {
							if (isEmailAlreadyRegistered(NewAcc.getEmail())) {
								System.out.println("Email already exists in database: " + NewAcc.getEmail());
								RegistrationResultEvent errorEvent =
										new RegistrationResultEvent(false,
												"This email address is already registered. Please use a different email or try logging in.");
								client.sendToClient(errorEvent);
								return;
							}

							NewAcc.setLoggedIn(true);
							Account savedAccount = addAccount(NewAcc);

							if (savedAccount != null) {
								System.out.println("Account added successfully: " + savedAccount.getEmail());
								client.sendToClient(savedAccount);
							} else {
								System.out.println("Failed to add account: " + NewAcc.getEmail());
								RegistrationResultEvent errorEvent =
										new RegistrationResultEvent(false, "Registration failed. Please try again.");
								client.sendToClient(errorEvent);
							}

						} catch (Exception e) {
							System.err.println("Error during account addition:");
							e.printStackTrace();
							RegistrationResultEvent errorEvent =
									new RegistrationResultEvent(false,
											"Registration failed due to a server error. Please try again later.");
							client.sendToClient(errorEvent);
						}

					} else if (updateClassFunction.equals("remove")) {
						String idToRemove = recievedMessage.getDelteId();
						removeAccount(idToRemove, client);
					} else if (updateClassFunction.equals("edit")) {
						System.out.println("Arrived edit account case in switch !");
						Account editAcc = recievedMessage.getAccount();
						try {
							editAccount(editAcc);
							client.sendToClient(new UserUpdateResponse(true, null));
						} catch (Exception e) {
							client.sendToClient(new UserUpdateResponse(false, "Failed to update account details."));
						}
					}
					break;

				case "worker":
					if (!requirePrivilegeAtLeast(client, 4)) {
						break;
					}
					if (updateClassFunction.equals("add")) {
						System.out.println("arrived to here inside worker add");
						Worker recievedWorker = recievedMessage.getWorker();
						WorkerUpdateManager.addWorker(recievedWorker);
					} else if (updateClassFunction.equals("remove")) {
						String idToRemove = recievedMessage.getDelteId();
						WorkerUpdateManager.removeWorker(idToRemove, client);
					} else if (updateClassFunction.equals("edit")) {
						System.out.println("Arrived edit worker case in switch !");
						Worker recievedWorker = recievedMessage.getWorker();
						try {
							WorkerUpdateManager.editWorker(recievedWorker);
							client.sendToClient(new UserUpdateResponse(true, null));
						} catch (Exception e) {
							client.sendToClient(new UserUpdateResponse(false, "Failed to update worker details."));
						}
					}
					break;

				case "manager":
					if (!requirePrivilegeAtLeast(client, 4)) {
						break;
					}
					if (updateClassFunction.equals("add")) {
						System.out.println("arrived to here inside manager add");
						Manager recievedManager = recievedMessage.getManager();
						ManagerUpdateManager.addManager(recievedManager);
					} else if (updateClassFunction.equals("remove")) {
						String idToRemove = recievedMessage.getDelteId();
						ManagerUpdateManager.removeManager(idToRemove, client);
					} else if (updateClassFunction.equals("edit")) {
						System.out.println("Arrived edit manager case in switch !");
						Manager recievedManager = recievedMessage.getManager();
						try {
							ManagerUpdateManager.editManager(recievedManager);
							client.sendToClient(new UserUpdateResponse(true, null));
						} catch (Exception e) {
							client.sendToClient(new UserUpdateResponse(false, "Failed to update manager details."));
						}
					}
					break;

					case "order":
						if (updateClassFunction.equals("add")) {
							System.out.println("arrived to here inside order add");
							Order recievedOrder = recievedMessage.getOrder();
							try {
								normalizeOrderForServer(recievedOrder, client);
								OrderUpdateManager.addOrder(recievedOrder);
								client.sendToClient(new UserUpdateResponse(true, null));
							} catch (IllegalArgumentException ex) {
								client.sendToClient(new UserUpdateResponse(false, ex.getMessage()));
								break;
							}
						} else if (updateClassFunction.equals("remove")) {
							if (!requirePrivilegeAtLeast(client, 1) || !isCustomer(client)) {
								sendAuthError(client, "forbidden");
								break;
							}
							String idToRemove = recievedMessage.getDelteId();
							OrderUpdateManager.removeOrder(idToRemove, client);
						}
						break;

					case "complaint":
						System.out.println("Tried Adding Complaint");
						if (updateClassFunction.equals("add")) {
							if (!requirePrivilegeAtLeast(client, 1) || !isCustomer(client)) {
								sendAuthError(client, "forbidden");
								break;
							}
							System.out.println("arrived to here inside complaint add");
							Complaint recievedComp = recievedMessage.getComplaint();
							ComplaintUpdateManager.addComplaint(recievedComp);
						} else if (updateClassFunction.equals("edit")) {
							if (!requirePrivilegeAtLeast(client, 2)) {
								break;
							}
							System.out.println("arrived to here inside complaint edit");
							Complaint recievedComp = recievedMessage.getComplaint();
							boolean replyLate = ComplaintUpdateManager.editComplaint(recievedComp);
							if (replyLate) {
								client.sendToClient("Reply sent after 24 hours");
							}
						}
					break;

				case "message":
					if (updateClassFunction.equals("add")) {
						System.out.println("arrived to here inside message  add");
						Message recievedMESSAGE = recievedMessage.getMessage();
						addMessage(recievedMESSAGE);
					}
					break;
			}
		}

		System.out.println("Arrived At UpdateMessage 3");

	// ================== هنا عدّلنا منطق CHECKMAIL ==================
	if (msg instanceof CheckMail) {
			SessionFactory sessionFactory = getSessionFactory();
			Session localSession = null;
			Transaction tx1 = null;
			try {
				localSession = sessionFactory.openSession();
				tx1 = localSession.beginTransaction();

				CheckMail recievedMessage = (CheckMail) msg;
				String recievedMailStr = recievedMessage.getEmail();
				String recievedPasswordStr = recievedMessage.getPassword();
				String person = recievedMessage.getPerson();
				if (person == null || person.isBlank()) {
					person = "customer";
				}

				Account matchedAccount = null;
				boolean isEmployeeLogin = person.equalsIgnoreCase("employee")
						|| person.equalsIgnoreCase("worker")
						|| person.equalsIgnoreCase("manager");

				if (isEmployeeLogin) {
					Manager matchedManager = findAccountByEmail(localSession, Manager.class, recievedMailStr);
					if (matchedManager != null) {
						matchedAccount = matchedManager;
					} else {
						matchedAccount = findAccountByEmail(localSession, Worker.class, recievedMailStr);
					}
				} else {
					matchedAccount = findAccountByEmail(localSession, Account.class, recievedMailStr);
				}

				if (matchedAccount == null) {
					client.sendToClient("mail not found");
				} else {
					// DEBUG – اطبع الباسووردين مع الأطوال
					String dbPass = matchedAccount.getPassword() != null ? matchedAccount.getPassword() : "";
					String uiPass = recievedPasswordStr != null ? recievedPasswordStr : "";

					if (matchedAccount.getFrozen()) {
						client.sendToClient("account frozen");
						tx1.commit();
						return;
					}

					System.out.println("DEBUG LOGIN:");
					System.out.println("  DB email     = '" + matchedAccount.getEmail() + "'");
					System.out.println("  DB password  = '" + dbPass + "' (len=" + dbPass.length() + ")");
					System.out.println("  UI password  = '" + uiPass + "' (len=" + uiPass.length() + ")");

					// نقارن بعد trim عشان spaces مخفية ما تخرب
					if (!dbPass.trim().equals(uiPass.trim())) {
						client.sendToClient("wrong password");
					} else if (matchedAccount.getLoggedIn()) {
						client.sendToClient(new UserUpdateResponse(false, "User already logged in"));
					} else {
						matchedAccount.setLoggedIn(true);
						localSession.update(matchedAccount);

						client.setInfo("account", matchedAccount);
						client.sendToClient(matchedAccount);
						client.sendToClient("found mail and password");
					}
				}
				tx1.commit();
			} catch (Exception ex) {
				if (tx1 != null) {
					tx1.rollback();
				}
				throw ex;
			} finally {
				if (localSession != null) {
					localSession.close();
				}
			}
		}
		// ================== نهاية تعديل CHECKMAIL ==================

		if (msg instanceof MailClass) {
			System.out.println("arrived to msg instance of MailClass ");
			SessionFactory sessionFactory = getSessionFactory();
			Session localSession = null;
			Transaction tx1 = null;
			try {
				localSession = sessionFactory.openSession();
				tx1 = localSession.beginTransaction();
				List<Account> accountsList = getAllAccounts(localSession);

				MailClass recievedMessage = (MailClass) msg;
				String recievedMailStr = recievedMessage.getMail();

				Account matchedAccount = null;
				for (Account account : accountsList) {
					System.out.println(account.getEmail());
					if (account.getEmail().equals(recievedMailStr)) {
						matchedAccount = account;
						break;
					}
				}

				if (matchedAccount != null) {
					client.sendToClient(matchedAccount);
				} else {
					client.sendToClient("mail not found");
				}

				tx1.commit();
			} catch (Exception ex) {
				if (tx1 != null) {
					tx1.rollback();
				}
				throw ex;
			} finally {
				if (localSession != null) {
					localSession.close();
				}
			}
		}

		if (msg instanceof LogOut) {
			System.out.println("arrived to Logout in server 1");
			SessionFactory sessionFactory = getSessionFactory();
			Session localSession = null;
			Transaction tx1 = null;
			try {
				localSession = sessionFactory.openSession();
				tx1 = localSession.beginTransaction();

				System.out.println("arrived to Logout in server 2");
				LogOut recievedMessage = (LogOut) msg;
				String recievedMailStr = recievedMessage.getMail();
				System.out.println("the mail is: " + recievedMailStr);
				System.out.println("arrived to Logout in server 3");

				Account matchedAccount = findAccountByEmail(localSession, Account.class, recievedMailStr);
				if (matchedAccount != null) {
					System.out.println("arrived to Logout in server 5");
					Account updateAcc = localSession.load(Account.class, matchedAccount.getAccountID());
					updateAcc.setLoggedIn(false);
					System.out.println("arrived to Logout in server 6");
					localSession.update(updateAcc);
				}

				tx1.commit();
				System.out.println("arrived to Logout in server 7");
				client.setInfo("account", null);
			} catch (Exception ex) {
				if (tx1 != null) {
					tx1.rollback();
				}
				throw ex;
			} finally {
				if (localSession != null) {
					localSession.close();
				}
			}
		}

		if (msg instanceof getAllOrdersMessage) {
			SessionFactory sessionFactory = getSessionFactory();
			Session localSession = null;
			Transaction tx1 = null;
			try {
				localSession = sessionFactory.openSession();
				tx1 = localSession.beginTransaction();
				if (requirePrivilegeAtLeast(client, 1)) {
					Account account = getClientAccount(client);
					if (requiresBranchAssignment(account) && resolveBranchId(account) <= 0) {
						sendAuthError(client, "forbidden");
					} else {
						getAllOrdersMessage ordersToBeSent = new getAllOrdersMessage();
						System.out.println("arrived to get all orders in simple server ! \n");
						List<Order> orderList = getScopedOrders(localSession, account);
						ordersToBeSent.setOrderList(orderList);
						client.sendToClient(ordersToBeSent);
					}
				}
				tx1.commit();
			} catch (Exception ex) {
				if (tx1 != null) {
					tx1.rollback();
				}
				throw ex;
			} finally {
				if (localSession != null) {
					localSession.close();
				}
			}
		}

		if (msg instanceof CancelOrderRequest) {
			SessionFactory sessionFactory = getSessionFactory();
			Session localSession = null;
			Transaction tx1 = null;
			try {
				localSession = sessionFactory.openSession();
				tx1 = localSession.beginTransaction();
				CancelOrderRequest request = (CancelOrderRequest) msg;
				CancelOrderResponse response = handleCancelOrder(request, client, localSession);
				client.sendToClient(response);
				tx1.commit();
			} catch (Exception ex) {
				if (tx1 != null) {
					tx1.rollback();
				}
				throw ex;
			} finally {
				if (localSession != null) {
					localSession.close();
				}
			}
		}

		if (msg instanceof GetAllComplaints) {
			SessionFactory sessionFactory = getSessionFactory();
			Session localSession = null;
			Transaction tx1 = null;
			try {
				localSession = sessionFactory.openSession();
				tx1 = localSession.beginTransaction();

				System.out.println("arrived to getAllComplaints in server !");
				Account account = getClientAccount(client);
				if (account == null || account.getPrivilegeLevel() < 2) {
					sendAuthError(client, "Access denied");
				} else if (requiresBranchAssignment(account) && resolveBranchId(account) <= 0) {
					sendAuthError(client, "Access denied");
				} else {
					GetAllComplaints complaintsToClient = new GetAllComplaints();
					List<Complaint> recievedComplaints = getScopedComplaints(localSession, account);
					complaintsToClient.setComplaintsList(recievedComplaints);
					client.sendToClient(complaintsToClient);
				}

				tx1.commit();
			} catch (Exception ex) {
				if (tx1 != null) {
					tx1.rollback();
				}
				throw ex;
			} finally {
				if (localSession != null) {
					localSession.close();
				}
			}

		}

		if (msg instanceof GetAllMessages) {
			SessionFactory sessionFactory = getSessionFactory();
			Session localSession = null;
			Transaction tx1 = null;
			try {
				localSession = sessionFactory.openSession();
				tx1 = localSession.beginTransaction();

				System.out.println("arrived to getAllComplaints in server !");
				GetAllMessages messagesToClient = new GetAllMessages();
				List<Message> recievedMessages = getAllMessages(localSession);
				messagesToClient.setMessageList(recievedMessages);
				client.sendToClient(messagesToClient);
				tx1.commit();
			} catch (Exception ex) {
				if (tx1 != null) {
					tx1.rollback();
				}
				throw ex;
			} finally {
				if (localSession != null) {
					localSession.close();
				}
			}
		}

		if (msg instanceof Order) {
			Order recievedMessage = (Order) msg;
			int orderID = recievedMessage.getOrderID();
			OrderUpdateManager.deliveredOrder(orderID);
		}

		if (msg instanceof ArrayList) {
			System.out.println("Arrived here: msg instance of arrayList ");

			System.out.println("list size 11111 = " + flowersnum);

			SessionFactory sessionFactory = getSessionFactory();
			Session localSession = null;
			Transaction tx1 = null;
			try {
				localSession = sessionFactory.openSession();
				tx1 = localSession.beginTransaction();
				System.out.println("msg instance of arrayList ");

				List<Product> resultList = (List<Product>) msg;
				flowersnum = resultList.size();
				System.out.println("list size 2222 = " + flowersnum);
				for (int i = 0; i < resultList.size(); i++) {
					localSession.save(resultList.get(i));
					localSession.flush();
					System.out.println(resultList.get(i).getName());
				}
				tx1.commit();

				for (int i = 0; i < resultList.size(); i++) {
					productGeneralList.add(resultList.get(i));
					System.out.println(resultList.get(i).getName());
				}
			} catch (Exception ex) {
				if (tx1 != null) {
					tx1.rollback();
				}
				throw ex;
			} finally {
				if (localSession != null) {
					localSession.close();
				}
			}
		} else {
			// nothing
		}
	}

	private static List<Message> getAllMessages(Session session) {
		System.out.println("Arrived to getAllmessages 1");
		CriteriaBuilder builder = session.getCriteriaBuilder();
		System.out.println("Arrived to getAllMessages 2");
		CriteriaQuery<Message> query = builder.createQuery(Message.class);
		System.out.println("Arrived to getAllMessages 3");
		query.from(Message.class);
		System.out.println("Arrived to getAllMessages 4");
		List<Message> result = session.createQuery(query).getResultList();
		System.out.println("Arrived to getAllMessages 5");
		return result;
	}

	private void handleAddProductRequest(AddProductRequest request, ConnectionToClient client) throws IOException {
		Account account = getClientAccount(client);
		if (account == null || !Boolean.TRUE.equals(account.getLoggedIn()) || account.getPrivilegeLevel() < 3) {
			client.sendToClient(new AddProductResponse(false, "Unauthorized: manager access required.", null));
			return;
		}

		Product product = request.getProduct();
		String validationError = validateProduct(product);
		if (validationError != null) {
			client.sendToClient(new AddProductResponse(false, validationError, null));
			return;
		}

		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();

			try {
				if (isSkuAlreadyRegistered(product.getSku(), session)) {
					tx.rollback();
					client.sendToClient(new AddProductResponse(false, "Duplicate SKU: " + product.getSku(), null));
					return;
				}

				int newProductId = getNextProductId(session);
				product.setID(newProductId);

				session.save(product);
				session.flush();
				tx.commit();
				client.sendToClient(new AddProductResponse(true, null, product));
				sendToAllClients(getAllProducts(session));
			} catch (Exception exception) {
				tx.rollback();
				client.sendToClient(new AddProductResponse(false, "Failed to add product due to a server error.", null));
			}
		}
	}

	private boolean isSkuAlreadyRegistered(String sku, Session session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Product> root = query.from(Product.class);
		query.select(builder.count(root));
		query.where(builder.equal(builder.lower(root.get("sku")), sku.toLowerCase()));
		Long count = session.createQuery(query).getSingleResult();
		return count != null && count > 0;
	}

	private String validateProduct(Product product) {
		if (product == null) {
			return "Product data is missing.";
		}
		if (isBlank(product.getName())) {
			return "Product name is required.";
		}
		if (isBlank(product.getButton())) {
			return "Button label is required.";
		}
		if (isBlank(product.getSku())) {
			return "SKU is required.";
		}
		if (isBlank(product.getCategory())) {
			return "Category is required.";
		}
		if (isBlank(product.getColor())) {
			return "Color is required.";
		}
		if (isBlank(product.getDetails())) {
			return "Product details are required.";
		}
		if (product.getPrice() <= 0) {
			return "Price must be greater than 0.";
		}
		if (product.isPromotion()) {
			double discount = product.getDiscountPercent();
			if (discount < 0 || discount > 100) {
				return "Discount must be between 0 and 100.";
			}
		}
		if (product.isCustomProduct()) {
			if (isBlank(product.getCustomType())) {
				return "Custom type is required for custom products.";
			}
			double minPrice = product.getPriceRangeMin();
			double maxPrice = product.getPriceRangeMax();
			if (minPrice < 0 || maxPrice < 0) {
				return "Custom price range must be positive.";
			}
			if (minPrice > 0 && maxPrice > 0 && minPrice >= maxPrice) {
				return "Custom minimum price must be less than maximum price.";
			}
		}
		return null;
	}

	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		Account account = client != null ? (Account) client.getInfo("account") : null;
		if (account == null) {
			return;
		}
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				int accountId = account.getAccountID();
				Account managedAccount = null;
				if (accountId > 0) {
					managedAccount = session.get(account.getClass(), accountId);
					if (managedAccount == null && account.getClass() != Account.class) {
						managedAccount = session.get(Account.class, accountId);
					}
				}
				if (managedAccount != null) {
					managedAccount.setLoggedIn(false);
					session.update(managedAccount);
				}
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				System.err.println("Failed to clear login state on disconnect: " + ex.getMessage());
			}
		}
	}

	private boolean isManager(ConnectionToClient client) {
		Account account = getClientAccount(client);
		return account != null && account.getPrivilegeLevel() >= 3;
	}

	private boolean isWorker(ConnectionToClient client) {
		Account account = getClientAccount(client);
		return account != null && account.getPrivilegeLevel() == 2;
	}

	private boolean isCustomer(ConnectionToClient client) {
		Account account = getClientAccount(client);
		return account != null && account.getPrivilegeLevel() == 1;
	}

	private boolean isSystemManager(ConnectionToClient client) {
		Account account = getClientAccount(client);
		return account != null && account.getPrivilegeLevel() >= 4;
	}

	private Account getClientAccount(ConnectionToClient client) {
		if (client == null) {
			return null;
		}
		Account account = (Account) client.getInfo("account");
		if (account == null) {
			return null;
		}
		int accountId = account.getAccountID();
		if (accountId <= 0) {
			return account;
		}
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				Account refreshed = session.get(account.getClass(), accountId);
				if (refreshed == null && account.getClass() != Account.class) {
					refreshed = session.get(Account.class, accountId);
				}
				tx.commit();
				return refreshed != null ? refreshed : account;
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	private void sendAuthError(ConnectionToClient client, String message) throws IOException {
		client.sendToClient(new UserUpdateResponse(false, message));
	}

	private boolean requireLoggedIn(ConnectionToClient client) throws IOException {
		Account account = getClientAccount(client);
		if (account == null || !Boolean.TRUE.equals(account.getLoggedIn())) {
			sendAuthError(client, "not logged in");
			return false;
		}
		return true;
	}

	private boolean requirePrivilegeAtLeast(ConnectionToClient client, int minPrivilege) throws IOException {
		if (!requireLoggedIn(client)) {
			return false;
		}
		Account account = getClientAccount(client);
		if (account == null || account.getPrivilegeLevel() <= 0) {
			sendAuthError(client, "unauthorized");
			return false;
		}
		if (account.getPrivilegeLevel() < minPrivilege) {
			sendAuthError(client, "forbidden");
			return false;
		}
		return true;
	}

	private boolean requirePrivilege(ConnectionToClient client, int minPrivilege, String message) throws IOException {
		if (!requireLoggedIn(client)) {
			return false;
		}
		Account account = getClientAccount(client);
		if (account == null || account.getPrivilegeLevel() < minPrivilege) {
			client.sendToClient(new UserUpdateResponse(false, message));
			return false;
		}
		return true;
	}

	private boolean requiresSystemManagerPrivileges(UpdateMessage message) {
		if (message == null) {
			return false;
		}
		String updateClass = message.getUpdateClass();
		String updateFunction = message.getUpdateFunction();
		if (updateClass == null || updateFunction == null) {
			return false;
		}
		boolean isEdit = "edit".equals(updateFunction);
		boolean isUserDetails = "account".equals(updateClass)
				|| "worker".equals(updateClass)
				|| "manager".equals(updateClass);
		return isEdit && isUserDetails;
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private CancelOrderResponse handleCancelOrder(CancelOrderRequest request, ConnectionToClient client, Session session)
			throws IOException {
		Account account = getClientAccount(client);
		if (account == null || !Boolean.TRUE.equals(account.getLoggedIn())) {
			return new CancelOrderResponse(false, "Unauthorized: login required.", request.getOrderId(),
					0, 0, "NONE");
		}
		int orderId = request.getOrderId();
		Order order = session.get(Order.class, orderId);
		if (order == null) {
			return new CancelOrderResponse(false, "Order not found.", orderId, 0, 0, "NONE");
		}
		boolean isSystemManager = account.getPrivilegeLevel() >= 4;
		if (!isSystemManager && order.getAccountID() != account.getAccountID()) {
			return new CancelOrderResponse(false, "Unauthorized: order ownership required.", orderId, 0, 0, "NONE");
		}
		if (order.isCancelled()) {
			return new CancelOrderResponse(false, "Order already cancelled.", orderId,
					order.getRefundAmount(), refundPercentFromStatus(order.getRefundStatus()),
					order.getRefundStatus());
		}
		if (order.isDelivered()) {
			return new CancelOrderResponse(false, "Order already delivered.", orderId, 0, 0, "NONE");
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime deliveryTime;
		try {
			deliveryTime = LocalDateTime.of(
					order.getPrepareYear(),
					order.getPrepareMonth(),
					order.getPrepareDay(),
					order.getPrepareHour(),
					order.getPrepareMin()
			);
		} catch (DateTimeException ex) {
			return new CancelOrderResponse(false, "Order delivery time is invalid.", orderId, 0, 0, "NONE");
		}
		double refundFactor = order.calculateRefund(
				now.getDayOfMonth(),
				now.getMonthValue(),
				now.getYear(),
				now.getHour(),
				now.getMinute()
		);
		double refundAmount = order.getRefundAmount();
		String refundStatus = order.getRefundStatus();
		order.setCancelled(true);
		order.setCancelDay(now.getDayOfMonth());
		order.setCancelMonth(now.getMonthValue());
		order.setCancelYear(now.getYear());
		order.setCancelHour(now.getHour());
		order.setCancelMinute(now.getMinute());
		session.update(order);

		if (refundAmount > 0) {
			Account refundAccount = session.get(Account.class, order.getAccountID());
			if (refundAccount != null) {
				refundAccount.addCreditBalance(refundAmount);
				session.update(refundAccount);
			}
		}

		String message = String.format(Locale.US, "Order cancelled successfully. Refund amount: ₪%.2f", refundAmount);
		return new CancelOrderResponse(true, message, orderId,
				refundAmount, refundFactor * 100.0, refundStatus);
	}

	private double refundPercentFromStatus(String refundStatus) {
		if ("FULL".equalsIgnoreCase(refundStatus)) {
			return 100.0;
		}
		if ("HALF".equalsIgnoreCase(refundStatus)) {
			return 50.0;
		}
		return 0.0;
	}

	private void normalizeOrderForServer(Order order, ConnectionToClient client) {
		if (order == null) {
			throw new IllegalArgumentException("Order details are required.");
		}

		Account account = resolveAccount(client, order.getAccountID());
		if (account == null) {
			throw new IllegalArgumentException("Account is required to place an order.");
		}
		if (account.getFrozen()) {
			throw new IllegalArgumentException("Account is frozen.");
		}
		if (account.getPrivilegeLevel() != 1) {
			throw new IllegalArgumentException("Unauthorized: customer access required.");
		}
		order.setAccountID(account.getAccountID());

		try {
			LocalDateTime orderTime = order.getOrderDate();
			LocalDateTime deliveryTime = order.getDelivery_time();
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime immediateCutoff = now.plusHours(3);
			boolean isImmediate = !deliveryTime.isAfter(immediateCutoff);

			if (deliveryTime.isBefore(orderTime)) {
				throw new IllegalArgumentException("Requested delivery time cannot be before the order time.");
			}
			if (isImmediate) {
				if (!deliveryTime.isAfter(now) || deliveryTime.isAfter(immediateCutoff)) {
					throw new IllegalArgumentException("Immediate orders must be delivered within 3 hours");
				}
			} else if (!deliveryTime.isAfter(now)) {
				throw new IllegalArgumentException("Requested delivery time must be in the future.");
			}
		} catch (DateTimeException ex) {
			throw new IllegalArgumentException("Requested delivery time is invalid.", ex);
		}

		order.setDelivered(false);
		order.setCancelled(false);
		order.setRefundAmount(0);
		order.setRefundStatus("NONE");

		PricingResult pricing = calculateProductsTotal(order.getProducts(), account);
		order.setProducts(pricing.productsSummary);

		BigDecimal deliveryFee = roundCurrency(BigDecimal.valueOf(resolveDeliveryFee(order)));
		order.setDeliveryFee(deliveryFee.doubleValue());

		BigDecimal total = roundCurrency(pricing.total.add(deliveryFee));
		order.setTotalPrice(total.setScale(0, RoundingMode.HALF_UP).intValue());
	}

	private Account resolveAccount(ConnectionToClient client, int accountId) {
		Account account = getClientAccount(client);
		if (account != null) {
			return account;
		}
		if (accountId <= 0) {
			return null;
		}
		SessionFactory sessionFactory = getSessionFactory();
		try (Session localSession = sessionFactory.openSession()) {
			Transaction tx = localSession.beginTransaction();
			try {
				Account resolved = localSession.get(Account.class, accountId);
				tx.commit();
				return resolved;
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	private PricingResult calculateProductsTotal(String products, Account account) {
		if (isBlank(products)) {
			return new PricingResult(BigDecimal.ZERO, "");
		}
		BigDecimal total = BigDecimal.ZERO;
		StringBuilder normalizedProducts = new StringBuilder();
		SessionFactory sessionFactory = getSessionFactory();
		try (Session localSession = sessionFactory.openSession()) {
			Transaction tx = localSession.beginTransaction();
			try {
				for (String item : products.split("%")) {
					String trimmed = item.trim();
					if (trimmed.isEmpty()) {
						continue;
					}
					String[] parts = trimmed.split(" - ", 2);
					String productName = parts[0].trim();
					if (productName.isEmpty()) {
						continue;
					}
					Product product = findProductByName(localSession, productName);
					if (product == null) {
						throw new IllegalArgumentException("Unknown product: " + productName);
					}
					BigDecimal promoPrice = product.hasActivePromotion()
							? applyDiscount(BigDecimal.valueOf(product.getPrice()), product.getDiscountPercent())
							: roundCurrency(BigDecimal.valueOf(product.getPrice()));
					BigDecimal finalPrice = promoPrice;
					if (account != null && account.isSubscription() && promoPrice.compareTo(BigDecimal.valueOf(50.0)) > 0) {
						finalPrice = applyDiscount(promoPrice, 10.0);
					}
					finalPrice = roundCurrency(finalPrice);
					total = total.add(finalPrice);
					normalizedProducts
							.append("%")
							.append(productName)
							.append(" - ")
							.append(formatCurrency(finalPrice))
							.append("%");
				}
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
		return new PricingResult(total, normalizedProducts.toString());
	}

	private static BigDecimal applyDiscount(BigDecimal basePrice, double discountPercent) {
		BigDecimal normalizedDiscount = BigDecimal.valueOf(Product.normalizeDiscountPercent(discountPercent));
		BigDecimal discountFactor = BigDecimal.ONE.subtract(
				normalizedDiscount.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP));
		return basePrice.multiply(discountFactor);
	}

	private static BigDecimal roundCurrency(BigDecimal value) {
		return value.setScale(2, RoundingMode.HALF_UP);
	}

	private static String formatCurrency(BigDecimal value) {
		return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
	}

	private static class PricingResult {
		private final BigDecimal total;
		private final String productsSummary;

		private PricingResult(BigDecimal total, String productsSummary) {
			this.total = total;
			this.productsSummary = productsSummary;
		}
	}

	private Product findProductByName(Session session, String name) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> root = query.from(Product.class);
		query.select(root).where(builder.equal(builder.lower(root.get("name")), name.toLowerCase()));
		List<Product> results = session.createQuery(query).getResultList();
		if (results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}

	private double resolveDeliveryFee(Order order) {
		if (order.isPickUp()) {
			return 0;
		}
		SessionFactory sessionFactory = getSessionFactory();
		try (Session localSession = sessionFactory.openSession()) {
			Transaction tx = localSession.beginTransaction();
			try {
				BranchSettings settings = localSession.get(BranchSettings.class, order.getShopID());
				if (settings == null) {
					throw new IllegalArgumentException("Delivery configuration missing for shop " + order.getShopID());
				}
				if (!settings.isDeliveryEnabled()) {
					throw new IllegalArgumentException("Delivery is disabled for shop " + order.getShopID());
				}
				double fee = settings.getDeliveryFee();
				tx.commit();
				return fee;
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	private static List<Complaint> getAllComplaints(Session session) {
		System.out.println("Arrived to getAllComplaints 1");
		CriteriaBuilder builder = session.getCriteriaBuilder();
		System.out.println("Arrived to getAllCompliants 2");
		CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
		System.out.println("Arrived to getAllComplaints 3");
		query.from(Complaint.class);
		System.out.println("Arrived to getAllComplaints 4");
		List<Complaint> result = session.createQuery(query).getResultList();
		ComplaintUpdateManager.refreshComplaintSlaStatuses(session, result);
		System.out.println("Arrived to getAllComplaints 5");
		return result;
	}

	private static List<Complaint> getScopedComplaints(Session session, Account account) {
		System.out.println("Arrived to getScopedComplaints 1");
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
		Root<Complaint> root = query.from(Complaint.class);
		if (account != null) {
			int privilegeLevel = account.getPrivilegeLevel();
			if (privilegeLevel == 1) {
				query.where(builder.equal(root.get("CustomerID"), account.getAccountID()));
			} else if (privilegeLevel >= 2 && privilegeLevel < 4) {
				int branchId = resolveBranchId(account);
				if (branchId > 0) {
					query.where(builder.equal(root.get("shopID"), branchId));
				}
			}
		}
		List<Complaint> result = session.createQuery(query).getResultList();
		ComplaintUpdateManager.refreshComplaintSlaStatuses(session, result);
		System.out.println("Arrived to getScopedComplaints 2");
		return result;
	}

	private static List<Order> getAllOrders(Session session) {
		System.out.println("Arrived to getAllOrders 1");
		CriteriaBuilder builder = session.getCriteriaBuilder();
		System.out.println("Arrived to getAllOrders 2");
		CriteriaQuery<Order> query = builder.createQuery(Order.class);
		System.out.println("Arrived to getAllOrders 3");
		query.from(Order.class);
		System.out.println("Arrived to getAllOrders 4");
		List<Order> result = session.createQuery(query).getResultList();
		System.out.println("Arrived to getAllOrders 5");
		return result;
	}

	private ReportDataResponse buildReportDataResponse(Session session, ReportDataRequest request) {
		LocalDate startDate = request.getStartDate();
		LocalDate endDate = request.getEndDate();
		int branchId = request.getBranchId();

		List<Order> orders = getOrdersForReport(session, startDate, endDate, branchId);
		List<Complaint> complaints = getComplaintsForReport(session, startDate, endDate, branchId);
		List<BranchSettings> branches = getBranchesForReport(session, branchId);

		double totalRevenue = queryTotalRevenue(session, startDate, endDate, branchId);
		Map<String, Integer> ordersByProductType = queryOrdersByProductType(session, startDate, endDate, branchId);
		Map<LocalDate, Integer> complaintsHistogram = queryComplaintsHistogram(session, startDate, endDate, branchId);

		return new ReportDataResponse(
				true,
				null,
				request.getRequestId(),
				request.getPeriodLabel(),
				branchId,
				totalRevenue,
				ordersByProductType,
				complaintsHistogram,
				orders,
				complaints,
				branches
		);
	}

	private void handleReportDataRequest(ReportDataRequest request, ConnectionToClient client) throws IOException {
		Account account = getClientAccount(client);
		if (account == null || account.getPrivilegeLevel() < 3) {
			client.sendToClient(new ReportDataResponse(false, "Access denied", request.getRequestId(),
					request.getPeriodLabel(), request.getBranchId(), 0.0, null, null,
					Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
			return;
		}
		if (request.getBranchId() == 0 && account.getPrivilegeLevel() < 4) {
			client.sendToClient(new ReportDataResponse(false, "Access denied", request.getRequestId(),
					request.getPeriodLabel(), request.getBranchId(), 0.0, null, null,
					Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
			return;
		}
		if (requiresBranchAssignment(account) && request.getBranchId() > 0
				&& resolveBranchId(account) != request.getBranchId()) {
			client.sendToClient(new ReportDataResponse(false, "Access denied", request.getRequestId(),
					request.getPeriodLabel(), request.getBranchId(), 0.0, null, null,
					Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
			return;
		}

		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				ReportDataResponse response = buildReportDataResponse(session, request);
				tx.commit();
				client.sendToClient(response);
			} catch (Exception ex) {
				tx.rollback();
				client.sendToClient(new ReportDataResponse(false, "Failed to load report data.",
						request.getRequestId(), request.getPeriodLabel(), request.getBranchId(), 0.0,
						null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
			}
		}
	}

	private List<Order> getOrdersForReport(Session session, LocalDate startDate, LocalDate endDate, int branchId) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Order> query = builder.createQuery(Order.class);
		Root<Order> root = query.from(Order.class);
		if (branchId > 0) {
			query.where(builder.equal(root.get("shopID"), branchId));
		}
		List<Order> orders = session.createQuery(query).getResultList();
		if (startDate == null || endDate == null) {
			return orders;
		}
		List<Order> filtered = new ArrayList<>();
		for (Order order : orders) {
			LocalDate orderDate = resolveOrderDate(order);
			if (orderDate != null && isWithinRange(orderDate, startDate, endDate)) {
				filtered.add(order);
			}
		}
		return filtered;
	}

	private List<Complaint> getComplaintsForReport(Session session, LocalDate startDate, LocalDate endDate, int branchId) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
		Root<Complaint> root = query.from(Complaint.class);
		if (branchId > 0) {
			query.where(builder.equal(root.get("shopID"), branchId));
		}
		List<Complaint> complaints = session.createQuery(query).getResultList();
		if (startDate == null || endDate == null) {
			return complaints;
		}
		List<Complaint> filtered = new ArrayList<>();
		for (Complaint complaint : complaints) {
			LocalDate complaintDate = resolveComplaintDate(complaint);
			if (complaintDate != null && isWithinRange(complaintDate, startDate, endDate)) {
				filtered.add(complaint);
			}
		}
		return filtered;
	}

	private List<BranchSettings> getBranchesForReport(Session session, int branchId) {
		if (branchId <= 0) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<BranchSettings> query = builder.createQuery(BranchSettings.class);
			query.from(BranchSettings.class);
			return session.createQuery(query).getResultList();
		}
		BranchSettings settings = session.get(BranchSettings.class, branchId);
		if (settings == null) {
			return Collections.emptyList();
		}
		return Collections.singletonList(settings);
	}

	private double queryTotalRevenue(Session session, LocalDate startDate, LocalDate endDate, int branchId) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Double> query = builder.createQuery(Double.class);
		Root<Order> root = query.from(Order.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(root.get("isCancelled"), false));
		if (branchId > 0) {
			predicates.add(builder.equal(root.get("shopID"), branchId));
		}
		if (startDate != null && endDate != null) {
			predicates.add(buildOrderDateRangePredicate(builder, root, startDate, endDate));
		}
		query.select(builder.coalesce(builder.sumAsDouble(root.get("totalPrice")), 0.0))
				.where(predicates.toArray(new Predicate[0]));
		Double result = session.createQuery(query).getSingleResult();
		return result != null ? result : 0.0;
	}

	private Map<String, Integer> queryOrdersByProductType(Session session, LocalDate startDate, LocalDate endDate, int branchId) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Order> orderRoot = query.from(Order.class);
		Root<Product> productRoot = query.from(Product.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.greaterThan(
				builder.locate(builder.lower(orderRoot.get("Products")), builder.lower(productRoot.get("name"))), 0));
		if (branchId > 0) {
			predicates.add(builder.equal(orderRoot.get("shopID"), branchId));
		}
		if (startDate != null && endDate != null) {
			predicates.add(buildOrderDateRangePredicate(builder, orderRoot, startDate, endDate));
		}
		Expression<String> categoryExpr = builder.<String>selectCase()
				.when(builder.and(
						builder.isNotNull(productRoot.get("category")),
						builder.notEqual(builder.trim(productRoot.get("category")), "")),
						productRoot.get("category"))
				.when(builder.and(
						builder.isNotNull(productRoot.get("customType")),
						builder.notEqual(builder.trim(productRoot.get("customType")), "")),
						productRoot.get("customType"))
				.otherwise("Uncategorized");

		query.multiselect(categoryExpr, builder.count(productRoot))
				.where(predicates.toArray(new Predicate[0]))
				.groupBy(categoryExpr)
				.orderBy(builder.asc(categoryExpr));

		List<Object[]> results = session.createQuery(query).getResultList();
		Map<String, Integer> counts = new LinkedHashMap<>();
		for (Object[] row : results) {
			String category = row[0] != null ? row[0].toString() : "Uncategorized";
			Number count = (Number) row[1];
			counts.put(category, count != null ? count.intValue() : 0);
		}
		return counts;
	}

	private Map<LocalDate, Integer> queryComplaintsHistogram(Session session, LocalDate startDate, LocalDate endDate, int branchId) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<Complaint> root = query.from(Complaint.class);
		Expression<java.sql.Date> dateExpr = builder.function("date", java.sql.Date.class, root.get("createdAt"));
		List<Predicate> predicates = new ArrayList<>();
		if (branchId > 0) {
			predicates.add(builder.equal(root.get("shopID"), branchId));
		}
		if (startDate != null && endDate != null) {
			Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date endExclusive = Date.from(endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), start));
			predicates.add(builder.lessThan(root.get("createdAt"), endExclusive));
		}
		query.multiselect(dateExpr, builder.count(root))
				.where(predicates.toArray(new Predicate[0]))
				.groupBy(dateExpr)
				.orderBy(builder.asc(dateExpr));
		List<Object[]> results = session.createQuery(query).getResultList();
		Map<LocalDate, Integer> histogram = new LinkedHashMap<>();
		for (Object[] row : results) {
			java.sql.Date date = (java.sql.Date) row[0];
			Number count = (Number) row[1];
			if (date != null) {
				histogram.put(date.toLocalDate(), count != null ? count.intValue() : 0);
			}
		}
		return histogram;
	}

	private Predicate buildOrderDateRangePredicate(CriteriaBuilder builder, Root<Order> root,
												  LocalDate startDate, LocalDate endDate) {
		Path<Integer> yearPath = root.get("orderYear");
		Path<Integer> monthPath = root.get("orderMonth");
		Path<Integer> dayPath = root.get("orderDay");

		Predicate startPredicate = builder.or(
				builder.greaterThan(yearPath, startDate.getYear()),
				builder.and(
						builder.equal(yearPath, startDate.getYear()),
						builder.greaterThan(monthPath, startDate.getMonthValue())
				),
				builder.and(
						builder.equal(yearPath, startDate.getYear()),
						builder.equal(monthPath, startDate.getMonthValue()),
						builder.greaterThanOrEqualTo(dayPath, startDate.getDayOfMonth())
				)
		);

		Predicate endPredicate = builder.or(
				builder.lessThan(yearPath, endDate.getYear()),
				builder.and(
						builder.equal(yearPath, endDate.getYear()),
						builder.lessThan(monthPath, endDate.getMonthValue())
				),
				builder.and(
						builder.equal(yearPath, endDate.getYear()),
						builder.equal(monthPath, endDate.getMonthValue()),
						builder.lessThanOrEqualTo(dayPath, endDate.getDayOfMonth())
				)
		);

		return builder.and(startPredicate, endPredicate);
	}

	private LocalDate resolveOrderDate(Order order) {
		try {
			return LocalDate.of(order.getOrderYear(), order.getOrderMonth(), order.getOrderDay());
		} catch (Exception ex) {
			return null;
		}
	}

	private LocalDate resolveComplaintDate(Complaint complaint) {
		if (complaint.getCreatedAt() != null) {
			return complaint.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		int year = complaint.getYear();
		int month = complaint.getMonth();
		int day = complaint.getDay();
		if (year <= 0 || month <= 0 || day <= 0) {
			return null;
		}
		try {
			return LocalDate.of(year, month, day);
		} catch (Exception ex) {
			return null;
		}
	}

	private boolean isWithinRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
		return (date.isEqual(startDate) || date.isAfter(startDate))
				&& (date.isEqual(endDate) || date.isBefore(endDate));
	}

	private static List<Order> getScopedOrders(Session session, Account account) {
		System.out.println("Arrived to getScopedOrders 1");
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Order> query = builder.createQuery(Order.class);
		Root<Order> root = query.from(Order.class);
		if (account != null) {
			int privilegeLevel = account.getPrivilegeLevel();
			if (privilegeLevel == 1) {
				query.where(builder.equal(root.get("accountID"), account.getAccountID()));
			} else if (privilegeLevel == 2 || privilegeLevel == 3) {
				int branchId = resolveBranchId(account);
				if (branchId > 0) {
					query.where(builder.equal(root.get("shopID"), branchId));
				} else {
					return Collections.emptyList();
				}
			}
		}
		List<Order> result = session.createQuery(query).getResultList();
		System.out.println("Arrived to getScopedOrders 2");
		return result;
	}

	private List<Account> getScopedAccounts(Session session, Account account) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Account> query = builder.createQuery(Account.class);
		Root<Account> root = query.from(Account.class);
		if (account != null) {
			int privilegeLevel = account.getPrivilegeLevel();
			if (privilegeLevel == 1) {
				query.where(builder.equal(root.get("accountID"), account.getAccountID()));
			} else if (privilegeLevel == 2 || privilegeLevel == 3) {
				int branchId = resolveBranchId(account);
				if (branchId > 0) {
					query.where(builder.equal(root.get("belongShop"), branchId));
				} else {
					return Collections.emptyList();
				}
			}
		}
		return session.createQuery(query).getResultList();
	}

	private static List<Product> getAllProducts(Session session) {
		System.out.println("Arrived to getAllProducts 1");
		CriteriaBuilder builder = session.getCriteriaBuilder();
		System.out.println("Arrived to getAllProducts 2");
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		System.out.println("Arrived to getAllProducts 3");
		query.from(Product.class);
		System.out.println("Arrived to getAllProducts 4");
		List<Product> result = session.createQuery(query).getResultList();
		System.out.println("Arrived to getAllProducts 5");
		return result;
	}

	private List<Manager> filterManagersByScope(List<Manager> managers, Account requester) {
		if (managers == null) {
			return Collections.emptyList();
		}
		if (requester != null && requester.getPrivilegeLevel() >= 4) {
			return new ArrayList<>(managers);
		}
		List<Manager> scoped = new ArrayList<>();
		for (Manager manager : managers) {
			if (sameBranch(requester, manager)) {
				scoped.add(manager);
			}
		}
		return scoped;
	}

	private List<Worker> filterWorkersByScope(List<Worker> workers, Account requester) {
		if (workers == null) {
			return Collections.emptyList();
		}
		if (requester != null && requester.getPrivilegeLevel() >= 4) {
			return new ArrayList<>(workers);
		}
		List<Worker> scoped = new ArrayList<>();
		for (Worker worker : workers) {
			if (sameBranch(requester, worker)) {
				scoped.add(worker);
			}
		}
		return scoped;
	}

	private boolean sameBranch(Account accountA, Account accountB) {
		int branchA = resolveBranchId(accountA);
		int branchB = resolveBranchId(accountB);
		return branchA > 0 && branchA == branchB;
	}

	private static int resolveBranchId(Account account) {
		if (account == null) {
			return 0;
		}
		if (account instanceof Manager) {
			int managerBranch = ((Manager) account).getShopID();
			if (managerBranch > 0) {
				return managerBranch;
			}
		}
		return account.getBelongShop();
	}

	private boolean requiresBranchAssignment(Account account) {
		if (account == null) {
			return false;
		}
		int privilege = account.getPrivilegeLevel();
		return privilege == 2 || privilege == 3;
	}

	private List<Product> loadAllProducts() {
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				List<Product> products = getAllProducts(session);
				tx.commit();
				return products;
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	Long countRows(Session session) {
		System.out.println("Arrived to coutnrwos 1");
		final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		System.out.println("Arrived to coutnrwos 2");
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		System.out.println("Arrived to coutnrwos 3");
		Root<Product> root = criteria.from(Product.class);
		System.out.println("Arrived to coutnrwos 4");
		criteria.select(criteriaBuilder.count(root));
		System.out.println("Arrived to coutnrwos 5");
		return session.createQuery(criteria).getSingleResult();
	}

	private int getNextProductId(Session session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
		Root<Product> root = query.from(Product.class);
		query.select(builder.max(root.get("id")));
		Integer maxId = session.createQuery(query).getSingleResult();
		return maxId == null ? 1 : maxId + 1;
	}

	void addItemToCatalog(Product recievedProd) {
		System.out.println("inside additemTocatalog1");
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				int newProductId = getNextProductId(session);
				recievedProd.setID(newProductId);

				session.save(recievedProd);
				session.flush();
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	void editCatalogProduct(Product productEdit) {
		System.out.println("Arrived to edit catalog product 1");
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				int recievedProductID = productEdit.getID();
				double recievedProductPrice = productEdit.getPrice();

				Product updateProd = session.load(Product.class, recievedProductID);
				updateProd.setButton(productEdit.getButton());
				updateProd.setPrice(recievedProductPrice);
				updateProd.setName(productEdit.getName());
				updateProd.setDetails(productEdit.getDetails());
				updateProd.setImage(productEdit.getImage());
				updateProd.setSku(productEdit.getSku());
				updateProd.setCategory(productEdit.getCategory());
				updateProd.setColor(productEdit.getColor());
				updateProd.setPromotion(productEdit.isPromotion());
				updateProd.setDiscountPercent(productEdit.getDiscountPercent());
				updateProd.setCustomProduct(productEdit.isCustomProduct());
				updateProd.setCustomType(productEdit.getCustomType());
				updateProd.setPriceRangeMin(productEdit.getPriceRangeMin());
				updateProd.setPriceRangeMax(productEdit.getPriceRangeMax());
				updateProd.setGreetingCard(productEdit.getGreetingCard());

				session.update(updateProd);
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	void removeItemFromCatalog(String prodIdToRemove, ConnectionToClient _client) throws IOException {
		System.out.println("arrived to removeItemFromCatalog 1");

		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				flowersnum = Math.max(0, flowersnum - 1);

				int removedId = Integer.parseInt(prodIdToRemove);
				Product product = session.get(Product.class, removedId);
				if (product != null) {
					session.delete(product);
				}
				tx.commit();
				System.out.println("arrived to removeItemFromCatalog 2.8");
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	public void deleteProduct(int deleteIndex) {
		System.out.println("arrived to deleteProd 1");
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				System.out.println("arrived to deleteProd 2");

				Object persistentInstance = session.get(Product.class, deleteIndex);
				Product perProd = (Product) persistentInstance;
				if (persistentInstance != null) {
					session.delete(perProd);
				}
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	public void deleteAllProducts() {
		System.out.println("arrived to deleteAllProducts 1");
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				List<Product> products = getAllProducts(session);
				for (Product product : products) {
					session.delete(product);
				}
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
		System.out.println("arrived to deleteAllProducts 4");
	}

	public static List<Account> getAllAccounts(Session session) {
		System.out.println("Arrived to getAllAccounts 1");
		CriteriaBuilder builder = session.getCriteriaBuilder();
		System.out.println("Arrived to getAllAccounts 2");
		CriteriaQuery<Account> query = builder.createQuery(Account.class);
		System.out.println("Arrived to getAllAccounts 3");
		query.from(Account.class);
		System.out.println("Arrived to getAllAccounts 4");
		List<Account> resultlest = session.createQuery(query).getResultList();
		System.out.println("Arrived to getAllAccounts 5");
		return resultlest;
	}

	private int getNextAccountId(Session session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
		Root<Account> root = query.from(Account.class);
		query.select(builder.max(root.get("accountID")));
		Integer maxId = session.createQuery(query).getSingleResult();
		return maxId == null ? 1 : maxId + 1;
	}

	private <T extends Account> T findAccountByEmail(Session session, Class<T> type, String email) {
		if (email == null) {
			return null;
		}
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(type);
		Root<T> root = query.from(type);
		query.select(root).where(builder.equal(root.get("email"), email));
		List<T> results = session.createQuery(query).getResultList();
		if (results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}

	private boolean isEmailAlreadyRegistered(String email) {
		System.out.println("Checking for existing email " + email);
		SessionFactory sessionFactory = getSessionFactory();
		Session localSession = sessionFactory.openSession();
		Transaction tx = localSession.beginTransaction();
		try {
			CriteriaBuilder builder = localSession.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			Root<Account> root = query.from(Account.class);
			query.select(builder.count(root));
			query.where(builder.equal(builder.lower(root.get("email")), email.toLowerCase()));
			Long count = localSession.createQuery(query).getSingleResult();
			tx.commit();
			return count != null && count > 0;
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			localSession.close();
		}
	}

	public Account addAccount(Account newAcc) {
		System.out.println("inside Add Account To Catalog");

		try {
			SessionFactory sessionFactory = getSessionFactory();
			try (Session session = sessionFactory.openSession()) {
				Transaction tx = session.beginTransaction();
				try {
					if (isEmailAlreadyRegistered(newAcc.getEmail())) {
						System.out.println("Email already exists: " + newAcc.getEmail());
						tx.rollback();
						return null;
					}

					int newId = getNextAccountId(session);
					newAcc.setAccountID(newId);
					newAcc.setLoggedIn(true);

					System.out.println("Saving account with email: " + newAcc.getEmail());
					session.save(newAcc);
					session.flush();
					tx.commit();

					System.out.println("Account saved successfully with ID: " + newId);
					return newAcc;
				} catch (Exception ex) {
					tx.rollback();
					throw ex;
				}
			}

		} catch (Exception exception) {
			System.err.println("Error during account registration:");
			exception.printStackTrace();

			return null;
		}
	}

	public void removeAccount(String AccIdToRemove, ConnectionToClient _client) {
		System.out.println("arrived to removeItemFromCatalog 1");

		int removedId = Integer.parseInt(AccIdToRemove);
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx1 = session.beginTransaction();
			try {
				Account account = session.get(Account.class, removedId);
				if (account != null) {
					session.delete(account);
				}
				List<Account> updatedAccounts = getAllAccounts(session);
				GetAllAccounts response = new GetAllAccounts();
				response.setAll_accounts(updatedAccounts);
				try {
					_client.sendToClient(response);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
				tx1.commit();
				System.out.println("arrived to removeItemFromCatalog 2.8");
			} catch (Exception ex) {
				tx1.rollback();
				throw ex;
			}
		}
	}

	public void deleteAccount(int deleteIndex) {
		System.out.println("arrived to deleteProd 1");
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				System.out.println("arrived to deleteProd 2");

				Object persistentInstance = session.get(Account.class, deleteIndex);
				Account peracc = (Account) persistentInstance;
				System.out.println("arrived to deleteProd 3");
				if (persistentInstance != null) {
					session.delete(peracc);
				}
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	public Long countAccountRows() {
		System.out.println("Arrived to coutnrwos 1");
		SessionFactory sessionFactory = getSessionFactory();
		try (Session countSession = sessionFactory.openSession()) {
			Transaction tx = countSession.beginTransaction();
			try {
				final CriteriaBuilder criteriaBuilder = countSession.getCriteriaBuilder();
				System.out.println("Arrived to coutnrwos 2");
				CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
				System.out.println("Arrived to coutnrwos 3");
				Root<Account> root = criteria.from(Account.class);
				System.out.println("Arrived to coutnrwos 4");
				criteria.select(criteriaBuilder.count(root));
				Long count = countSession.createQuery(criteria).getSingleResult();
				System.out.println("Arrived to coutnrwos 5");
				System.out.println(count);
				tx.commit();
				return count;
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	public void editAccount(Account accountEdit) {
		System.out.println("Arrived to edit catalog product 1");
		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				int recievedAccountID = accountEdit.getAccountID();

				Account updateAccount = session.load(Account.class, recievedAccountID);

				updateAccount.setID((int) accountEdit.getID());
				updateAccount.setFullName(accountEdit.getFullName());
				updateAccount.setAddress(accountEdit.getAddress());
				updateAccount.setEmail(accountEdit.getEmail());
				updateAccount.setPassword(accountEdit.getPassword());
				updateAccount.setPhoneNumber(accountEdit.getPhoneNumber());
				updateAccount.setCreditCardNumber(accountEdit.getCreditCardNumber());
				updateAccount.setCcv(accountEdit.getCcv());
				updateAccount.setCreditMonthExpire(accountEdit.getCreditMonthExpire());
				updateAccount.setCreditYearExpire(accountEdit.getCreditYearExpire());
				updateAccount.setLoggedIn(accountEdit.getLoggedIn());
				updateAccount.setBelongShop(accountEdit.getBelongShop());
				updateAccount.setSubscription(accountEdit.isSubscription());
				updateAccount.setPrivialge(accountEdit.getPrivialge());
				updateAccount.setFrozen(accountEdit.getFrozen());

				session.update(updateAccount);
				tx.commit();
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	public void addMessage(Message newMessage) {
		System.out.println("inside Add Account To Catalog");
		long numOfRows = countMessageRows();
		int castedId = (int) numOfRows;
		int newId = castedId + 1;
		newMessage.setMessageID(newId);

		SessionFactory sessionFactory = getSessionFactory();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				session.save(newMessage);
				session.flush();
				tx.commit();
				System.out.println("khaled");
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		}
	}

	public Long countMessageRows() {
		System.out.println("Arrived to coutnrwos 1");
		SessionFactory sessionFactory = getSessionFactory();
		Session localSession = sessionFactory.openSession();
		try {
			Transaction tx = localSession.beginTransaction();
			try {
				final CriteriaBuilder criteriaBuilder = localSession.getCriteriaBuilder();
				System.out.println("Arrived to coutnrwos 2");
				CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
				System.out.println("Arrived to coutnrwos 3");
				Root<Message> root = criteria.from(Message.class);
				System.out.println("Arrived to coutnrwos 4");
				criteria.select(criteriaBuilder.count(root));
				System.out.println("Arrived to coutnrwos 5");
				Long count = localSession.createQuery(criteria).getSingleResult();
				System.out.println(count);
				tx.commit();
				return count;
			} catch (Exception ex) {
				tx.rollback();
				throw ex;
			}
		} finally {
			localSession.close();
		}
	}
}
