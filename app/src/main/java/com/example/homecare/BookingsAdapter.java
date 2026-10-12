package com.example.homecare;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private List<Booking> bookings;

    public BookingsAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.tvServiceName.setText(booking.getServiceName());
        holder.tvDateTime.setText(booking.getDate() + " • " + booking.getTime());
        holder.tvPrice.setText(booking.getServicePrice());
        holder.tvStatus.setText(booking.getStatus());

        // Status coloring
        if ("PENDING".equals(booking.getStatus())) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_orange_dark));
            // Show provider info if available even for pending bookings
            if (booking.getProviderName() != null) {
                holder.layoutProviderInfo.setVisibility(View.VISIBLE);
                holder.tvProviderName.setText(booking.getProviderName());
                holder.tvProviderRating.setText(booking.getProviderRating());

                if (booking.getProviderImage() != null && !booking.getProviderImage().isEmpty()) {
                    Glide.with(holder.itemView.getContext())
                            .load(booking.getProviderImage())
                            .placeholder(R.drawable.user_profile)
                            .into(holder.ivProviderImage);
                }
            } else {
                holder.layoutProviderInfo.setVisibility(View.GONE);
            }
        } else if ("ACCEPTED".equals(booking.getStatus()) || "COMPLETED".equals(booking.getStatus())) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.emerald_green));
            
            // Show provider info if booking is accepted or completed
            if (booking.getProviderName() != null) {
                holder.layoutProviderInfo.setVisibility(View.VISIBLE);
                holder.tvProviderName.setText(booking.getProviderName());
                holder.tvProviderRating.setText(booking.getProviderRating());
                
                if (booking.getProviderImage() != null && !booking.getProviderImage().isEmpty()) {
                    Glide.with(holder.itemView.getContext())
                            .load(booking.getProviderImage())
                            .placeholder(R.drawable.user_profile)
                            .into(holder.ivProviderImage);
                }

                holder.btnViewProfile.setOnClickListener(v -> {
                    PublicProviderProfileFragment fragment = PublicProviderProfileFragment.newInstance(
                            booking.getProviderName(),
                            booking.getProviderRating(),
                            booking.getProviderImage()
                    );
                    
                    if (v.getContext() instanceof AppCompatActivity) {
                        ((AppCompatActivity) v.getContext()).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            } else {
                holder.layoutProviderInfo.setVisibility(View.GONE);
            }
        } else if ("DECLINED".equals(booking.getStatus())) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
            holder.layoutProviderInfo.setVisibility(View.GONE);
        }

        if ("COMPLETED".equals(booking.getStatus())) {
            holder.btnAction.setText("Download Invoice");
        } else {
            holder.btnAction.setText("View Details");
        }

        holder.btnAction.setOnClickListener(v -> {
            // Navigate to Detail Fragment for any status that shows "View Details"
            if ("View Details".equals(holder.btnAction.getText().toString())) {
                BookingDetailFragment fragment = BookingDetailFragment.newInstance(booking.getId());
                
                // Safely find the AppCompatActivity from context (handles ContextWrappers)
                AppCompatActivity activity = null;
                Context context = v.getContext();
                while (context instanceof ContextWrapper) {
                    if (context instanceof AppCompatActivity) {
                        activity = (AppCompatActivity) context;
                        break;
                    }
                    context = ((ContextWrapper) context).getBaseContext();
                }

                if (activity != null) {
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        // Delete/Cancel button logic
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // 1. Get the booking to remove
                Booking bookingToRemove = bookings.get(currentPos);
                
                // 2. Remove from the central Repository
                BookingRepository.getInstance().removeBooking(bookingToRemove.getId());
                
                // 3. IMPORTANT: Remove from the local list held by this adapter
                bookings.remove(currentPos);
                
                // 4. Notify the adapter about the removal
                notifyItemRemoved(currentPos);
                
                // 5. Notify about range change to refresh positions for remaining items
                notifyItemRangeChanged(currentPos, bookings.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvStatus, tvDateTime, tvPrice;
        MaterialButton btnAction;
        ImageView btnDelete;
        
        // Provider info views
        LinearLayout layoutProviderInfo;
        ImageView ivProviderImage;
        TextView tvProviderName, tvProviderRating, btnViewProfile;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tv_booking_service_name);
            tvStatus = itemView.findViewById(R.id.tv_booking_status);
            tvDateTime = itemView.findViewById(R.id.tv_booking_date_time);
            tvPrice = itemView.findViewById(R.id.tv_booking_price_value);
            btnAction = itemView.findViewById(R.id.btn_booking_action);
            btnDelete = itemView.findViewById(R.id.btn_delete_booking);
            
            layoutProviderInfo = itemView.findViewById(R.id.layout_provider_info);
            ivProviderImage = itemView.findViewById(R.id.iv_assigned_provider);
            tvProviderName = itemView.findViewById(R.id.tv_assigned_provider_name);
            tvProviderRating = itemView.findViewById(R.id.tv_provider_rating);
            btnViewProfile = itemView.findViewById(R.id.btn_view_provider_profile);
        }
    }
}
