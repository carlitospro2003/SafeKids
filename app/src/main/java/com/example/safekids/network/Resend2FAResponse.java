package com.example.safekids.network;

public class Resend2FAResponse {

    private boolean success;
    private String message;
    private GuardianData data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public GuardianData getData() { return data; }
}
