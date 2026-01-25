package il.cshaifasweng.OCSFMediatorExample.server.ocsf;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.entities.*;

//import il.cshaifasweng.OCSFMediatorExample.server.Product;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManager;
import javax.persistence.PreUpdate;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ManagerUpdateManager {
    public static int managersnum = 0;
    public static List<Manager> managerGeneralList = new ArrayList<Manager>();

    public ManagerUpdateManager(){
        managerGeneralList = getAllManagers();
    }

    private static List<Manager> getAllManagers() {
        System.out.println("Arrived to getAllManagers 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                System.out.println("Arrived to getAllManagers 2");
                CriteriaQuery<Manager> query = builder.createQuery(Manager.class);
                System.out.println("Arrived to getAllManagers 3");
                query.from(Manager.class);
                System.out.println("Arrived to getAllManagers 4");
                List<Manager> result = session.createQuery(query).getResultList();
                System.out.println("Arrived to getAllManagers 5");
                tx.commit();
                return result;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    static Long countRowsManager() {
        System.out.println("Arrived to coutnrwos 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                System.out.println("Arrived to coutnrwos 2");
                CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
                System.out.println("Arrived to coutnrwos 3");
                Root<Manager> root = criteria.from(Manager.class);
                System.out.println("Arrived to coutnrwos 4");
                criteria.select(criteriaBuilder.count(root));
                System.out.println("Arrived to coutnrwos 5");
                Long count = session.createQuery(criteria).getSingleResult();
                tx.commit();
                return count;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    public static void addManager(Manager recievedManager) {
        System.out.println("inside additemTocatalog1");

        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                int managerId = recievedManager.getAccountID();
                if (managerId <= 0) {
                    managerId = getNextManagerId(session);
                    recievedManager.setAccountID(managerId);
                }
                recievedManager.setPersonID(managerId);

                System.out.println("inside additemTocatalog8");
                System.out.println("the new index is:" + managerId);

                session.save(recievedManager);
                System.out.println("inside additemTocatalog9");
                session.flush();
                System.out.println("inside additemTocatalog10");
                tx.commit();
                System.out.println("inside additemTocatalog11");
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }

        System.out.println("inside additemTocatalog12");
    }

    private static int getNextManagerId(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
        Root<Manager> root = query.from(Manager.class);
        query.select(builder.max(root.get("accountID")));
        Integer maxId = session.createQuery(query).getSingleResult();
        return maxId == null ? 1 : maxId + 1;
    }

    public static void removeManager(String managerIdToRemove, ConnectionToClient _client) {
        System.out.println("arrived to removeManager");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        int removedId = Integer.parseInt(managerIdToRemove);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Manager manager = session.get(Manager.class, removedId);
                if (manager != null) {
                    session.delete(manager);
                }
                tx.commit();
                System.out.println("arrived to removeManager 2.8");
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    public static void deleteManager(int deleteIndex) {
        System.out.println("arrived to deleteManager 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                System.out.println("arrived to deleteManager 2");

                Object persistentInstance = session.get(Manager.class, deleteIndex);
                Manager perManager = (Manager) persistentInstance;
                System.out.println("arrived to deleteManager 3");
                if (persistentInstance != null) {
                    session.delete(perManager);
                }
                System.out.println("arrived to deleteProd 4");

                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }

    }

    public static void editManager(Manager managerEdit){
        System.out.println("Arrived to edit Manager");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // managerEdit.getPersonID() returns a long but the entity setters take int.
                // Cast to int explicitly to avoid a possible lossy conversion compilation error.
                int recievedManagerID = (int) managerEdit.getPersonID();
                String recievedManagerName = managerEdit.getFullName();
                String recievedManagerEmail = managerEdit.getEmail();
                String recievedManagerPassword = managerEdit.getPassword();
                Boolean recievedManagerIsLoggedIn = managerEdit.getLoggedIn();
                boolean recievedManagerFrozen = managerEdit.getFrozen();
                int recievedManagerPrivilege = managerEdit.getPrivialge();
                int recievedManagerShopId = managerEdit.getShopID();

                System.out.println("Arrived to edit Manager 2");
                Manager updateManager  = session.load(Manager.class, recievedManagerID);

                //System.out.println(updateManager.getButton());
                //Manager updateManager = (Manager) persistentInstance1 ;
                //updateManager.setID(16);
                updateManager.setPersonID(recievedManagerID);
                updateManager.setFullName(recievedManagerName);
                updateManager.setEmail(recievedManagerEmail);
                updateManager.setPassword(recievedManagerPassword);
                updateManager.setLoggedIn(recievedManagerIsLoggedIn);
                updateManager.setFrozen(recievedManagerFrozen);
                updateManager.setPrivialge(recievedManagerPrivilege);
                updateManager.setShopID(recievedManagerShopId);

                System.out.println("Arrived to edit catalog product 3");
                System.out.println(updateManager.getPersonID());
                session.update(updateManager);
                System.out.println("Arrived to edit managercatalog product 4");
                tx.commit();
                System.out.println("Arrived to edit manager5");
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }

		/*try {

			Product updatedProduct = new Product(recievedProductID, recievedProductButton, recievedProductName, recievedProductDetails, recievedProductPrice);


			for (int i = 0; i < productGeneralList.size(); i++) {
				if (productGeneralList.get(i).getID() == recievedProductID) {
					productGeneralList.set(i, updatedProduct);
				}
			}


			*//* USE UPDATE METHOD IN THE FUTURE *//*
			Saveinsess();
			tx.commit();


		} catch (Exception exception) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			System.err.println("An error occured, changes have been rolled back.");
			exception.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}*/
    }
}
