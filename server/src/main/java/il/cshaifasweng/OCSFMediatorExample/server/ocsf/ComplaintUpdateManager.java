package il.cshaifasweng.OCSFMediatorExample.server.ocsf;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                System.out.println("Arrived to getAllComplaints 2");
                CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
                System.out.println("Arrived to getAllComplaints 3");
                query.from(Complaint.class);
                System.out.println("Arrived to getAllComplaints 4");
                List<Complaint> result = session.createQuery(query).getResultList();
                refreshComplaintSlaStatuses(session, result);
                System.out.println("Arrived to getAllComplaints 5");
                tx.commit();
                return result;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }



    public static void addComplaint(Complaint recievedComplaint) {
        System.out.println("inside addCompliTocatalog1");
        if (recievedComplaint.getCreatedAt() == null) {
            recievedComplaint.setCreatedAt(new Date());
        }
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                System.out.println("inside additemTocatalog8");
                int incomingId = recievedComplaint.getComplaintID();
                if (incomingId <= 0) {
                    int generatedComplaintId = reserveNextComplaintId(session);
                    recievedComplaint.setComplaintID(generatedComplaintId);
                } else {
                    ensureNextComplaintIdAfter(incomingId, session);
                }

                int responseWindow = resolveResponseWindowHours(session);
                applySlaStatus(recievedComplaint, responseWindow);

                session.save(recievedComplaint);
                System.out.println("inside additemTocatalog9");
                session.flush();
                System.out.println("inside additemTocatalog10");
                tx.commit();
                System.out.println("inside additemTocatalog11");
                System.out.println("inside additemTocatalog12");
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
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

    public static long countRowsComplaint() {
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Long> query = builder.createQuery(Long.class);
                Root<Complaint> root = query.from(Complaint.class);
                query.select(builder.count(root));
                Long count = session.createQuery(query).uniqueResult();
                tx.commit();
                return count != null ? count : 0;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        } finally {
            session.close();
        }
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
            Transaction tx = session.beginTransaction();
            try {
                int nextId = reserveNextComplaintId(session);
                tx.commit();
                return nextId;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        } finally {
            session.close();
        }
    }

    public static boolean editComplaint(Complaint recievedComplaint){
        System.out.println("Arrived to edit Complaint");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        boolean replyLate = false;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                int receivedComplaintID = recievedComplaint.getComplaintID();

                int recievedComplaintID = recievedComplaint.getComplaintID();
                int recievedCustomerID = recievedComplaint.getCustomerID();
                int recievedAnswerWorkerID = recievedComplaint.getAnswerworkerID();
                String recievedReplyText = recievedComplaint.getReplyText();
                int recievedMoneyValue = recievedComplaint.getReturnedmoneyvalue();
                boolean recievedIsReturnMoney = recievedComplaint.isReturnedMoney();
                boolean recievedIsAccpeted = recievedComplaint.isAccepted();
                String compensationDecision = recievedIsReturnMoney ? recievedMoneyValue + "₪ compensation approved" : "No compensation";


                System.out.println("Arrived to edit Complaint 2");
                Complaint updateComplaint  = session.load(Complaint.class, recievedComplaintID);

                updateComplaint.setComplaintID(recievedComplaintID);
                updateComplaint.setCustomerID(recievedCustomerID);
                updateComplaint.setAnswerworkerID(recievedAnswerWorkerID);
                updateComplaint.setReplyText(recievedReplyText);
                updateComplaint.setReturnedmoneyvalue(recievedMoneyValue);
                updateComplaint.setReturnedMoney(recievedIsReturnMoney);
                updateComplaint.setAccepted(recievedIsAccpeted);
                updateComplaint.setCompensationDecision(recievedComplaint.getCompensationDecision() != null ? recievedComplaint.getCompensationDecision() : compensationDecision);
                if (updateComplaint.getCreatedAt() == null) {
                    updateComplaint.setCreatedAt(buildCreatedAtFromLegacy(updateComplaint));
                }
                Date respondedAt = new Date();
                updateComplaint.setRespondedAt(respondedAt);
                replyLate = isReplyLate(updateComplaint.getCreatedAt(), respondedAt);

                int responseWindow = resolveResponseWindowHours(session);
                applySlaStatus(updateComplaint, responseWindow);
                if (replyLate) {
                    updateComplaint.setSlaStatus("LATE");
                    updateComplaint.setIn24Hours(false);
                }

                if (recievedIsAccpeted && recievedIsReturnMoney && recievedMoneyValue > 0) {
                    Account customer = session.get(Account.class, updateComplaint.getCustomerID());
                    if (customer != null) {
                        customer.addCreditBalance(recievedMoneyValue);
                        session.update(customer);
                    }
                }

                System.out.println("Arrived to edit Complaint 3");
                session.update(updateComplaint);
                System.out.println("Arrived to edit Complaint 4");
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
        return replyLate;
    }

    public static void refreshComplaintSlaStatuses(Session session, List<Complaint> complaints) {
        if (complaints == null || complaints.isEmpty()) {
            return;
        }
        Transaction tx = session.getTransaction();
        boolean newTx = false;
        if (tx == null || !tx.isActive()) {
            tx = session.beginTransaction();
            newTx = true;
        }
        int responseWindow = resolveResponseWindowHours(session);
        for (Complaint complaint : complaints) {
            if (complaint.getCreatedAt() == null) {
                complaint.setCreatedAt(buildCreatedAtFromLegacy(complaint));
            }
            applySlaStatus(complaint, responseWindow);
            session.update(complaint);
        }
        if (newTx) {
            tx.commit();
        }
    }

    private static int resolveResponseWindowHours(Session session) {
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GlobalSettings> query = builder.createQuery(GlobalSettings.class);
            query.from(GlobalSettings.class);
            List<GlobalSettings> settings = session.createQuery(query).setMaxResults(1).getResultList();
            if (!settings.isEmpty()) {
                return settings.get(0).getComplaintResponseHours();
            }
        } catch (Exception ignored) {
        }
        return 24;
    }

    private static void applySlaStatus(Complaint complaint, int responseWindow) {
        if (complaint.getCreatedAt() == null) {
            complaint.setCreatedAt(new Date());
        }
        LocalDateTime created = LocalDateTime.ofInstant(complaint.getCreatedAt().toInstant(), ZoneId.systemDefault());
        LocalDateTime deadline = created.plusHours(responseWindow);
        String status;
        if (complaint.isAccepted()) {
            if (complaint.getRespondedAt() != null) {
                LocalDateTime responded = LocalDateTime.ofInstant(complaint.getRespondedAt().toInstant(), ZoneId.systemDefault());
                status = responded.isAfter(deadline) ? "RESOLVED_LATE" : "RESOLVED_ON_TIME";
            } else {
                status = "RESOLVED";
            }
        } else {
            status = LocalDateTime.now().isAfter(deadline) ? "OVERDUE" : "IN_PROGRESS";
        }
        complaint.setSlaStatus(status);
        complaint.setIn24Hours(!"OVERDUE".equals(status) && !"RESOLVED_LATE".equals(status));
    }

    private static Date buildCreatedAtFromLegacy(Complaint complaint) {
        if (complaint.getDay() == 0 || complaint.getMonth() == 0 || complaint.getYear() == 0) {
            return new Date();
        }
        LocalDateTime timestamp = LocalDateTime.of(complaint.getYear(), complaint.getMonth(), complaint.getDay(), 12, 0);
        return Date.from(timestamp.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static boolean isReplyLate(Date createdAt, Date respondedAt) {
        if (createdAt == null || respondedAt == null) {
            return false;
        }
        long hours = Duration.between(createdAt.toInstant(), respondedAt.toInstant()).toHours();
        return hours > 24;
    }

}
