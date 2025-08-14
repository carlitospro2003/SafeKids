package com.example.safekids.network;
import com.example.safekids.models.Tutor;

import java.util.List;
public class GuardiansResponse {
    private boolean success;
    private String message;
    private List<Tutor> data;
    private String timestamp;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Tutor> getData() {
        return data;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
