package Data;

import Presentation.Controller.MainController;
import Presentation.Model.ClientThread;
import Presentation.Model.ConnectionServer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;


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

    public boolean addUser(String name , String password){

            //sql statement for inserting record
            String sql = "INSERT INTO user (username, password , online) VALUES (?, ? , ?)";

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

    public int updateUser(int id ,String name , String password){

        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate("UPDATE user SET " + "username='" + name + "',password='" + password +"' WHERE id=" + id + ";");
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteUser(int id){

        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate("DELETE FROM user WHERE id=" + id + ";");
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    }



