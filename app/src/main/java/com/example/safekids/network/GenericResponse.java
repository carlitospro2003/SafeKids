package com.example.safekids.network;

import com.google.gson.annotations.SerializedName;

public class GenericResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private String data; // Aquí se guardará el email que devuelve la API

    @SerializedName("timestamp")
    private String timestamp;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
