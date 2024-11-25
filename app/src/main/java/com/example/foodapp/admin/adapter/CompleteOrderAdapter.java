package com.example.foodapp.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.admin.model.Order;
import com.example.foodapp.admin.model.OrderedItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CompleteOrderAdapter extends RecyclerView.Adapter<CompleteOrderAdapter.OrderViewHolder> {

    private List<Order> orders;

    public CompleteOrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completeorder_list_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Set client name
        if (order.getClient() != null) {
            holder.customerName.setText(order.getClient().getFullName());
        } else {
            holder.customerName.setText("Unknown Client");
        }

        // Display ordered items
        if (order.getListOrderedItem() != null) {
            StringBuilder orderedItemsText = new StringBuilder();
            for (OrderedItem orderedItem : order.getListOrderedItem()) {
                orderedItemsText.append("• ")
                        .append(orderedItem.getItem().getItemName())
                        .append(" x")
                        .append(orderedItem.getQuantity())
                        .append("\n");
            }
            holder.orderedItems.setText(orderedItemsText.toString().trim());
        } else {
            holder.orderedItems.setText("No items ordered");
        }

        // Set date ordered
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        if (order.getOrderDate() != null) {
            holder.dateOrdered.setText(dateFormat.format(order.getOrderDate()));
        } else {
            holder.dateOrdered.setText("Unknown Date");
        }

        // Set order status
        if (order.getOrderStatus() != null) {
            holder.orderStatus.setText(order.getOrderStatus());
        } else {
            holder.orderStatus.setText("Status Unknown");
        }

    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, orderedItems, dateOrdered, orderStatus;
        ImageView customerImage, icDone;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            orderedItems = itemView.findViewById(R.id.orderedItems);
            dateOrdered = itemView.findViewById(R.id.textDateOrdered);
            orderStatus = itemView.findViewById(R.id.textView10);
            customerImage = itemView.findViewById(R.id.customerImage);
            icDone = itemView.findViewById(R.id.ic_done);
        }
    }
}
