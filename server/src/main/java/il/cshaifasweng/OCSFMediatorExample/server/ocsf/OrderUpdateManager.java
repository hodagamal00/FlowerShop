package il.cshaifasweng.OCSFMediatorExample.server.ocsf;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import org.hibernate.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class OrderUpdateManager {
    private static final int IMMEDIATE_ORDER_WINDOW_HOURS = 3;
    public static int ordersnum = 0;
    public static List<Order> orderGeneralList = new ArrayList<Order>();

    private static List<Order> getAllOrders() {
        System.out.println("Arrived to getAllOrders 1");
        CriteriaBuilder builder = SimpleServer.session.getCriteriaBuilder();
        System.out.println("Arrived to getAllOrders 2");
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        System.out.println("Arrived to getAllOrders 3");
        query.from(Order.class);
        System.out.println("Arrived to getAllOrders 4");
        List<Order> result = SimpleServer.session.createQuery(query).getResultList();
        System.out.println("Arrived to getAllOrders 5");
        return result;
    }

    static Long countRowsOrder() {
        System.out.println("Arrived to coutnrwos 1");
        final CriteriaBuilder criteriaBuilder = SimpleServer.session.getCriteriaBuilder();
        System.out.println("Arrived to coutnrwos 2");
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
        System.out.println("Arrived to coutnrwos 3");
        Root<Order> root = criteria.from(Order.class);
        System.out.println("Arrived to coutnrwos 4");
        criteria.select(criteriaBuilder.count(root));
        System.out.println("Arrived to coutnrwos 5");
        return SimpleServer.session.createQuery(criteria).getSingleResult();
    }

    public static void addOrder(Order recievedOrder) {
        System.out.println("inside additemTocatalog1");

        validateOrder(recievedOrder);

        long numOfRowsOrder = countRowsOrder();
        int castedId = (int) numOfRowsOrder;
        int newOrderId = castedId + 1;
        recievedOrder.setOrderID(newOrderId);
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

        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();
        System.out.println("inside additemTocatalog8");
        System.out.println("the new index is:" + newOrderId);

        SimpleServer.session.save(recievedOrder);
        System.out.println("inside additemTocatalog9");
        SimpleServer.session.flush();
        System.out.println("inside additemTocatalog10");
        tx.commit();
        System.out.println("inside additemTocatalog11");

        System.out.println("inside additemTocatalog12");
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

            if (deliveryDate.isBefore(orderDate)) {
                throw new IllegalArgumentException("Requested delivery time cannot be before the order time");
            }

            if (orderDate.toLocalDate().equals(deliveryDate.toLocalDate())) {
                long minutesBetween = Duration.between(orderDate, deliveryDate).toMinutes();
                if (minutesBetween > IMMEDIATE_ORDER_WINDOW_HOURS * 60L) {
                    throw new IllegalArgumentException("Immediate orders must be scheduled within a 3-hour window");
                }
            }
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException("Invalid order or delivery date/time provided", ex);
        }
    }

    public static void removeOrder(String orderIdToRemove, ConnectionToClient _client) {


        System.out.println("arrived to removeOrder");

        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();


        ordersnum--;

        int removedId = Integer.parseInt(orderIdToRemove);
        orderGeneralList = getAllOrders();
        orderGeneralList.remove(removedId-1); // remove the wanted item from the list
        for(int i=0;i<orderGeneralList.size();i++){ // update all the items id's
            if(orderGeneralList.get(i).getOrderID() > removedId){
                orderGeneralList.get(i).setOrderID((orderGeneralList.get(i).getOrderID()-1));
            }
        }
        System.out.println("arrived to removeOrder 2");


        SimpleServer.session = sessionFactory.openSession();
        Transaction tx1 = SimpleServer.session.beginTransaction();
        long longID = countRowsOrder();
        SimpleServer.session.close();
        //tx1.commit();
        System.out.println("arrived to removeOrder 3 and the longID is " + longID);
        int castedID = (int) longID;
        for(int l=0;l<castedID;l++){

            System.out.println("arrived to removeItemFromCatalog 2.5");
            deleteOrder(l+1);
        }

        SimpleServer.session = sessionFactory.openSession();
        Transaction tx2 = SimpleServer.session.beginTransaction();
        for(int i=0;i<orderGeneralList.size();i++){
            SimpleServer.session.save(orderGeneralList.get(i));
            SimpleServer.session.flush();
        }
        tx2.commit();
        SimpleServer.session.close();

        //session.close(); // here we finished deleting a Order, everything else is for updating the id's
        System.out.println("arrived to removeItemFromCatalog 2.8");

    }

    public static void deleteOrder(int deleteIndex) {
        System.out.println("arrived to deleteOrder 1");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();
        System.out.println("arrived to deleteOrder 2");

        Object persistentInstance = SimpleServer.session.load(Order.class, deleteIndex);
        Order perOrder = (Order) persistentInstance;
        System.out.println("arrived to deleteOrder 3");
        if (persistentInstance != null) {
            SimpleServer.session.delete(perOrder);
        }
        System.out.println("arrived to deleteProd 4");

        tx.commit();
        SimpleServer.session.close();

    }
    public static void deliveredOrder(int orderID){
        System.out.println("Arrived to delivered order");
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        SimpleServer.session = sessionFactory.openSession();
        Transaction tx = SimpleServer.session.beginTransaction();

        System.out.println("Arrived to delivered order 2");
        Order updateOrder  = SimpleServer.session.load(Order.class, orderID);

        updateOrder.setDelivered(true);

        System.out.println("Arrived to delivered order 3");
        SimpleServer.session.update(updateOrder);
        System.out.println("Arrived to delivered order 4");
        tx.commit();
    }
}
