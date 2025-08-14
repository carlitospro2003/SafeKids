package com.example.safekids.network;

import com.google.gson.annotations.SerializedName;


public class RefreshTokenResponse {
    private boolean success;
    private String message;
    private Data data;
    private String timestamp;
    @SerializedName("error_code")
    private String errorCode;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static class Data {
        private String token;
        private String timestamp;
        private User user;

        public String getToken() {
            return token;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public User getUser() {
            return user;
        }
    }

    public static class User {
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String type;

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getType() {
            return type;
        }
    }
}
