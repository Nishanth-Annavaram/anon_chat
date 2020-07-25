package com.example.chat;

public class Message {

    String message;
    String sender_uid;

    public Message(){};

    public Message(String message,String sender_uid) {
        this.message = message;
        this.sender_uid=sender_uid;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
