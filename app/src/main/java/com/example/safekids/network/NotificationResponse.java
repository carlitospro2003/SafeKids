package com.example.safekids.network;

import com.example.safekids.models.Notifications;

import java.util.List;
public class NotificationResponse {
    private boolean success;
    private String message;
    private List<Notifications> data;
    private int count;
    private String timestamp;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Notifications> getData() {
        return data;
    }

    public int getCount() {
        return count;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
