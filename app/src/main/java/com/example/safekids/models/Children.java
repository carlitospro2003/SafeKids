package com.example.safekids.models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Children implements Serializable {

    private int id;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("birthDate")
    private String birthDate;

    @SerializedName("photo")
    private String photo;

    @SerializedName("status")
    private boolean status;

    @SerializedName("created_at")
    private String createdAt;

    // Getters para Retrofit
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getBirthDate() { return birthDate; }
    public String getPhoto() { return photo; }
    public boolean isStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
