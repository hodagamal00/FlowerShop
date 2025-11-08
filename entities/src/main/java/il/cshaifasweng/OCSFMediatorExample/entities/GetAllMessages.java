package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class GetAllMessages implements Serializable { // added 16.8

    List<Message> messageList;

    public GetAllMessages(){

    }

    public void setMessageList(List<Message> msgList){
        messageList = msgList;
    }

    public List<Message> getMessageList(){
        return messageList;
    }
}