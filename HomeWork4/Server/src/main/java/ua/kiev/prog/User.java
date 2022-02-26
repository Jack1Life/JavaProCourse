package ua.kiev.prog;

import java.util.Date;

public class User {
    private String login;
    private String status;
    private long lastPing;
    private boolean online;

    public User(String login) {
        this.login = login;
        this.lastPing = (new Date()).getTime();
        this.status = "";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public long getLastPing() {
        return lastPing;
    }

    public void setLastPing(long lastPing) {
        this.lastPing = lastPing;
    }

    public synchronized void updatePing() {
        this.lastPing = (new Date()).getTime();
    }
}
