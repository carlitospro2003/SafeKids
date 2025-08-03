package com.example.safekids.network;

import com.example.safekids.models.Family;
import java.util.List;
public class AuthorizedResponse {

    private boolean success;
    private String message;
    private List<Family> data;
    private String timestamp;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Family> getData() { return data; }
    public String getTimestamp() { return timestamp; }
}
