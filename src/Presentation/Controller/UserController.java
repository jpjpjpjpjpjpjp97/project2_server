package Presentation.Controller;

import Logic.UserLogic;
import Presentation.Model.User;

import java.util.List;

public class UserController {
    private UserLogic userLogic;
    public UserController(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    public boolean authenticate(String clientUsername, String clientPassword) {
        return true;
    }


    public List<User> filterUsers(String text) {
        return userLogic.filterUsers(text);
    }

    public List<User> listUsers() {
        return userLogic.listUsers();
    }

    public boolean registerUser(String username, String password) {
        return userLogic.registerUser(username , password);
    }

    public boolean updateUser(int id ,String username, String password) {
        return userLogic.updateUser(id,username , password);
    }

    public boolean deleteUser(int id) {
        return userLogic.deleteUser(id);
    }
}
