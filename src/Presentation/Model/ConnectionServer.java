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
        String url = "jdbc:mysql://localhost:3306/project2?user=" + usuario + "&password=" + password +"&serverTimezone=UTC";

        try {
            connection = DriverManager.getConnection(url);
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


    public void registerUser(){

        //sql statement for inserting record
        String sql = "INSERT INTO usuario (idPersona, Nombre) VALUES (?, ?)";
        //getting input from user
        Scanner input=new Scanner(System.in);
        System.out.println("Enter ID");
        int id=Integer.parseInt(input.nextLine());
        System.out.println("Enter First Name");
        String lName=input.nextLine();


        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            //setting parameter values
            statement.setInt(1, id);
            statement.setString(2, lName);
            //executing query which will return an integer value
            int rowsInserted = statement.executeUpdate();
            //if rowInserted is greate r then 0 mean rows are inserted
            if (rowsInserted > 0) {
                logger.debug("A new user was inserted successfully!");
            }
        }catch (Exception e){
            logger.error("Exception in connection: "+ e.toString());

        }
    }
}
