package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Manager;
import il.cshaifasweng.OCSFMediatorExample.entities.Worker;

public class PassAccountEventAdmin {
    Account recievedAccount = new Account();
    Manager recievedManagerAccount = new Manager();
    Worker recievedWorkerAccount = new Worker();

    PassAccountEventAdmin(Account recAcc){
        recievedAccount = recAcc;
    }
    PassAccountEventAdmin(Manager recAcc){
        recievedManagerAccount = recAcc;
    }
    PassAccountEventAdmin(Worker recAcc){
        recievedWorkerAccount = recAcc;
    }

    public Account getRecievedAccount() {
        return recievedAccount;
    }

    public void setRecievedAccount(Account acc){
        recievedAccount = acc ;
    }
}
