package Presentation.Controller;

import Logic.MessageLogic;
import Logic.UserLogic;
import Presentation.Model.Message;
import Presentation.Model.User;

import java.util.List;

public class MessageController {

    private MessageLogic messageLogic;
    public MessageController(MessageLogic messageLogic) {
        this.messageLogic = messageLogic;
    }


    public List<Message> filterMessages(String text) {
        return messageLogic.filterMessages(text);
    }

    public List<Message> listMessages() {
        return messageLogic.listMessages();
    }

    public boolean addMessage(String text, int conversation_id) {
        return messageLogic.addMessage(text , conversation_id);
    }

    public boolean updateMessage(int id ,String text, int conversation_id) {
        return messageLogic.updateMessage(id,text ,conversation_id);
    }

    public boolean deleteMessage(int id) {
        return messageLogic.deleteMessage(id);
    }
}
