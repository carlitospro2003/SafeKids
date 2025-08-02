package com.example.safekids.models;

public class Notifications {
    private String description;
    private int imageResId;

    public Notifications(String description, int imageResId) {
        this.description = description;
        this.imageResId = imageResId;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }
}
