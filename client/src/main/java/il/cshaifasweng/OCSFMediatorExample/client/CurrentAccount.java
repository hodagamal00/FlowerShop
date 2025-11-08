/*package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import org.greenrobot.eventbus.Subscribe;


public class CurrentAccount {
    protected Account currentAccount;
    protected String FullNameA;
    public CurrentAccount()
        {

        }
    @Subscribe
    public void PassAccountEvent(PassAccountEvent passAcc){ // added today
        System.out.println("arrived to passAccountToPrimary sucessfuly");
        Account recvAccount = passAcc.getRecievedAccount();
        System.out.println(recvAccount.getPassword());
        System.out.println(recvAccount.getAccountID());
        System.out.println(recvAccount.getEmail());
        System.out.println(recvAccount.getFullName());
        System.out.println(recvAccount.getAddress());
        System.out.println(recvAccount.getCreditCardNumber());
        System.out.println(recvAccount.getCreditMonthExpire());
        currentAccount = recvAccount;
    }
    public Account getCurrentAccountAA()
    {
        return currentAccount;
    }



}

 */
