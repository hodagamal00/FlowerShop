package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Manager;
import il.cshaifasweng.OCSFMediatorExample.entities.Worker;

public class PassAccountEventDelivery {
    Account recievedAccount = new Account();
    Manager recievedManagerAccount = new Manager();
    Worker recievedWorkerAccount = new Worker();

    PassAccountEventDelivery(Account recAcc){
        recievedAccount = recAcc;
    }
    PassAccountEventDelivery(Manager recAcc){
        recievedManagerAccount = recAcc;
    }
    PassAccountEventDelivery(Worker recAcc){
        recievedWorkerAccount = recAcc;
    }

    public Account getRecievedAccount() {
        return recievedAccount;
    }

    public void setRecievedAccount(Account acc){
        recievedAccount = acc ;
    }
}
