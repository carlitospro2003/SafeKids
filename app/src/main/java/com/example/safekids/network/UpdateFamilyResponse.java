package com.example.safekids.network;

import com.example.safekids.models.Family;

public class UpdateFamilyResponse {
    private boolean success;
    private String message;
    private Family data;
    private String timestamp;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Family getData() { return data; }
    public String getTimestamp() { return timestamp; }
}
