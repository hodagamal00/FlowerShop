package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class GetAllComplaints implements Serializable { // added new 21/7

    List<Complaint> complaintsList;

    public GetAllComplaints(){

    }

    public void setComplaintsList(List<Complaint> compList){
        complaintsList = compList;
    }

    public List<Complaint> getComplaintsList(){
        return complaintsList;
    }

}
