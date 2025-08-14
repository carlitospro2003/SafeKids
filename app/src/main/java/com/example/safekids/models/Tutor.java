package com.example.safekids.models;
import com.google.gson.annotations.SerializedName;

public class Tutor {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String photo;
    private boolean status;
    @SerializedName("school_id")
    private int schoolId;
    @SerializedName("img_route")
    private String imgRoute;

    public Tutor(int id, String firstName, String lastName, String phone, String email, String photo, boolean status, int schoolId, String imgRoute) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
        this.status = status;
        this.schoolId = schoolId;
        this.imgRoute = imgRoute;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public boolean isStatus() {
        return status;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public String getImgRoute() {
        return imgRoute;
    }

    // Método auxiliar para obtener el nombre completo
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
