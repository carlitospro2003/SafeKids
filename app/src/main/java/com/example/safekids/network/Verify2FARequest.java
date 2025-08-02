package com.example.safekids.network;

public class Verify2FARequest {
    private String temporaryToken;
    private String code;

    public Verify2FARequest(String temporaryToken, String code) {
        this.temporaryToken = temporaryToken;
        this.code = code;
    }
}
