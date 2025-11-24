package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders_table")
public class Order implements Serializable {

    @Id
    private int orderID;
    @Column(name = "Pick_Up")
    private boolean pickUp;
    @Column(name = "Shop_ID")
    private int shopID;                     // Privilage 0 = GUEST
    @Column(name = "Greeting")                 // Privilage 1 = Customer
    private String greeting;                   // Privilage 2 = Worker
    @Column(name = "Total_Price")              // Privilage 3 = Manager
    private int totalPrice;                // Privilage 4 = Chain Manager
    @Column(name = "Delivered_Address")
    private String deliveredAddress;
    @Column(name = "Account_ID")
    private int accountID;
    @Column(name = "Gift")
    private boolean gift;
    @Column(name = "Delivered")
    private boolean delivered;
    @Column(name = "Delivery_Fee")
    private double deliveryFee;
    @Column(name = "Payment_Method")
    private String paymentMethod;
    @Column(name = "CreditNumber")
    private long creditCardNumber;
    @Column(name = "Prepare_Day")
    private int prepareDay;
    @Column(name = "Prepare_Month")
    private int prepareMonth;
    @Column(name = "Prepare_Hour")
    private int prepareHour;
    @Column(name = "Prepare_Minute")
    private int prepareMin;
    @Column(name = "Prepare_Year")
    private int prepareYear;
    @Column(name = "Order_Hour")
    private int OrderHour;
    @Column(name = "Order_Mintue")
    private int OrderMintue;
    @Column(name = "Order_Day")
    private int orderDay;
    @Column(name = "Order_Month")
    private int orderMonth;
    @Column(name = "Order_Year")
    private int orderYear;
    @Column(name = "creditExpMonth")
    private int creditCardExpMonth;
    @Column(name = "creditExpYear")
    private int creditCardExpYear;
    @Column(name = "creditCVV")
    private int creditCardCVV;
    @Column(name = "recepName")
    private String RecepName;
    @Column(name = "recepPhone")
    private long RecepPhone;
    @Column(name = "recepAddress")
    private String RecepAddress;
    @Column(name = "Products")
    private String Products;
    
    // New fields for cancellation and refund tracking
    @Column(name = "is_cancelled")
    private boolean isCancelled;
    @Column(name = "cancel_day")
    private int cancelDay;
    @Column(name = "cancel_month")
    private int cancelMonth;
    @Column(name = "cancel_year")
    private int cancelYear;
    @Column(name = "cancel_hour")
    private int cancelHour;
    @Column(name = "cancel_minute")
    private int cancelMinute;
    @Column(name = "refund_amount")
    private double refundAmount;
    @Column(name = "refund_status")
    private String refundStatus;  // "FULL", "HALF", "NONE"
    /*private List<Product> products;*/

    public Order(){}

    public Order(int orderID, boolean pickUp, int shopID, String greeting, int totalPrice, String deliveredAddress, int accountID, boolean gift, boolean delivered, int prepareDay, int prepareMonth, int prepareYear, int orderDay, int orderMonth, int orderYear, long creditCardNumber, int creditCardExpMonth, int creditCardExpYear, int creditCardCVV, String recepName, long recepPhone, String recepAddress,String Products,int orderHour,int orderMintue,int prepareHour,int prepareMin,double deliveryFee,String paymentMethod) {
        this.orderID = orderID;
        this.pickUp = pickUp;
        this.shopID = shopID;
        this.greeting = greeting;
        this.totalPrice = totalPrice;
        this.deliveredAddress = deliveredAddress;
        this.accountID = accountID;
        this.gift = gift;
        this.delivered = delivered;
        this.deliveryFee = deliveryFee;
        this.paymentMethod = paymentMethod;
        this.creditCardNumber = creditCardNumber;
        this.prepareDay = prepareDay;
        this.prepareMonth = prepareMonth;
        this.prepareYear = prepareYear;
        this.orderDay = orderDay;
        this.orderMonth = orderMonth;
        this.orderYear = orderYear;
        this.creditCardExpMonth = creditCardExpMonth;
        this.creditCardExpYear = creditCardExpYear;
        this.creditCardCVV = creditCardCVV;
        this.creditCardNumber = creditCardNumber;
        this.RecepName = recepName;
        this.RecepPhone = recepPhone;
        this.RecepAddress = recepAddress;
        this.Products = Products;
        this.OrderHour = orderHour;
        this.OrderMintue = orderMintue;
        this.prepareMin = prepareMin;
        this.prepareHour = prepareHour;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", pickUp=" + pickUp +
                ", shopID=" + shopID +
                ", greeting='" + greeting + '\'' +
                ", totalPrice=" + totalPrice +
                ", deliveredAddress='" + deliveredAddress + '\'' +
                ", accountID=" + accountID +
                ", gift=" + gift +
                ", delivered=" + delivered +
                ", deliveryFee=" + deliveryFee +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", creditCardNumber=" + creditCardNumber +
                ", prepareDay=" + prepareDay +
                ", prepareMonth=" + prepareMonth +
                ", prepareYear=" + prepareYear +
                ", orderDay=" + orderDay +
                ", orderMonth=" + orderMonth +
                ", orderYear=" + orderYear +
                ", creditCardExpMonth=" + creditCardExpMonth +
                ", creditCardExpYear=" + creditCardExpYear +
                ", creditCardCVV=" + creditCardCVV +
                ", RecepName='" + RecepName + '\'' +
                ", RecepPhone=" + RecepPhone +
                ", RecepAddress='" + RecepAddress + '\'' +
                '}';
    }

    public String getDate()
    {
        String result = "";
        result = this.orderDay + "/" + this.orderMonth + "/" + this.orderYear;
        return result;
    }
    public boolean sameDate(Order other)
    {
        if(this.orderDay == other.orderDay && this.orderMonth == other.orderMonth && this.orderYear == other.orderYear)
            return true;
        return false;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setPickUp(boolean pickUp) {
        this.pickUp = pickUp;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDeliveredAddress(String deliveredAddress) {
        this.deliveredAddress = deliveredAddress;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public void setGift(boolean gift) {
        this.gift = gift;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public void setCreditCardNumber(long creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setPrepareDay(int prepareDay) {
        this.prepareDay = prepareDay;
    }

    public void setPrepareMonth(int prepareMonth) {
        this.prepareMonth = prepareMonth;
    }

    public void setPrepareYear(int prepareYear) {
        this.prepareYear = prepareYear;
    }

    public void setOrderDay(int orderDay) {
        this.orderDay = orderDay;
    }

    public void setOrderMonth(int orderMonth) {
        this.orderMonth = orderMonth;
    }

    public void setOrderYear(int orderYear) {
        this.orderYear = orderYear;
    }


    public void setCreditCardExpMonth(int creditCardExpMonth) {
        this.creditCardExpMonth = creditCardExpMonth;
    }

    public void setCreditCardExpYear(int creditCardExpYear) {
        this.creditCardExpYear = creditCardExpYear;
    }

    public void setCreditCardCVV(int creditCardCVV) {
        this.creditCardCVV = creditCardCVV;
    }

    public void setRecepName(String recepName) {
        RecepName = recepName;
    }

    public void setRecepPhone(int recepPhone) {
        RecepPhone = recepPhone;
    }

    public void setRecepAddress(String recepAddress) {
        RecepAddress = recepAddress;
    }

    public int getOrderID() {
        return orderID;
    }

    public boolean isPickUp() {
        return pickUp;
    }

    public int getShopID() {
        return shopID;
    }

    public String getGreeting() {
        return greeting;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getDeliveredAddress() {
        return deliveredAddress;
    }

    public int getAccountID() {
        return accountID;
    }

    public boolean isGift() {
        return gift;
    }

    public boolean isDelivered() {
        return delivered;
    }
    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public long getCreditCardNumber() {
        return creditCardNumber;
    }

    public int getPrepareDay() {
        return prepareDay;
    }

    public int getPrepareMonth() {
        return prepareMonth;
    }

    public int getPrepareYear() {
        return prepareYear;
    }

    public int getOrderDay() {
        return orderDay;
    }

    public int getOrderMonth() {
        return orderMonth;
    }

    public int getOrderYear() {
        return orderYear;
    }

    public int getCreditCardExpMonth() {
        return creditCardExpMonth;
    }

    public int getCreditCardExpYear() {
        return creditCardExpYear;
    }

    public int getCreditCardCVV() {
        return creditCardCVV;
    }

    public String getRecepName() {
        return RecepName;
    }

    public long getRecepPhone() {
        return RecepPhone;
    }

    public String getRecepAddress() {
        return RecepAddress;
    }

    public String getProducts() {
        return Products;
    }

    public void setProducts(String products) {
        Products = products;
    }

    public void setOrderHour(int orderHour) {
        OrderHour = orderHour;
    }

    public void setOrderMintue(int orderMintue) {
        OrderMintue = orderMintue;
    }

    public void setRecepPhone(long recepPhone) {
        RecepPhone = recepPhone;
    }

    public int getOrderHour() {
        return OrderHour;
    }

    public int getOrderMintue() {
        return OrderMintue;
    }

    // Getters and Setters for cancellation fields
    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public int getCancelDay() {
        return cancelDay;
    }

    public void setCancelDay(int cancelDay) {
        this.cancelDay = cancelDay;
    }

    public int getCancelMonth() {
        return cancelMonth;
    }

    public void setCancelMonth(int cancelMonth) {
        this.cancelMonth = cancelMonth;
    }

    public int getCancelYear() {
        return cancelYear;
    }

    public void setCancelYear(int cancelYear) {
        this.cancelYear = cancelYear;
    }

    public int getCancelHour() {
        return cancelHour;
    }

    public void setCancelHour(int cancelHour) {
        this.cancelHour = cancelHour;
    }

    public int getCancelMinute() {
        return cancelMinute;
    }

    public void setCancelMinute(int cancelMinute) {
        this.cancelMinute = cancelMinute;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    // Alias methods for compatibility with controllers
    public int getId() {
        return orderID;
    }

    public void setId(int id) {
        this.orderID = id;
    }

    public String getStatus() {
        if (isCancelled()) {
            return "Cancelled";
        } else if (isDelivered()) {
            return "Delivered";
        } else {
            return "Pending";
        }
    }

    public void setStatus(String status) {
        // Set based on status string
        if ("Cancelled".equals(status)) {
            setCancelled(true);
        } else if ("Delivered".equals(status)) {
            setDelivered(true);
        } else {
            setDelivered(false);
            setCancelled(false);
        }
    }

    public java.time.LocalDateTime getOrderDate() {
        // Convert the individual date components to LocalDateTime
        return java.time.LocalDateTime.of(
            getOrderYear(), getOrderMonth(), getOrderDay(), 
            getOrderHour(), getOrderMintue()
        );
    }

    public void setOrderDate(java.time.LocalDateTime orderDate) {
        setOrderDay(orderDate.getDayOfMonth());
        setOrderMonth(orderDate.getMonthValue());
        setOrderYear(orderDate.getYear());
        setOrderHour(orderDate.getHour());
        setOrderMintue(orderDate.getMinute());
    }

    public double getPrice() {
        return getTotalPrice();
    }

    public java.time.LocalDateTime getDelivery_time() {
        // Convert the individual prepare date components to LocalDateTime
        return java.time.LocalDateTime.of(
            getPrepareYear(), getPrepareMonth(), getPrepareDay(),
            getPrepareHour(), getPrepareMin()
        );
    }

    public String getAddress() {
        return getDeliveredAddress();
    }

    public int getShop() {
        return getShopID();
    }

    public int getPrepareHour() {
        return prepareHour;
    }

    public void setPrepareHour(int prepareHour) {
        this.prepareHour = prepareHour;
    }

    public int getPrepareMin() {
        return prepareMin;
    }

    public void setPrepareMin(int prepareMin) {
        this.prepareMin = prepareMin;
    }

    /**
     * Calculate refund amount based on cancellation policy:
     * - >= 3 hours before delivery: 100% refund
     * - Between 1-3 hours before delivery: 50% refund
     * - < 1 hour before delivery: No refund
     * 
     * @param cancelDay Current cancellation day
     * @param cancelMonth Current cancellation month
     * @param cancelYear Current cancellation year
     * @param cancelHour Current cancellation hour
     * @param cancelMinute Current cancellation minute
     * @return Refund percentage (0.0, 0.5, or 1.0)
     */
    public double calculateRefund(int cancelDay, int cancelMonth, int cancelYear, int cancelHour, int cancelMinute) {
        // Create simple time representations (in minutes from start of year)
        // This is a simplified calculation - in production, use proper date/time libraries
        long deliveryTimeInMinutes = (prepareDay * 24 * 60) + (prepareHour * 60) + prepareMin + 
                                      (prepareMonth * 30 * 24 * 60) + (prepareYear * 365 * 24 * 60);
        long cancelTimeInMinutes = (cancelDay * 24 * 60) + (cancelHour * 60) + cancelMinute + 
                                   (cancelMonth * 30 * 24 * 60) + (cancelYear * 365 * 24 * 60);
        
        long hoursUntilDelivery = (deliveryTimeInMinutes - cancelTimeInMinutes) / 60;
        
        if (hoursUntilDelivery >= 3) {
            this.refundStatus = "FULL";
            this.refundAmount = totalPrice;
            return 1.0;  // 100% refund
        } else if (hoursUntilDelivery >= 1) {
            this.refundStatus = "HALF";
            this.refundAmount = totalPrice * 0.5;
            return 0.5;  // 50% refund
        } else {
            this.refundStatus = "NONE";
            this.refundAmount = 0;
            return 0.0;  // No refund
        }
    }
}
