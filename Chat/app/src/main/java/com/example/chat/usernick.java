package com.example.chat;

public class usernick {
    private String uid;
    private String nick;

    public usernick(){

    }
    public usernick(String uid, String nick) {
        this.uid = uid;
        this.nick = nick;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
