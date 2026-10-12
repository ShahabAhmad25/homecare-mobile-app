package com.example.homecare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class ProviderOrdersAdapter extends RecyclerView.Adapter<ProviderOrdersAdapter.OrderViewHolder> {

    private List<Booking> orders;

    public ProviderOrdersAdapter(List<Booking> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_provider_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Booking order = orders.get(position);
        holder.tvOrderId.setText(order.getId());
        holder.tvCustomerName.setText("Customer: User"); // In a real app, you'd have customer info
        holder.tvServiceOrdered.setText(order.getServiceName());
        holder.tvOrderDate.setText(order.getDate() + " - " + order.getTime());
        holder.tvOrderPrice.setText(order.getServicePrice());
        holder.tvOrderStatus.setText(order.getStatus());
        
        // Bind new fields
        holder.tvExpectedPrice.setText(order.getExpectedPrice());
        holder.tvPaymentMethod.setText(order.getPaymentMethod());

        // Update status UI
        if ("PENDING".equals(order.getStatus())) {
            holder.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnDecline.setVisibility(View.VISIBLE);
        } else if ("ACCEPTED".equals(order.getStatus())) {
            holder.tvOrderStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.emerald_green));
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnDecline.setVisibility(View.GONE);
        } else if ("DECLINED".equals(order.getStatus())) {
            holder.tvOrderStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnDecline.setVisibility(View.GONE);
        }

        holder.btnAccept.setOnClickListener(v -> {
            order.setStatus("ACCEPTED");
            notifyItemChanged(position);
        });

        holder.btnDecline.setOnClickListener(v -> {
            order.setStatus("DECLINED");
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerName, tvServiceOrdered, tvOrderDate, tvOrderPrice, tvOrderStatus;
        TextView tvExpectedPrice, tvPaymentMethod;
        MaterialButton btnAccept, btnDecline;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvServiceOrdered = itemView.findViewById(R.id.tv_service_ordered);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderPrice = itemView.findViewById(R.id.tv_order_price);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvExpectedPrice = itemView.findViewById(R.id.tv_expected_price);
            tvPaymentMethod = itemView.findViewById(R.id.tv_payment_method);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnDecline = itemView.findViewById(R.id.btn_decline);
        }
    }
}
