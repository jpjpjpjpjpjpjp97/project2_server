package Presentation.Model;

import java.util.ArrayList;
import java.util.List;

public class User extends AbstractUser{
    private boolean online;

    public User(int id, String username) {
        super(id, username);
        this.online = false;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
