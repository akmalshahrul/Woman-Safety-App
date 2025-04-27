package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlertActivity extends AppCompatActivity {

    TextView navHome, navLocation, navCall, navAlert, navProfile;
    EditText sosInput;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        // Navigation
        navHome = findViewById(R.id.nav_home);
        navLocation = findViewById(R.id.nav_location);
        navCall = findViewById(R.id.nav_call);
        navAlert = findViewById(R.id.nav_alert);
        navProfile = findViewById(R.id.nav_profile);

        // UI Elements
        sosInput = findViewById(R.id.sosInput);
        sendButton = findViewById(R.id.btnSendSOS);

        // Navigation Listeners
        navHome.setOnClickListener(v ->
                startActivity(new Intent(AlertActivity.this, HomePageActivity.class))
        );

         navLocation.setOnClickListener(v ->
                 startActivity(new Intent(AlertActivity.this, LocationActivity.class))
         );

        navCall.setOnClickListener(v ->
                startActivity(new Intent(AlertActivity.this, CallActivity.class))
        );

        navAlert.setOnClickListener(v -> recreate());

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(AlertActivity.this, ProfileActivity.class))
        );

        // SOS Send Button
        sendButton.setOnClickListener(v -> {
            String message = sosInput.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(AlertActivity.this, "Please describe your emergency!", Toast.LENGTH_SHORT).show();
            } else {
                // Placeholder action
                Toast.makeText(AlertActivity.this, "🚨 SOS Sent: " + message, Toast.LENGTH_LONG).show();
                sosInput.setText(""); // Clear input after sending
            }
        });
    }
}
