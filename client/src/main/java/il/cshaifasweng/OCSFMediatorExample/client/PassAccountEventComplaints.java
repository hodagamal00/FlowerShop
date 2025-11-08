package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Manager;
import il.cshaifasweng.OCSFMediatorExample.entities.Worker;

public class PassAccountEventComplaints {

    Account recievedAccount = new Account();
    Manager recievedManagerAccount = new Manager();
    Worker recievedWorkerAccount = new Worker();

    PassAccountEventComplaints(Account recAcc){
        recievedAccount = recAcc;
    }
    PassAccountEventComplaints(Manager recAcc){
        recievedManagerAccount = recAcc;
    }
    PassAccountEventComplaints(Worker recAcc){
        recievedWorkerAccount = recAcc;
    }

    public Account getRecievedAccount() {
        return recievedAccount;
    }

    public void setRecievedAccount(Account acc){
        recievedAccount = acc ;
    }
}
