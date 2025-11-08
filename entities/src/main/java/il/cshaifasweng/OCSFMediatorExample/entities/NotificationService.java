package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Notification Service to simulate Email/SMS notifications.
 * In a production system, this would integrate with actual email/SMS providers.
 * For this system, notifications are logged to console and file.
 */
public class NotificationService implements Serializable {

    private static final String NOTIFICATION_LOG_FILE = "notifications.log";

    /**
     * Send order confirmation notification to customer
     * Simulates email/SMS confirmation sent immediately upon order placement
     */
    public static void sendOrderConfirmation(Order order, Account account) {
        String timestamp = getCurrentTimestamp();
        StringBuilder message = new StringBuilder();
        message.append("========== ORDER CONFIRMATION ==========\n");
        message.append("Time: ").append(timestamp).append("\n");
        message.append("Customer: ").append(account.getFullName()).append("\n");
        message.append("Email: ").append(account.getEmail()).append("\n");
        message.append("Phone: ").append(account.getPhoneNumber()).append("\n");
        message.append("Order ID: ").append(order.getOrderID()).append("\n");
        message.append("Total Price: ₪").append(order.getTotalPrice()).append("\n");
        message.append("Delivery Type: ").append(order.isPickUp() ? "Pickup" : "Delivery").append("\n");
        if (!order.isPickUp()) {
            message.append("Delivery Address: ").append(order.getRecepAddress()).append("\n");
            message.append("Recipient: ").append(order.getRecepName()).append("\n");
            message.append("Recipient Phone: ").append(order.getRecepPhone()).append("\n");
        }
        message.append("Expected Date: ").append(order.getPrepareDay()).append("/")
               .append(order.getPrepareMonth()).append("/").append(order.getPrepareYear()).append(" ")
               .append(order.getPrepareHour()).append(":").append(String.format("%02d", order.getPrepareMin())).append("\n");
        message.append("========================================\n");

        logNotification(message.toString());
        System.out.println("[EMAIL/SMS SENT] " + message.toString());
    }

    /**
     * Send delivery arrival notification to purchaser
     * Simulates notification sent when order is delivered
     */
    public static void sendDeliveryNotification(Order order, Account account) {
        String timestamp = getCurrentTimestamp();
        StringBuilder message = new StringBuilder();
        message.append("========== DELIVERY NOTIFICATION ==========\n");
        message.append("Time: ").append(timestamp).append("\n");
        message.append("Dear ").append(account.getFullName()).append(",\n");
        message.append("Your order #").append(order.getOrderID()).append(" has been delivered!\n");
        if (!order.isPickUp()) {
            message.append("Delivered to: ").append(order.getRecepName()).append("\n");
            message.append("Address: ").append(order.getRecepAddress()).append("\n");
        }
        message.append("Thank you for choosing Lilach Flower Shop!\n");
        message.append("===========================================\n");

        logNotification(message.toString());
        System.out.println("[EMAIL/SMS SENT] " + message.toString());
    }

    /**
     * Send cancellation confirmation notification
     */
    public static void sendCancellationConfirmation(Order order, Account account, double refundAmount, String refundStatus) {
        String timestamp = getCurrentTimestamp();
        StringBuilder message = new StringBuilder();
        message.append("========== CANCELLATION CONFIRMATION ==========\n");
        message.append("Time: ").append(timestamp).append("\n");
        message.append("Customer: ").append(account.getFullName()).append("\n");
        message.append("Email: ").append(account.getEmail()).append("\n");
        message.append("Order ID: ").append(order.getOrderID()).append("\n");
        message.append("Cancellation Status: ").append(refundStatus).append("\n");
        message.append("Refund Amount: ₪").append(String.format("%.2f", refundAmount)).append("\n");
        if (refundAmount > 0) {
            message.append("The refund will be credited to your account.\n");
        }
        message.append("===============================================\n");

        logNotification(message.toString());
        System.out.println("[EMAIL/SMS SENT] " + message.toString());
    }

    /**
     * Send complaint response notification
     */
    public static void sendComplaintResponse(Complaint complaint, Account account, String replyText) {
        String timestamp = getCurrentTimestamp();
        StringBuilder message = new StringBuilder();
        message.append("========== COMPLAINT RESPONSE ==========\n");
        message.append("Time: ").append(timestamp).append("\n");
        message.append("Customer: ").append(account.getFullName()).append("\n");
        message.append("Email: ").append(account.getEmail()).append("\n");
        message.append("Complaint ID: ").append(complaint.getComplaintID()).append("\n");
        message.append("Response: ").append(replyText).append("\n");
        if (complaint.isReturnedMoney()) {
            message.append("Compensation: ₪").append(complaint.getReturnedmoneyvalue()).append("\n");
        }
        message.append("Thank you for your feedback!\n");
        message.append("========================================\n");

        logNotification(message.toString());
        System.out.println("[EMAIL/SMS SENT] " + message.toString());
    }

    /**
     * Log notification to file for record keeping
     */
    private static void logNotification(String message) {
        try (FileWriter fw = new FileWriter(NOTIFICATION_LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(message);
            pw.flush();
        } catch (IOException e) {
            System.err.println("Error logging notification: " + e.getMessage());
        }
    }

    /**
     * Get current timestamp as formatted string
     */
    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
