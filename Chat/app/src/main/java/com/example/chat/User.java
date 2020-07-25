package com.example.chat;

import java.util.ArrayList;

public class User {
    private String uid;
    private String nick;
    private ArrayList<String> interests;
    private String aboutme;

    public User() {}

    public User(String uid,String nick,ArrayList<String> interests,String aboutme){
        this.uid=uid;
        this.nick=nick;
        this.interests=interests;
        this.aboutme=aboutme;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getUid() {
        return uid;
    }

    public String getNick() {
        return nick;
    }

    public ArrayList<String> getInterests() {
        return this.interests;
    }
}
