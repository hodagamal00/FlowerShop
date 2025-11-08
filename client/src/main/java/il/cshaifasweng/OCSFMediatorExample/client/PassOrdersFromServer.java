package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;

import java.util.List;

public class PassOrdersFromServer { // added 18/7
    List<Order> recievedOrders ;
    public PassOrdersFromServer(){

    }

    public void setRecievedOrders(List<Order> orders){
        recievedOrders = orders ;
    }

    public List<Order> getRecievedOrders(){
        return recievedOrders;
    }
}
