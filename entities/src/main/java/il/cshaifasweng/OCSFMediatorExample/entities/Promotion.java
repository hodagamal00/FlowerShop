package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Promotion Entity
 * Represents promotional campaigns that can be branch-specific or network-wide
 */
@Entity
@Table(name = "promotions_table")
public class Promotion implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private int promotionId;
    
    @Column(name = "promotion_name", nullable = false)
    private String promotionName;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "discount_percent")
    private double discountPercent;  // Discount percentage (0-100)
    
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    @Column(name = "is_active")
    private boolean isActive;
    
    @Column(name = "is_network_wide")
    private boolean isNetworkWide;  // true = all branches, false = specific branch
    
    @Column(name = "branch_id")
    private Integer branchId;  // Null if network-wide
    
    @Column(name = "target_category")
    private String targetCategory;  // e.g., "All", "Bouquets", "Arrangements"
    
    @Column(name = "minimum_order_value")
    private double minimumOrderValue;  // Minimum order value to qualify
    
    @Column(name = "created_by")
    private String createdBy;  // Username of creator
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Column(name = "modified_by")
    private String modifiedBy;  // Last user who modified
    
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    // Constructors
    public Promotion() {
        this.createdDate = new Date();
        this.modifiedDate = new Date();
        this.isActive = true;
    }

    public Promotion(String promotionName, String description, double discountPercent,
                     Date startDate, Date endDate, boolean isNetworkWide, Integer branchId) {
        this();
        this.promotionName = promotionName;
        this.description = description;
        this.discountPercent = discountPercent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isNetworkWide = isNetworkWide;
        this.branchId = branchId;
    }




    // Getters and Setters
    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNetworkWide() {
        return isNetworkWide;
    }

    public void setNetworkWide(boolean networkWide) {
        isNetworkWide = networkWide;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getTargetCategory() {
        return targetCategory;
    }

    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    public double getMinimumOrderValue() {
        return minimumOrderValue;
    }

    public void setMinimumOrderValue(double minimumOrderValue) {
        this.minimumOrderValue = minimumOrderValue;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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

    /**
     * Check if promotion is currently valid (active and within date range)
     */
    public boolean isValidNow() {
        if (!isActive) return false;
        
        Date now = new Date();
        if (startDate != null && now.before(startDate)) return false;
        if (endDate != null && now.after(endDate)) return false;
        
        return true;
    }

    /**
     * Check if promotion applies to a specific branch
     */
    public boolean appliesToBranch(int branchIdToCheck) {
        if (isNetworkWide) return true;
        return branchId != null && branchId == branchIdToCheck;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "promotionId=" + promotionId +
                ", promotionName='" + promotionName + '\'' +
                ", discountPercent=" + discountPercent +
                ", isActive=" + isActive +
                ", isNetworkWide=" + isNetworkWide +
                ", branchId=" + branchId +
                '}';
    }
}
