package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class HomePageActivity extends AppCompatActivity {

    // Bottom Navigation
    TextView locationText, callText, homeText, alertText, profileText;

    // Buttons
    Button btnSOS, btnContacts;

    // Firebase Alert List
    RecyclerView alertRecyclerView;
    List<AlertModel> alertList = new ArrayList<>();
    AlertAdapter alertAdapter;
    FirebaseFirestore db;
    CollectionReference alertRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // NavBar
        locationText = findViewById(R.id.locationText);
        callText = findViewById(R.id.callText);
        homeText = findViewById(R.id.homeText);
        alertText = findViewById(R.id.alertText);
        profileText = findViewById(R.id.profileText);

        locationText.setOnClickListener(v -> startActivity(new Intent(this, LocationActivity.class)));
        callText.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));
        homeText.setOnClickListener(v -> {}); // already here
        alertText.setOnClickListener(v -> startActivity(new Intent(this, AlertActivity.class)));
        profileText.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        // Quick access buttons
        btnSOS = findViewById(R.id.btnSOS);
        btnContacts = findViewById(R.id.btnContacts);

        btnSOS.setOnClickListener(v -> startActivity(new Intent(this, LocationActivity.class)));
        btnContacts.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));

        // Alert RecyclerView
        alertRecyclerView = findViewById(R.id.alertRecyclerView);
        alertRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        alertAdapter = new AlertAdapter(alertList);
        alertRecyclerView.setAdapter(alertAdapter);

        // Load alerts from Firestore
        db = FirebaseFirestore.getInstance();
        alertRef = db.collection("alerts");

        alertRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;

                        alertList.clear();
                        for (DocumentSnapshot doc : snapshots) {
                            AlertModel alert = doc.toObject(AlertModel.class);
                            if (alert != null) {
                                alertList.add(alert);
                            }
                        }
                        alertAdapter.notifyDataSetChanged();
                    }
                });
    }
}
