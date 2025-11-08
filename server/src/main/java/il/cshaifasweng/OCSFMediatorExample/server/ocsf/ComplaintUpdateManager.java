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

    static Long countRowsComplaint() {
        System.out.println("Arrived to coutnrwos 1");
        final CriteriaBuilder criteriaBuilder = SimpleServer.session.getCriteriaBuilder();
        System.out.println("Arrived to coutnrwos 2");
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
        System.out.println("Arrived to coutnrwos 3");
        Root<Complaint> root = criteria.from(Complaint.class);
        System.out.println("Arrived to coutnrwos 4");
        criteria.select(criteriaBuilder.count(root));
        System.out.println("Arrived to coutnrwos 5");
        return SimpleServer.session.createQuery(criteria).getSingleResult();
    }

    public static void addComplaint(Complaint recievedComplaint) {
        System.out.println("inside addCompliTocatalog1");
        long numOfRowsComplaint = countRowsComplaint();
        int castedId = (int)numOfRowsComplaint;
        int newComplaintId = castedId + 1;
        recievedComplaint.setComplaintID(newComplaintId);
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();
        System.out.println("inside additemTocatalog8");

        SimpleServer.session.save(recievedComplaint);
        System.out.println("inside additemTocatalog9");
        SimpleServer.session.flush();
        System.out.println("inside additemTocatalog10");
        tx.commit();
        System.out.println("inside additemTocatalog11");

        System.out.println("inside additemTocatalog12");
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