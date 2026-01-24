package il.cshaifasweng.OCSFMediatorExample.server.ocsf;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.entities.*;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
//import il.cshaifasweng.OCSFMediatorExample.server.Product;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class WorkerUpdateManager {
    public static int workersnum = 0;
    public static List<Worker> workerGeneralList = new ArrayList<Worker>();

    public WorkerUpdateManager(){
        workerGeneralList = getAllWorkers();
    }
    private static List<Worker> getAllWorkers() {
        System.out.println("Arrived to getAllWorkers 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                System.out.println("Arrived to getAllWorkers 2");
                CriteriaQuery<Worker> query = builder.createQuery(Worker.class);
                System.out.println("Arrived to getAllWorkers 3");
                query.from(Worker.class);
                System.out.println("Arrived to getAllWorkers 4");
                List<Worker> result = session.createQuery(query).getResultList();
                System.out.println("Arrived to getAllWorkers 5");
                tx.commit();
                return result;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    static Long countRowsWorker() {
        System.out.println("Arrived to coutnrwos 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                System.out.println("Arrived to coutnrwos 2");
                CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
                System.out.println("Arrived to coutnrwos 3");
                Root<Worker> root = criteria.from(Worker.class);
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

    public static void addWorker(Worker recievedWorker) {
        System.out.println("inside additemTocatalog1");

        long numOfRowsWorker = countRowsWorker();
        int castedId = (int) numOfRowsWorker;
        int newWorkerId = castedId + 1;
        recievedWorker.setPersonID(newWorkerId);
        if (recievedWorker.getAccountID() <= 0) {
            recievedWorker.setAccountID(newWorkerId);
        }
        if (recievedWorker.getLoggedIn() == null) {
            recievedWorker.setLoggedIn(false);
        }
        System.out.println("inside additemTocatalog2");
        String recievedWorkerName = recievedWorker.getFullName();
        System.out.println("inside additemTocatalog3");
        String recievedWorkerEmail = recievedWorker.getEmail();
        System.out.println("inside additemTocatalog4");
        String recievedWorkerPassword = recievedWorker.getPassword();
        System.out.println("inside additemTocatalog5");
        Boolean recievedWorkerloggedIn = recievedWorker.getLoggedIn();

        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                System.out.println("inside additemTocatalog8");
                System.out.println("the new index is:" + newWorkerId);

                session.save(recievedWorker);
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

    public static void removeWorker(String workerIdToRemove, ConnectionToClient _client) {


        System.out.println("arrived to removeWorker");

        SessionFactory sessionFactory = SimpleServer.getSessionFactory();


        workersnum--;

        int removedId = Integer.parseInt(workerIdToRemove);
        workerGeneralList = getAllWorkers();
        workerGeneralList.remove(removedId-1); // remove the wanted item from the list
        for(int i=0;i<workerGeneralList.size();i++){ // update all the items id's
            if(workerGeneralList.get(i).getPersonID() > removedId){
                // Cast the result of the subtraction to int to satisfy the setter
                // signature.  getPersonID returns long and setPersonID expects int.
                workerGeneralList.get(i).setPersonID((int)(workerGeneralList.get(i).getPersonID() - 1));
            }
        }
        System.out.println("arrived to removeWorker 2");


        long longID = countRowsWorker();
        //tx1.commit();
        System.out.println("arrived to removeWorker 3 and the longID is " + longID);
        int castedID = (int) longID;
        for(int l=0;l<castedID;l++){

            System.out.println("arrived to removeItemFromCatalog 2.5");
            deleteWorker(l+1);
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx2 = session.beginTransaction();
            try {
                for(int i=0;i<workerGeneralList.size();i++){
                    session.save(workerGeneralList.get(i));
                    session.flush();
                }
                tx2.commit();
            } catch (Exception ex) {
                tx2.rollback();
                throw ex;
            }
        }

        //session.close(); // here we finished deleting a worker, everything else is for updating the id's
        System.out.println("arrived to removeItemFromCatalog 2.8");

    }

    public static void deleteWorker(int deleteIndex) {
        System.out.println("arrived to deleteWorker 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                System.out.println("arrived to deleteWorker 2");

                Object persistentInstance = session.get(Worker.class, deleteIndex);
                Worker perWorker = (Worker) persistentInstance;
                System.out.println("arrived to deleteWorker 3");
                if (persistentInstance != null) {
                    session.delete(perWorker);
                }
                System.out.println("arrived to deleteProd 4");

                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }

    }

    public static void editWorker(Worker workerEdit){
        System.out.println("Arrived to edit worker");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                // workerEdit.getPersonID() returns a long.  Cast to int to avoid
                // lossy conversion errors when passing to methods that expect int.
                int recievedWorkerID = (int) workerEdit.getPersonID();
                String recievedWorkerName = workerEdit.getFullName();
                String recievedWorkerEmail = workerEdit.getEmail();
                String recievedWorkerPassword = workerEdit.getPassword();
                Boolean recievedWorkerIsLoggedIn = workerEdit.getLoggedIn();
                boolean recievedWorkerFrozen = workerEdit.getFrozen();
                int recievedWorkerPrivilege = workerEdit.getPrivialge();

                System.out.println("Arrived to edit worker 2");
                Worker updateWorker  = session.load(Worker.class, recievedWorkerID);

                //System.out.println(updateWorker.getButton());
                //Worker updateWorker = (Worker) persistentInstance1 ;
                //updateWorker.setID(16);
                updateWorker.setPersonID(recievedWorkerID);
                updateWorker.setFullName(recievedWorkerName);
                updateWorker.setEmail(recievedWorkerEmail);
                updateWorker.setPassword(recievedWorkerPassword);
                updateWorker.setLoggedIn(recievedWorkerIsLoggedIn);
                updateWorker.setFrozen(recievedWorkerFrozen);
                updateWorker.setPrivialge(recievedWorkerPrivilege);

                System.out.println("Arrived to edit catalog product 3");
                System.out.println(updateWorker.getPersonID());
                session.update(updateWorker);
                System.out.println("Arrived to edit cworkeratalog product 4");
                tx.commit();
                System.out.println("Arrived to edit worker5");
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
