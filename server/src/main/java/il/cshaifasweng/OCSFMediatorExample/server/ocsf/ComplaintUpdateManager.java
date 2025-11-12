package il.cshaifasweng.OCSFMediatorExample.server.ocsf;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ComplaintUpdateManager {
    public static int complaintsnum = 0;
    public static List<Complaint> complaintGeneralList = new ArrayList<Complaint>();
    private static final Object complaintIdLock = new Object();
    private static Integer nextComplaintIdCache = null;

    private static List<Complaint> getAllComplaints() {
        System.out.println("Arrived to getAllComplaints 1");
        CriteriaBuilder builder = SimpleServer.session.getCriteriaBuilder();
        System.out.println("Arrived to getAllComplaints 2");
        CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
        System.out.println("Arrived to getAllComplaints 3");
        query.from(Complaint.class);
        System.out.println("Arrived to getAllComplaints 4");
        List<Complaint> result = SimpleServer.session.createQuery(query).getResultList();
        System.out.println("Arrived to getAllComplaints 5");
        return result;
    }



    public static void addComplaint(Complaint recievedComplaint) {
        System.out.println("inside addCompliTocatalog1");

        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();
        System.out.println("inside additemTocatalog8");
        int incomingId = recievedComplaint.getComplaintID();
        if (incomingId <= 0) {
            int newComplaintId = reserveNextComplaintId(SimpleServer.session);
            recievedComplaint.setComplaintID(newComplaintId);
        } else {
            ensureNextComplaintIdAfter(incomingId, SimpleServer.session);
        }




        SimpleServer.session.save(recievedComplaint);
        System.out.println("inside additemTocatalog9");
        SimpleServer.session.flush();
        System.out.println("inside additemTocatalog10");
        tx.commit();
        System.out.println("inside additemTocatalog11");

        System.out.println("inside additemTocatalog12");
    }


    private static int getNextComplaintId(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
        Root<Complaint> root = query.from(Complaint.class);
        query.select(builder.max(root.get("complaintID")));

        Integer maxId = session.createQuery(query).uniqueResult();
        if (maxId == null) {
            return 1;
        }
        return maxId + 1;
    }


    private static int reserveNextComplaintId(Session session) {
        synchronized (complaintIdLock) {
            if (nextComplaintIdCache == null) {
                nextComplaintIdCache = getNextComplaintId(session);
            }
            return nextComplaintIdCache++;
        }
    }

    private static void ensureNextComplaintIdAfter(int complaintId, Session session) {
        synchronized (complaintIdLock) {
            if (nextComplaintIdCache == null) {
                nextComplaintIdCache = getNextComplaintId(session);
            }
            if (nextComplaintIdCache <= complaintId) {
                nextComplaintIdCache = complaintId + 1;
            }
        }
    }

    public static int previewNextComplaintId() {
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            return reserveNextComplaintId(session);
        } finally {
            session.close();
        }
    }

    public static void editComplaint(Complaint recievedComplaint){
        System.out.println("Arrived to edit Complaint");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();

        int receivedComplaintID = recievedComplaint.getComplaintID();

        int recievedComplaintID = recievedComplaint.getComplaintID();
        int recievedCustomerID = recievedComplaint.getCustomerID();
        int recievedAnswerWorkerID = recievedComplaint.getAnswerworkerID();
        String recievedReplyText = recievedComplaint.getReplyText();
        int recievedMoneyValue = recievedComplaint.getReturnedmoneyvalue();
        boolean recievedIsReturnMoney = recievedComplaint.isReturnedMoney();
        boolean recievedIsAccpeted = recievedComplaint.isAccepted();


        System.out.println("Arrived to edit Complaint 2");
        Complaint updateComplaint  = SimpleServer.session.load(Complaint.class, recievedComplaintID);

        updateComplaint.setComplaintID(recievedComplaintID);
        updateComplaint.setCustomerID(recievedCustomerID);
        updateComplaint.setAnswerworkerID(recievedAnswerWorkerID);
        updateComplaint.setReplyText(recievedReplyText);
        updateComplaint.setReturnedmoneyvalue(recievedMoneyValue);
        updateComplaint.setReturnedMoney(recievedIsReturnMoney);
        updateComplaint.setAccepted(recievedIsAccpeted);


        System.out.println("Arrived to edit Complaint 3");
        SimpleServer.session.update(updateComplaint);
        System.out.println("Arrived to edit Complaint 4");
        tx.commit();
    }

}