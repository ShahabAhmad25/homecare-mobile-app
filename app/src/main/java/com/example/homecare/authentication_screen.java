package com.example.homecare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class authentication_screen extends AppCompatActivity {

    private String selectedRole;
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_screen);

        selectedRole = getIntent().getStringExtra("SELECTED_ROLE");
        if (selectedRole == null) {
            selectedRole = "customer";
        }

        etEmail = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        MaterialButton btnLogin = findViewById(R.id.btn_login);
        MaterialButton btnGoogle = findViewById(R.id.btn_google);
        TextView tvSignup = findViewById(R.id.tv_signup);
        TextView tvForgotPassword = findViewById(R.id.tv_forgot_password);

        btnLogin.setOnClickListener(v -> loginUser());
        
        // Google Sign In removed as it depends on Firebase Auth
        btnGoogle.setOnClickListener(v -> {
            Toast.makeText(this, "Login successful (Random)", Toast.LENGTH_SHORT).show();
            navigateBasedOnRole(selectedRole);
        });

        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(authentication_screen.this, SignupActivity.class);
            intent.putExtra("SELECTED_ROLE", selectedRole);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Feature disabled", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        
        // Bypassing Firebase Auth
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        navigateBasedOnRole(selectedRole);
    }

    private void navigateBasedOnRole(String role) {
        Intent intent;
        if ("provider".equals(role)) {
            intent = new Intent(authentication_screen.this, ProviderMainActivity.class);
        } else {
            intent = new Intent(authentication_screen.this, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
