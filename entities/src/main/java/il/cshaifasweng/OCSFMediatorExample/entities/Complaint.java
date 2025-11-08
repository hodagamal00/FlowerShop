package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "complaints_table")
public class Complaint implements Serializable {

    @Id
    int complaintID;
    @Column(name = "Customer_Id")
    int CustomerID;
    @Column(name = "Order_Id")
    int OrderID;
    @Column(name = "Accepted")
    boolean Accepted; // Update
    @Column(name = "in24Hours")
    boolean in24Hours;   // Update
    @Column(name = "complaintText")
    String complaintText;
    @Column(name = "shop_Id")
    int shopID;
    @Column(name = "AnswerWorker_Id")
    int answerworkerID;
    @Column(name = "returnedMoney")
    boolean returnedMoney;
    @Column(name = "returnedmoneyvalue")
    int returnedmoneyvalue;
    @Column(name = "RecDay")
    private int day;
    @Column(name = "RecMonth")
    private int month;
    @Column(name = "RecYear")
    private int year;
    @Column(name = "Reply")
    private String ReplyText;   // This is the reply text - Update


    public Complaint(int complaintID, int customerID, int orderID, boolean accepted, boolean in24Hours, String complaintText, int shopID, int answerworkerID, boolean returnedMoney, int returnedmoneyvalue, int day, int month, int year, String replyText) {
        this.complaintID = complaintID;
        this.CustomerID = customerID;
        this.OrderID = orderID;
        this.Accepted = accepted;
        this.in24Hours = in24Hours;
        this.complaintText = complaintText;
        this.shopID = shopID;
        this.answerworkerID = answerworkerID;
        this.returnedMoney = returnedMoney;
        this.returnedmoneyvalue = returnedmoneyvalue;
        this.day = day;
        this.month = month;
        this.year = year;
        this.ReplyText = replyText;
    }

    public  Complaint() {
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public void setAccepted(boolean accepted) {
        Accepted = accepted;
    }

    public void setIn24Hours(boolean in24Hours) {
        this.in24Hours = in24Hours;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public void setAnswerworkerID(int answerworkerID) {
        this.answerworkerID = answerworkerID;
    }

    public void setReturnedMoney(boolean returnedMoney) {
        this.returnedMoney = returnedMoney;
    }

    public void setReturnedmoneyvalue(int returnedmoneyvalue) {
        this.returnedmoneyvalue = returnedmoneyvalue;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setReplyText(String replyText) {
        ReplyText = replyText;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public int getOrderID() {
        return OrderID;
    }

    public boolean isAccepted() {
        return Accepted;
    }

    public boolean isIn24Hours() {
        return in24Hours;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public int getShopID() {
        return shopID;
    }

    public int getAnswerworkerID() {
        return answerworkerID;
    }

    public boolean isReturnedMoney() {
        return returnedMoney;
    }

    public int getReturnedmoneyvalue() {
        return returnedmoneyvalue;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getReplyText() {
        return ReplyText;
    }

    // Alias methods for compatibility with controllers
    public int getId() {
        return complaintID;
    }

    public void setId(int id) {
        this.complaintID = id;
    }

    public String getStatus() {
        if (isAccepted()) {
            return "Accepted";
        } else {
            return "Pending";
        }
    }

    public void setStatus(String status) {
        if ("Accepted".equals(status)) {
            setAccepted(true);
        } else {
            setAccepted(false);
        }
    }

    public int getResponseTime() {
        // Calculate response time in hours based on creation date
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime complaintDate = java.time.LocalDateTime.of(
            getYear(), getMonth(), getDay(), 12, 0
        );
        return (int) java.time.Duration.between(complaintDate, now).toHours();
    }

    public void setResponseTime(int responseTime) {
        // This is a calculation field, no direct setter needed
        // The actual response time is calculated from the creation date
    }

    public String getDate()
    {
        String result = "";
        result = this.day + "/" + this.month + "/" + this.year;
        return result;
    }
    public boolean sameDate(Complaint other)
    {
        if(this.day == other.day && this.month == other.month && this.year == other.year)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "complaintID=" + complaintID +
                ", CustomerID=" + CustomerID +
                ", OrderID=" + OrderID +
                ", Accepted=" + Accepted +
                ", in24Hours=" + in24Hours +
                ", complaintText='" + complaintText + '\'' +
                ", shopID=" + shopID +
                ", answerworkerID=" + answerworkerID +
                ", returnedMoney=" + returnedMoney +
                ", returnedmoneyvalue=" + returnedmoneyvalue +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", ReplyText='" + ReplyText + '\'' +
                '}';
    }
}