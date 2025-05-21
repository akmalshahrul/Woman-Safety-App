package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    TextView navHome, navLocation, navCall, navAlert, navProfile, textEmail;
    EditText editName, editPhone;
    Button btnLogout, btnSaveProfile;
    Button btnPushToggle, btnContrastToggle, btnLocationToggle;

    FirebaseFirestore firestore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Nav bar
        navHome = findViewById(R.id.nav_home);
        navLocation = findViewById(R.id.nav_location);
        navCall = findViewById(R.id.nav_call);
        navAlert = findViewById(R.id.nav_alert);
        navProfile = findViewById(R.id.nav_profile);

        // Profile UI
        textEmail = findViewById(R.id.textEmail);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnLogout = findViewById(R.id.btnLogout);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Settings
        btnPushToggle = findViewById(R.id.btnPushToggle);
        btnContrastToggle = findViewById(R.id.btnContrastToggle);
        btnLocationToggle = findViewById(R.id.btnLocationToggle);

        // 🔄 Navigation
        navHome.setOnClickListener(v -> startActivity(new Intent(this, HomePageActivity.class)));
        navLocation.setOnClickListener(v -> startActivity(new Intent(this, LocationActivity.class)));
        navCall.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));
        navAlert.setOnClickListener(v -> startActivity(new Intent(this, AlertActivity.class)));
        navProfile.setOnClickListener(v -> recreate());

        // 👤 Email Display
        if (currentUser != null) {
            textEmail.setText("Email: " + currentUser.getEmail());
            loadUserData(currentUser.getUid());
        }

        // 🔐 Logout
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // 💾 Save Profile Info
        btnSaveProfile.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Name and Phone are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", name);
            userMap.put("phone", phone);
            firestore.collection("users").document(currentUser.getUid())
                    .set(userMap)
                    .addOnSuccessListener(unused -> Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        // 🛠️ Settings Toggles
        btnPushToggle.setOnClickListener(v -> toggleButton(btnPushToggle));
        btnContrastToggle.setOnClickListener(v -> toggleButton(btnContrastToggle));
        btnLocationToggle.setOnClickListener(v -> toggleButton(btnLocationToggle));
    }

    private void toggleButton(Button btn) {
        String current = btn.getText().toString();
        btn.setText(current.equals("ON") ? "OFF" : "ON");
    }

    private void loadUserData(String uid) {
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phone");

                        editName.setText(name != null ? name : "");
                        editPhone.setText(phone != null ? phone : "");
                    }
                });
    }
}
