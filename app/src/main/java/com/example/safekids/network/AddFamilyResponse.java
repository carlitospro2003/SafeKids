package com.example.safekids.network;

public class AddFamilyResponse {
    private boolean success;
    private String message;
    private Data data;
    private String timestamp;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Data getData() { return data; }
    public String getTimestamp() { return timestamp; }

    public static class Data {
        private int id;
        private String firstName;
        private String lastName;
        private String phone;
        private String relationship;
        private String photo;
        private boolean status;

        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
        public String getRelationship() { return relationship; }
        public String getPhoto() { return photo; }
        public boolean isStatus() { return status; }
    }
}
