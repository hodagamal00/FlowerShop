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
        CriteriaBuilder builder = SimpleServer.session.getCriteriaBuilder();
        System.out.println("Arrived to getAllManagers 2");
        CriteriaQuery<Manager> query = builder.createQuery(Manager.class);
        System.out.println("Arrived to getAllManagers 3");
        query.from(Manager.class);
        System.out.println("Arrived to getAllManagers 4");
        List<Manager> result = SimpleServer.session.createQuery(query).getResultList();
        System.out.println("Arrived to getAllManagers 5");
        return result;
    }

    static Long countRowsManager() {
        System.out.println("Arrived to coutnrwos 1");
        final CriteriaBuilder criteriaBuilder = SimpleServer.session.getCriteriaBuilder();
        System.out.println("Arrived to coutnrwos 2");
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
        System.out.println("Arrived to coutnrwos 3");
        Root<Manager> root = criteria.from(Manager.class);
        System.out.println("Arrived to coutnrwos 4");
        criteria.select(criteriaBuilder.count(root));
        System.out.println("Arrived to coutnrwos 5");
        return SimpleServer.session.createQuery(criteria).getSingleResult();
    }

    public static void addManager(Manager recievedManager) {
        System.out.println("inside additemTocatalog1");

        long numOfRowsManager = countRowsManager();
        System.out.println(countRowsManager());
        int castedId = (int) numOfRowsManager;
        int newManagerId = castedId + 1;
        recievedManager.setPersonID(newManagerId);
        System.out.println("inside additemTocatalog2");
        String recievedManagerName = recievedManager.getFullName();
        System.out.println("inside additemTocatalog3");
        String recievedManagerEmail = recievedManager.getEmail();
        System.out.println("inside additemTocatalog4");
        String recievedManagerPassword = recievedManager.getPassword();
        System.out.println("inside additemTocatalog5");
        Boolean recievedManagerloggedIn = recievedManager.getLoggedIn();

        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();
        System.out.println("inside additemTocatalog8");
        System.out.println("the new index is:" + newManagerId);

        SimpleServer.session.save(recievedManager);
        System.out.println("inside additemTocatalog9");
        SimpleServer.session.flush();
        System.out.println("inside additemTocatalog10");
        tx.commit();
        System.out.println("inside additemTocatalog11");

        System.out.println("inside additemTocatalog12");
    }

    public static void removeManager(String managerIdToRemove, ConnectionToClient _client) {


        System.out.println("arrived to removeManager");

        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();


        managersnum--;

        int removedId = Integer.parseInt(managerIdToRemove);
        managerGeneralList = getAllManagers();
        managerGeneralList.remove(removedId-1); // remove the wanted item from the list
        for(int i=0;i<managerGeneralList.size();i++){ // update all the items id's
            if(managerGeneralList.get(i).getPersonID() > removedId){
                // getPersonID returns a long, while setPersonID accepts an int.  Cast
                // the result of the subtraction to int to avoid a lossy conversion
                // compilation error.
                managerGeneralList.get(i).setPersonID((int)(managerGeneralList.get(i).getPersonID() - 1));
            }
        }
        System.out.println("arrived to removeManager 2");


        SimpleServer.session = sessionFactory.openSession();
        Transaction tx1 = SimpleServer.session.beginTransaction();
        long longID = countRowsManager();
        SimpleServer.session.close();
        //tx1.commit();
        System.out.println("arrived to removeManager 3 and the longID is " + longID);
        int castedID = (int) longID;
        for(int l=0;l<castedID;l++){

            System.out.println("arrived to removeItemFromCatalog 2.5");
            deleteManager(l+1);
        }

        SimpleServer.session = sessionFactory.openSession();
        Transaction tx2 = SimpleServer.session.beginTransaction();
        for(int i=0;i<managerGeneralList.size();i++){
            SimpleServer.session.save(managerGeneralList.get(i));
            SimpleServer.session.flush();
        }
        tx2.commit();
        SimpleServer.session.close();

        //session.close(); // here we finished deleting a Manager, everything else is for updating the id's
        System.out.println("arrived to removeItemFromCatalog 2.8");

    }

    public static void deleteManager(int deleteIndex) {
        System.out.println("arrived to deleteManager 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();
        System.out.println("arrived to deleteManager 2");

        Object persistentInstance = SimpleServer.session.load(Manager.class, deleteIndex);
        Manager perManager = (Manager) persistentInstance;
        System.out.println("arrived to deleteManager 3");
        if (persistentInstance != null) {
            SimpleServer.session.delete(perManager);
        }
        System.out.println("arrived to deleteProd 4");

        tx.commit();
        SimpleServer.session.close();

    }

    public static void editManager(Manager managerEdit){
        System.out.println("Arrived to edit Manager");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();

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
        Manager updateManager  = SimpleServer.session.load(Manager.class, recievedManagerID);

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
        SimpleServer.session.update(updateManager);
        System.out.println("Arrived to edit managercatalog product 4");
        tx.commit();
        System.out.println("Arrived to edit manager5");

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
