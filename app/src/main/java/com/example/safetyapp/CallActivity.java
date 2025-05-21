package com.example.safetyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.*;

import java.util.*;

public class CallActivity extends AppCompatActivity {

    private RecyclerView contactRecyclerView, historyRecyclerView;
    private Button btnAddContact, btnClearHistory;
    private ContactAdapter contactAdapter;
    private HistoryAdapter historyAdapter;
    private List<EmergencyContactModel> contactList = new ArrayList<>();
    private List<CallHistoryModel> historyList = new ArrayList<>();

    private FirebaseFirestore db;
    private CollectionReference contactsRef, historyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        db = FirebaseFirestore.getInstance();
        contactsRef = db.collection("contacts");
        historyRef = db.collection("callHistory");

        contactRecyclerView = findViewById(R.id.contactRecyclerView);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        btnAddContact = findViewById(R.id.btnAddContact);
        btnClearHistory = findViewById(R.id.btnClearHistory);

        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactAdapter = new ContactAdapter(contactList, this::callContact, this::deleteContact);
        historyAdapter = new HistoryAdapter(historyList);

        contactRecyclerView.setAdapter(contactAdapter);
        historyRecyclerView.setAdapter(historyAdapter);

        btnAddContact.setOnClickListener(v -> showAddContactDialog());
        btnClearHistory.setOnClickListener(v -> clearCallHistory());

        loadContacts();
        loadHistory();

        setupNavigationBar();
    }

    private void loadContacts() {
        contactsRef.addSnapshotListener((snapshots, e) -> {
            contactList.clear();
            if (snapshots != null) {
                for (DocumentSnapshot snap : snapshots) {
                    EmergencyContactModel contact = snap.toObject(EmergencyContactModel.class);
                    if (contact != null) contactList.add(contact);
                }
                contactAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadHistory() {
        historyRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    historyList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot snap : snapshots) {
                            CallHistoryModel record = snap.toObject(CallHistoryModel.class);
                            if (record != null) historyList.add(record);
                        }
                        historyAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void callContact(EmergencyContactModel contact) {
        CallHistoryModel log = new CallHistoryModel(contact.name, contact.phone, System.currentTimeMillis());
        historyRef.add(log);

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contact.phone));
        startActivity(intent);
    }

    private void deleteContact(EmergencyContactModel contact) {
        contactsRef.document(contact.id).delete();
    }

    private void clearCallHistory() {
        historyRef.get().addOnSuccessListener(querySnapshot -> {
            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                doc.getReference().delete();
            }
        });
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Emergency Contact");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        EditText nameInput = new EditText(this);
        nameInput.setHint("Name");
        layout.addView(nameInput);

        EditText phoneInput = new EditText(this);
        phoneInput.setHint("Phone Number");
        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
        layout.addView(phoneInput);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();

            if (!name.isEmpty() && !phone.isEmpty()) {
                String id = contactsRef.document().getId();
                EmergencyContactModel contact = new EmergencyContactModel(id, name, phone);
                contactsRef.document(id).set(contact);
            } else {
                Toast.makeText(this, "Please enter both name and phone", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void setupNavigationBar() {
        findViewById(R.id.nav_home).setOnClickListener(v -> startActivity(new Intent(this, HomePageActivity.class)));
        findViewById(R.id.nav_location).setOnClickListener(v -> startActivity(new Intent(this, LocationActivity.class)));
        findViewById(R.id.nav_alert).setOnClickListener(v -> startActivity(new Intent(this, AlertActivity.class)));
        findViewById(R.id.nav_profile).setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }
}
