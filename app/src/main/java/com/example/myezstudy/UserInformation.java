package com.example.myezstudy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

public class UserInformation
{
    private static final String PREF_NAME = "UserPrefs";
    private static String KEY_USERNAME = "username";
    private static String KEY_PASSWORD = "password";


    // Method to save user credentials in SharedPreferences
    public static void saveUserCredentials(Context context, String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
        Log.i("UserInfo", username);
        Log.i("UserInfo", password);

    }

    // Method to retrieve saved username from SharedPreferences
    public static String getSavedUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, "");
    }
    // Method to retrieve saved password from SharedPreferences
    public static String getSavedPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }
}
