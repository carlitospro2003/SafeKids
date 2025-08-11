package com.example.safekids.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.safekids.models.Children;
import com.example.safekids.models.School;

import com.example.safekids.network.GuardianResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static final String PREF_NAME = "SafeKidsSession";
    private static final String KEY_TOKEN = "token";

    // Guardian keys
    private static final String KEY_ID = "guardian_id";
    private static final String KEY_FIRST_NAME = "guardian_first_name";
    private static final String KEY_LAST_NAME = "guardian_last_name";
    private static final String KEY_EMAIL = "guardian_email";
    private static final String KEY_PHONE = "guardian_phone";
    private static final String KEY_PHOTO = "guardian_photo";
    private static final String KEY_STATUS = "guardian_status";
    private static final String KEY_STUDENTS = "guardian_students";
    private static final String KEY_SCHOOL = "guardian_school";

    private static final String KEY_AUTHORIZED_IDS = "authorized_ids";

    private static final String KEY_LAST_AUTHORIZED_ID = "last_authorized_id";

    private static final String KEY_SCHOOL_ID = "school_id";






    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void saveGuardian(GuardianResponse.Guardian guardian) {
        editor.putInt(KEY_ID, guardian.getId());
        editor.putString(KEY_FIRST_NAME, guardian.getFirstName());
        editor.putString(KEY_LAST_NAME, guardian.getLastName());
        editor.putString(KEY_EMAIL, guardian.getEmail());
        editor.putString(KEY_PHONE, guardian.getPhone());
        editor.putString(KEY_PHOTO, guardian.getPhoto());
        editor.putBoolean(KEY_STATUS, guardian.getStatus() != null && guardian.getStatus());
        editor.apply();
    }

    public GuardianResponse.Guardian getGuardian() {
        return new GuardianResponse.Guardian() {
            private final int id = prefs.getInt(KEY_ID, -1);
            private final String firstName = prefs.getString(KEY_FIRST_NAME, "");
            private final String lastName = prefs.getString(KEY_LAST_NAME, "");
            private final String email = prefs.getString(KEY_EMAIL, "");
            private final String phone = prefs.getString(KEY_PHONE, "");
            private final String photo = prefs.getString(KEY_PHOTO, "");
            private final Boolean status = prefs.getBoolean(KEY_STATUS, false);

            @Override public int getId() { return id; }
            @Override public String getFirstName() { return firstName; }
            @Override public String getLastName() { return lastName; }
            @Override public String getEmail() { return email; }
            @Override public String getPhone() { return phone; }
            @Override public String getPhoto() { return photo; }
            @Override public Boolean getStatus() { return status; }
        };
    }

    public void saveStudents(List<Children> students) {
        Gson gson = new Gson();
        String json = gson.toJson(students);
        editor.putString(KEY_STUDENTS, json);
        editor.apply();
    }

    public List<Children> getStudents() {
        String json = prefs.getString(KEY_STUDENTS, null);
        if (json == null) return new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Children>>() {}.getType();
        return gson.fromJson(json, type);
    }


    public void saveSchool(School school) {
        Gson gson = new Gson();
        String json = gson.toJson(school);
        editor.putString(KEY_SCHOOL, json);
        editor.putInt(KEY_SCHOOL_ID, school.getId()); // 🔹 Guardar ID directo
        editor.apply();
    }

    public int getSchoolId() {
        return prefs.getInt(KEY_SCHOOL_ID, -1); // 🔹 Obtener ID directo
    }
    public int getGuardianId() {
        return prefs.getInt(KEY_ID, -1); // 🔹 Obtener ID directo
    }


    public School getSchool() {
        String json = prefs.getString(KEY_SCHOOL, null);
        if (json == null) return null;
        Gson gson = new Gson();
        return gson.fromJson(json, School.class);
    }

    // 🔹 NUEVO: Guardar IDs de responsables autorizados
    public void saveAuthorizedIds(List<Integer> ids) {
        Gson gson = new Gson();
        String json = gson.toJson(ids);
        editor.putString(KEY_AUTHORIZED_IDS, json);
        editor.apply();
    }

    // 🔹 NUEVO: Obtener IDs de responsables autorizados
    public List<Integer> getAuthorizedIds() {
        String json = prefs.getString(KEY_AUTHORIZED_IDS, null);
        if (json == null) return new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Guardar último ID creado
    public void saveLastAuthorizedId(int id) {
        editor.putInt(KEY_LAST_AUTHORIZED_ID, id);
        editor.apply();
    }

    // Obtener último ID creado
    public int getLastAuthorizedId() {
        return prefs.getInt(KEY_LAST_AUTHORIZED_ID, -1);
    }


    public void clearSession() {
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_ID);
        editor.remove(KEY_FIRST_NAME);
        editor.remove(KEY_LAST_NAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PHONE);
        editor.remove(KEY_PHOTO);
        editor.remove(KEY_STATUS);
        editor.apply();
    }
    public boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }



}
