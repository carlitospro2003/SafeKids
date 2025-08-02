package com.example.safekids.network;

public class ChangePasswordRequest {
    private String password;
    private String password_confirmation;

    public ChangePasswordRequest(String password, String password_confirmation) {
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    // Getters (si los necesitas)
    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return password_confirmation;
    }
}
