package Logic;

import Data.UserData;
import Presentation.Model.User;

import java.util.List;

public class UserLogic {
    UserData userData;

    public UserLogic(UserData userData) {
        this.userData = userData;
    }

    public int authenticate(String clientUsername, String clientPassword) {
        return userData.authenticate(clientUsername, clientPassword);
    }

    public int registerUser(String username , String password){
        return userData.addUser(username , password);
    }

    public List<User> filterUsers(String text) {
        return userData.filterUsers(text);
    }

    public int getUserId(String contactUsername) {
        return userData.getUserId(contactUsername);
    }
    public String getUserUsername(int contactId) {
        return userData.getUserUsername(contactId);
    }
    public List<Presentation.Model.User> listUsers() {
        return userData.listUsers();
    }

    public boolean updateUser(int id ,String username , String password){
        return userData.updateUser(id,username , password);
    }

    public boolean deleteUser(int id){
        return userData.deleteUser(id);
    }

}
