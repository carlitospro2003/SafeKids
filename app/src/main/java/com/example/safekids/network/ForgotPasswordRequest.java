package com.example.safekids.network;

import com.google.gson.annotations.SerializedName;


public class ForgotPasswordRequest {
    @SerializedName("email")
    private String email;

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }
}
