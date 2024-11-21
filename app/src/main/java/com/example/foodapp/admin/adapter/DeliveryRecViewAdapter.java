package com.example.foodapp.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        // Lấy thông tin đơn hàng hiện tại
        Order currentOrder = orders.get(position);

        // Hiển thị tên khách hàng và loại thanh toán
        holder.txtCustomerName.setText(currentOrder.getClient().getFullName());
        holder.txtPaymentType.setText(currentOrder.getPaymentType());

        // Kiểm tra trạng thái đơn hàng và cập nhật giao diện
        if (currentOrder.getOrderStatus().equalsIgnoreCase("Not Delivered")) {
            // Trạng thái "Not Delivered"
            holder.txtDelivered.setText("Not Delivered");
            holder.txtDeliveryStatus.setText("Not Recieved");
            holder.txtDeliveryStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.txtDelivered.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.statusButton.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.statusButton.setText("");
        } else if (currentOrder.getOrderStatus().equalsIgnoreCase("Delivered")) {
            // Trạng thái "Delivered"
            holder.txtDelivered.setText("Delivered");
            holder.txtDeliveryStatus.setText("Recieved");
            holder.txtDeliveryStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.txtDelivered.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.statusButton.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.statusButton.setText("Complete"); // Text "Complete"
        }

        // Xử lý sự kiện khi nhấn nút "statusButton"
        holder.statusButton.setOnClickListener(v -> {
            if (currentOrder.getOrderStatus().equalsIgnoreCase("Not Delivered")) {
                // Chuyển trạng thái từ "Not Delivered" sang "Delivered"
                currentOrder.setOrderStatus("Delivered");
                Toast.makeText(context, "Marked as Delivered for " + currentOrder.getClient().getFullName(), Toast.LENGTH_SHORT).show();
                notifyItemChanged(position); // Làm mới item
            } else if (currentOrder.getOrderStatus().equalsIgnoreCase("Delivered")) {
                // Chuyển trạng thái từ "Delivered" sang "Completed"
                currentOrder.setOrderStatus("Completed");
                Toast.makeText(context, "Marked as Completed for " + currentOrder.getClient().getFullName(), Toast.LENGTH_SHORT).show();
                removeOrder(position); // Xóa item nếu đã "Completed"
            }
        });

        // Sự kiện khi bấm vào toàn bộ CardView
        holder.parent.setOnClickListener(v -> {
            Toast.makeText(context, currentOrder.getClient().getFullName() + " Selected", Toast.LENGTH_SHORT).show();
        });
    }
    private void removeOrder(int position) {
        if (position >= 0 && position < orders.size()) {
            orders.remove(position); // Xóa đơn hàng khỏi danh sách
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
        private TextView txtCustomerName, txtPaymentType, txtDelivered, txtDeliveryStatus;
        private Button statusButton; // Nút để thay đổi trạng thái
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerName = itemView.findViewById(R.id.customerName);
            txtDeliveryStatus = itemView.findViewById(R.id.deliveryStatus);
            txtPaymentType = itemView.findViewById(R.id.paymentType);
            txtDelivered = itemView.findViewById(R.id.delivered);
            statusButton = itemView.findViewById(R.id.statusButton);
            parent = itemView.findViewById(R.id.parent);
        }
    }

}
