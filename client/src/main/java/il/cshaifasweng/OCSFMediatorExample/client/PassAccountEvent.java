package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Manager;
import il.cshaifasweng.OCSFMediatorExample.entities.Worker;

public class PassAccountEvent { // added today

    Account recievedAccount = new Account();
    Manager recievedManagerAccount = new Manager();
    Worker recievedWorkerAccount = new Worker();

    PassAccountEvent(Account recAcc){
        recievedAccount = recAcc;
    }
    PassAccountEvent(Manager recAcc){
        recievedManagerAccount = recAcc;
    }
    PassAccountEvent(Worker recAcc){
        recievedWorkerAccount = recAcc;
    }

    public Account getRecievedAccount() {
        return recievedAccount;
    }

    public void setRecievedAccount(Account acc){
        recievedAccount = acc ;
    }
}
