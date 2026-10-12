package com.example.homecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class PublicProviderProfileFragment extends Fragment {

    private String providerName;
    private String providerRating;
    private String providerImage;

    public static PublicProviderProfileFragment newInstance(String name, String rating, String imageUrl) {
        PublicProviderProfileFragment fragment = new PublicProviderProfileFragment();
        Bundle args = new Bundle();
        args.putString("provider_name", name);
        args.putString("provider_rating", rating);
        args.putString("provider_image", imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            providerName = getArguments().getString("provider_name");
            providerRating = getArguments().getString("provider_rating");
            providerImage = getArguments().getString("provider_image");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_provider_profile, container, false);

        ImageView btnBack = view.findViewById(R.id.btn_back_public_profile);
        ImageView ivProfile = view.findViewById(R.id.iv_public_provider_profile);
        TextView tvName = view.findViewById(R.id.tv_public_provider_name);
        TextView tvRating = view.findViewById(R.id.tv_public_provider_rating);
        MaterialButton btnMessage = view.findViewById(R.id.btn_message_provider);
        MaterialButton btnCall = view.findViewById(R.id.btn_call_provider);

        tvName.setText(providerName);
        tvRating.setText(providerRating);

        if (providerImage != null && !providerImage.isEmpty()) {
            Glide.with(this)
                    .load(providerImage)
                    .placeholder(R.drawable.user_profile)
                    .into(ivProfile);
        }

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
        btnMessage.setOnClickListener(v -> Toast.makeText(getContext(), "Chat feature coming soon!", Toast.LENGTH_SHORT).show());
        btnCall.setOnClickListener(v -> Toast.makeText(getContext(), "Calling " + providerName + "...", Toast.LENGTH_SHORT).show());

        return view;
    }
}
