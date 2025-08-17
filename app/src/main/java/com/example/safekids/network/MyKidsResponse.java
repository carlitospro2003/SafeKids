package com.example.safekids.network;
import com.example.safekids.models.Children;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class MyKidsResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Children> data;

    @SerializedName("token_id")
    private String tokenId;

    @SerializedName("timestamp")
    private String timestamp;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Children> getData() { return data; }
    public String getTokenId() { return tokenId; }
    public String getTimestamp() { return timestamp; }
}
