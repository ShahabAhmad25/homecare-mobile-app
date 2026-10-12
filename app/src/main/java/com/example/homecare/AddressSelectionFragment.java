package com.example.homecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;

public class AddressSelectionFragment extends Fragment {

    private String serviceName;
    private String servicePrice;
    private String selectedDate;
    private String selectedTime;
    private String providerName;
    private String providerRating;
    private String providerImage;

    public static AddressSelectionFragment newInstance(String serviceName, String servicePrice, String selectedDate, String selectedTime, String providerName, String providerRating, String providerImage) {
        AddressSelectionFragment fragment = new AddressSelectionFragment();
        Bundle args = new Bundle();
        args.putString("service_name", serviceName);
        args.putString("service_price", servicePrice);
        args.putString("selected_date", selectedDate);
        args.putString("selected_time", selectedTime);
        args.putString("provider_name", providerName);
        args.putString("provider_rating", providerRating);
        args.putString("provider_image", providerImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceName = getArguments().getString("service_name");
            servicePrice = getArguments().getString("service_price");
            selectedDate = getArguments().getString("selected_date");
            selectedTime = getArguments().getString("selected_time");
            providerName = getArguments().getString("provider_name");
            providerRating = getArguments().getString("provider_rating");
            providerImage = getArguments().getString("provider_image");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_selection, container, false);

        ImageView btnBack = view.findViewById(R.id.btn_back_address);
        MaterialButton btnProceed = view.findViewById(R.id.btn_proceed_summary);
        EditText etContact = view.findViewById(R.id.et_contact_number);
        
        // In a real app, you'd get these from user selection
        String selectedAddress = "123 Street Name, Sargodha, Punjab";
        String addressLabel = "Home";

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        btnProceed.setOnClickListener(v -> {
            String contactNumber = etContact.getText().toString().trim();
            
            if (contactNumber.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your contact number", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (contactNumber.length() < 10 || contactNumber.length() > 13) {
                Toast.makeText(getContext(), "Wrong format: Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            BookingSummaryFragment summaryFragment = BookingSummaryFragment.newInstance(
                    serviceName, servicePrice, selectedDate, selectedTime, selectedAddress, addressLabel, providerName, providerRating, providerImage
            );
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, summaryFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}