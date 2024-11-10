package com.example.foodapp.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodapp.R;
import com.example.foodapp.admin.model.Discount;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DiscountRecViewAdapter extends RecyclerView.Adapter<DiscountRecViewAdapter.ViewHolder> {

    private List<Discount> discounts;
    private Context context;

    // Constructor
    public DiscountRecViewAdapter(Context context, List<Discount> discounts) {
        this.context = context;
        this.discounts = discounts;
    }

    // ViewHolder class to hold each item view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCode;
        private final TextView textViewPercentage;
        private final TextView textViewExpiryDate;
        private final Button btnDelete;
        private final Button btnExpired;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCode = itemView.findViewById(R.id.textViewCode);
            textViewPercentage = itemView.findViewById(R.id.textViewPercentage);
            textViewExpiryDate = itemView.findViewById(R.id.textViewExpiryDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnExpired = itemView.findViewById(R.id.btnExpired);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Discount discount = discounts.get(position);

        // Set data for each item
        holder.textViewCode.setText(discount.getCode());

        // Format percentage as string with "%" symbol
        String percentageText = String.format("%.1f%%", discount.getPercentage());
        holder.textViewPercentage.setText(percentageText);

        holder.textViewExpiryDate.setText(discount.getExpiryDate());

        // Set up Delete button click listener
        holder.btnDelete.setOnClickListener(v -> {
            discounts.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, discounts.size());
        });

        // Set up Expired button click listener
        holder.btnExpired.setOnClickListener(v -> {
            // Get today's date
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    .format(Calendar.getInstance().getTime());

            // Update the expiry date for the current discount item
            discount.setExpiryDate(currentDate);
            holder.textViewExpiryDate.setText(currentDate);
            notifyItemChanged(position); // Refresh the item view to reflect the updated date
        });
    }

    @Override
    public int getItemCount() {
        return discounts.size();
    }

    // Optional: method to update discounts list and notify adapter
    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
        notifyDataSetChanged();
    }
}
