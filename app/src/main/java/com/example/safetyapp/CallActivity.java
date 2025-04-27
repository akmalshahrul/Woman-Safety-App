package com.example.safetyapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewGroup;
import android.widget.*;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CallActivity extends AppCompatActivity {

    TextView navHome, navLocation, navCall, navAlert, navProfile;
    Button addContactButton;
    LinearLayout contactContainer;
    boolean contactAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        // Nav bar
        navHome = findViewById(R.id.nav_home);
        navLocation = findViewById(R.id.nav_location);
        navCall = findViewById(R.id.nav_call);
        navAlert = findViewById(R.id.nav_alert);
        navProfile = findViewById(R.id.nav_profile);

        // Nav listeners
        navHome.setOnClickListener(v -> startActivity(new Intent(this, HomePageActivity.class)));
        navAlert.setOnClickListener(v -> startActivity(new Intent(this, AlertActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        navLocation.setOnClickListener(v -> startActivity(new Intent(this, LocationActivity.class)));

        // Main UI
        contactContainer = findViewById(R.id.contactContainer);
        addContactButton = findViewById(R.id.btnAddContact);

        // Add contact click
        addContactButton.setOnClickListener(v -> {
            if (contactAdded) {
                Toast.makeText(this, "Only 1 contact allowed for now!", Toast.LENGTH_SHORT).show();
                return;
            }
            showAddContactDialog();
        });

        // Setup delete for static contacts
        setupStaticDeleteButtons();
    }

    public void callPDRM(View view) {
        Toast.makeText(this, "📞 Calling PDRM...", Toast.LENGTH_SHORT).show();
    }

    public void callTalianNur(View view) {
        Toast.makeText(this, "📞 Calling Talian Nur...", Toast.LENGTH_SHORT).show();
    }

    private void setupStaticDeleteButtons() {
        LinearLayout contact1 = (LinearLayout) contactContainer.getChildAt(0);
        Button deleteBtn1 = (Button) contact1.getChildAt(2);
        deleteBtn1.setOnClickListener(v -> Toast.makeText(this, "🗑️ Deleting PDRM...", Toast.LENGTH_SHORT).show());

        LinearLayout contact2 = (LinearLayout) contactContainer.getChildAt(1);
        Button deleteBtn2 = (Button) contact2.getChildAt(2);
        deleteBtn2.setOnClickListener(v -> Toast.makeText(this, "🗑️ Deleting Talian Nur...", Toast.LENGTH_SHORT).show());
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Contact");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 10);

        final EditText inputName = new EditText(this);
        inputName.setHint("Name");
        layout.addView(inputName);

        final EditText inputNumber = new EditText(this);
        inputNumber.setHint("Phone Number");
        inputNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        layout.addView(inputNumber);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = inputName.getText().toString();
            String number = inputNumber.getText().toString();
            if (!name.isEmpty() && !number.isEmpty()) {
                addNewContact(name, number);
                contactAdded = true;
            } else {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addNewContact(String name, String number) {
        // Contact container layout
        LinearLayout contact = new LinearLayout(this);
        contact.setOrientation(LinearLayout.HORIZONTAL);
        contact.setPadding(12, 12, 12, 12);
        contact.setBackgroundColor(Color.WHITE);

        // 🔥 MATCH elevation & shadow like XML
        contact.setElevation(4 * getResources().getDisplayMetrics().density); // 4dp
        contact.setOutlineProvider(ViewOutlineProvider.PADDED_BOUNDS);

        LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        marginParams.setMargins(0, 0, 0, 16);
        contact.setLayoutParams(marginParams);

        // Text block
        LinearLayout textColumn = new LinearLayout(this);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        textColumn.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView nameView = new TextView(this);
        nameView.setText(name);
        nameView.setTextSize(16);
        nameView.setTextColor(0xFF000000);
        nameView.setTypeface(null, Typeface.BOLD);

        TextView numberView = new TextView(this);
        numberView.setText(number);
        numberView.setTextSize(14);
        numberView.setTextColor(0xFF000000);

        textColumn.addView(nameView);
        textColumn.addView(numberView);

        // Call button
        Button callBtn = new Button(this);
        callBtn.setText("📞");
        styleMiniButton(callBtn);
        callBtn.setOnClickListener(v ->
                Toast.makeText(this, "📞 Calling " + name, Toast.LENGTH_SHORT).show()
        );

        // Delete button
        Button deleteBtn = new Button(this);
        deleteBtn.setText("🗑️");
        styleMiniButton(deleteBtn);
        deleteBtn.setOnClickListener(v -> {
            contactContainer.removeView(contact);
            contactAdded = false;
            Toast.makeText(this, "🗑️ Deleted " + name, Toast.LENGTH_SHORT).show();
        });

        contact.addView(textColumn);
        contact.addView(callBtn);
        contact.addView(deleteBtn);

        contactContainer.addView(contact);
    }

    private void styleMiniButton(Button btn) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 0, 0);
        btn.setLayoutParams(params);
        btn.setTextSize(16);
        btn.setPadding(30, 16, 30, 16);
        btn.setTextColor(Color.WHITE);

        // Apply custom shape and color
        btn.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button));
    }
}
