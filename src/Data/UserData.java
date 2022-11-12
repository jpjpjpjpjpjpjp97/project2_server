package Data;

import Presentation.Controller.MainController;
import Presentation.Model.ClientThread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class UserData {
    Connection connection;
    //logger object for saving logs
    private static final Logger logger = LogManager.getLogger(ConnectionServer.class);
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ArrayList<ClientThread> clientList;

    public UserData(Connection connection) {
        this.connection = connection;
    }

    public List<Presentation.Model.User> listUsers() {
        String sql = "SELECT id, username, online FROM user;";
        ArrayList<Presentation.Model.User> userList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet userResultSet = statement.executeQuery();
            while (userResultSet.next()) {
                int id = userResultSet.getInt("id");
                String username = userResultSet.getString("username");
                boolean online = userResultSet.getBoolean("online");
                userList.add(new Presentation.Model.User(id, username, online));
            }
        }catch (Exception e){
            logger.error("Exception in connection: "+ e.toString());
        }
        return userList;
    }

    public List<Presentation.Model.User> filterUsers(String text) {
        ArrayList<Presentation.Model.User> filteredUserList = new ArrayList<>();
        try {
            ResultSet userResultSet = connection.createStatement().executeQuery("SELECT id, username, online FROM user WHERE username LIKE '%" + text + "%';");
            while (userResultSet.next()) {
                int id = userResultSet.getInt("id");
                String username = userResultSet.getString("username");
                boolean online = userResultSet.getBoolean("online");
                filteredUserList.add(new Presentation.Model.User(id, username, online));
            }
        }catch (Exception e){
            logger.error("Exception in connection: "+ e.toString());
        }
        return filteredUserList;
    }

    public boolean addUser(String name , String password){
            //sql statement for inserting record
            String sql = "INSERT INTO user (username, password , online) VALUES (?, ? , ?);";
            //getting input from user
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                //setting parameter values
                statement.setString(1,name);
                statement.setString(2, password);
                statement.setBoolean(3, false);
                //executing query which will return an integer value
                int rowsInserted = statement.executeUpdate();
                return (rowsInserted!=0);
            }catch (Exception e){
                logger.error("Exception in connection: "+ e.toString());
            }
            return false;
        }

    public boolean updateUser(int id ,String name , String password){
        try (Statement stmt = connection.createStatement()) {
            return (stmt.executeUpdate("UPDATE user SET " + "username='" + name + "',password='" + password +"' WHERE id=" + id + ";") != 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int id){
        try (Statement stmt = connection.createStatement()) {
            return (stmt.executeUpdate("DELETE FROM user WHERE id=" + id + ";") != 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}



