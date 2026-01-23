package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "workers_table")
public class Worker extends Account {
    
    public Worker() {}

    public Worker(String fullName, String email, String password, int accountID) {
        super(fullName, email, password, accountID);
    }

    public boolean isFrozen() {
        return super.getFrozen();
    }

    public void setFrozen(boolean frozen) {
        super.setFrozen(frozen);
    }
}
