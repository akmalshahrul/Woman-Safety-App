package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity {

    TextView locationText, callText, homeText, alertText, profileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationText = findViewById(R.id.locationText);
        callText = findViewById(R.id.callText);
        homeText = findViewById(R.id.homeText);
        alertText = findViewById(R.id.alertText);
        profileText = findViewById(R.id.profileText);

        // 🗺️ Location Page
        locationText.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, LocationActivity.class);
            startActivity(intent);
        });

        // 📞 Call Page
        callText.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CallActivity.class);
            startActivity(intent);
        });

        // 🏠 Home Page – Optional (already here)
        homeText.setOnClickListener(v -> {
            // Optional: Toast or refresh
        });

        // 🔔 Alert Page
        alertText.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, AlertActivity.class);
            startActivity(intent);
        });

        // 👤 Profile Page
        profileText.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}
