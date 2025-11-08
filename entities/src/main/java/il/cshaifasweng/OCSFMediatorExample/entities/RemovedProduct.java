package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;


public class RemovedProduct implements Serializable {

    private String id;
    private String message;
    public RemovedProduct(String recievedmsg,String ID){
        this.message=recievedmsg;
        this.id=ID;
    }
    public RemovedProduct(){

    }
    public void setmsg(String new_msg){
        this.message = new_msg;
    }
    public String getmsg(){
        return this.message;
    }
    public void setID(String new_id){
        this.id = new_id;
    }
    public String getid(){
        return this.id;
    }


}

