package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class LogOut implements Serializable {

    private String mail;
    public LogOut(){

    }
    public void setMail(String ml){
        this.mail = ml;
    }
    public String getMail(){
        return this.mail;
    }
}
