package Presentation.Controller;

import Logic.Logic;
import Presentation.Model.User;

import java.util.List;

public class MainController {

    private Logic logic;
    private UserController userController;

    private MessageController messageController;
    public List<User> getClientList() {
        return null;
    }

    public MainController(Logic logic, UserController userController ){
        this.logic = logic;
        this.userController = userController;
    }

    public MainController(Logic logic, MessageController messageController ){
        this.logic = logic;
        this.messageController = messageController;
    }

    public MainController(Logic logic){
        this.logic = logic;
    }

    public void openChatWindow() {

    }

    public void setError(String errorMessage) {

    }

    public UserController getUserController() {
        return this.userController;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}
