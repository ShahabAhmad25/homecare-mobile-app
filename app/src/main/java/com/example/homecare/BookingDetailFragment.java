package com.example.homecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BookingDetailFragment extends Fragment {

    private String bookingId;

    public static BookingDetailFragment newInstance(String bookingId) {
        BookingDetailFragment fragment = new BookingDetailFragment();
        Bundle args = new Bundle();
        args.putString("booking_id", bookingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookingId = getArguments().getString("booking_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_detail, container, false);

        ImageView btnBack = view.findViewById(R.id.btn_back_booking_detail);
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Load booking data
        Booking booking = null;
        for (Booking b : BookingRepository.getInstance().getBookings()) {
            if (b.getId().equals(bookingId)) {
                booking = b;
                break;
            }
        }

        if (booking != null) {
            ((TextView) view.findViewById(R.id.tv_detail_booking_id)).setText("ORDER #" + booking.getId());
            ((TextView) view.findViewById(R.id.tv_detail_service_name)).setText(booking.getServiceName());
            ((TextView) view.findViewById(R.id.tv_detail_status)).setText(booking.getStatus());
            ((TextView) view.findViewById(R.id.tv_detail_date)).setText(booking.getDate());
            ((TextView) view.findViewById(R.id.tv_detail_time)).setText(booking.getTime());
            ((TextView) view.findViewById(R.id.tv_detail_address)).setText(booking.getAddress());
            ((TextView) view.findViewById(R.id.tv_detail_provider_name)).setText(booking.getProviderName() != null ? booking.getProviderName() : "No provider assigned");
            ((TextView) view.findViewById(R.id.tv_detail_service_price)).setText(booking.getServicePrice());
            ((TextView) view.findViewById(R.id.tv_detail_expected_price)).setText(booking.getExpectedPrice());
            ((TextView) view.findViewById(R.id.tv_detail_payment)).setText(booking.getPaymentMethod());
        }

        return view;
    }
}