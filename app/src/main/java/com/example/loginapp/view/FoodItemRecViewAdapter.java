package com.example.loginapp.view;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.example.loginapp.R;
import com.example.loginapp.model.Item;

import java.util.ArrayList;

public class FoodItemRecViewAdapter extends RecyclerView.Adapter<FoodItemRecViewAdapter.ViewHolder> {

    private ArrayList<Item> items = new ArrayList<>();
    private Context context;

    public FoodItemRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Set item name, subtitle, and price
        holder.txtItemName.setText(items.get(position).getItemName());
        holder.txtItemSubtitle.setText(items.get(position).getShortDescription());
        holder.txtItemPrice.setText("$" + Double.toString(items.get(position).getItemPrice()));

        // Load image using Glide (or any other library you prefer)
        Glide.with(context)
                .load(items.get(position).getItemImage())  // URL or resource ID of the image
                .into(holder.image);


        // Handle click event on each item
        holder.parent.setOnClickListener(view ->
                Toast.makeText(context, items.get(position).getItemName() + " Selected", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Set new data for the adapter
    public void setItems(ArrayList<Item> items) {
        this.items = items;
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItemName, txtItemSubtitle, txtItemPrice;
        private ImageView image;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.itemName); // Find views by ID
            txtItemSubtitle = itemView.findViewById(R.id.itemSubtitle);
            txtItemPrice = itemView.findViewById(R.id.itemPrice);
            image = itemView.findViewById(R.id.itemImage);
            parent = itemView.findViewById(R.id.parent); // Assuming you have a parent CardView in your layout
        }
    }
}
