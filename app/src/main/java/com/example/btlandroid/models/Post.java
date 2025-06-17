package com.example.btlandroid.models;

import java.util.List;

public class Post {
    private String userId;
    private String name;
    private String title;
    private String description;
    private String subject;
    private String time;
    private String fee;
    private String supportType;
    private String postType;

    public Post() {
        // Empty constructor for Firebase
    }

    public Post(String userId, String name, String title, String description, String subject,
                String time, String fee, String supportType, String postType) {
        this.userId = userId;
        this.name = name;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.time = time;
        this.fee = fee;
        this.supportType = supportType;
        this.postType = postType;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSupportType() {
        return supportType;
    }

    public void setSupportType(String supportType) {
        this.supportType = supportType;
    }
}