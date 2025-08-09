package com.example.safekids.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
public class School implements Serializable {

    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("city")
    private String city;

    @SerializedName("status")
    private boolean status;

    @SerializedName("created_at")
    private String createdAt;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getCity() { return city; }
    public boolean isStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
