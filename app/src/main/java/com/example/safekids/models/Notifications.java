package com.example.safekids.models;

import com.google.gson.annotations.SerializedName;

public class Notifications {
    private int id;
    @SerializedName("message")
    private String description;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("studentId")
    private int studentId;
    @SerializedName("last_message")
    private String lastMessage;
    private String photo; // URL de la imagen, opcional

    // Constructor principal
    public Notifications(int id, String description, String createdAt, int studentId, String lastMessage) {
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
        this.studentId = studentId;
        this.lastMessage = lastMessage;
    }

    // Constructor con photo para compatibilidad
    public Notifications(int id, String description, String createdAt, int studentId, String lastMessage, String photo) {
        this(id, description, createdAt, studentId, lastMessage);
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

    public String getLastMessage() {
        return lastMessage;
    }

    public String getPhoto() {
        return photo;
    }
}