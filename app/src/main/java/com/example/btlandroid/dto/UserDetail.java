package com.example.btlandroid.dto;

import com.example.btlandroid.models.Department;
import com.example.btlandroid.models.User;

import java.io.Serializable;

public class UserDetail implements Serializable {
    private String id;
    private String name;
    private String email;
    private String avtURL;
    private String phone;
    private float avgRating;
    private String bio; //Giới thiệu
    private boolean isTutorAvailable = false;
    private String position;
    private Department department;
    private String skill;
    private String subject;

    public UserDetail() {
    }

    public UserDetail(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.avtURL = user.getAvtURL();
        this.phone = user.getPhone();
        this.avgRating = user.getAvgRating();
        this.bio = user.getBio();
        this.isTutorAvailable = user.isTutorAvailable();
        this.department = new Department(user.getDepartmentId());
        this.skill = user.getSkill();
        this.position = user.getPosition();
        this.subject = user.getSubject();
    }

    public UserDetail(float avgRating, String avtURL, String bio, Department department, String email, String id, boolean isTutorAvailable, String name, String phone, String position, String skill, String subject) {
        this.avgRating = avgRating;
        this.avtURL = avtURL;
        this.bio = bio;
        this.department = department;
        this.email = email;
        this.id = id;
        this.isTutorAvailable = isTutorAvailable;
        this.name = name;
        this.phone = phone;
        this.position = position;
        this.skill = skill;
        this.subject = subject;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}