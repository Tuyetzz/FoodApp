package com.example.foodapp.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.admin.model.Order;

import java.util.ArrayList;

public class OrderRecViewAdapter extends RecyclerView.Adapter<OrderRecViewAdapter.ViewHolder> {

    private ArrayList<Order> orders = new ArrayList<>();
    private Context context;

    public OrderRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set data to views in order_list_item
        Order currentOrder = orders.get(position);

        holder.customerName.setText(currentOrder.getCustomerName());
        holder.quantityValue.setText(String.valueOf(currentOrder.getQuantity()));

        // Assuming you have a method to load image, you can use libraries like Glide or Picasso
         Glide.with(context).load(currentOrder.getImageUrl()).into(holder.customerImage);

        holder.acceptButton.setOnClickListener(v ->
                Toast.makeText(context, "Order accepted for " + currentOrder.getCustomerName(), Toast.LENGTH_SHORT).show()
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
        private ImageView customerImage;
        private TextView customerName, quantityValue;
        private Button acceptButton;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerImage = itemView.findViewById(R.id.customerImage);
            customerName = itemView.findViewById(R.id.customerName);
            quantityValue = itemView.findViewById(R.id.quantityValue);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            parent = itemView.findViewById(R.id.parent); // Nếu bạn có `CardView` trong layout của bạn
        }
    }
}
