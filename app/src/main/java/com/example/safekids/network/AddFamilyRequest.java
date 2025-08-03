package com.example.safekids.network;

public class AddFamilyRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String relationship;
    private String photo;

    public AddFamilyRequest(String firstName, String lastName, String phone, String relationship, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.relationship = relationship;
        this.photo = photo;
    }

}
