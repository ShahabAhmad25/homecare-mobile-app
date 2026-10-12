package com.example.homecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.button.MaterialButton;
import java.util.UUID;

public class BookingSummaryFragment extends Fragment {

    private String serviceName;
    private String servicePrice;
    private String selectedDate;
    private String selectedTime;
    private String selectedAddress;
    private String addressLabel;
    private String providerName;
    private String providerRating;
    private String providerImage;

    public static BookingSummaryFragment newInstance(String serviceName, String servicePrice, String selectedDate, String selectedTime, String selectedAddress, String addressLabel, String providerName, String providerRating, String providerImage) {
        BookingSummaryFragment fragment = new BookingSummaryFragment();
        Bundle args = new Bundle();
        args.putString("service_name", serviceName);
        args.putString("service_price", servicePrice);
        args.putString("selected_date", selectedDate);
        args.putString("selected_time", selectedTime);
        args.putString("selected_address", selectedAddress);
        args.putString("address_label", addressLabel);
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
            selectedAddress = getArguments().getString("selected_address");
            addressLabel = getArguments().getString("address_label");
            providerName = getArguments().getString("provider_name");
            providerRating = getArguments().getString("provider_rating");
            providerImage = getArguments().getString("provider_image");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_summary, container, false);

        TextView tvName = view.findViewById(R.id.tv_summary_service_name);
        TextView tvProviderName = view.findViewById(R.id.tv_summary_provider_name);
        TextView tvSchedule = view.findViewById(R.id.tv_summary_schedule);
        TextView tvAddressLabel = view.findViewById(R.id.tv_summary_address_label);
        TextView tvAddressValue = view.findViewById(R.id.tv_summary_address_value);
        TextView tvFee = view.findViewById(R.id.tv_summary_fee);
        EditText etExpectedPrice = view.findViewById(R.id.et_expected_price);
        
        RadioButton rbCash = view.findViewById(R.id.rb_cash);
        RadioButton rbCard = view.findViewById(R.id.rb_card);
        
        ImageView btnBack = view.findViewById(R.id.btn_back_summary);
        MaterialButton btnConfirm = view.findViewById(R.id.btn_confirm_booking);

        tvName.setText(serviceName);
        if (tvProviderName != null) {
            tvProviderName.setText("Provider: " + providerName);
        }
        tvSchedule.setText(selectedDate + " • " + selectedTime);
        tvAddressLabel.setText(addressLabel);
        tvAddressValue.setText(selectedAddress);
        tvFee.setText(servicePrice);

        // Manual mutual exclusion for RadioButtons inside CardViews
        rbCash.setOnClickListener(v -> {
            rbCash.setChecked(true);
            rbCard.setChecked(false);
        });

        rbCard.setOnClickListener(v -> {
            rbCard.setChecked(true);
            rbCash.setChecked(false);
        });

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        btnConfirm.setOnClickListener(v -> {
            String expectedPrice = etExpectedPrice.getText().toString().trim();
            if (expectedPrice.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your expected price", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = rbCard.isChecked() ? "Credit / Debit Card" : "Cash After Service";

            // Create a new Booking object with PENDING status
            String bookingId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Booking newBooking = new Booking(
                    bookingId,
                    serviceName,
                    servicePrice,
                    expectedPrice,
                    paymentMethod,
                    selectedDate,
                    selectedTime,
                    selectedAddress,
                    "PENDING",
                    providerName,
                    providerRating,
                    providerImage
            );

            // Save to repository
            BookingRepository.getInstance().addBooking(newBooking);

            Toast.makeText(getContext(), "Request Sent to Provider!", Toast.LENGTH_LONG).show();
            
            // Go back to the main screen (Home/Bookings)
            getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });

        return view;
    }
}