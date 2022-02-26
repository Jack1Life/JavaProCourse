package ua.kiev.prog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class JsonUsers {
    private final List<User> list = new ArrayList<>();

    public JsonUsers(List<User> sourceList, boolean online) {
        int offlineTimeMs = 10000;
        long curTime = (new Date()).getTime();
        for(User us : sourceList) {
            us.setOnline(((curTime - us.getLastPing()) < offlineTimeMs));
            if(!online || us.isOnline()){
                list.add(us);
            }
        }
    }

    public JsonUsers(List<User> sourceList, String login) {
        for(User us : sourceList) {
            if(us.getLogin().equals(login)){
                list.add(us);
            }
        }
    }

    public List<User> getList() {
        return Collections.unmodifiableList(list);
    }
}
