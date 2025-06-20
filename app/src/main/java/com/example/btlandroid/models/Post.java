package com.example.btlandroid.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post implements java.io.Serializable {
    private String id;
    private String userId;
    private String userName;
    private String userAvtURL;
    private String title;
    private String description;
    private String subject; // môn học, kỹ năng, chuyên môn ...
    @ServerTimestamp
    private Date createdAt;
    private long budget;
    private String supportType; // online, offline, other
    private String postType; // need hoặc offer

    public Post() {
        // Empty constructor for Firebase
    }

    public Post(long budget, Date createdAt, String description, String id, String postType, String subject, String supportType, String title, String userAvtURL, String userId, String userName) {
        this.budget = budget;
        this.createdAt = createdAt;
        this.description = description;
        this.id = id;
        this.postType = postType;
        this.subject = subject;
        this.supportType = supportType;
        this.title = title;
        this.userAvtURL = userAvtURL;
        this.userId = userId;
        this.userName = userName;
    }
    // Getters and Setters

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSupportType() {
        return supportType;
    }

    public void setSupportType(String supportType) {
        this.supportType = supportType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserAvtURL() {
        return userAvtURL;
    }

    public void setUserAvtURL(String userAvtURL) {
        this.userAvtURL = userAvtURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}