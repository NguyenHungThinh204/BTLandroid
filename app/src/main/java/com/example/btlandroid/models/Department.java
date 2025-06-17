package com.example.btlandroid.models;

public class Department {
    private String id;
    private String name;
    private String desc;

    public Department() {
    }

    public Department(String desc, String id, String name) {
        this.desc = desc;
        this.id = id;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
