package com.example.btlandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPrefUtil {
    private static final String PREF_NAME = "skill_share_app";
    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext()
                    .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    public static void putObject(String key, Object value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        sharedPreferences.edit().putString(key, json).apply();
    }

    public static <T> T getObject(String key, Class<T> clazz) {
        String json = sharedPreferences.getString(key, null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        }
        return null;
    }

    public static void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public static void clear() {
        sharedPreferences.edit().clear().apply();
    }
}