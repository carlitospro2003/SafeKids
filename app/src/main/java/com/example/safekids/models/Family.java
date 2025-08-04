package com.example.safekids.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
public class Family implements Serializable {

    private int id;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("relationship")
    private String relationship;

    @SerializedName("photo")
    private String photo;

    @SerializedName("status")
    private boolean status;

    @SerializedName("created_at")
    private String createdAt;

    public Family(String firstName, String lastName, String phone,
                  String relationship, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.relationship = relationship;
        this.photo = photo;
    }


    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getPhone() { return phone; }
    public String getRelationship() { return relationship; }
    public String getPhoto() { return photo; }
    public boolean isStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
