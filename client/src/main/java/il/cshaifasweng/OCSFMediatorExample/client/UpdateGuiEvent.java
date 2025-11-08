package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Product;

import java.util.List;

public class UpdateGuiEvent {

    List<Product> recievedList ;

    public UpdateGuiEvent(List<Product> recvList){
        this.recievedList = recvList ;
    }

    public List<Product> getRecievedList(){
        return this.recievedList;
    }

    /**
     * Get the list of products from this event (alias for getRecievedList)
     * @return List of products
     */
    public List<Product> getProducts(){
        return this.recievedList;
    }

}
