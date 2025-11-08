package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class CheckMail implements Serializable {
    private String email;
    private boolean exists;
    private String person;
    private String password ;
    public CheckMail(String email,String person,String pass) {
        this.email = email;
        this.person = person;
        this.password = pass ;
    }

    // SETTERS
    public void setPassword(String password) {
        this.password = password;
    }

    // GETTERS
    public String getEmail() {
        return email;
    }
    public boolean getExists(){
        return exists;
    }
    public String getPerson(){ return this.person; }
    public String getPassword(){return this.password; }


    public CheckMail(boolean exists){
        this.exists = exists;
    }
}
