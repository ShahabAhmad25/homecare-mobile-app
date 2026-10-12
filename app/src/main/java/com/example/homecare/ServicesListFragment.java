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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ServicesListFragment extends Fragment {

    private String searchQuery;
    private FirebaseFirestore db;
    private static final String TAG = "ServicesListFragment";

    public static ServicesListFragment newInstance(String query) {
        ServicesListFragment fragment = new ServicesListFragment();
        Bundle args = new Bundle();
        args.putString("search_query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchQuery = getArguments().getString("search_query");
        }
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services_list, container, false);

        TextView tvTitle = view.findViewById(R.id.tv_category_title);
        ImageView btnBack = view.findViewById(R.id.btn_back);
        GridLayout gridLayout = view.findViewById(R.id.services_grid);

        if (searchQuery != null) {
            tvTitle.setText(searchQuery);
        }
        
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        loadServicesFromFirestore(inflater, gridLayout);

        return view;
    }

    private void loadServicesFromFirestore(LayoutInflater inflater, GridLayout gridLayout) {
        if (searchQuery == null) {
            Log.e(TAG, "Search query is null");
            return;
        }

        String lowerQuery = searchQuery.toLowerCase().trim();
        boolean isAll = lowerQuery.isEmpty() || lowerQuery.equals("all") || lowerQuery.equals("all services");

        Log.d(TAG, "Loading services for: " + lowerQuery + " (isAll=" + isAll + ")");

        db.collection("services")
                .get()
                .addOnCompleteListener(task -> {
                    if (!isAdded()) return;

                    if (task.isSuccessful() && task.getResult() != null) {
                        int totalDocs = task.getResult().size();
                        Log.d(TAG, "Successfully fetched " + totalDocs + " documents");
                        
                        gridLayout.removeAllViews();
                        int matchCount = 0;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                ServiceItem service = document.toObject(ServiceItem.class);
                                if (service == null) {
                                    Log.w(TAG, "Failed to map document " + document.getId());
                                    continue;
                                }

                                String category = (service.getCategory() != null) ? service.getCategory().toLowerCase() : "";
                                String name = (service.getName() != null) ? service.getName().toLowerCase() : "";

                                Log.d(TAG, "Checking: name=" + name + ", category=" + category);

                                if (isAll || category.contains(lowerQuery) || name.contains(lowerQuery)) {
                                    addServiceItem(inflater, gridLayout, service);
                                    matchCount++;
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing document " + document.getId(), e);
                            }
                        }

                        Log.d(TAG, "Displayed " + matchCount + " matches");
                        if (matchCount == 0) {
                            String message = (totalDocs == 0) ? "Collection 'services' is empty." : "No matches found for '" + searchQuery + "'.";
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                        Toast.makeText(getContext(), "Failed to load services: " + 
                                (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void addServiceItem(LayoutInflater inflater, GridLayout container, ServiceItem service) {
        View itemView = inflater.inflate(R.layout.item_service_row, container, false);
        
        ImageView ivBanner = itemView.findViewById(R.id.iv_service_banner);
        TextView tvName = itemView.findViewById(R.id.tv_service_name);
        TextView tvPrice = itemView.findViewById(R.id.tv_service_price);
        TextView tvRating = itemView.findViewById(R.id.tv_service_rating);
        MaterialButton btnBookNow = itemView.findViewById(R.id.btn_book_now);

        tvName.setText(service.getName());
        tvPrice.setText(service.getPrice());
        tvRating.setText(service.getRatingString());

        if (service.getImageUrl() != null && !service.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(service.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(ivBanner);
        }

        btnBookNow.setOnClickListener(v -> {
            // Open Service Detail with the recommended provider logic integrated
            ServiceDetailFragment detailFragment = ServiceDetailFragment.newInstance(
                    service.getName(),
                    service.getPrice(),
                    service.getRatingString(),
                    service.getImageUrl(),
                    5, // Default experience if not in ServiceItem
                    service.getCategory()
            );
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        container.addView(itemView);
    }
}
