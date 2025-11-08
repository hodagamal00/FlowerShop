package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * GlobalSettings Entity
 * Stores network-wide configuration and settings for the entire FlowerShop chain
 */
@Entity
@Table(name = "global_settings_table")
public class GlobalSettings implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private int settingId;
    
    @Column(name = "company_name")
    private String companyName;
    
    @Column(name = "company_logo_url")
    private String companyLogoUrl;
    
    @Column(name = "support_email")
    private String supportEmail;
    
    @Column(name = "support_phone")
    private String supportPhone;
    
    @Column(name = "website_url")
    private String websiteUrl;
    
    @Column(name = "default_currency")
    private String defaultCurrency;
    
    @Column(name = "default_tax_rate")
    private double defaultTaxRate;
    
    @Column(name = "subscription_enabled")
    private boolean subscriptionEnabled;
    
    @Column(name = "subscription_discount")
    private double subscriptionDiscount;  // Discount percent for subscribers
    
    @Column(name = "early_order_guarantee_hours")
    private int earlyOrderGuaranteeHours;  // Hours for guaranteed delivery time
    
    @Column(name = "immediate_order_max_hours")
    private int immediateOrderMaxHours;  // Max hours for immediate orders (default 3)
    
    @Column(name = "full_refund_min_hours")
    private int fullRefundMinHours;  // Min hours before delivery for full refund (default 3)
    
    @Column(name = "partial_refund_min_hours")
    private int partialRefundMinHours;  // Min hours before delivery for partial refund (default 1)
    
    @Column(name = "partial_refund_percent")
    private double partialRefundPercent;  // Partial refund percentage (default 50%)
    
    @Column(name = "complaint_response_hours")
    private int complaintResponseHours;  // Hours to respond to complaints (default 24)
    
    @Column(name = "complaint_compensation_enabled")
    private boolean complaintCompensationEnabled;
    
    @Column(name = "default_delivery_fee")
    private double defaultDeliveryFee;
    
    @Column(name = "min_order_for_free_delivery")
    private double minOrderForFreeDelivery;
    
    @Column(name = "loyalty_points_enabled")
    private boolean loyaltyPointsEnabled;
    
    @Column(name = "loyalty_points_per_dollar")
    private double loyaltyPointsPerDollar;  // Points earned per dollar spent
    
    @Column(name = "notifications_enabled")
    private boolean notificationsEnabled;
    
    @Column(name = "email_notifications_default")
    private boolean emailNotificationsDefault;
    
    @Column(name = "sms_notifications_default")
    private boolean smsNotificationsDefault;
    
    @Column(name = "maintenance_mode")
    private boolean maintenanceMode;  // System-wide maintenance mode
    
    @Column(name = "maintenance_message")
    private String maintenanceMessage;
    
    @Column(name = "allow_guest_checkout")
    private boolean allowGuestCheckout;
    
    @Column(name = "require_account_verification")
    private boolean requireAccountVerification;
    
    @Column(name = "max_login_attempts")
    private int maxLoginAttempts;
    
    @Column(name = "session_timeout_minutes")
    private int sessionTimeoutMinutes;
    
    @Column(name = "modified_by")
    private String modifiedBy;
    
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    // Constructors
    public GlobalSettings() {
        this.modifiedDate = new Date();
        this.companyName = "FlowerShop";
        this.defaultCurrency = "ILS";
        this.defaultTaxRate = 17.0;
        this.subscriptionEnabled = true;
        this.subscriptionDiscount = 10.0;
        this.earlyOrderGuaranteeHours = 24;
        this.immediateOrderMaxHours = 3;
        this.fullRefundMinHours = 3;
        this.partialRefundMinHours = 1;
        this.partialRefundPercent = 50.0;
        this.complaintResponseHours = 24;
        this.complaintCompensationEnabled = true;
        this.notificationsEnabled = true;
        this.emailNotificationsDefault = true;
        this.smsNotificationsDefault = false;
        this.maintenanceMode = false;
        this.allowGuestCheckout = false;
        this.requireAccountVerification = true;
        this.maxLoginAttempts = 5;
        this.sessionTimeoutMinutes = 60;
    }

    // Getters and Setters
    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    public String getSupportPhone() {
        return supportPhone;
    }

    public void setSupportPhone(String supportPhone) {
        this.supportPhone = supportPhone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public double getDefaultTaxRate() {
        return defaultTaxRate;
    }

    public void setDefaultTaxRate(double defaultTaxRate) {
        this.defaultTaxRate = defaultTaxRate;
    }

    public boolean isSubscriptionEnabled() {
        return subscriptionEnabled;
    }

    public void setSubscriptionEnabled(boolean subscriptionEnabled) {
        this.subscriptionEnabled = subscriptionEnabled;
    }

    public double getSubscriptionDiscount() {
        return subscriptionDiscount;
    }

    public void setSubscriptionDiscount(double subscriptionDiscount) {
        this.subscriptionDiscount = subscriptionDiscount;
    }

    public int getEarlyOrderGuaranteeHours() {
        return earlyOrderGuaranteeHours;
    }

    public void setEarlyOrderGuaranteeHours(int earlyOrderGuaranteeHours) {
        this.earlyOrderGuaranteeHours = earlyOrderGuaranteeHours;
    }

    public int getImmediateOrderMaxHours() {
        return immediateOrderMaxHours;
    }

    public void setImmediateOrderMaxHours(int immediateOrderMaxHours) {
        this.immediateOrderMaxHours = immediateOrderMaxHours;
    }

    public int getFullRefundMinHours() {
        return fullRefundMinHours;
    }

    public void setFullRefundMinHours(int fullRefundMinHours) {
        this.fullRefundMinHours = fullRefundMinHours;
    }

    public int getPartialRefundMinHours() {
        return partialRefundMinHours;
    }

    public void setPartialRefundMinHours(int partialRefundMinHours) {
        this.partialRefundMinHours = partialRefundMinHours;
    }

    public double getPartialRefundPercent() {
        return partialRefundPercent;
    }

    public void setPartialRefundPercent(double partialRefundPercent) {
        this.partialRefundPercent = partialRefundPercent;
    }

    public int getComplaintResponseHours() {
        return complaintResponseHours;
    }

    public void setComplaintResponseHours(int complaintResponseHours) {
        this.complaintResponseHours = complaintResponseHours;
    }

    public boolean isComplaintCompensationEnabled() {
        return complaintCompensationEnabled;
    }

    public void setComplaintCompensationEnabled(boolean complaintCompensationEnabled) {
        this.complaintCompensationEnabled = complaintCompensationEnabled;
    }

    public double getDefaultDeliveryFee() {
        return defaultDeliveryFee;
    }

    public void setDefaultDeliveryFee(double defaultDeliveryFee) {
        this.defaultDeliveryFee = defaultDeliveryFee;
    }

    public double getMinOrderForFreeDelivery() {
        return minOrderForFreeDelivery;
    }

    public void setMinOrderForFreeDelivery(double minOrderForFreeDelivery) {
        this.minOrderForFreeDelivery = minOrderForFreeDelivery;
    }

    public boolean isLoyaltyPointsEnabled() {
        return loyaltyPointsEnabled;
    }

    public void setLoyaltyPointsEnabled(boolean loyaltyPointsEnabled) {
        this.loyaltyPointsEnabled = loyaltyPointsEnabled;
    }

    public double getLoyaltyPointsPerDollar() {
        return loyaltyPointsPerDollar;
    }

    public void setLoyaltyPointsPerDollar(double loyaltyPointsPerDollar) {
        this.loyaltyPointsPerDollar = loyaltyPointsPerDollar;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public boolean isEmailNotificationsDefault() {
        return emailNotificationsDefault;
    }

    public void setEmailNotificationsDefault(boolean emailNotificationsDefault) {
        this.emailNotificationsDefault = emailNotificationsDefault;
    }

    public boolean isSmsNotificationsDefault() {
        return smsNotificationsDefault;
    }

    public void setSmsNotificationsDefault(boolean smsNotificationsDefault) {
        this.smsNotificationsDefault = smsNotificationsDefault;
    }

    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public String getMaintenanceMessage() {
        return maintenanceMessage;
    }

    public void setMaintenanceMessage(String maintenanceMessage) {
        this.maintenanceMessage = maintenanceMessage;
    }

    public boolean isAllowGuestCheckout() {
        return allowGuestCheckout;
    }

    public void setAllowGuestCheckout(boolean allowGuestCheckout) {
        this.allowGuestCheckout = allowGuestCheckout;
    }

    public boolean isRequireAccountVerification() {
        return requireAccountVerification;
    }

    public void setRequireAccountVerification(boolean requireAccountVerification) {
        this.requireAccountVerification = requireAccountVerification;
    }

    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }

    public void setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }

    public int getSessionTimeoutMinutes() {
        return sessionTimeoutMinutes;
    }

    public void setSessionTimeoutMinutes(int sessionTimeoutMinutes) {
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
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
        return "GlobalSettings{" +
                "settingId=" + settingId +
                ", companyName='" + companyName + '\'' +
                ", maintenanceMode=" + maintenanceMode +
                '}';
    }
}
