package com.example.safekids.models;

public class Tutor {
    private String name;
    private String phone;
    private String email;
    private int imageRes;

    public Tutor(String name, String phone, String email, int imageRes) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.imageRes = imageRes;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public int getImageRes() { return imageRes; }
}
