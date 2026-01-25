package il.cshaifasweng.OCSFMediatorExample.server.ocsf;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import org.hibernate.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class OrderUpdateManager {
    public static int ordersnum = 0;
    public static List<Order> orderGeneralList = new ArrayList<Order>();

    private static List<Order> getAllOrders() {
        System.out.println("Arrived to getAllOrders 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                System.out.println("Arrived to getAllOrders 2");
                CriteriaQuery<Order> query = builder.createQuery(Order.class);
                System.out.println("Arrived to getAllOrders 3");
                query.from(Order.class);
                System.out.println("Arrived to getAllOrders 4");
                List<Order> result = session.createQuery(query).getResultList();
                System.out.println("Arrived to getAllOrders 5");
                tx.commit();
                return result;
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    static Long countRowsOrder() {
        System.out.println("Arrived to coutnrwos 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                System.out.println("Arrived to coutnrwos 2");
                CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
                System.out.println("Arrived to coutnrwos 3");
                Root<Order> root = criteria.from(Order.class);
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

    public static void addOrder(Order recievedOrder) {
        System.out.println("inside additemTocatalog1");

        validateOrder(recievedOrder);

        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        int orderId = recievedOrder.getOrderID();
        if (orderId <= 0) {
            orderId = getNextOrderId(sessionFactory);
            recievedOrder.setOrderID(orderId);
        }
   /*     boolean recievedOrderPickUp = recievedOrder.isPickUp();
        int recievedOrderShopID = recievedOrder.getShopID();
        System.out.println("inside additemTocatalog4");
        String recievedOrderGreeting = recievedOrder.getGreeting();
        System.out.println("inside additemTocatalog5");
        int recievedOrderTotalPrice = recievedOrder.getTotalPrice();
        String recievedOrderDeliveredAddress = recievedOrder.getDeliveredAddress();
        Account recievedAccOrder = recievedOrder.getAccOrder();
        boolean recievedOrderGift = recievedOrder.isGift();
        boolean recievedOrderDelivered = recievedOrder.isDelivered();
        Date recievedOrderArrivalTime = recievedOrder.getArrivalTime();
        Date recievedOrderTime = recievedOrder.getOrderTime();
        Date recievedOrderCardExpire = recievedOrder.getCreditCardExpire();
        long recievedOrderCardNumber = recievedOrder.getCreditCardNumber();
        int recievedOrderCVV = recievedOrder.getCvv();

*/

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                System.out.println("inside additemTocatalog8");
                System.out.println("the new index is:" + orderId);

                session.save(recievedOrder);
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

    private static int getNextOrderId(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
            Root<Order> root = query.from(Order.class);
            query.select(builder.max(root.get("orderID")));
            Integer maxId = session.createQuery(query).getSingleResult();
            return maxId == null ? 1 : maxId + 1;
        }
    }

    private static void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is required");
        }

        if (order.getPaymentMethod() == null || order.getPaymentMethod().isBlank()) {
            throw new IllegalArgumentException("Payment method must be provided");
        }

        if (order.isPickUp()) {
            if (order.getShopID() <= 0) {
                throw new IllegalArgumentException("Pickup orders must include a valid shop ID");
            }
        } else {
            if (order.getDeliveredAddress() == null || order.getDeliveredAddress().isBlank()) {
                throw new IllegalArgumentException("Delivery orders require a destination address");
            }
            if (order.getRecepName() == null || order.getRecepName().isBlank()) {
                throw new IllegalArgumentException("Delivery orders require a recipient name");
            }
            if (order.getRecepPhone() <= 0) {
                throw new IllegalArgumentException("Delivery orders require a recipient phone number");
            }
            if (order.getDeliveryFee() <= 0) {
                throw new IllegalArgumentException("A delivery fee must be provided for delivery orders");
            }
        }

        try {
            LocalDateTime orderDate = order.getOrderDate();
            LocalDateTime deliveryDate = order.getDelivery_time();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime immediateCutoff = now.plusHours(3);

            if (deliveryDate.isBefore(orderDate)) {
                throw new IllegalArgumentException("Requested delivery time cannot be before the order time");
            }
            if (deliveryDate.isBefore(now)) {
                throw new IllegalArgumentException("Requested delivery time must be in the future");
            }
            if (!deliveryDate.isAfter(immediateCutoff)) {
                // Immediate orders are those scheduled within the next 3 hours.
                return;
            }
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException("Invalid order or delivery date/time provided", ex);
        }
    }

    public static void removeOrder(String orderIdToRemove, ConnectionToClient _client) {
        System.out.println("arrived to removeOrder");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        int removedId = Integer.parseInt(orderIdToRemove);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Order order = session.get(Order.class, removedId);
                if (order != null) {
                    session.delete(order);
                }
                tx.commit();
                System.out.println("arrived to removeOrder 2.8");
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    public static void deleteOrder(int deleteIndex) {
        System.out.println("arrived to deleteOrder 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                System.out.println("arrived to deleteOrder 2");

                Object persistentInstance = session.get(Order.class, deleteIndex);
                Order perOrder = (Order) persistentInstance;
                System.out.println("arrived to deleteOrder 3");
                if (persistentInstance != null) {
                    session.delete(perOrder);
                }
                System.out.println("arrived to deleteProd 4");

                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }

    }
    public static void deliveredOrder(int orderID){
        System.out.println("Arrived to delivered order");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                System.out.println("Arrived to delivered order 2");
                Order updateOrder  = session.load(Order.class, orderID);

                updateOrder.setDelivered(true);

                System.out.println("Arrived to delivered order 3");
                session.update(updateOrder);
                System.out.println("Arrived to delivered order 4");
                tx.commit();
                notifyDeliveryIfGift(updateOrder, session);
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    private static void notifyDeliveryIfGift(Order order, Session session) {
        Account account = session.get(Account.class, order.getAccountID());
        if (account == null) {
            System.err.println("Warning: Unable to find account for delivered order " + order.getOrderID());
            return;
        }
        String recipientName = order.getRecepName();
        boolean hasRecipientName = recipientName != null;
        boolean hasRecipientPhone = order.getRecepPhone() > 0;
        boolean recipientNameDiffers = hasRecipientName
                && account.getFullName() != null
                && !recipientName.equals(account.getFullName());
        boolean sentToSomeoneElse = (hasRecipientName && hasRecipientPhone) || recipientNameDiffers;

        if (sentToSomeoneElse) {
            try {
                NotificationService.sendDeliveryNotification(order, account);
            } catch (Exception ex) {
                System.err.println("Warning: Failed to send delivery notification for order "
                        + order.getOrderID() + ". " + ex.getMessage());
            }
        }
    }
}
