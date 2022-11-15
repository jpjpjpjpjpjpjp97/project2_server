package Logic;

import Data.MessageData;
import Presentation.Model.Message;
import Presentation.Model.User;

import java.util.List;

public class MessageLogic {

    MessageData messageData;

    public MessageLogic(MessageData messageData) {
        this.messageData = messageData;
    }

    public boolean addMessage(Message message){
        return messageData.addMessage(message);
    }

    public List<Message> filterMessages(String text) {
        return messageData.filterMessages(text);
    }

    public List<Presentation.Model.Message> listMessages() {
        return messageData.listMessages();
    }

    public List<Message> getNewMessages(int receiverId) {
        return messageData.getNewMessages(receiverId);
    }

    public List<Message> getPendingMessages(int id) {
        return messageData.getPendingMessages(id);
    }

    public boolean updateMessage(int id ,String text , int conversation_id){
        return messageData.updateMessage(id,text,conversation_id);
    }

    public boolean deleteMessage(int id){
        return messageData.deleteMessage(id);
    }

}
