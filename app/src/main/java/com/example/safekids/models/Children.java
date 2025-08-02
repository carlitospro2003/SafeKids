package com.example.safekids.models;
import java.io.Serializable;


public class Children implements Serializable {
    private String name;
    private int imageResId;
    private String birthDate;
    private String school;

    public Children(String name, int imageResId, String birthDate, String school) {
        this.name = name;
        this.imageResId = imageResId;
        this.birthDate = birthDate;
        this.school = school;
    }

    public String getName() {
        return name;
    }


    public int getImageResId() {
        return imageResId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getSchool() {
        return school;
    }
}
