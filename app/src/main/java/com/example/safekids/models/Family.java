package com.example.safekids.models;

public class Family {
    private String name;
    private String phone;
    private String relationship;
    private int imageRes;

    public Family(String name, String phone, String relationship, int imageRes) {
        this.name = name;
        this.phone = phone;
        this.relationship = relationship;
        this.imageRes = imageRes;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getRelationship() { return relationship; }
    public int getImageRes() { return imageRes; }
}
