package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class FoundTable implements Serializable {

    private String message ;
    List<Product> recievedProducts ;
    List<Manager> recievedManagers;
    List<Worker> recievedWokers;

    public FoundTable(String msg, List<Product> recievedProds){
        this.message = msg;
        this.recievedProducts = recievedProds;
    }

    public FoundTable(String msg){
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public List<Product> getRecievedProducts() {
        return recievedProducts;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecievedProducts(List<Product> recievedProducts) {
        this.recievedProducts = recievedProducts;
    }

    public void setRecievedManagers(List<Manager> recievedManagers){ this.recievedManagers = recievedManagers; }

    public List<Manager> getRecievedManagers() { return this.recievedManagers;}

    public void setRecievedWorkers(List<Worker> recievedWokers){this.recievedWokers = recievedWokers;}

    public List<Worker> getRecievedWokers() { return this.recievedWokers;}

}
