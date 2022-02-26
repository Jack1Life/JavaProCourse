package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class UsersList {
    private static final UsersList usrList = new UsersList();

    private final Gson gson;
    private final ArrayList<User> list = new ArrayList<User>();

    public static UsersList getInstance() {

        return usrList;
    }

    private UsersList() {
        gson = new GsonBuilder().create();
    }

    public synchronized void add(User user) {
        list.add(user);
    }

    public void updateUser(String login) {
        for(User us : this.list) {
            if(us.getLogin().equals(login)) {
                us.updatePing();
                return;
            }
        }
        this.add(new User(login));
    }

    public void updateUser(String login, String status) {
        for(User us : this.list) {
            if(us.getLogin().equals(login)) {
                us.updatePing();
                us.setStatus(status);
                return;
            }
        }
        this.add(new User(login));
    }

    public synchronized String getAllJSON() {
        return gson.toJson(new JsonUsers(this.list, false));
    }

    public synchronized String getOnlineJSON() {
        return gson.toJson(new JsonUsers(this.list, true));
    }

    public synchronized String getUserJSON(String login) {
        return gson.toJson(new JsonUsers(this.list, login));
    }

}
