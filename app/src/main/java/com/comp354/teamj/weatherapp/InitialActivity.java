package com.comp354.teamj.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.comp354.teamj.weatherapp.utils.AuthUtils;

/***
 * Initial Activity to prompt for login if necessary
 */
public class InitialActivity extends AppCompatActivity {

    private AuthUtils authUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;
        authUtils = new AuthUtils(getApplicationContext());

        // Check if login is required before showing MainActivity
        if (authUtils.loginRemembered()) {
            activityIntent = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            activityIntent = new Intent(getApplicationContext(), LoginActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }
}