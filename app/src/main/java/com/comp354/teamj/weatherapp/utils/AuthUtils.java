package com.comp354.teamj.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.toolbox.Volley;

/***
 * Utilities for Authentication and Login
 */
public class AuthUtils {

    // Constants and Variables
    private static final String AuthRememberKey = "authenticationRemember";
    private static final String DEFAULT_USERNAME = "user";
    private static final String DEFAULT_PASSWORD = "password";

    private SharedPreferences sharedPreferences;

    public AuthUtils(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    /**
     * Check if the user has its credentials were saved on the device
     * @return
     */
    public boolean loginRemembered() {
        return sharedPreferences.getBoolean(AuthUtils.AuthRememberKey, false);
    }

    /**
     * Perform a login operation
     * @param username Username
     * @param password Password
     * @return Returns if the login was successful
     */
    public boolean login(String username, String password, boolean rememberLogin) {
        sharedPreferences.edit().putBoolean(AuthUtils.AuthRememberKey, rememberLogin).apply();
        return DEFAULT_USERNAME.equals(username)
                && DEFAULT_PASSWORD.equals(password);
    }
}
