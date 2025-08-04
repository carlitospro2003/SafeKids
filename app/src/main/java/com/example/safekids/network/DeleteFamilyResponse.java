package com.example.safekids.network;

import com.google.gson.annotations.SerializedName;

public class DeleteFamilyResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
