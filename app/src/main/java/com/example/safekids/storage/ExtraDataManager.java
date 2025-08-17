package com.example.safekids.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExtraDataManager {
    private static final String PREF_NAME = "extra_data_pref";
    private static final String KEY_GUARDIAN_ID = "guardian_id";
    private static final String KEY_GUARDIAN_PHOTO = "guardian_photo";
    private static final String KEY_SCHOOL_ID = "school_id";
    private static final String KEY_SCHOOL_LOGO = "school_logo";

    private static final String KEY_AUTHORIZED_IDS = "authorized_ids";
    private static final String KEY_AUTHORIZED_PHOTOS = "authorized_photos";
    private static final String KEY_AUTHORIZED_IMG_ROUTES = "authorized_img_routes";

    private static final String KEY_TUTOR_IMG_ROUTES = "tutors_img_routes";

    private static final String KEY_CHILDREN_PHOTOS = "children_photos";
    private static final String KEY_CHILDREN_IDS = "children_ids";


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public ExtraDataManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Guardar ID del guardián
    public void saveGuardianId(int guardianId) {
        editor.putInt(KEY_GUARDIAN_ID, guardianId).apply();
    }

    // Obtener ID del guardián
    public int getGuardianId() {
        return prefs.getInt(KEY_GUARDIAN_ID, -1);
    }

    // Guardar foto del guardián
    public void saveGuardianPhoto(String photoUrl) {
        editor.putString(KEY_GUARDIAN_PHOTO, photoUrl).apply();
    }

    // Obtener foto del guardián
    public String getGuardianPhoto() {
        return prefs.getString(KEY_GUARDIAN_PHOTO, null);
    }

    // Guardar ID de la escuela
    public void saveSchoolId(int schoolId) {
        editor.putInt(KEY_SCHOOL_ID, schoolId).apply();
    }

    // Obtener ID de la escuela
    public int getSchoolId() {
        return prefs.getInt(KEY_SCHOOL_ID, -1);
    }

    // Guardar logo de la escuela
    public void saveSchoolLogo(String logoUrl) {
        editor.putString(KEY_SCHOOL_LOGO, logoUrl).apply();
    }

    // Obtener logo de la escuela
    public String getSchoolLogo() {
        return prefs.getString(KEY_SCHOOL_LOGO, null);
    }

    // Guardar lista de IDs de autorizados
    public void saveAuthorizedIds(java.util.List<Integer> ids) {
        java.util.List<String> strIds = new java.util.ArrayList<>();
        for (Integer id : ids) strIds.add(String.valueOf(id));
        editor.putStringSet(KEY_AUTHORIZED_IDS, new java.util.HashSet<>(strIds)).apply();
    }

    // Obtener lista de IDs de autorizados
    public java.util.List<Integer> getAuthorizedIds() {
        java.util.Set<String> set = prefs.getStringSet(KEY_AUTHORIZED_IDS, new java.util.HashSet<>());
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        for (String s : set) {
            try { ids.add(Integer.parseInt(s)); } catch (Exception ignored) {}
        }
        return ids;
    }

    // Guardar lista de fotos de autorizados
    public void saveAuthorizedPhotos(java.util.List<String> photos) {
        editor.putStringSet(KEY_AUTHORIZED_PHOTOS, new java.util.HashSet<>(photos)).apply();
    }

    // Obtener lista de fotos de autorizados
    public java.util.List<String> getAuthorizedPhotos() {
        java.util.Set<String> set = prefs.getStringSet(KEY_AUTHORIZED_PHOTOS, new java.util.HashSet<>());
        return new java.util.ArrayList<>(set);
    }

    // Guardar lista de img_route de autorizados
    public void saveAuthorizedImgRoutes(java.util.List<String> imgRoutes) {
        editor.putStringSet(KEY_AUTHORIZED_IMG_ROUTES, new java.util.HashSet<>(imgRoutes)).apply();
    }

    // Obtener lista de img_route de autorizados
    public java.util.List<String> getAuthorizedImgRoutes() {
        java.util.Set<String> set = prefs.getStringSet(KEY_AUTHORIZED_IMG_ROUTES, new java.util.HashSet<>());
        return new java.util.ArrayList<>(set);
    }
    // Guardar lista de img_route de tutores
    public void saveTutorImgRoutes(List<String> imgRoutes) {
        editor.putStringSet(KEY_TUTOR_IMG_ROUTES, new HashSet<>(imgRoutes)).apply();
    }
    // Obtener lista de img_route de tutores
    // Obtener lista de img_route de tutores
    public List<String> getTutorsImgRoutes() {
        Set<String> set = prefs.getStringSet(KEY_TUTOR_IMG_ROUTES, new HashSet<>());
        return new ArrayList<>(set);
    }

    // Guardar lista de fotos de niños
    public void saveChildrenPhotos(List<String> photos) {
        editor.putStringSet(KEY_CHILDREN_PHOTOS, new HashSet<>(photos)).apply();
    }

    // Obtener lista de fotos de niños
    public List<String> getChildrenPhotos() {
        Set<String> set = prefs.getStringSet(KEY_CHILDREN_PHOTOS, new HashSet<>());
        return new ArrayList<>(set);
    }
    // Guardar lista de IDs de niños
    public void saveChildrenIds(List<Integer> ids) {
        List<String> strIds = new ArrayList<>();
        for (Integer id : ids) strIds.add(String.valueOf(id));
        editor.putStringSet(KEY_CHILDREN_IDS, new HashSet<>(strIds)).apply();
    }

    // Obtener lista de IDs de niños
    public List<Integer> getChildrenIds() {
        Set<String> set = prefs.getStringSet(KEY_CHILDREN_IDS, new HashSet<>());
        List<Integer> ids = new ArrayList<>();
        for (String s : set) {
            try { ids.add(Integer.parseInt(s)); } catch (Exception ignored) {}
        }
        return ids;
    }
}
