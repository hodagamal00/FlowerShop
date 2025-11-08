package il.cshaifasweng.OCSFMediatorExample.client;

public class MailPassMatch {
    private String email;
    private String password;
    private String person;
    private boolean exists;

    public MailPassMatch(String email, String password, String person) {
        this.email = email;
        this.password = password;
        this.person = person;
    }
    public MailPassMatch(boolean exists){
        this.exists = exists;
    }

    public boolean getexists() {
        return exists;
    }

    public String getPerson() {
        return person;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
