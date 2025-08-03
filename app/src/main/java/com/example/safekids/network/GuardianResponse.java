package com.example.safekids.network;

import com.example.safekids.models.Children;

import java.util.List;

public class GuardianResponse {
    private boolean success;
    private String message;
    private Guardian data;
    private String temporaryToken; // solo viene en login
    private String token; // solo viene en 2FA
    private List<Children> students;
    private String timestamp;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Guardian getData() { return data; }
    public String getTemporaryToken() { return temporaryToken; }
    public String getToken() { return token; }

    public List<Children> getStudents() { return students; }
    public String getTimestamp() { return timestamp; }

    public static class Guardian {
        private int id;
        private String firstName;
        private String lastName;
        private String phone;
        private String email;
        private String photo;
        private Boolean status;



        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getPhoto() { return photo; }
        public Boolean getStatus() { return status; }


    }

}
