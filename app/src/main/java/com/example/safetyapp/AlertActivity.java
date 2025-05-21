package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.*;

import java.text.DateFormat;
import java.util.*;

public class AlertActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextAlert;
    private Button btnSend;
    private FloatingActionButton btnClearAll;

    private FirebaseFirestore firestore;
    private CollectionReference alertsRef;

    private PushAlertAdapter adapter;
    private List<PushAlertModel> alertList = new ArrayList<>();

    private TextView navHome, navLocation, navCall, navAlert, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        recyclerView = findViewById(R.id.recyclerView);
        editTextAlert = findViewById(R.id.editTextAlert);
        btnSend = findViewById(R.id.btnSend);
        btnClearAll = findViewById(R.id.btnClearAll);

        // Navigation bar
        navHome = findViewById(R.id.nav_home);
        navLocation = findViewById(R.id.nav_location);
        navCall = findViewById(R.id.nav_call);
        navAlert = findViewById(R.id.nav_alert);
        navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(v -> startActivity(new Intent(this, HomePageActivity.class)));
        navLocation.setOnClickListener(v -> startActivity(new Intent(this, LocationActivity.class)));
        navCall.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));
        navAlert.setOnClickListener(v -> {}); // Already here
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        // Firebase setup
        firestore = FirebaseFirestore.getInstance();
        alertsRef = firestore.collection("push_alerts");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PushAlertAdapter(alertList);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendAlert());
        btnClearAll.setOnClickListener(v -> clearAllAlerts());

        loadAlerts();
    }

    private void sendAlert() {
        String message = editTextAlert.getText().toString().trim();
        if (!message.isEmpty()) {
            Map<String, Object> alert = new HashMap<>();
            alert.put("message", message);
            alert.put("timestamp", System.currentTimeMillis());
            alertsRef.add(alert)
                    .addOnSuccessListener(doc -> {
                        editTextAlert.setText("");
                        Toast.makeText(this, "🚨 Alert sent!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void loadAlerts() {
        alertsRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    alertList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        PushAlertModel alert = doc.toObject(PushAlertModel.class);
                        alertList.add(alert);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void clearAllAlerts() {
        alertsRef.get().addOnSuccessListener(querySnapshot -> {
            WriteBatch batch = firestore.batch();
            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                batch.delete(doc.getReference());
            }
            batch.commit().addOnSuccessListener(unused ->
                    Toast.makeText(this, "All alerts cleared", Toast.LENGTH_SHORT).show());
        });
    }
}
