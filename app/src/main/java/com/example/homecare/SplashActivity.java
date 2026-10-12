package com.example.homecare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Firebase Auth removed, always go to Role Selection
            Intent intent = new Intent(SplashActivity.this, RoleSelectionActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
