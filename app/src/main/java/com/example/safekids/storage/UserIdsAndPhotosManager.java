package com.example.safekids.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserIdsAndPhotosManager {
    private static final String PREF_NAME = "SafeKidsUserIdsAndPhotos";
    private static final String KEY_GUARDIAN_ID = "guardian_id";
    private static final String KEY_GUARDIAN_PHOTO = "guardian_photo";
    private static final String KEY_SCHOOL_ID = "school_id";
    private static final String KEY_CHILDREN_IDS = "children_ids";
    private static final String KEY_CHILDREN_PHOTOS = "children_photos";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public UserIdsAndPhotosManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveGuardianId(int id) {
        editor.putInt(KEY_GUARDIAN_ID, id);
        editor.apply();
    }

    public int getGuardianId() {
        return prefs.getInt(KEY_GUARDIAN_ID, -1);
    }

    public void saveGuardianPhoto(String photo) {
        editor.putString(KEY_GUARDIAN_PHOTO, photo);
        editor.apply();
    }

    public String getGuardianPhoto() {
        return prefs.getString(KEY_GUARDIAN_PHOTO, "");
    }

    public void saveSchoolId(int id) {
        editor.putInt(KEY_SCHOOL_ID, id);
        editor.apply();
    }

    public int getSchoolId() {
        return prefs.getInt(KEY_SCHOOL_ID, -1);
    }

    public void saveChildrenIds(List<Integer> ids) {
        Set<String> set = new HashSet<>();
        for (Integer id : ids) set.add(String.valueOf(id));
        editor.putStringSet(KEY_CHILDREN_IDS, set);
        editor.apply();
    }

    public List<Integer> getChildrenIds() {
        Set<String> set = prefs.getStringSet(KEY_CHILDREN_IDS, new HashSet<>());
        List<Integer> ids = new ArrayList<>();
        for (String s : set) {
            try { ids.add(Integer.parseInt(s)); } catch (Exception ignored) {}
        }
        return ids;
    }

    public void saveChildrenPhotos(List<String> photos) {
        Set<String> set = new HashSet<>(photos);
        editor.putStringSet(KEY_CHILDREN_PHOTOS, set);
        editor.apply();
    }

    public List<String> getChildrenPhotos() {
        Set<String> set = prefs.getStringSet(KEY_CHILDREN_PHOTOS, new HashSet<>());
        return new ArrayList<>(set);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}