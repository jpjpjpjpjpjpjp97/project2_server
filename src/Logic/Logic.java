package Logic;

import Presentation.Controller.MainController;
import Presentation.Model.ClientThread;
import Presentation.Model.Server;

import java.net.Socket;

public class Logic {
    public static void main(String[] args) {
        MainController mainController = new MainController();
        Server server = new Server(mainController);
        server.start();
    }
}
