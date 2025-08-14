package com.example.safekids.models;

import com.google.gson.annotations.SerializedName;

public class Notifications {
    private int id;
    private String description;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("studentId")
    private int studentId;
    private String photo; // URL de la imagen, si la API la proporciona

    public Notifications(int id, String description, String createdAt, int studentId, String photo) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
        this.studentId = studentId;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getPhoto() {
        return photo;
    }
}
