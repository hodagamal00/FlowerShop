package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "messages_table")
public class Message implements Serializable {
    @Id
    int messageID;
    @Column(name = "customer_ID")
    private int customerID;
    @Column(name = "Message_Txt")
    private String msgText;

    public Message() {
    }

    public Message(int messageID, int customerID, String msgText) {
        this.messageID = messageID;
        this.customerID = customerID;
        this.msgText = msgText;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }
}