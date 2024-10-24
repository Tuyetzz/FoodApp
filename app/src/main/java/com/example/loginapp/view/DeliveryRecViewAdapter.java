package com.example.loginapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.model.Delivery;

import java.util.ArrayList;

public class DeliveryRecViewAdapter extends RecyclerView.Adapter<DeliveryRecViewAdapter.ViewHolder> {

    private ArrayList<Delivery> deliveries = new ArrayList<>();
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
        holder.txtCustomerName.setText(deliveries.get(position).getCustomerName());
        holder.txtDeliveryStatus.setText(deliveries.get(position).getDeliveryStatus());
        holder.txtPaymentType.setText(deliveries.get(position).getPaymentType());
        holder.txtDelivered.setText(deliveries.get(position).getDelivered());

        holder.parent.setOnClickListener(view ->
                Toast.makeText(context, deliveries.get(position).getCustomerName() + " Selected", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    public void setDeliveries(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCustomerName, txtPaymentType, txtDeliveryStatus, txtDelivered;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerName = itemView.findViewById(R.id.customerName); // Find views by ID
            txtPaymentType = itemView.findViewById(R.id.paymentType);
            txtDeliveryStatus = itemView.findViewById(R.id.deliveryStatus);
            txtDelivered = itemView.findViewById(R.id.delivered);
            parent = itemView.findViewById(R.id.parent); // Assuming you have a parent CardView in your layout
        }
    }
}
