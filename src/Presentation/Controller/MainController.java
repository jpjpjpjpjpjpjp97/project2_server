package Presentation.Controller;

import Logic.Logic;
import Presentation.Model.User;

import java.util.List;

public class MainController {

    private Logic logic;
    private UserController userController;
    public List<User> getClientList() {
        return null;
    }

    public MainController(Logic logic, UserController userController){
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
