package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView navHome, navLocation, navCall, navAlert, navProfile;
    Button btnLogout;

    // Setting toggles
    Button btnPushToggle, btnContrastToggle, btnLocationToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Navigation Bar
        navHome = findViewById(R.id.nav_home);
        navLocation = findViewById(R.id.nav_location);
        navCall = findViewById(R.id.nav_call);
        navAlert = findViewById(R.id.nav_alert);
        navProfile = findViewById(R.id.nav_profile);

        // Profile & Logout
        btnLogout = findViewById(R.id.btnLogout);

        // Setting Buttons
        btnPushToggle = findViewById(R.id.btnPushToggle);
        btnContrastToggle = findViewById(R.id.btnContrastToggle);
        btnLocationToggle = findViewById(R.id.btnLocationToggle);

        // 🏠 Home
        navHome.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, HomePageActivity.class))
        );

        // 📍 Location
        navLocation.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, LocationActivity.class))
        );

        // 📞 Call
        navCall.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, CallActivity.class))
        );

        // 🔔 Alert
        navAlert.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, AlertActivity.class))
        );

        // 👤 Profile (stay here)
        navProfile.setOnClickListener(v -> recreate());

        // 🚪 Logout
        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });

        // 🔄 Toggle ON/OFF Logic
        btnPushToggle.setOnClickListener(v -> {
            String current = btnPushToggle.getText().toString();
            btnPushToggle.setText(current.equals("ON") ? "OFF" : "ON");
        });

        btnContrastToggle.setOnClickListener(v -> {
            String current = btnContrastToggle.getText().toString();
            btnContrastToggle.setText(current.equals("ON") ? "OFF" : "ON");
        });

        btnLocationToggle.setOnClickListener(v -> {
            String current = btnLocationToggle.getText().toString();
            btnLocationToggle.setText(current.equals("ON") ? "OFF" : "ON");
        });
    }
}
