package com.example.btlandroid.models;

public class User {
    private String id;
    private String name;
    private String email;
    private String avtURL;
    private String phone;
    private float avgRating;
    private String bio; //Giới thiệu
    private boolean isTutorAvailable = false;
    private String position;
    private String departmentId;
    private String skill;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(float avgRating, String avtURL, String bio, String departmentId, String email, String id, boolean isTutorAvailable, String name, String phone, String position, String skill) {
        this.avgRating = avgRating;
        this.avtURL = avtURL;
        this.bio = bio;
        this.departmentId = departmentId;
        this.email = email;
        this.id = id;
        this.isTutorAvailable = isTutorAvailable;
        this.name = name;
        this.phone = phone;
        this.position = position;
        this.skill = skill;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public String getAvtURL() {
        return avtURL;
    }

    public void setAvtURL(String avtURL) {
        this.avtURL = avtURL;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTutorAvailable() {
        return isTutorAvailable;
    }

    public void setTutorAvailable(boolean tutorAvailable) {
        isTutorAvailable = tutorAvailable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}
