package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Manager;
import il.cshaifasweng.OCSFMediatorExample.entities.Worker;

public class PassAccountEventReplyComplaint {
    Account recievedAccount = new Account();
    Manager recievedManagerAccount = new Manager();
    Worker recievedWorkerAccount = new Worker();

    PassAccountEventReplyComplaint(Account recAcc){
        recievedAccount = recAcc;
    }
    PassAccountEventReplyComplaint(Manager recAcc){
        recievedManagerAccount = recAcc;
    }
    PassAccountEventReplyComplaint(Worker recAcc){
        recievedWorkerAccount = recAcc;
    }

    public Account getRecievedAccount() {
        return recievedAccount;
    }

    public void setRecievedAccount(Account acc){
        recievedAccount = acc ;
    }
}
