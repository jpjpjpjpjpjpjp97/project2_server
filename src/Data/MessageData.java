package Data;

import Presentation.Model.ClientThread;
import Presentation.Model.Message;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageData {
    Connection connection;
    //logger object for saving logs
    private static final Logger logger = LogManager.getLogger(ConnectionServer.class);
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ArrayList<ClientThread> clientList;

    public MessageData(Connection connection) {
        this.connection = connection;
    }

    public List<Presentation.Model.Message> listMessages() {
        String sql = "SELECT id, text, conversation_id, created FROM message;";
        ArrayList<Presentation.Model.Message> messageList = new ArrayList<>();
//        try {
//            PreparedStatement statement = connection.prepareStatement(sql);
//            ResultSet messageResultSet = statement.executeQuery();
//            while (messageResultSet.next()) {
//                int id = messageResultSet.getInt("id");
//                String text = messageResultSet.getString("text");
//                int conversation_id  = messageResultSet.getInt("conversation_id");
//                Timestamp created = messageResultSet.getTimestamp("created");
//                messageList.add(new Presentation.Model.Message(id, text, conversation_id,created ));
//            }
//        }catch (Exception e){
//            logger.error("Exception in connection: "+ e.toString());
//        }
        return messageList;
    }

    public List<Presentation.Model.Message> filterMessages(String text) {
        ArrayList<Presentation.Model.Message> filteredMessageList = new ArrayList<>();
//        try {
//            ResultSet messageResultSet = connection.createStatement().executeQuery("SELECT id, text, conversation_id, created FROM message WHERE text LIKE '%" + text + "%';");
//            while (messageResultSet.next()) {
//
//                int id = messageResultSet.getInt("id");
//                String text2 = messageResultSet.getString("text");
//                int conversation_id  = messageResultSet.getInt("conversation_id");
//                Timestamp created = messageResultSet.getTimestamp("created");
//
//                filteredMessageList.add(new Presentation.Model.Message(id, text2, conversation_id,created));
//            }
//        }catch (Exception e){
//            logger.error("Exception in connection: "+ e.toString());
//        }
        return filteredMessageList;
    }

    public boolean addMessage(Message message) {
        try {
            ResultSet messageResultSet = connection.createStatement().executeQuery("SELECT id, sender_id, receiver_id FROM conversation WHERE sender_id=" + message.getSenderId() + " AND receiver_id=" + message.getReceiverId() + ";");
            if (!messageResultSet.next()) {

                PreparedStatement statement = connection.prepareStatement("INSERT INTO conversation (sender_id, receiver_id) VALUES (?,?);");
                statement.setInt(1, message.getSenderId());
                statement.setInt(2, message.getReceiverId());
                statement.executeUpdate();

                messageResultSet = connection.createStatement().executeQuery("SELECT id, sender_id, receiver_id FROM conversation WHERE sender_id=" + message.getSenderId() + " AND receiver_id=" + message.getReceiverId() + ";");
                messageResultSet.next();
            }

            int conversationId = messageResultSet.getInt("id");



            String sql = "INSERT INTO message (text, conversation_id) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, message.getText());
            statement.setInt(2, conversationId);
            int rowsInserted = statement.executeUpdate();
            return (rowsInserted != 0);
        } catch (Exception e) {
            logger.error("Exception in connection: " + e.toString());
        }
        return false;
    }

    public boolean updateMessage(int id, String text, int conversation_id) {
        try (Statement stmt = connection.createStatement()) {
            return (stmt.executeUpdate("UPDATE message SET text='" + text + "', conversation_id='" + conversation_id + "' WHERE id=" + id + ";") != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMessage(int id) {
        try (Statement stmt = connection.createStatement()) {
            return (stmt.executeUpdate("DELETE FROM message WHERE id=" + id + ";") != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Message> getNewMessages(int receiverId) {
        ArrayList<Message> messageList = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet messageResultSet = stmt.executeQuery("SELECT message.id, message.text, message.conversation_id, message.created, conversation.sender_id FROM message INNER JOIN conversation ON conversation_id = conversation.id WHERE conversation.receiver_id = " + receiverId + ";");
            while (messageResultSet.next()) {
                int id = messageResultSet.getInt("message.id");
                String text = messageResultSet.getString("message.text");
                int senderId = messageResultSet.getInt("conversation.sender_id");
                Timestamp created = messageResultSet.getTimestamp("created");
                messageList.add(new Presentation.Model.Message("individual", text, senderId, receiverId, false));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return messageList;
    }

    public List<Message> getPendingMessages(int receiverId) {
        ArrayList<Message> messageList = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet messageResultSet = stmt.executeQuery("SELECT message.id, message.text, message.conversation_id, message.created, conversation.sender_id FROM message INNER JOIN conversation ON conversation_id = conversation.id WHERE conversation.receiver_id = " + receiverId + ";");
            while (messageResultSet.next()) {
                int id = messageResultSet.getInt("message.id");
                String text = messageResultSet.getString("message.text");
                int senderId = messageResultSet.getInt("conversation.sender_id");
                Timestamp created = messageResultSet.getTimestamp("created");
                messageList.add(new Presentation.Model.Message("individual",id ,text, senderId, receiverId,created ,false));


            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return messageList;
    }
}