package com.example.homecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
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

public class ServiceDetailFragment extends Fragment {

    private String serviceName;
    private String servicePrice;
    private String serviceRating;
    private String serviceImageUrl;
    private String serviceCategory;
    private String bestProviderName = "Unknown Provider";
    private String bestProviderRating = "4.5";
    private String bestProviderImage = "";

    private int serviceExperience;

    public static ServiceDetailFragment newInstance(String name, String price, String rating, String imageUrl, int experience, String category) {
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        Bundle args = new Bundle();
        args.putString("service_name", name);
        args.putString("service_price", price);
        args.putString("service_rating", rating);
        args.putString("service_image_url", imageUrl);
        args.putInt("service_experience", experience);
        args.putString("service_category", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceName = getArguments().getString("service_name");
            servicePrice = getArguments().getString("service_price");
            serviceRating = getArguments().getString("service_rating");
            serviceImageUrl = getArguments().getString("service_image_url");
            serviceExperience = getArguments().getInt("service_experience");
            serviceCategory = getArguments().getString("service_category");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_detail, container, false);

        TextView tvName = view.findViewById(R.id.tv_detail_name);
        TextView tvPrice = view.findViewById(R.id.tv_detail_price);
        TextView tvBottomPrice = view.findViewById(R.id.tv_bottom_price);
        TextView tvRating = view.findViewById(R.id.tv_detail_rating);
        TextView tvExperience = view.findViewById(R.id.tv_detail_experience);
        ImageView ivBanner = view.findViewById(R.id.iv_detail_banner);
        ImageView btnBack = view.findViewById(R.id.btn_back_detail);
        MaterialButton btnProceed = view.findViewById(R.id.btn_proceed_schedule);

        tvName.setText(serviceName);
        tvPrice.setText(servicePrice);
        tvBottomPrice.setText(servicePrice);
        tvRating.setText(serviceRating);
        if (tvExperience != null) {
            tvExperience.setText(serviceExperience + " Years Experience");
        }

        // Load image dynamically using Glide
        if (serviceImageUrl != null && !serviceImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(serviceImageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivBanner);
        }

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        btnProceed.setOnClickListener(v -> {
            SchedulingFragment schedulingFragment = SchedulingFragment.newInstance(serviceName, servicePrice, bestProviderName, bestProviderRating, bestProviderImage);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, schedulingFragment)
                    .addToBackStack(null)
                    .commit();
        });

        displayBestProvider(view);

        return view;
    }

    private void displayBestProvider(View view) {
        String json = loadJSONFromAsset();
        if (json == null) return;

        Gson gson = new Gson();
        Type listType = new TypeToken<List<ProviderItem>>() {}.getType();
        List<ProviderItem> allProviders = gson.fromJson(json, listType);

        if (allProviders == null) return;

        List<ProviderItem> filteredProviders = new ArrayList<>();
        for (ProviderItem p : allProviders) {
            if (p.getCategory() != null && p.getCategory().equalsIgnoreCase(serviceCategory)) {
                filteredProviders.add(p);
            }
        }

        LinearLayout container = view.findViewById(R.id.ll_providers_container);
        if (filteredProviders.isEmpty()) {
            container.setVisibility(View.GONE);
            return;
        }

        BestProviderSelection.sortProvidersByBestMatch(filteredProviders);
        
        container.removeAllViews();
        List<View> itemViews = new ArrayList<>();

        int limit = Math.min(filteredProviders.size(), 5);
        for (int i = 0; i < limit; i++) {
            ProviderItem provider = filteredProviders.get(i);
            boolean isBestMatch = (i == 0);

            View itemView = getLayoutInflater().inflate(R.layout.item_provider_selectable, container, false);
            
            TextView tvProvName = itemView.findViewById(R.id.tv_provider_name);
            TextView tvProvExp = itemView.findViewById(R.id.tv_provider_experience);
            TextView tvProvRating = itemView.findViewById(R.id.tv_provider_rating);
            TextView tvBadge = itemView.findViewById(R.id.tv_best_match_badge);
            RadioButton rbSelect = itemView.findViewById(R.id.rb_provider_select);

            tvProvName.setText(provider.getName());
            tvProvExp.setText(provider.getExperience() + " Years Exp.");
            tvProvRating.setText(provider.getRating());
            
            if (isBestMatch) {
                tvBadge.setVisibility(View.VISIBLE);
                // Default select the best match
                rbSelect.setChecked(true);
                bestProviderName = provider.getName();
                bestProviderRating = provider.getRating();
                bestProviderImage = provider.getImageUrl();
            } else {
                tvBadge.setVisibility(View.GONE);
                rbSelect.setChecked(false);
            }

            itemView.setOnClickListener(v -> {
                // Uncheck all others
                for (View otherView : itemViews) {
                    ((RadioButton) otherView.findViewById(R.id.rb_provider_select)).setChecked(false);
                }
                rbSelect.setChecked(true);
                
                // Update selected provider data
                bestProviderName = provider.getName();
                bestProviderRating = provider.getRating();
                bestProviderImage = provider.getImageUrl();
            });

            itemViews.add(itemView);
            container.addView(itemView);
        }
    }

    private String loadJSONFromAsset() {
        String json;
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
}
