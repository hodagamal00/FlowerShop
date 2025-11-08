package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "reports_table")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportID;
    
    @Column(name = "report_type")
    private String reportType;  // "INCOME", "ORDERS", "COMPLAINTS"
    
    @Column(name = "shop_id")
    private int shopID;  // 0 for chain-wide reports
    
    @Column(name = "start_day")
    private int startDay;
    @Column(name = "start_month")
    private int startMonth;
    @Column(name = "start_year")
    private int startYear;
    
    @Column(name = "end_day")
    private int endDay;
    @Column(name = "end_month")
    private int endMonth;
    @Column(name = "end_year")
    private int endYear;
    
    @Column(name = "generated_day")
    private int generatedDay;
    @Column(name = "generated_month")
    private int generatedMonth;
    @Column(name = "generated_year")
    private int generatedYear;
    
    @Column(name = "report_data", length = 10000)
    private String reportData;  // JSON or formatted string with report data
    
    @Column(name = "total_income")
    private double totalIncome;  // For income reports
    
    @Column(name = "total_orders")
    private int totalOrders;  // For order reports
    
    @Column(name = "total_complaints")
    private int totalComplaints;  // For complaint reports

    public Report() {
    }

    public Report(String reportType, int shopID, int startDay, int startMonth, int startYear,
                  int endDay, int endMonth, int endYear, int generatedDay, int generatedMonth,
                  int generatedYear, String reportData) {
        this.reportType = reportType;
        this.shopID = shopID;
        this.startDay = startDay;
        this.startMonth = startMonth;
        this.startYear = startYear;
        this.endDay = endDay;
        this.endMonth = endMonth;
        this.endYear = endYear;
        this.generatedDay = generatedDay;
        this.generatedMonth = generatedMonth;
        this.generatedYear = generatedYear;
        this.reportData = reportData;
    }

    // Getters and Setters
    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getGeneratedDay() {
        return generatedDay;
    }

    public void setGeneratedDay(int generatedDay) {
        this.generatedDay = generatedDay;
    }

    public int getGeneratedMonth() {
        return generatedMonth;
    }

    public void setGeneratedMonth(int generatedMonth) {
        this.generatedMonth = generatedMonth;
    }

    public int getGeneratedYear() {
        return generatedYear;
    }

    public void setGeneratedYear(int generatedYear) {
        this.generatedYear = generatedYear;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getTotalComplaints() {
        return totalComplaints;
    }

    public void setTotalComplaints(int totalComplaints) {
        this.totalComplaints = totalComplaints;
    }

    public String getDateRange() {
        return startDay + "/" + startMonth + "/" + startYear + " - " + 
               endDay + "/" + endMonth + "/" + endYear;
    }

    @Override
    public String toString() {
        return "Report{" +
                "reportID=" + reportID +
                ", reportType='" + reportType + '\'' +
                ", shopID=" + shopID +
                ", dateRange='" + getDateRange() + '\'' +
                ", totalIncome=" + totalIncome +
                ", totalOrders=" + totalOrders +
                ", totalComplaints=" + totalComplaints +
                '}';
    }
}
