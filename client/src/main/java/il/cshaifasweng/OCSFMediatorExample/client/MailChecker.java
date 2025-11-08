package il.cshaifasweng.OCSFMediatorExample.client;

public class MailChecker {
    private boolean mailExists = true;
    private boolean passwordExists = true;
    private boolean loggedIn = true;

    public MailChecker(boolean existsMail){
        this.mailExists = existsMail;

    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean getExistsMail(){
        return mailExists;
    }
    public boolean getExistsPassword(){return passwordExists;}

    public void setPasswordExists(boolean existsPass){ this.passwordExists = existsPass; }

}
