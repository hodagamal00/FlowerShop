package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;

import java.util.List;

public class PassAllComplaintsEvent { // added new 21/7

    List<Complaint> complaintsToPass ;

    public PassAllComplaintsEvent(){

    }

    public void setComplaintsToPass(List<Complaint> recComps){
        complaintsToPass = recComps;

    }

    public List<Complaint> getComplaintsToPass(){
        return complaintsToPass;
    }

}
