package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Seeds the database with demo data so the application can be used immediately
 * after launching the server.  The initializer is idempotent – if records are
 * already present in a table it skips seeding that table.
 */
public final class DemoDataInitializer {

    private static boolean initialized = false;

    private DemoDataInitializer() {
    }

    public static synchronized void initialize(SessionFactory sessionFactory) {
        if (initialized) {
            return;
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                seedProducts(session);
                seedAccounts(session);
                seedWorkers(session);
                seedManagers(session);
                seedOrders(session);
                seedComplaints(session);
                seedMessages(session);
                seedReports(session);
                seedPromotions(session);
                seedBranchSettings(session);
                seedGlobalSettings(session);

                tx.commit();
                initialized = true;
            } catch (RuntimeException ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    private static long count(Session session, Class<?> entityClass) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<?> root = criteria.from(entityClass);
        criteria.select(builder.count(root));
        return session.createQuery(criteria).getSingleResult();
    }

    private static void seedProducts(Session session) {
        if (count(session, Product.class) > 0) {
            return;
        }

        List<Product> products = Arrays.asList(
                createProduct(1, "btnRose", "Red Rose Bouquet", "A dozen fresh red roses", 120.0,
                        "ROSE-001", "Bouquet", "Red", false, 0, false, null, 0.0, 0.0,
                        "Classic bouquet for any celebration"),
                createProduct(2, "btnSun", "Sunny Sunflowers", "Bright sunflowers in a rustic vase", 95.0,
                        "SUN-002", "Arrangement", "Yellow", true, 15, false, null, 0.0, 0.0,
                        "Bring sunshine indoors"),
                createProduct(3, "btnOrchid", "Orchid Elegance", "White orchids in a ceramic pot", 180.0,
                        "ORC-003", "Flowering Pot", "White", false, 0, false, null, 0.0, 0.0,
                        "Elegant orchids that last weeks"),
                createProduct(4, "btnMix", "Color Splash", "Mixed seasonal flowers", 140.0,
                        "MIX-004", "Bouquet", "Mixed", true, 10, false, null, 0.0, 0.0,
                        "Perfect for birthdays and anniversaries"),
                createProduct(5, "btnCustom", "Custom Bridal Bouquet", "Tailored bridal bouquet design", 350.0,
                        "CUS-005", "Custom", "Varies", false, 0, true, "Bridal Bouquet", 250.0, 600.0,
                        "Work with our designers to craft your dream bouquet")
        );

        for (Product product : products) {
            session.save(product);
        }
    }

    private static Product createProduct(int id, String button, String name, String details, double price,
                                         String sku, String category, String color, boolean promotion,
                                         double discountPercent, boolean custom, String customType,
                                         double priceRangeMin, double priceRangeMax, String greetingCard) {
        Product product = new Product(id, button, name, details, price);
        product.setImage(button + ".jpg");
        product.setSku(sku);
        product.setCategory(category);
        product.setColor(color);
        product.setPromotion(promotion);
        product.setDiscountPercent(discountPercent);
        product.setCustomProduct(custom);
        product.setCustomType(customType);
        product.setPriceRangeMin(priceRangeMin);
        product.setPriceRangeMax(priceRangeMax);
        product.setGreetingCard(greetingCard);
        return product;
    }

    private static void seedAccounts(Session session) {
        if (count(session, Account.class) > 0) {
            return;
        }

        Account alice = new Account(1, "Alice Green", 1001, "12 Flower St, Haifa",
                "alice@example.com", "alice123", 972501112233L, 4111111111111111L,
                12, 2026, 123, false, 1, true);
        alice.setPrivialge(1);

        Account ben = new Account(2, "Ben Bloom", 1002, "45 Garden Ave, Tel Aviv",
                "ben@example.com", "ben123", 972541234567L, 4222222222222222L,
                11, 2025, 456, false, 2, false);
        ben.setPrivialge(1);

        Account chloe = new Account(3, "Chloe Petal", 1003, "78 Bouquet Rd, Jerusalem",
                "chloe@example.com", "chloe123", 972552223344L, 4333333333333333L,
                10, 2027, 789, false, 0, true);
        chloe.setPrivialge(4);

        for (Account account : Arrays.asList(alice, ben, chloe)) {
            session.save(account);
        }
    }

    private static void seedWorkers(Session session) {
        if (count(session, Worker.class) > 0) {
            return;
        }

        Worker emma = new Worker("Emma Bloom", "emma@flowershop.com", "emmaPass", 10);
        emma.setPersonID(2001);
        emma.setBelongShop(1);
        emma.setPrivialge(2);
        emma.setLoggedIn(false);

        Worker liam = new Worker("Liam Leaf", "liam@flowershop.com", "liamPass", 11);
        liam.setPersonID(2002);
        liam.setBelongShop(2);
        liam.setPrivialge(2);
        liam.setLoggedIn(false);

        session.save(emma);
        session.save(liam);
    }

    private static void seedManagers(Session session) {
        if (count(session, Manager.class) > 0) {
            return;
        }

        Manager maya = new Manager("Maya Stem", "maya@flowershop.com", "mayaPass", 20);
        maya.setPersonID(3001);
        maya.setPrivialge(3);
        maya.setShopID(1);
        maya.setBelongShop(1);
        maya.setLoggedIn(false);

        Manager noam = new Manager("Noam Garden", "noam@flowershop.com", "noamPass", 21);
        noam.setPersonID(3002);
        noam.setPrivialge(4);
        noam.setShopID(0); // Chain manager
        noam.setBelongShop(0);
        noam.setLoggedIn(false);

        session.save(maya);
        session.save(noam);
    }

    private static void seedOrders(Session session) {
        if (count(session, Order.class) > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        Order pickupOrder = createDemoOrder(
                1,
                true,
                1,
                "Happy Birthday!",
                120,
                "12 Flower St, Haifa",
                1,
                false,
                false,
                now.minusDays(5).withHour(10).withMinute(15),
                now.minusDays(5).withHour(8).withMinute(30),
                4111111111111111L,
                12,
                2026,
                123,
                "Alice Green",
                972501112233L,
                "12 Flower St, Haifa",
                "Red Rose Bouquet",
                0.0
        );
        pickupOrder.setRefundStatus("NONE");
        pickupOrder.setCancelled(false);
        pickupOrder.setRefundAmount(0.0);

        Order deliveryOrder = createDemoOrder(
                2,
                false,
                2,
                "Congratulations!",
                235,
                "45 Garden Ave, Tel Aviv",
                2,
                true,
                true,
                now.minusDays(20).withHour(15).withMinute(0),
                now.minusDays(20).withHour(12).withMinute(30),
                4222222222222222L,
                11,
                2025,
                456,
                "Ben Bloom",
                972541234567L,
                "45 Garden Ave, Tel Aviv",
                "Sunny Sunflowers, Color Splash",
                25.0
        );
        deliveryOrder.setRefundStatus("FULL");
        deliveryOrder.setRefundAmount(235.0);
        deliveryOrder.setCancelled(false);
        deliveryOrder.setDelivered(true);

        Order recentDelivery = createDemoOrder(
                3,
                false,
                1,
                "Welcome home!",
                180,
                "78 Bouquet Rd, Jerusalem",
                1,
                false,
                false,
                now.minusDays(2).withHour(17).withMinute(45),
                now.minusDays(2).withHour(14).withMinute(15),
                4111111111111111L,
                12,
                2026,
                123,
                "Alice Green",
                972501112233L,
                "78 Bouquet Rd, Jerusalem",
                "Orchid Elegance",
                20.0
        );

        Order pastPickup = createDemoOrder(
                4,
                true,
                3,
                "Thank you!",
                140,
                "Ramat Aviv",
                2,
                false,
                true,
                now.minusDays(60).withHour(11).withMinute(0),
                now.minusDays(60).withHour(9).withMinute(15),
                4222222222222222L,
                11,
                2025,
                456,
                "Ben Bloom",
                972541234567L,
                "Ramat Aviv",
                "Color Splash",
                0.0
        );
        pastPickup.setDelivered(true);

        for (Order order : Arrays.asList(pickupOrder, deliveryOrder, recentDelivery, pastPickup)) {
            session.save(order);
        }
    }

    private static Order createDemoOrder(int id, boolean pickup, int shopId, String greeting, int totalPrice,
                                         String deliveredAddress, int accountId, boolean gift, boolean delivered,
                                         LocalDateTime prepareTime, LocalDateTime orderTime,
                                         long creditCardNumber, int creditCardExpMonth, int creditCardExpYear, int creditCardCVV,
                                         String recepName, long recepPhone, String recepAddress, String products,
                                         double deliveryFee) {
        Order order = new Order(
                id,
                pickup,
                shopId,
                greeting,
                totalPrice,
                deliveredAddress,
                accountId,
                gift,
                delivered,
                prepareTime.getDayOfMonth(),
                prepareTime.getMonthValue(),
                prepareTime.getYear(),
                orderTime.getDayOfMonth(),
                orderTime.getMonthValue(),
                orderTime.getYear(),
                creditCardNumber,
                creditCardExpMonth,
                creditCardExpYear,
                creditCardCVV,
                recepName,
                recepPhone,
                recepAddress,
                products,
                orderTime.getHour(),
                orderTime.getMinute(),
                prepareTime.getHour(),
                prepareTime.getMinute(),
                deliveryFee,
                "CREDIT_CARD"
        );
        order.setDelivered(delivered);
        return order;
    }

    private static void seedComplaints(Session session) {
        if (count(session, Complaint.class) > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Complaint complaint = new Complaint(1, 1, 2, false, true,
                "Flowers arrived later than expected", 2, 2002, true,
                50, now.minusDays(12).getDayOfMonth(), now.minusDays(12).getMonthValue(),
                now.minusDays(12).getYear(), "We apologize for the delay and refunded 50 ILS");
        complaint.setCreatedAt(java.sql.Timestamp.valueOf(now.minusDays(12)));
        complaint.setRespondedAt(java.sql.Timestamp.valueOf(now.minusDays(11)));
        complaint.setSlaStatus("RESOLVED_ON_TIME");
        complaint.setCompensationDecision("50% refund approved");

        Complaint openComplaint = new Complaint(2, 2, 3, false, false,
                "Arrangement missing the greeting card", 1, 0, false,
                0, now.minusDays(3).getDayOfMonth(), now.minusDays(3).getMonthValue(),
                now.minusDays(3).getYear(), "");
        openComplaint.setCreatedAt(java.sql.Timestamp.valueOf(now.minusDays(3)));
        openComplaint.setSlaStatus("PENDING");

        session.save(complaint);
        session.save(openComplaint);
    }

    private static void seedMessages(Session session) {
        if (count(session, Message.class) > 0) {
            return;
        }

        Message welcome = new Message(1, 1, "Welcome to FlowerShop! Enjoy 10% off your first order.");
        Message promo = new Message(2, 2, "Summer promotion: Sunflowers are now 15% off!");
        Message reminder = new Message(3, 1, "Reminder: Your delivery is scheduled within the next 24 hours.");
        Message managerNote = new Message(4, 0, "Branch managers: review pending orders for today.");
        session.save(welcome);
        session.save(promo);
        session.save(reminder);
        session.save(managerNote);
    }

    private static void seedReports(Session session) {
        if (count(session, Report.class) > 0) {
            return;
        }

        Report incomeReport = new Report("INCOME", 0, 1, 5, 2024,
                31, 5, 2024, 1, 6, 2024,
                "{\"totalIncome\": 15420, \"orders\": 87}");
        incomeReport.setTotalIncome(15420.0);
        incomeReport.setTotalOrders(87);
        incomeReport.setTotalComplaints(3);
        session.save(incomeReport);
    }

    private static void seedPromotions(Session session) {
        if (count(session, Promotion.class) > 0) {
            return;
        }

        LocalDate today = LocalDate.now();
        Promotion summerSale = new Promotion("Summer Blossoms", "15% off all sunflower arrangements",15.0,
                java.sql.Date.valueOf(today.minusDays(7)),
                java.sql.Date.valueOf(today.plusDays(21)), true, null);
        summerSale.setDiscountPercent(15.0);
        summerSale.setNetworkWide(true);
        summerSale.setTargetCategory("Sunflowers");
        summerSale.setMinimumOrderValue(75.0);
        summerSale.setCreatedBy("system");
        summerSale.setModifiedBy("system");
        summerSale.setActive(true);
        session.save(summerSale);
    }

    private static void seedBranchSettings(Session session) {
        if (count(session, BranchSettings.class) > 0) {
            return;
        }

        BranchSettings haifa = new BranchSettings(1, "Haifa Central", "12 Flower St, Haifa");
        haifa.setBranchPhone("+972-4-123-4567");
        haifa.setBranchEmail("haifa@flowershop.com");
        haifa.setManagerName("Maya Stem");
        haifa.setOpeningTime("08:00");
        haifa.setClosingTime("20:00");
        haifa.setOpenWeekends(true);
        haifa.setWeekendOpeningTime("09:00");
        haifa.setWeekendClosingTime("18:00");
        haifa.setDeliveryEnabled(true);
        haifa.setDeliveryFee(20.0);
        haifa.setDeliveryRadiusKm(15.0);
        haifa.setMinDeliveryOrder(100.0);
        haifa.setPickupEnabled(true);
        haifa.setKioskEnabled(true);
        haifa.setNotificationsEnabled(true);
        haifa.setEmailNotifications(true);
        haifa.setSmsNotifications(false);
        haifa.setAutoConfirmOrders(true);
        haifa.setMaxDailyOrders(150);
        haifa.setTaxRate(17.0);
        haifa.setCurrency("ILS");
        haifa.setActive(true);
        haifa.setModifiedBy("system");

        BranchSettings telAviv = new BranchSettings(2, "Tel Aviv Port", "45 Garden Ave, Tel Aviv");
        telAviv.setBranchPhone("+972-3-987-6543");
        telAviv.setBranchEmail("telaviv@flowershop.com");
        telAviv.setManagerName("Noam Garden");
        telAviv.setOpeningTime("09:00");
        telAviv.setClosingTime("21:00");
        telAviv.setOpenWeekends(true);
        telAviv.setWeekendOpeningTime("10:00");
        telAviv.setWeekendClosingTime("17:00");
        telAviv.setDeliveryEnabled(true);
        telAviv.setDeliveryFee(25.0);
        telAviv.setDeliveryRadiusKm(20.0);
        telAviv.setMinDeliveryOrder(120.0);
        telAviv.setPickupEnabled(true);
        telAviv.setKioskEnabled(false);
        telAviv.setNotificationsEnabled(true);
        telAviv.setEmailNotifications(true);
        telAviv.setSmsNotifications(true);
        telAviv.setAutoConfirmOrders(false);
        telAviv.setMaxDailyOrders(200);
        telAviv.setTaxRate(17.0);
        telAviv.setCurrency("ILS");
        telAviv.setActive(true);
        telAviv.setModifiedBy("system");

        session.save(haifa);
        session.save(telAviv);
    }

    private static void seedGlobalSettings(Session session) {
        if (count(session, GlobalSettings.class) > 0) {
            return;
        }

        GlobalSettings settings = new GlobalSettings();
        settings.setCompanyLogoUrl("https://example.com/logo.png");
        settings.setSupportEmail("support@flowershop.com");
        settings.setSupportPhone("+972-1-800-555-123");
        settings.setWebsiteUrl("https://flowershop.example.com");
        settings.setDefaultCurrency("ILS");
        settings.setDefaultTaxRate(17.0);
        settings.setSubscriptionDiscount(10.0);
        settings.setEarlyOrderGuaranteeHours(24);
        settings.setImmediateOrderMaxHours(3);
        settings.setFullRefundMinHours(3);
        settings.setPartialRefundMinHours(1);
        settings.setPartialRefundPercent(50.0);
        settings.setComplaintResponseHours(24);
        settings.setComplaintCompensationEnabled(true);
        settings.setDefaultDeliveryFee(25.0);
        settings.setMinOrderForFreeDelivery(200.0);
        settings.setLoyaltyPointsEnabled(true);
        settings.setLoyaltyPointsPerDollar(1.5);
        settings.setNotificationsEnabled(true);
        settings.setEmailNotificationsDefault(true);
        settings.setSmsNotificationsDefault(false);
        settings.setMaintenanceMode(false);
        settings.setMaintenanceMessage(null);
        settings.setAllowGuestCheckout(false);
        settings.setRequireAccountVerification(true);
        settings.setMaxLoginAttempts(5);
        settings.setSessionTimeoutMinutes(60);
        settings.setModifiedBy("system");
        settings.setModifiedDate(new Date());

        session.save(settings);
    }
}
