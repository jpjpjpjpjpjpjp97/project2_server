package Logic;

import Data.UserData;
import Presentation.Controller.MainController;
import Data.ConnectionServer;
import Presentation.Controller.UserController;
import Presentation.Model.Server;

import java.sql.Connection;
import java.util.List;

public class Logic {

    private UserData userData;
    private UserLogic userLogic;
    private Connection connection;
    private MainController mainController;
    private UserController userController;

    public Logic() {
    }

    public void start(){
        this.connection = new ConnectionServer().connectionDataBase();
        this.userData = new UserData(connection);
        this.userLogic = new UserLogic(userData);
        this.userController = new UserController(this.userLogic);
        this.mainController = new MainController(this, userController);
        Server server = new Server(mainController, userController);
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
