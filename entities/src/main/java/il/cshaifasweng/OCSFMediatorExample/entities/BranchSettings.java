package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * BranchSettings Entity
 * Stores configuration and settings for individual branches
 */
@Entity
@Table(name = "branch_settings_table")
public class BranchSettings implements Serializable {
    
    @Id
    @Column(name = "branch_id")
    private int branchId;
    
    @Column(name = "branch_name", nullable = false)
    private String branchName;
    
    @Column(name = "branch_address")
    private String branchAddress;
    
    @Column(name = "branch_phone")
    private String branchPhone;
    
    @Column(name = "branch_email")
    private String branchEmail;
    
    @Column(name = "manager_name")
    private String managerName;
    
    @Column(name = "opening_time")
    private String openingTime;  // e.g., "08:00"
    
    @Column(name = "closing_time")
    private String closingTime;  // e.g., "20:00"
    
    @Column(name = "is_open_weekends")
    private boolean isOpenWeekends;
    
    @Column(name = "weekend_opening_time")
    private String weekendOpeningTime;
    
    @Column(name = "weekend_closing_time")
    private String weekendClosingTime;
    
    @Column(name = "delivery_enabled")
    private boolean deliveryEnabled;
    
    @Column(name = "delivery_fee")
    private double deliveryFee;
    
    @Column(name = "delivery_radius_km")
    private double deliveryRadiusKm;  // Maximum delivery distance
    
    @Column(name = "min_delivery_order")
    private double minDeliveryOrder;  // Minimum order for delivery
    
    @Column(name = "pickup_enabled")
    private boolean pickupEnabled;
    
    @Column(name = "kiosk_enabled")
    private boolean kioskEnabled;
    
    @Column(name = "notifications_enabled")
    private boolean notificationsEnabled;
    
    @Column(name = "email_notifications")
    private boolean emailNotifications;
    
    @Column(name = "sms_notifications")
    private boolean smsNotifications;
    
    @Column(name = "auto_confirm_orders")
    private boolean autoConfirmOrders;
    
    @Column(name = "max_daily_orders")
    private int maxDailyOrders;  // 0 = unlimited
    
    @Column(name = "tax_rate")
    private double taxRate;  // Tax percentage
    
    @Column(name = "currency")
    private String currency;  // e.g., "USD", "ILS"
    
    @Column(name = "is_active")
    private boolean isActive;
    
    @Column(name = "modified_by")
    private String modifiedBy;  // Last user who modified
    
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    // Constructors
    public BranchSettings() {
        this.modifiedDate = new Date();
        this.isActive = true;
        this.deliveryEnabled = true;
        this.pickupEnabled = true;
        this.notificationsEnabled = true;
        this.emailNotifications = true;
        this.smsNotifications = false;
        this.currency = "ILS";
        this.taxRate = 17.0;  // Default Israeli VAT
    }

    public BranchSettings(int branchId, String branchName, String branchAddress) {
        this();
        this.branchId = branchId;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
    }

    // Getters and Setters
    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public void setBranchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
    }

    public String getBranchEmail() {
        return branchEmail;
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public boolean isOpenWeekends() {
        return isOpenWeekends;
    }

    public void setOpenWeekends(boolean openWeekends) {
        isOpenWeekends = openWeekends;
    }

    public String getWeekendOpeningTime() {
        return weekendOpeningTime;
    }

    public void setWeekendOpeningTime(String weekendOpeningTime) {
        this.weekendOpeningTime = weekendOpeningTime;
    }

    public String getWeekendClosingTime() {
        return weekendClosingTime;
    }

    public void setWeekendClosingTime(String weekendClosingTime) {
        this.weekendClosingTime = weekendClosingTime;
    }

    public boolean isDeliveryEnabled() {
        return deliveryEnabled;
    }

    public void setDeliveryEnabled(boolean deliveryEnabled) {
        this.deliveryEnabled = deliveryEnabled;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getDeliveryRadiusKm() {
        return deliveryRadiusKm;
    }

    public void setDeliveryRadiusKm(double deliveryRadiusKm) {
        this.deliveryRadiusKm = deliveryRadiusKm;
    }

    public double getMinDeliveryOrder() {
        return minDeliveryOrder;
    }

    public void setMinDeliveryOrder(double minDeliveryOrder) {
        this.minDeliveryOrder = minDeliveryOrder;
    }

    public boolean isPickupEnabled() {
        return pickupEnabled;
    }

    public void setPickupEnabled(boolean pickupEnabled) {
        this.pickupEnabled = pickupEnabled;
    }

    public boolean isKioskEnabled() {
        return kioskEnabled;
    }

    public void setKioskEnabled(boolean kioskEnabled) {
        this.kioskEnabled = kioskEnabled;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public boolean isAutoConfirmOrders() {
        return autoConfirmOrders;
    }

    public void setAutoConfirmOrders(boolean autoConfirmOrders) {
        this.autoConfirmOrders = autoConfirmOrders;
    }

    public int getMaxDailyOrders() {
        return maxDailyOrders;
    }

    public void setMaxDailyOrders(int maxDailyOrders) {
        this.maxDailyOrders = maxDailyOrders;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "BranchSettings{" +
                "branchId=" + branchId +
                ", branchName='" + branchName + '\'' +
                ", branchAddress='" + branchAddress + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
