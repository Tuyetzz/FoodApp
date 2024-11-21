package com.example.foodapp.admin.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.admin.adapter.OrderRecViewAdapter;
import com.example.foodapp.admin.model.Order;
import com.example.foodapp.admin.model.OrderedItem;
import com.example.foodapp.admin.model.User;
import com.example.foodapp.admin.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PendingOrder extends AppCompatActivity {

    private FloatingActionButton gobackBtn;
    private RecyclerView orderRecyclerView;
    private OrderRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pending_order);

        // Áp dụng insets để giao diện tương thích với thiết bị không viền
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy thông tin user từ intent
        User user = (User) getIntent().getSerializableExtra("user");

        // Xử lý nút quay lại
        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(view -> navigateBackToMainAdmin(user));

        // Khởi tạo RecyclerView
        setupRecyclerView();

        // Thêm dữ liệu mẫu
        loadSampleOrders(user);
    }

    private void navigateBackToMainAdmin(User user) {
        Intent intent = new Intent(getApplicationContext(), MainAdminActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private void setupRecyclerView() {
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderRecViewAdapter(this);
        orderRecyclerView.setAdapter(adapter);
    }

    private void loadSampleOrders(User manager) {
        ArrayList<Order> allOrders = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Tạo các ngày cụ thể
            Date orderDate1 = dateFormat.parse("2024-11-21");
            Date orderDate2 = dateFormat.parse("2024-11-20");
            Date orderDate3 = dateFormat.parse("2024-11-19");

            // Tạo dữ liệu mẫu cho các món ăn
            Item item1 = new Item("item1", "Burger", 5.99, "https://botocuchigiasi.vn/wp-content/uploads/2022/02/pho-bo.jpg", "Delicious beef burger", new ArrayList<>(List.of("Beef", "Bread", "Lettuce")));
            Item item2 = new Item("item2", "Pizza", 8.99, "https://botocuchigiasi.vn/wp-content/uploads/2022/02/pho-bo.jpg", "Cheesy pizza", new ArrayList<>(List.of("Cheese", "Tomato", "Bread")));
            Item item3 = new Item("item3", "Soda", 1.99, "https://botocuchigiasi.vn/wp-content/uploads/2022/02/pho-bo.jpg", "Refreshing soda", new ArrayList<>(List.of("Water", "Sugar", "Flavoring")));

            // Tạo danh sách OrderedItem
            List<OrderedItem> orderedItems1 = List.of(
                    new OrderedItem("orderedItem1", item1, 2),
                    new OrderedItem("orderedItem2", item2, 1)
            );
            List<OrderedItem> orderedItems2 = List.of(
                    new OrderedItem("orderedItem3", item3, 3)
            );

            // Thêm các đơn hàng mẫu với `orderDate` cụ thể
            allOrders.add(new Order("order1", new User("client1", "John Doe", "123 Main St", "johndoe", "123456789", "password", "client"), manager, "Ready", "Cash", orderDate1, orderedItems1));
            allOrders.add(new Order("order2", new User("client2", "Jane Smith", "456 Elm St", "janesmith", "987654321", "password", "client"), manager, "Not delivery", "Card", orderDate2, orderedItems2));
            allOrders.add(new Order("order3", new User("client3", "Michael Brown", "789 Oak St", "michaelbrown", "123987654", "password", "client"), manager, "Completed", "Cash", orderDate3, orderedItems1));

            // Lọc các đơn hàng có trạng thái "Ready"
            ArrayList<Order> filteredOrders = new ArrayList<>();
            for (Order order : allOrders) {
                if ("Ready".equalsIgnoreCase(order.getOrderStatus())) {
                    filteredOrders.add(order);
                }
            }

            // Cập nhật dữ liệu vào adapter
            adapter.setOrders(filteredOrders);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
