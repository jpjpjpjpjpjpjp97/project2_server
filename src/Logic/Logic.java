package Logic;

import Data.Data;
import Presentation.Controller.MainController;
import Presentation.Model.ClientThread;
import Presentation.Model.ConnectionServer;
import Presentation.Model.Server;

import java.net.Socket;
import java.sql.Connection;

public class Logic {

    private Data data;
    private Connection connection;

    public Logic() {
    }

    public void start(){
        MainController mainController = new MainController(this);
        Server server = new Server(mainController);
        connection = new ConnectionServer().connectionDataBase();
        this.data = new Data(mainController, connection);
        server.start();
    }

    public boolean registerUser(String username , String password){
        return data.addUser(username , password);
    }


}
