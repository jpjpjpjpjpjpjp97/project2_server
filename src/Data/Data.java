package Data;

import Presentation.Controller.MainController;
import Presentation.Model.ClientThread;
import Presentation.Model.ConnectionServer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Scanner;



public class Data {


    Connection connection;

    //logger object for saving logs
    private static final Logger logger = LogManager.getLogger(ConnectionServer.class);
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ArrayList<ClientThread> clientList;

    private MainController mainController;


    public Data(MainController mainController) {
        this.mainController = mainController;

    }

    public boolean addUser(String name , String password){

            //sql statement for inserting record
            String sql = "INSERT INTO user (username, password , online) VALUES (?, ? , ?)";


            //getting input from user

            try {
                PreparedStatement statement = connection.prepareStatement(sql);

                //setting parameter values
                statement.setString(2,name);
                statement.setString(3, password);
                statement.setBoolean(4, false);
                //executing query which will return an integer value
                int rowsInserted = statement.executeUpdate();

                return (rowsInserted!=0);

            }catch (Exception e){
                logger.error("Exception in connection: "+ e.toString());
            }
            return false;
        }

    }


