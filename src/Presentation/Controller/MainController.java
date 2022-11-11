package Presentation.Controller;

import Logic.Logic;
import Presentation.Model.User;

import java.util.List;

public class MainController {

    private Logic logic;
    public List<User> getClientList() {
        return null;
    }

    public MainController(Logic logic){
        this.logic = logic;
    }

    public void openChatWindow() {

    }

    public void setError(String errorMessage) {

    }

    public boolean authenticate(String clientUsername, String clientPassword) {
        return true;
    }

    public boolean registerUser(String username, String password) {
      return logic.registerUser(username , password);
    }
}
