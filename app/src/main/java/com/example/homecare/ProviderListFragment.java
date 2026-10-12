package com.example.homecare;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProviderListFragment extends Fragment {

    private String category;
    private static final String TAG = "ProviderListFragment";

    public static ProviderListFragment newInstance(String category) {
        ProviderListFragment fragment = new ProviderListFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString("category");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_list, container, false);

        TextView tvTitle = view.findViewById(R.id.tv_provider_category_title);
        ImageView btnBack = view.findViewById(R.id.btn_back_providers);
        GridLayout gridLayout = view.findViewById(R.id.providers_grid);

        if (category != null) {
            tvTitle.setText(category + " Providers");
        }
        
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        loadProvidersFromJson(inflater, gridLayout);

        return view;
    }

    private void loadProvidersFromJson(LayoutInflater inflater, GridLayout gridLayout) {
        String json = loadJSONFromAsset();
        if (json == null) {
            Toast.makeText(getContext(), "Error loading providers data", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<ProviderItem>>() {}.getType();
        List<ProviderItem> allProviders = gson.fromJson(json, listType);

        if (allProviders == null) return;

        // Filter by category
        List<ProviderItem> filteredProviders = new ArrayList<>();
        for (ProviderItem p : allProviders) {
            if (p.getCategory() != null && p.getCategory().equalsIgnoreCase(category)) {
                filteredProviders.add(p);
            }
        }

        // Apply our ML Ranking Algorithm
        BestProviderSelection.sortProvidersByBestMatch(filteredProviders);

        gridLayout.removeAllViews();
        int count = 0;
        for (ProviderItem provider : filteredProviders) {
            boolean isBestMatch = (count == 0); // Top ranked is the best match
            addProviderItem(inflater, gridLayout, provider, isBestMatch);
            count++;
        }

        if (count == 0) {
            Toast.makeText(getContext(), "No providers found for: " + category, Toast.LENGTH_SHORT).show();
        }
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = requireContext().getAssets().open("providers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void addProviderItem(LayoutInflater inflater, GridLayout container, ProviderItem provider, boolean isBestMatch) {
        View itemView = inflater.inflate(R.layout.item_service_row, container, false);
        
        ImageView ivBanner = itemView.findViewById(R.id.iv_service_banner);
        TextView tvName = itemView.findViewById(R.id.tv_service_name);
        TextView tvPrice = itemView.findViewById(R.id.tv_service_price);
        TextView tvRating = itemView.findViewById(R.id.tv_service_rating);
        TextView tvExperience = itemView.findViewById(R.id.tv_service_tags);
        TextView tvBestMatchBadge = itemView.findViewById(R.id.tv_best_match_badge);
        MaterialButton btnBookNow = itemView.findViewById(R.id.btn_book_now);

        tvName.setText(provider.getName());
        tvPrice.setText(provider.getPrice());
        tvRating.setText(provider.getRating());
        tvExperience.setText(provider.getExperience() + " Years Exp.");

        if (isBestMatch) {
            tvBestMatchBadge.setVisibility(View.VISIBLE);
            tvBestMatchBadge.setText("BEST MATCH");
        } else {
            tvBestMatchBadge.setVisibility(View.GONE);
        }

        if (provider.getImageUrl() != null && !provider.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(provider.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivBanner);
        }

        btnBookNow.setOnClickListener(v -> {
            ServiceDetailFragment detailFragment = ServiceDetailFragment.newInstance(
                    provider.getName(), 
                    provider.getPrice(), 
                    provider.getRating(),
                    provider.getImageUrl(),
                    provider.getExperience(),
                    provider.getCategory()
            );
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        container.addView(itemView);
    }
}
