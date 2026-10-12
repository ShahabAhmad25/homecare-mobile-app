package com.example.homecare;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import java.util.Calendar;

public class SchedulingFragment extends Fragment {

    private String serviceName;
    private String servicePrice;
    private String providerName;
    private String providerRating;
    private String providerImage;
    private String selectedDate;
    private String selectedTime;

    public static SchedulingFragment newInstance(String serviceName, String servicePrice, String providerName, String providerRating, String providerImage) {
        SchedulingFragment fragment = new SchedulingFragment();
        Bundle args = new Bundle();
        args.putString("service_name", serviceName);
        args.putString("service_price", servicePrice);
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
            providerName = getArguments().getString("provider_name");
            providerRating = getArguments().getString("provider_rating");
            providerImage = getArguments().getString("provider_image");
        }
        
        // Set default date to today
        Calendar calendar = Calendar.getInstance();
        selectedDate = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduling, container, false);

        ImageView btnBack = view.findViewById(R.id.btn_back_schedule);
        MaterialButton btnConfirm = view.findViewById(R.id.btn_confirm_schedule);
        CalendarView calendarView = view.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year);

        setupTimeSlots(view);

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        btnConfirm.setOnClickListener(v -> {
            if (selectedTime == null) {
                Toast.makeText(getContext(), "Please select a time slot", Toast.LENGTH_SHORT).show();
                return;
            }
            // Pass all collected data to the next fragment
            AddressSelectionFragment addressFragment = AddressSelectionFragment.newInstance(
                    serviceName, servicePrice, selectedDate, selectedTime, providerName, providerRating, providerImage
            );
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addressFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void setupTimeSlots(View view) {
        int[] buttonIds = {R.id.slot1, R.id.slot2, R.id.slot3, R.id.slot4, R.id.slot5};
        
        View.OnClickListener timeSlotListener = v -> {
            MaterialButton clickedBtn = (MaterialButton) v;
            selectedTime = clickedBtn.getText().toString();
            
            // Reset colors for all buttons and highlight the selected one
            for (int id : buttonIds) {
                MaterialButton btn = view.findViewById(id);
                if (btn == null) continue;

                if (btn == clickedBtn) {
                    // Selected: Dark background, White text
                    btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.navy_blue)));
                    btn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                    btn.setStrokeWidth(0);
                } else {
                    // Unselected: White background, Black text, Grey outline
                    btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), android.R.color.white)));
                    btn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
                    btn.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), android.R.color.darker_gray)));
                    btn.setStrokeWidth(2);
                }
            }
        };

        for (int id : buttonIds) {
            View btn = view.findViewById(id);
            if (btn != null) {
                btn.setOnClickListener(timeSlotListener);
            }
        }
    }
}