package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;

import java.util.List;

public class RetrieveDataBaseEvent {

    List<Product> recievedList ;

    public RetrieveDataBaseEvent(List<Product> recvList){
        this.recievedList = recvList ;
    }

    public List<Product> getRecievedList(){
        return this.recievedList;
    }
}
