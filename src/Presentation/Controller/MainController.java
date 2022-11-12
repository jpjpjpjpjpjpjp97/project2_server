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


    public List<User> filterUsers(String text) {
        return logic.filterUsers(text);
    }

    public List<User> listUsers() {
        return logic.listUsers();
    }

    public boolean registerUser(String username, String password) {
      return logic.registerUser(username , password);
    }

    public boolean updateUser(int id ,String username, String password) {
        return logic.updateUser(id,username , password);
    }

    public boolean deleteUser(int id) {
        return logic.deleteUser(id);
    }
}
