package View;

import Data.Data;
import Logic.Logic;
import Presentation.Controller.MainController;
import Presentation.Model.ConnectionServer;
import Presentation.Model.Server;


public class MainView {

    public static void main(String[] args) {

        Logic logic = new Logic();
        MainController mainController = new MainController(logic);
        Server server = new Server(mainController);
        Data data = new Data(mainController);
        ConnectionServer connectionServer = new ConnectionServer();

        connectionServer.connectionDataBase();
        server.start();
        //data.registerUser();

    }


}
