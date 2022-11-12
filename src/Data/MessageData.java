package Data;

import Presentation.Model.ClientThread;
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
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet messageResultSet = statement.executeQuery();
            while (messageResultSet.next()) {
                int id = messageResultSet.getInt("id");
                String text = messageResultSet.getString("text");
                int conversation_id  = messageResultSet.getInt("conversation_id");
                Timestamp created = messageResultSet.getTimestamp("created");
                messageList.add(new Presentation.Model.Message(id, text, conversation_id,created ));
            }
        }catch (Exception e){
            logger.error("Exception in connection: "+ e.toString());
        }
        return messageList;
    }



    public List<Presentation.Model.Message> filterMessages(String text) {
        ArrayList<Presentation.Model.Message> filteredMessageList = new ArrayList<>();
        try {
            ResultSet messageResultSet = connection.createStatement().executeQuery("SELECT id, text, conversation_id, created FROM message WHERE text LIKE '%" + text + "%';");
            while (messageResultSet.next()) {

                int id = messageResultSet.getInt("id");
                String text2 = messageResultSet.getString("text");
                int conversation_id  = messageResultSet.getInt("conversation_id");
                Timestamp created = messageResultSet.getTimestamp("created");

                filteredMessageList.add(new Presentation.Model.Message(id, text2, conversation_id,created));
            }
        }catch (Exception e){
            logger.error("Exception in connection: "+ e.toString());
        }
        return filteredMessageList;
    }



    public boolean addMessage(String text , int conversation_id ){
        //sql statement for inserting record
        String sql = "INSERT INTO message (text, conversation_id) VALUES (?, ?);";
        //getting input from user
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            //setting parameter values
            statement.setString(1,text);
            statement.setInt(2,  conversation_id );

            //executing query which will return an integer value
            int rowsInserted = statement.executeUpdate();
            return (rowsInserted!=0);
        }catch (Exception e){
            logger.error("Exception in connection: "+ e.toString());
        }
        return false;
    }

    //NO SE DEBERÍA EDITAR UN MENSAJE
    public boolean updateMessage(int id ,String text , int conversation_id){
        try (Statement stmt = connection.createStatement()) {
            return (stmt.executeUpdate("UPDATE message SET " + "text='" + text + "', conversation_id='" +  conversation_id +"' WHERE id=" + id + ";") != 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMessage(int id){
        try (Statement stmt = connection.createStatement()) {
            return (stmt.executeUpdate("DELETE FROM message WHERE id=" + id + ";") != 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
