package com.example.safekids.network;

import com.google.gson.annotations.SerializedName;

public class VerifyCodeResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("resetToken")
    private String resetToken;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getResetToken() { return resetToken; }
}
