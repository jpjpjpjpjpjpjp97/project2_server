package Logic;

import Data.MessageData;
import Data.UserData;
import Presentation.Controller.MainController;
import Data.ConnectionServer;
import Presentation.Controller.MessageController;
import Presentation.Controller.UserController;
import Presentation.Model.Server;

import java.sql.Connection;
import java.util.List;

public class Logic {

    private UserData userData;
    private UserLogic userLogic;

    private MessageData messageData;

    private MessageLogic messageLogic;
    private Connection connection;
    private MainController mainController;
    private UserController userController;

    private MessageController messageController;


    public Logic() {
    }

    public void start(){
        this.connection = new ConnectionServer().connectionDataBase();
        this.userData = new UserData(connection);
        this.userLogic = new UserLogic(userData);

        this.messageData = new MessageData(connection);
        this.messageLogic = new MessageLogic(messageData);

        this.userController = new UserController(this.userLogic);
        this.messageController = new MessageController(this.messageLogic);

        this.mainController = new MainController(this, userController);
        this.mainController = new MainController(this, messageController);

        Server server = new Server(mainController, userController , messageController);
        server.start();
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserLogic getUserLogic() {
        return userLogic;
    }

    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }
}
