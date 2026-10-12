package com.example.homecare;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProviderMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_main);

        BottomNavigationView bottomNav = findViewById(R.id.provider_bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_provider_home) {
                selectedFragment = new ProviderHomeFragment();
            } else if (itemId == R.id.nav_provider_orders) {
                selectedFragment = new ProviderOrdersFragment();
            } else if (itemId == R.id.nav_provider_profile) {
                selectedFragment = new ProviderProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.provider_fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.provider_fragment_container, new ProviderHomeFragment())
                    .commit();
        }
    }
}