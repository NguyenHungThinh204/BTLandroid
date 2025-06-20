package com.example.btlandroid.model;

public class User {
    public String id;
    public String name;

    public User() {}  // Required by Firebase

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
