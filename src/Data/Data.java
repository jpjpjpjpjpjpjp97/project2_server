package Data;

import Presentation.Controller.MainController;
import Presentation.Model.ClientThread;
import Presentation.Model.ConnectionServer;
import Presentation.Model.User;
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


public class Data {
    Connection connection;
    //logger object for saving logs
    private static final Logger logger = LogManager.getLogger(ConnectionServer.class);
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ArrayList<ClientThread> clientList;
    private MainController mainController;

    public Data(MainController mainController, Connection connection) {
        this.mainController = mainController;
        this.connection = connection;
    }

    public List<User> listUsers() {
        String sql = "SELECT id, username, online FROM user;";
        try {
            ArrayList<User> userList = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet userResultSet = statement.executeQuery();
            while (userResultSet.next()) {
                int id = userResultSet.getInt("id");
                String username = userResultSet.getString("username");
                boolean online = userResultSet.getBoolean("online");
                userList.add(new User(id, username, online));
            }
            return userList;
        }catch (Exception e){
            logger.error("Exception in connection: "+ e.toString());
        }
        return null;
    }

    public List<User> filterUsers(String text) {
        try {
            ArrayList<User> filteredUserList = new ArrayList<>();
            ResultSet userResultSet = connection.createStatement().executeQuery("SELECT id, username, online FROM user WHERE username LIKE '%" + text + "%';");
            while (userResultSet.next()) {
                int id = userResultSet.getInt("id");
                String username = userResultSet.getString("username");
                boolean online = userResultSet.getBoolean("online");
                filteredUserList.add(new User(id, username, online));
            }
            return filteredUserList;
        }catch (Exception e){
            logger.error("Exception in connection: "+ e.toString());
        }
        return null;
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



