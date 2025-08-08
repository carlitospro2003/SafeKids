package com.example.safekids.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
public class School implements Serializable {

    private int id;
    private String name;
    private String address;
    private String phone;
    private String city;
    private boolean status;

    @SerializedName("created_at")
    private String createdAt;

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getCity() { return city; }
    public boolean isStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
