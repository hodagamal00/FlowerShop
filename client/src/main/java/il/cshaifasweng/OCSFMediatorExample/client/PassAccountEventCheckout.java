package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Manager;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;
import il.cshaifasweng.OCSFMediatorExample.entities.Worker;

import java.util.List;

public class PassAccountEventCheckout { // added today

    Account recievedAccount = new Account();
    Manager recievedManagerAccount = new Manager();
    Worker recievedWorkerAccount = new Worker();
    public List<Product> productsToCheckout ;


    PassAccountEventCheckout(Account recAcc){
        recievedAccount = recAcc;
    }
    PassAccountEventCheckout(Manager recAcc){
        recievedManagerAccount = recAcc;
    }
    PassAccountEventCheckout(Worker recAcc){
        recievedWorkerAccount = recAcc;
    }
    public List<Product> getProductsToCheckout(){
        return productsToCheckout;
    }


    public Account getRecievedAccount() {
        return recievedAccount;
    }

    public void setRecievedAccount(Account acc){
        recievedAccount = acc ;
    }
}
