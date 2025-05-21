package com.example.safetyapp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextRegisterEmail, editTextRegisterPassword, editTextRegisterConfirm;
    Button buttonRegister;
    TextView textLoginRedirect;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        editTextRegisterConfirm = findViewById(R.id.editTextRegisterConfirm);
        buttonRegister = findViewById(R.id.buttonRegister);
        textLoginRedirect = findViewById(R.id.textLoginRedirect);

        // Underline the login redirect text
        textLoginRedirect.setPaintFlags(textLoginRedirect.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        buttonRegister.setOnClickListener(v -> registerUser());

        textLoginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String email = editTextRegisterEmail.getText().toString().trim();
        String password = editTextRegisterPassword.getText().toString().trim();
        String confirm = editTextRegisterConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, HomePageActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
