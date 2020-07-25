package com.example.chat;

public class LastMessageItem {
    private String sender_nick;
    private String sender_uid;
    private String message;
    private String friend_uid;
    private String friend_nick;

    public LastMessageItem() {
    }

    public LastMessageItem(String sender_nick, String sender_uid, String message,String friend_uid,String friend_nick) {
        this.sender_nick = sender_nick;
        this.sender_uid = sender_uid;
        this.message = message;
        this.friend_uid=friend_uid;
        this.friend_nick=friend_nick;
    }

    public String getFriend_uid() {
        return friend_uid;
    }

    public void setFriend_uid(String friend_uid) {
        this.friend_uid = friend_uid;
    }

    public String getFriend_nick() {
        return friend_nick;
    }

    public void setFriend_nick(String friend_nick) {
        this.friend_nick = friend_nick;
    }

    public String getSender_nick() {
        return sender_nick;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public String getMessage() {
        return message;
    }

    public void setSender_nick(String sender_nick) {
        this.sender_nick = sender_nick;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
