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
import com.example.foodapp.admin.model.OrderedItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
        // Lấy Order hiện tại
        Order currentOrder = orders.get(position);

        holder.customerName.setText(currentOrder.getClient().getFullName());

        // Hiển thị danh sách món OrderedItem
        StringBuilder orderedItemsBuilder = new StringBuilder();
        List<OrderedItem> orderedItems = currentOrder.getListOrderedItem();
        for (OrderedItem item : orderedItems) {
            orderedItemsBuilder.append("• ")
                    .append(item.getItem().getItemName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append("\n");
        }
        holder.orderedItems.setText(orderedItemsBuilder.toString().trim());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(currentOrder.getOrderDate());
        holder.textDateOrdered.setText(formattedDate);

        // Xử lý sự kiện Accept Button
        holder.acceptButton.setOnClickListener(v -> {
            currentOrder.setOrderStatus("Accepted");
            removeOrder(position);
            Toast.makeText(context, "Order accepted for " + currentOrder.getClient().getFullName(), Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện Cancel Button
        holder.cancelButton.setOnClickListener(v -> {
            currentOrder.setOrderStatus("Cancelled");
            removeOrder(position); // Xóa Order khỏi danh sách
            Toast.makeText(context, "Order cancelled for " + currentOrder.getClient().getFullName(), Toast.LENGTH_SHORT).show();
        });
    }


    //xoa order khi chuyen dang
    private void removeOrder(int position) {
        if (position >= 0 && position < orders.size()) {
            orders.remove(position); // Xóa Order khỏi danh sách
            notifyItemRemoved(position); // Thông báo RecyclerView cập nhật
            notifyItemRangeChanged(position, orders.size()); // Cập nhật lại các vị trí
        }
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
        private TextView customerName, orderedItems, textDateOrdered;
        private Button acceptButton, cancelButton;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            orderedItems = itemView.findViewById(R.id.orderedItems);
            textDateOrdered = itemView.findViewById(R.id.textDateOrdered); // TextView hiển thị ngày
            acceptButton = itemView.findViewById(R.id.acceptButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            parent = itemView.findViewById(R.id.parent); // Nếu bạn có `CardView` trong layout của bạn
        }
    }

}
