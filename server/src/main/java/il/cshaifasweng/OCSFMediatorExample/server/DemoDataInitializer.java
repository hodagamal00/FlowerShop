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

            long productCount = count(session, Product.class);
            long accountCount = count(session, Account.class);
            long orderCount = count(session, Order.class);
            long complaintCount = count(session, Complaint.class);
            boolean hadAnyData = productCount > 0 || accountCount > 0 || orderCount > 0 || complaintCount > 0;

            if (!hadAnyData) {
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
                    return;
                } catch (RuntimeException ex) {
                    tx.rollback();
                    throw ex;
                }
            }

            if (productCount == 0) {
                Transaction tx = session.beginTransaction();
                try {
                    seedProducts(session);
                    logProductCount(session, "after catalog seed");
                    seedPromotions(session);
                    tx.commit();
                } catch (RuntimeException ex) {
                    tx.rollback();
                    throw ex;
                }
            } else {
                System.out.println("Found " + productCount
                        + " products. Run with RESET_PRODUCTS=true to reseed the catalog.");
            }

            if (hadAnyData && productCount == 0) {
                System.out.println("Existing data detected; seeded catalog only to avoid overwriting.");
            }

            initialized = true;
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

    private static void seedProducts(Session session) {
        if (count(session, Product.class) > 0) {
            return;
        }

        List<Product> products = Arrays.asList(
                createProduct(1, "btn1", "Vanilla Bliss Candle",
                        "A soothing candle with a soft vanilla fragrance. Size: 200g. Burn time: ~40 hours.", 35.0,
                        "SKU-001", "Scented Candle", "White", false, 0, false, null, 0.0, 0.0, null,
                        "/images/1.jpeg"),
                createProduct(2, "btn2", "Rose Garden Candle",
                        "A romantic candle with fresh rose scent. Size: 250g. Burn time: ~45 hours.", 40.0,
                        "SKU-002", "Scented Candle", "Pink", false, 0, false, null, 0.0, 0.0, null,
                        "/images/2.jpeg"),
                createProduct(3, "btn3", "Ocean Breeze Candle",
                        "A refreshing candle with clean ocean aroma. Size: 180g. Burn time: ~35 hours.", 30.0,
                        "SKU-003", "Scented Candle", "Blue", false, 0, false, null, 0.0, 0.0, null,
                        "/images/3.jpeg"),
                createProduct(4, "btn4", "Lavender Dreams Candle",
                        "A calming candle with lavender fragrance. Size: 220g. Burn time: ~42 hours.", 38.0,
                        "SKU-004", "Scented Candle", "Purple", false, 0, false, null, 0.0, 0.0, null,
                        "/images/4.jpeg"),
                createProduct(5, "btn5", "Birthday Celebration Card",
                        "A colorful birthday card with envelope. Dimensions: 15×10 cm. Weight: 25g.", 10.0,
                        "SKU-005", "Card", "Yellow", false, 0, false, null, 0.0, 0.0, null,
                        "/images/5.jpeg"),
                createProduct(6, "btn6", "Sympathy & Comfort Card",
                        "A gentle sympathy card. Dimensions: 15×10 cm. Weight: 22g.", 11.0,
                        "SKU-006", "Card", "White", false, 0, false, null, 0.0, 0.0, null,
                        "/images/6.jpeg"),
                createProduct(7, "btn7", "Congratulations Card",
                        "A festive congratulations card. Dimensions: 14×10 cm. Weight: 25g.", 12.0,
                        "SKU-007", "Card", "Green", false, 0, false, null, 0.0, 0.0, null,
                        "/images/7.png"),
                createProduct(8, "btn8", "Sunflower Seeds Pack",
                        "Easy-to-grow sunflower seeds. Germination: 7–10 days. Full bloom: 70–90 days. Pack weight: 50g.", 15.0,
                        "SKU-008", "Seeds", "Yellow", false, 0, false, null, 0.0, 0.0, null,
                        "/images/8.jpeg"),
                createProduct(9, "btn9", "Rose Seeds Pack",
                        "Rose seeds pack. Germination: 7–10 days. Full bloom: 70–90 days. Pack weight: 50g.", 18.0,
                        "SKU-009", "Seeds", "Red", false, 0, false, null, 0.0, 0.0, null,
                        "/images/9.jpeg"),
                createProduct(10, "btn10", "Lavender Seeds Pack",
                        "Fragrant lavender seeds. Germination: 14–21 days. Harvest: 90–110 days. Pack weight: 25g.", 20.0,
                        "SKU-010", "Seeds", "Purple", false, 0, false, null, 0.0, 0.0, null,
                        "/images/10.jpeg"),
                createProduct(11, "btn11", "Daisy Seeds Pack",
                        "Colorful daisy seeds. Germination: 10–14 days. Bloom: 60–80 days. Pack weight: 20g.", 15.0,
                        "SKU-011", "Seeds", "White", false, 0, false, null, 0.0, 0.0, null,
                        "/images/11.jpeg"),
                createProduct(12, "btn12", "Red Rose Stem",
                        "A classic long-stemmed red rose. Vase life: 7 days.", 12.0,
                        "SKU-012", "Flower", "Red", false, 0, false, null, 0.0, 0.0, null,
                        "/images/12.jpeg"),
                createProduct(13, "btn13", "White Lily Stem",
                        "An elegant white lily flower. Vase life: 8–10 days.", 15.0,
                        "SKU-013", "Flower", "White", false, 0, false, null, 0.0, 0.0, null,
                        "/images/13.jpeg"),
                createProduct(14, "btn14", "Yellow Tulip Stem",
                        "A cheerful tulip stem. Vase life: 5–7 days.", 10.0,
                        "SKU-014", "Flower", "Yellow", false, 0, false, null, 0.0, 0.0, null,
                        "/images/14.jpeg"),
                createProduct(15, "btn15", "Blue Hydrangea Stem",
                        "A fresh hydrangea stem with soft blue petals. Vase life: 7–9 days.", 20.0,
                        "SKU-015", "Flower", "Blue", false, 0, false, null, 0.0, 0.0, null,
                        "/images/15.jpeg"),
                createProduct(16, "btn16", "Classic Roses Bouquet",
                        "12 red roses with greenery. Vase life: 7–9 days.", 120.0,
                        "SKU-016", "Bouquets", "Red", false, 0, false, null, 0.0, 0.0, null,
                        "/images/16.jpeg"),
                createProduct(17, "btn17", "Tulip Spring Mix",
                        "15 tulips in mixed colors. Vase life: 6–8 days.", 110.0,
                        "SKU-017", "Bouquets", "Yellow", false, 0, false, null, 0.0, 0.0, null,
                        "/images/17.jpeg"),
                createProduct(18, "btn18", "Elegant Lily Bouquet",
                        "10 white lilies with leaves. Vase life: 8–10 days.", 150.0,
                        "SKU-018", "Bouquets", "White", false, 0, false, null, 0.0, 0.0, null,
                        "/images/18.jpeg"),
                createProduct(19, "btn19", "Gerbera Joy Bouquet",
                        "12 gerberas in assorted colors. Vase life: 6–8 days.", 100.0,
                        "SKU-019", "Bouquets", "Pink", false, 0, false, null, 0.0, 0.0, null,
                        "/images/19.jpeg"),
                createProduct(20, "btn20", "Orchid Pot",
                        "Potted orchid with blooms. Pot size: 15 cm. Plant height: 40–50 cm. Average life: months with care.", 90.0,
                        "SKU-020", "Potted Plants", "Purple", false, 0, false, null, 0.0, 0.0, null,
                        "/images/20.jpeg"),
                createProduct(21, "btn21", "Mini Rose Plant",
                        "Potted mini rose plant. Pot size: 15 cm. Plant height: 40–50 cm. Average life: months with care.", 75.0,
                        "SKU-021", "Potted Plants", "Red", false, 0, false, null, 0.0, 0.0, null,
                        "/images/21.jpeg"),
                createProduct(22, "btn22", "Peace Lily Plant",
                        "Peace lily in pot. Pot size: 14 cm. Plant height: 35–45 cm. Lifespan: years with care.", 85.0,
                        "SKU-022", "Potted Plants", "White", false, 0, false, null, 0.0, 0.0, null,
                        "/images/22.jpeg"),
                createProduct(23, "btn23", "Succulent Mix",
                        "Assorted succulents. Pot size: 10 cm. Plant height: 10–15 cm. Lifespan: several years.", 60.0,
                        "SKU-023", "Potted Plants", "Green", false, 0, false, null, 0.0, 0.0, null,
                        "/images/23.jpeg"),
                createProduct(24, "btn24", "Sunflower Arrangement",
                        "A bright sunflower arrangement (40 cm). Vase life: 7–9 days.", 150.0,
                        "SKU-024", "Flower Arrangements", "Yellow", false, 0, false, null, 0.0, 0.0, null,
                        "/images/24.jpeg"),
                createProduct(25, "btn25", "Mixed Seasonal Basket",
                        "Basket of colorful flowers. Vase life: 6–8 days.", 140.0,
                        "SKU-025", "Flower Arrangements", "Orange", false, 0, false, null, 0.0, 0.0, null,
                        "/images/25.jpeg"),
                createProduct(26, "btn26", "White & Blue Hydrangeas",
                        "Elegant hydrangea arrangement. Vase life: 8–10 days.", 170.0,
                        "SKU-026", "Flower Arrangements", "Blue", false, 0, false, null, 0.0, 0.0, null,
                        "/images/26.jpeg"),
                createProduct(27, "btn27", "Pink Peony Arrangement",
                        "Chic peony arrangement. Vase life: 7–9 days.", 160.0,
                        "SKU-027", "Flower Arrangements", "Pink", false, 0, false, null, 0.0, 0.0, null,
                        "/images/27.jpeg"),
                createProduct(28, "btn28", "Flower & Chocolate Box",
                        "A sweet box of flowers & chocolates. Box size: 20×15 cm.", 120.0,
                        "SKU-028", "Gift Items", "Red", false, 0, false, null, 0.0, 0.0, null,
                        "/images/28.jpeg"),
                createProduct(29, "btn29", "Teddy & Roses Combo",
                        "Teddy bear with roses. Teddy size: 25 cm.", 150.0,
                        "SKU-029", "Gift Items", "White", false, 0, false, null, 0.0, 0.0, null,
                        "/images/29.jpeg"),
                createProduct(30, "btn30", "Lavender Gift Set",
                        "Relaxing lavender-themed gift set. Includes: candle, soap, sachet.", 200.0,
                        "SKU-030", "Gift Items", "Purple", false, 0, false, null, 0.0, 0.0, null,
                        "/images/30.jpeg"),
                createProduct(31, "btn31", "Colorful Balloon & Flowers",
                        "Mixed flowers with 2 helium balloons. Total height: ~1 m.", 130.0,
                        "SKU-031", "Gift Items", "Multi", false, 0, false, null, 0.0, 0.0, null,
                        "/images/31.jpeg")
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
