package Presentation.Model;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ConnectionServer {
    Connection connection;
    //logger object for saving logs
    private static final Logger logger = LogManager.getLogger(ConnectionServer.class);
    public Connection connectionDataBase(){

        String password = "Leosanpo16";
        String usuario = "root";
        String url = "jdbc:mysql://localhost:3306/project2";

        try {
            connection = DriverManager.getConnection(url, "root", password);
            if(connection != null){
                System.out.println("You have successfully connected to the database");
            }
        }
        catch (SQLException e){
            System.out.println("Failed to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }


}
