package com.example.btlandroid.models;

import java.util.List;

public class Post implements java.io.Serializable {
    private String id; // id củ bảng post
    private String userId; // id của user lưu trong bảng post trong db
    private String name; // nối bảng user với post rồi lấy trường name của user
    private String avtURL; // nối bảng user với post rồi lấy trường name của user
    private String title; // title bài post
    private String description; // mô tả chi tiết bài viết
    private String subject; // môn học, kỹ năng, chuyên môn ...
    private String time; // là create at, không để người dùng nhập mà code tự động tạo để hiển thị
    private String fee; // budget của người cần giúp đỡ hoặc mức mong muốn của người offer
    private String supportType; // online, offline, zoom, meet ....
    private String postType; // need hoặc offer để phân biệt người cần giúp và người giúp đỡ

    public Post() {
        // Empty constructor for Firebase
    }

    public Post(String id, String userId, String name, String avtURL, String title, String description, String subject,
                String time, String fee, String supportType, String postType) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.avtURL = avtURL;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.time = time;
        this.fee = fee;
        this.supportType = supportType;
        this.postType = postType;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvtURL() {
        return avtURL;
    }

    public void setAvtURL(String avtUrl) {
        this.avtURL = avtUrl;
    }

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