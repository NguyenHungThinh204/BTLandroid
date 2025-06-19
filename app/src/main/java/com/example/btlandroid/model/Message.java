package com.example.btlandroid.model;

public class Message {
    public String senderId;
    public String text;
    public long timestamp;

    public Message() {} // Required by Firebase

    public Message(String senderId, String text, long timestamp) {
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }
}
