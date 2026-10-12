package com.example.homecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class RoleSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        MaterialCardView customerCard = findViewById(R.id.card_customer);
        MaterialCardView providerCard = findViewById(R.id.card_provider);

        customerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin("customer");
            }
        });

        providerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin("provider");
            }
        });
    }

    private void navigateToLogin(String role) {
        Intent intent = new Intent(RoleSelectionActivity.this, authentication_screen.class);
        intent.putExtra("SELECTED_ROLE", role);
        startActivity(intent);
    }
}