package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "managers_table")
public class Manager extends Account {
    @Column(name = "ShopID") // 0 = Chain Manager, Else = Shop Manager
    private int ShopID;

    public Manager() {}

    public Manager(String fullName, String email, String password, int accountID) {
        super(fullName, email, password, accountID);
    }

    public void setShopID(int shopID) {
        ShopID = shopID;
    }

    public int getShopID() {
        return ShopID;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "accountID=" + getAccountID() +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", ShopID=" + ShopID +
                ", loggedIn=" + getLoggedIn() +
                '}';
    }
}
