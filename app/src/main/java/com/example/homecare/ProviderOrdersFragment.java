package com.example.homecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProviderOrdersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_orders, container, false);
        
        ImageView btnBack = view.findViewById(R.id.btn_back_orders);
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            } else if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        RecyclerView rvOrders = view.findViewById(R.id.rv_provider_orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Use the shared repository to get real-time bookings
        List<Booking> orderList = BookingRepository.getInstance().getBookings();
        
        ProviderOrdersAdapter adapter = new ProviderOrdersAdapter(orderList);
        rvOrders.setAdapter(adapter);
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list when returning to this fragment
        View view = getView();
        if (view != null) {
            RecyclerView rvOrders = view.findViewById(R.id.rv_provider_orders);
            if (rvOrders != null && rvOrders.getAdapter() != null) {
                rvOrders.getAdapter().notifyDataSetChanged();
            }
        }
    }
}