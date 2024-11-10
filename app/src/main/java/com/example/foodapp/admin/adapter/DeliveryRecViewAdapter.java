package com.example.foodapp.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.admin.model.Order;

import java.util.ArrayList;

public class DeliveryRecViewAdapter extends RecyclerView.Adapter<DeliveryRecViewAdapter.ViewHolder> {

    private ArrayList<Order> orders = new ArrayList<>();
    private Context context;

    public DeliveryRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DeliveryRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_list_item, parent, false);
        return new DeliveryRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryRecViewAdapter.ViewHolder holder, int position) {
        holder.txtCustomerName.setText(orders.get(position).getCustomerName());
        holder.txtDeliveryStatus.setText(orders.get(position).getOrderStatus());
        holder.txtPaymentType.setText(orders.get(position).getPaymentType());
        holder.txtDelivered.setText(orders.get(position).getQuantity() > 0 ? "Delivered" : "Not Delivered");

        holder.parent.setOnClickListener(view ->
                Toast.makeText(context, orders.get(position).getCustomerName() + " Selected", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCustomerName, txtPaymentType, txtDeliveryStatus, txtDelivered;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerName = itemView.findViewById(R.id.customerName);
            txtPaymentType = itemView.findViewById(R.id.paymentType);
            txtDeliveryStatus = itemView.findViewById(R.id.deliveryStatus);
            txtDelivered = itemView.findViewById(R.id.delivered);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
