package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LocationActivity extends AppCompatActivity {

    TextView navHome, navLocation, navCall, navAlert, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        navHome = findViewById(R.id.nav_home);
        navLocation = findViewById(R.id.nav_location);
        navCall = findViewById(R.id.nav_call);
        navAlert = findViewById(R.id.nav_alert);
        navProfile = findViewById(R.id.nav_profile);

        // 🏠 Home
        navHome.setOnClickListener(v ->
                startActivity(new Intent(LocationActivity.this, HomePageActivity.class)));

        // 📍 Location (already here)
        navLocation.setOnClickListener(v -> recreate());

        // 📞 Call
        navCall.setOnClickListener(v ->
                startActivity(new Intent(LocationActivity.this, CallActivity.class)));

        // 🔔 Alert
        navAlert.setOnClickListener(v ->
                startActivity(new Intent(LocationActivity.this, AlertActivity.class)));

        // 👤 Profile
        navProfile.setOnClickListener(v ->
                startActivity(new Intent(LocationActivity.this, ProfileActivity.class)));
    }
}
