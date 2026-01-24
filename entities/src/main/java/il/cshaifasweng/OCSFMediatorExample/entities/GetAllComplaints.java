package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class GetAllComplaints implements Serializable { // added new 21/7

    List<Complaint> complaintsList;
    private String scope;

    public GetAllComplaints(){

    }

    public void setComplaintsList(List<Complaint> compList){
        complaintsList = compList;
    }

    public List<Complaint> getComplaintsList(){
        return complaintsList;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
