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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Seeds the database with demo data so the application can be used immediately
 * after launching the server. The initializer runs only when no products exist
 * to avoid resetting real data on subsequent launches.
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
            if (shouldResetProducts()) {
                Transaction tx = session.beginTransaction();
                try {
                    resetProducts(session);
                    seedProducts(session);
                    logProductCount(session, "after RESET_PRODUCTS");
                    tx.commit();
                    System.out.println("RESET_PRODUCTS done.");
                    initialized = true;
                    return;
                } catch (RuntimeException ex) {
                    tx.rollback();
                    throw ex;
                }
            }

            if (hasExistingData(session)) {
                initialized = true;
                return;
            }
            Transaction tx = session.beginTransaction();
            try {
                seedProducts(session);
                logProductCount(session, "after demo seed");
                seedAccounts(session);
                seedWorkers(session);
                seedManagers(session);
                seedOrders(session);
                seedComplaints(session);
                seedMessages(session);
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

    private static void logProductCount(Session session, String context) {
        long productCount = count(session, Product.class);
        System.out.println("Product count " + context + ": " + productCount);
    }

    private static boolean hasExistingData(Session session) {
        long productCount = count(session, Product.class);
        long accountCount = count(session, Account.class);
        long orderCount = count(session, Order.class);
        long complaintCount = count(session, Complaint.class);
        boolean hasData = productCount > 0 || accountCount > 0 || orderCount > 0 || complaintCount > 0;
        if (hasData) {
            if (productCount > 0) {
                System.out.println("Found " + productCount
                        + " products. Run with RESET_PRODUCTS=true to reseed the catalog.");
            } else {
                System.out.println("Existing data detected; demo seed skipped to avoid overwriting.");
            }
        }
        return hasData;
    }

    private static void seedProducts(Session session) {
        if (count(session, Product.class) > 0) {
            return;
        }

        List<Product> products = Arrays.asList(
                createProduct(1, "btn1", "Crimson Rose Bouquet", "A dozen velvety red roses tied with ribbon", 120.0,
                        "ROS-001", "Bouquet", "Red", true, 10, false, null, 0.0, 0.0,
                        "Classic roses for any celebration", "/images/1.jpeg"),
                createProduct(2, "btn2", "Sunflower Radiance", "Bright sunflowers in a rustic vase", 95.0,
                        "SUN-002", "Arrangement", "Yellow", false, 0, false, null, 0.0, 0.0,
                        "Bring sunshine indoors", "/images/2.jpeg"),
                createProduct(3, "btn3", "Orchid Elegance", "White orchids in a ceramic pot", 180.0,
                        "ORC-003", "Flowering Pot", "White", false, 0, false, null, 0.0, 0.0,
                        "Elegant orchids that last weeks", "/images/3.jpeg"),
                createProduct(4, "btn4", "Spring Meadow Mix", "Mixed seasonal flowers with fresh greenery", 140.0,
                        "MIX-004", "Bouquet", "Mixed", true, 15, false, null, 0.0, 0.0,
                        "Perfect for birthdays and anniversaries", "/images/4.jpeg"),
                createProduct(5, "btn5", "Tulip Charm", "Soft tulips arranged for spring", 110.0,
                        "TUL-005", "Bouquet", "Pink", false, 0, false, null, 0.0, 0.0,
                        "Fresh tulips to brighten any room", "/images/5.jpeg"),
                createProduct(6, "btn6", "Lavender Dreams", "Lavender and lilac blooms in a glass vase", 130.0,
                        "LAV-006", "Arrangement", "Purple", false, 0, false, null, 0.0, 0.0,
                        "Calming hues with a gentle fragrance", "/images/6.jpeg"),
                createProduct(7, "btn7", "Garden Peony Bliss", "Fluffy peonies with soft accents", 165.0,
                        "PEO-007", "Bouquet", "Pink", false, 0, false, null, 0.0, 0.0,
                        "A lush bouquet for elegant occasions", "/images/7.png"),
                createProduct(8, "btn8", "Blue Hydrangea Halo", "Blue hydrangeas with eucalyptus", 150.0,
                        "HYD-008", "Arrangement", "Blue", true, 12, false, null, 0.0, 0.0,
                        "Cool tones for a modern feel", "/images/8.jpeg"),
                createProduct(9, "btn9", "White Lily Grace", "White lilies with baby’s breath", 135.0,
                        "LIL-009", "Bouquet", "White", false, 0, false, null, 0.0, 0.0,
                        "Graceful lilies for serene moments", "/images/9.jpeg"),
                createProduct(10, "btn10", "Citrus Bloom Basket", "Orange and yellow blooms in a woven basket", 145.0,
                        "CIT-010", "Arrangement", "Orange", false, 0, false, null, 0.0, 0.0,
                        "A bright pick-me-up for any room", "/images/10.jpeg"),
                createProduct(11, "btn11", "Blush Garden Roses", "Blush roses with soft greenery", 125.0,
                        "ROS-011", "Bouquet", "Blush", false, 0, false, null, 0.0, 0.0,
                        "Romantic roses with a delicate touch", "/images/11.jpeg"),
                createProduct(12, "btn12", "Tropical Hibiscus", "Tropical blooms with bold foliage", 160.0,
                        "TRO-012", "Arrangement", "Mixed", false, 0, false, null, 0.0, 0.0,
                        "Island-inspired energy in a vase", "/images/12.jpeg"),
                createProduct(13, "btn13", "Pastel Garden", "Pastel blooms wrapped with satin", 118.0,
                        "PAS-013", "Bouquet", "Mixed", true, 8, false, null, 0.0, 0.0,
                        "Soft colors for a gentle statement", "/images/13.jpeg"),
                createProduct(14, "btn14", "Ruby Romance", "Deep red blooms with velvet accents", 155.0,
                        "RUB-014", "Bouquet", "Red", false, 0, false, null, 0.0, 0.0,
                        "Bold romance in every stem", "/images/14.jpeg"),
                createProduct(15, "btn15", "Greenhouse Fresh", "Green-white mix with crisp leaves", 128.0,
                        "GRN-015", "Arrangement", "Green", false, 0, false, null, 0.0, 0.0,
                        "Clean, modern, and refreshing", "/images/15.jpeg"),
                createProduct(16, "btn16", "Golden Hour Bouquet", "Warm yellow roses and carnations", 132.0,
                        "GLD-016", "Bouquet", "Yellow", false, 0, false, null, 0.0, 0.0,
                        "Capture the glow of golden hour", "/images/16.jpeg"),
                createProduct(17, "btn17", "Coral Bloom Box", "Coral roses in a premium box", 170.0,
                        "COR-017", "Arrangement", "Coral", true, 10, false, null, 0.0, 0.0,
                        "Luxury blooms in a sleek box", "/images/17.jpeg"),
                createProduct(18, "btn18", "Winter White Pines", "White blooms with pine accents", 148.0,
                        "WIN-018", "Bouquet", "White", false, 0, false, null, 0.0, 0.0,
                        "Seasonal whites with evergreen flair", "/images/18.jpeg"),
                createProduct(19, "btn19", "Berry Bloom Basket", "Berry-toned blooms in a basket", 142.0,
                        "BER-019", "Arrangement", "Berry", false, 0, false, null, 0.0, 0.0,
                        "Rich berry tones for cozy vibes", "/images/19.jpeg"),
                createProduct(20, "btn20", "Sunrise Tulip Mix", "Yellow and pink tulips", 115.0,
                        "TUL-020", "Bouquet", "Mixed", false, 0, false, null, 0.0, 0.0,
                        "Bright tulips that feel like sunrise", "/images/20.jpeg"),
                createProduct(21, "btn21", "Serenity Orchid Pot", "Orchids in a matte ceramic pot", 190.0,
                        "ORC-021", "Flowering Pot", "White", false, 0, false, null, 0.0, 0.0,
                        "Minimalist orchids with long-lasting blooms", "/images/21.jpeg"),
                createProduct(22, "btn22", "Wildflower Wrap", "Wildflowers wrapped in kraft paper", 105.0,
                        "WLD-022", "Bouquet", "Mixed", false, 0, false, null, 0.0, 0.0,
                        "A casual bouquet full of charm", "/images/22.jpeg"),
                createProduct(23, "btn23", "Midnight Violet", "Deep violet blooms with silver leaves", 158.0,
                        "MID-023", "Arrangement", "Purple", false, 0, false, null, 0.0, 0.0,
                        "Moody tones for a dramatic statement", "/images/23.jpeg"),
                createProduct(24, "btn24", "Peach Blossom", "Peach roses with white accents", 122.0,
                        "PEA-024", "Bouquet", "Peach", false, 0, false, null, 0.0, 0.0,
                        "Warm peach tones with delicate texture", "/images/24.jpeg"),
                createProduct(25, "btn25", "Fresh Citrus Vase", "Citrus blooms with green foliage", 138.0,
                        "CIT-025", "Arrangement", "Yellow", true, 10, false, null, 0.0, 0.0,
                        "Zesty colors for bright interiors", "/images/25.jpeg"),
                createProduct(26, "btn26", "Cherry Blossom Breeze", "Cherry blossom-inspired mix", 150.0,
                        "CHE-026", "Bouquet", "Pink", false, 0, false, null, 0.0, 0.0,
                        "Soft petals with airy greens", "/images/26.jpeg"),
                createProduct(27, "btn27", "Ocean Mist Bouquet", "Blue and white blooms with eucalyptus", 145.0,
                        "OCN-027", "Bouquet", "Blue", false, 0, false, null, 0.0, 0.0,
                        "A coastal palette of calm", "/images/27.jpeg"),
                createProduct(28, "btn28", "Rustic Amber Jar", "Amber-toned florals in a mason jar", 112.0,
                        "AMB-028", "Arrangement", "Amber", false, 0, false, null, 0.0, 0.0,
                        "Rustic charm with warm hues", "/images/28.jpeg"),
                createProduct(29, "btn29", "Snowberry Glow", "White blooms with silver accents", 155.0,
                        "SNW-029", "Bouquet", "White", false, 0, false, null, 0.0, 0.0,
                        "Elegant whites with a soft shimmer", "/images/29.jpeg"),
                createProduct(30, "btn30", "Citrus Sunrise Basket", "Orange blooms with seasonal greens", 148.0,
                        "SUN-030", "Arrangement", "Orange", false, 0, false, null, 0.0, 0.0,
                        "Sunny hues for a cheerful gift", "/images/30.png"),
                createProduct(31, "btn31", "Custom Celebration Bouquet", "Work with our designers to craft your bouquet", 250.0,
                        "CUS-031", "Custom", "Varies", false, 0, true, "Custom Bouquet", 150.0, 500.0,
                        "Tailored designs for milestone moments", "/images/31.jpeg")
        );

        for (Product product : products) {
            session.save(product);
        }
    }

    private static Product createProduct(int id, String button, String name, String details, double price,
                                         String sku, String category, String color, boolean promotion,
                                         double discountPercent, boolean custom, String customType,
                                         double priceRangeMin, double priceRangeMax, String greetingCard,
                                         String imagePath) {
        Product product = new Product(id, button, name, details, price);
        if (imagePath != null && !imagePath.isBlank()) {
            product.setImage(imagePath);
        }
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

    private static void resetProducts(Session session) {
        session.createQuery("delete from Complaint").executeUpdate();
        session.createQuery("delete from Order").executeUpdate();
        session.createQuery("delete from Product").executeUpdate();
    }

    private static boolean shouldResetProducts() {
        String flag = System.getProperty("RESET_PRODUCTS");
        if (flag == null || flag.isBlank()) {
            flag = System.getenv("RESET_PRODUCTS");
        }
        return parseBooleanFlag(flag);
    }

    private static boolean parseBooleanFlag(String flag) {
        if (flag == null) {
            return false;
        }
        String normalized = flag.trim().toLowerCase();
        return normalized.equals("true")
                || normalized.equals("1")
                || normalized.equals("yes")
                || normalized.equals("y");
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

        Account dana = new Account(3, "Dana Bloom", 1003, "78 Bouquet Rd, Jerusalem",
                "dana@example.com", "dana123", 972552223344L, 4333333333333333L,
                10, 2027, 789, false, 1, true);
        dana.setPrivialge(1);

        for (Account account : Arrays.asList(alice, ben, dana)) {
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

        Worker noa = new Worker("Noa Petal", "noa@flowershop.com", "noaPass", 12);
        noa.setPersonID(2003);
        noa.setBelongShop(1);
        noa.setPrivialge(2);
        noa.setLoggedIn(false);

        session.save(emma);
        session.save(liam);
        session.save(noa);
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

        Manager amit = new Manager("Amit Bloom", "amit@flowershop.com", "amitPass", 21);
        amit.setPersonID(3002);
        amit.setPrivialge(3);
        amit.setShopID(2);
        amit.setBelongShop(2);
        amit.setLoggedIn(false);

        Manager noam = new Manager("Noam Garden", "noam@flowershop.com", "noamPass", 22);
        noam.setPersonID(3003);
        noam.setPrivialge(4);
        noam.setShopID(0); // Chain manager
        noam.setBelongShop(0);
        noam.setLoggedIn(false);

        session.save(maya);
        session.save(amit);
        session.save(noam);
    }

    private static void seedOrders(Session session) {
        if (count(session, Order.class) > 0) {
            return;
        }

        List<Product> products = session.createQuery("from Product", Product.class).getResultList();
        if (products.isEmpty()) {
            return;
        }
        List<Account> customers = session.createQuery("from Account", Account.class).getResultList();
        if (customers.isEmpty()) {
            return;
        }

        Random random = new Random(42);
        int orderId = 1;
        List<Order> seededOrders = new ArrayList<>();
        int[] branches = new int[]{1, 2};
        for (int branchId : branches) {
            int orderCount = 40 + random.nextInt(41);
            for (int i = 0; i < orderCount; i++) {
                Account customer = customers.get(random.nextInt(customers.size()));
                LocalDateTime orderTime = LocalDateTime.now()
                        .minusDays(random.nextInt(90))
                        .withHour(8 + random.nextInt(10))
                        .withMinute(random.nextInt(60));
                LocalDateTime prepareTime = orderTime.plusDays(random.nextInt(4)).plusHours(random.nextInt(6));
                boolean pickUp = random.nextBoolean();
                boolean delivered = random.nextDouble() < 0.6;
                boolean cancelled = !delivered && random.nextDouble() < 0.25;

                Map<Product, Integer> itemQuantities = buildRandomItems(products, random);
                String productSummary = buildProductsSummary(itemQuantities);
                int totalPrice = calculateOrderTotal(itemQuantities, pickUp);

                String deliveredAddress = pickUp ? "" : customer.getAddress();
                Order order = new Order(orderId++, pickUp, branchId, "Enjoy your blooms!", totalPrice,
                        deliveredAddress, customer.getAccountID(), random.nextBoolean(), delivered,
                        prepareTime.getDayOfMonth(), prepareTime.getMonthValue(), prepareTime.getYear(),
                        orderTime.getDayOfMonth(), orderTime.getMonthValue(), orderTime.getYear(),
                        customer.getCreditCardNumber(), customer.getCreditMonthExpire(),
                        customer.getCreditYearExpire(), customer.getCcv(),
                        customer.getFullName(), customer.getPhoneNumber(), deliveredAddress,
                        productSummary, orderTime.getHour(), orderTime.getMinute(),
                        prepareTime.getHour(), prepareTime.getMinute(),
                        pickUp ? 0.0 : 20.0, "CREDIT_CARD");
                order.setCancelled(cancelled);
                order.setDelivered(delivered && !cancelled);
                if (cancelled) {
                    order.setRefundStatus("FULL");
                    order.setRefundAmount(totalPrice);
                } else {
                    order.setRefundStatus("NONE");
                    order.setRefundAmount(0.0);
                }
                seededOrders.add(order);
            }
        }

        for (Order order : seededOrders) {
            session.save(order);
        }
    }

    private static void seedComplaints(Session session) {
        if (count(session, Complaint.class) > 0) {
            return;
        }
        List<Order> orders = session.createQuery("from Order", Order.class).getResultList();
        if (orders.isEmpty()) {
            return;
        }
        Random random = new Random(24);
        int complaintCount = 10 + random.nextInt(11);
        for (int i = 0; i < complaintCount; i++) {
            Order order = orders.get(random.nextInt(orders.size()));
            LocalDate orderDate = LocalDate.of(order.getOrderYear(), order.getOrderMonth(), order.getOrderDay());
            LocalDate complaintDate = orderDate.plusDays(random.nextInt(5));
            Complaint complaint = new Complaint(i + 1, order.getAccountID(), order.getOrderID(), false, true,
                    "Delivery issue reported for order #" + order.getOrderID(), order.getShopID(),
                    2001, random.nextBoolean(), random.nextInt(120),
                    complaintDate.getDayOfMonth(), complaintDate.getMonthValue(),
                    complaintDate.getYear(), "We are reviewing your complaint.");
            Date createdAt = Date.from(complaintDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
            complaint.setCreatedAt(createdAt);
            if (random.nextBoolean()) {
                complaint.setRespondedAt(Date.from(complaintDate.plusDays(1).atStartOfDay()
                        .atZone(java.time.ZoneId.systemDefault()).toInstant()));
                complaint.setSlaStatus("RESOLVED_ON_TIME");
                complaint.setCompensationDecision("Store credit issued");
            } else {
                complaint.setSlaStatus("PENDING");
            }
            session.save(complaint);
        }
    }

    private static void seedMessages(Session session) {
        if (count(session, Message.class) > 0) {
            return;
        }

        Message welcome = new Message(1, 1, "Welcome to FlowerShop! Enjoy 10% off your first order.");
        Message promo = new Message(2, 2, "Summer promotion: Sunflowers are now 15% off!");
        session.save(welcome);
        session.save(promo);
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

    private static Map<Product, Integer> buildRandomItems(List<Product> products, Random random) {
        Map<Product, Integer> items = new LinkedHashMap<>();
        int itemCount = 2 + random.nextInt(3);
        for (int i = 0; i < itemCount; i++) {
            Product product = products.get(random.nextInt(products.size()));
            int quantity = 1 + random.nextInt(3);
            items.put(product, items.getOrDefault(product, 0) + quantity);
        }
        return items;
    }

    private static String buildProductsSummary(Map<Product, Integer> items) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(entry.getKey().getID()).append(":").append(entry.getValue());
        }
        return builder.toString();
    }

    private static int calculateOrderTotal(Map<Product, Integer> items, boolean pickUp) {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        if (!pickUp) {
            total += 20.0;
        }
        return (int) Math.round(total);
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
