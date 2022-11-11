package Logic;

import Data.Data;
import Presentation.Controller.MainController;
import Presentation.Model.ClientThread;
import Presentation.Model.ConnectionServer;
import Presentation.Model.Server;

import java.net.Socket;

public class Logic {

    private Data data;

    public boolean registerUser(String username , String password){
        return data.addUser(username , password);
    }


}
