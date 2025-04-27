package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textForgotPassword, textSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textForgotPassword = findViewById(R.id.textForgotPassword);
        textSignUp = findViewById(R.id.textSignUp);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.equals("user@example.com") && password.equals("123456")) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(LoginActivity.this, MainActivity.class)); // Next screen
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        textSignUp.setOnClickListener(v -> {
            // Intent to Sign Up Page
        });

        textForgotPassword.setOnClickListener(v -> {
            // Intent to Forgot Password Page
        });

        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish(); // Optional: closes login screen so user can’t go back to it
        });

    }
}