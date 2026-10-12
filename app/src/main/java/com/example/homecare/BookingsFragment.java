package com.example.homecare;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class BookingsFragment extends Fragment {

    private MaterialButton btnUpcoming, btnCompleted;
    private RecyclerView rvUpcoming, rvCompleted;
    private BookingsAdapter upcomingAdapter, completedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        ImageView btnBack = view.findViewById(R.id.btn_back_bookings);
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            } else if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        btnUpcoming = view.findViewById(R.id.btn_upcoming);
        btnCompleted = view.findViewById(R.id.btn_completed);
        rvUpcoming = view.findViewById(R.id.rv_upcoming);
        rvCompleted = view.findViewById(R.id.rv_completed);

        setupRecyclerViews();

        btnUpcoming.setOnClickListener(v -> showUpcoming());
        btnCompleted.setOnClickListener(v -> showCompleted());

        // Initial state
        showUpcoming();

        return view;
    }

    private void setupRecyclerViews() {
        rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCompleted.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Booking> allBookings = BookingRepository.getInstance().getBookings();
        List<Booking> upcomingList = new ArrayList<>();
        List<Booking> completedList = new ArrayList<>();

        for (Booking b : allBookings) {
            if ("COMPLETED".equals(b.getStatus())) {
                completedList.add(b);
            } else {
                upcomingList.add(b);
            }
        }

        upcomingAdapter = new BookingsAdapter(upcomingList);
        completedAdapter = new BookingsAdapter(completedList);

        rvUpcoming.setAdapter(upcomingAdapter);
        rvCompleted.setAdapter(completedAdapter);
    }

    private void showUpcoming() {
        rvUpcoming.setVisibility(View.VISIBLE);
        rvCompleted.setVisibility(View.GONE);

        int navy = ContextCompat.getColor(requireContext(), R.color.navy_blue);
        int grey = ContextCompat.getColor(requireContext(), R.color.text_grey);
        int strokeColor = ContextCompat.getColor(requireContext(), R.color.light_grey);

        btnUpcoming.setBackgroundTintList(ColorStateList.valueOf(navy));
        btnUpcoming.setTextColor(Color.WHITE);
        btnUpcoming.setStrokeWidth(0);

        btnCompleted.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        btnCompleted.setTextColor(grey);
        btnCompleted.setStrokeWidth(2);
        btnCompleted.setStrokeColor(ColorStateList.valueOf(strokeColor));
    }

    private void showCompleted() {
        rvUpcoming.setVisibility(View.GONE);
        rvCompleted.setVisibility(View.VISIBLE);

        int navy = ContextCompat.getColor(requireContext(), R.color.navy_blue);
        int grey = ContextCompat.getColor(requireContext(), R.color.text_grey);
        int strokeColor = ContextCompat.getColor(requireContext(), R.color.light_grey);

        btnCompleted.setBackgroundTintList(ColorStateList.valueOf(navy));
        btnCompleted.setTextColor(Color.WHITE);
        btnCompleted.setStrokeWidth(0);

        btnUpcoming.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        btnUpcoming.setTextColor(grey);
        btnUpcoming.setStrokeWidth(2);
        btnUpcoming.setStrokeColor(ColorStateList.valueOf(strokeColor));
    }
}
