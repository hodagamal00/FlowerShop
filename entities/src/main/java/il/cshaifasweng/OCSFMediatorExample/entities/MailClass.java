package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;


public class MailClass implements Serializable {
    private String mail ;


    public MailClass(String ml){
        mail = ml;

    }


    public String getMail(){
        return mail;
    }
    public void setMail(String ml){
        mail = ml;
    }
}
