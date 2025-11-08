package il.cshaifasweng.OCSFMediatorExample.client;

import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class passAllMessagesEvent {


    List<Message> messagesToPass ;

    public passAllMessagesEvent(){

    }

    public void setMessagesToPasssToPass(List<Message> recMessage){
        messagesToPass = recMessage;

    }

    public List<Message> getMessagesToPassToPass(){
        return messagesToPass;
    }

}