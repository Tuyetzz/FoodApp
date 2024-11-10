package com.example.foodapp.admin.adapter;

import android.annotation.SuppressLint;
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
import com.example.foodapp.admin.model.Item;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FoodItemRecViewAdapter extends RecyclerView.Adapter<FoodItemRecViewAdapter.ViewHolder> {

    private ArrayList<Item> items = new ArrayList<>();
    private Context context;
    private FirebaseFirestore db; // Firebase Firestore instance

    public FoodItemRecViewAdapter(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance(); // Initialize Firestore instance
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Item currentItem = items.get(position);

        // Set item name, subtitle, and price
        holder.txtItemName.setText(currentItem.getItemName());
        holder.txtItemSubtitle.setText(currentItem.getShortDescription());
        holder.txtItemPrice.setText("$" + Double.toString(currentItem.getItemPrice()));

        // Load image using Glide (or any other library you prefer)
        Glide.with(context)
                .load(currentItem.getItemImage())
                .into(holder.image);

        // Handle click event on each item
        holder.parent.setOnClickListener(view ->
                Toast.makeText(context, currentItem.getItemName() + " Selected", Toast.LENGTH_SHORT).show()
        );

        // Handle delete button click
        holder.deleteButton.setOnClickListener(view -> {
            // Delete from Firebase Firestore
            db.collection("items").document(currentItem.getItemId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Remove the item from the list and notify the adapter
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, items.size()); // Update the list
                        Toast.makeText(context, currentItem.getItemName() + " deleted!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to delete " + currentItem.getItemName() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Set new data for the adapter
    public void setItems(ArrayList<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItemName, txtItemSubtitle, txtItemPrice;
        private ImageView image;
        private CardView parent;
        private Button deleteButton; // Reference for delete button

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.itemName);
            txtItemSubtitle = itemView.findViewById(R.id.itemSubtitle);
            txtItemPrice = itemView.findViewById(R.id.itemPrice);
            image = itemView.findViewById(R.id.itemImage);
            parent = itemView.findViewById(R.id.parent);
            deleteButton = itemView.findViewById(R.id.delete); // Find delete button
        }
    }
}
