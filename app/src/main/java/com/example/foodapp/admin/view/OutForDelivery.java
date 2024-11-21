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
import com.example.foodapp.admin.adapter.DeliveryRecViewAdapter;
import com.example.foodapp.admin.model.Item;
import com.example.foodapp.admin.model.Order;
import com.example.foodapp.admin.model.OrderedItem;
import com.example.foodapp.admin.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OutForDelivery extends AppCompatActivity {

    private RecyclerView deliveryRecView;
    private FloatingActionButton gobackBtn;
    private DeliveryRecViewAdapter adapter;
    private User user; // Biến toàn cục

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery);

        // Áp dụng insets để giao diện tương thích với các thiết bị không viền
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = (User) getIntent().getSerializableExtra("user");

        setupGoBackButton();

        setupRecyclerView();

        List<Order> orders = loadSampleOrders();

        List<Order> filteredOrders = filterOrdersForDelivery(orders);

        adapter.setOrders(new ArrayList<>(filteredOrders));
    }

    private void setupGoBackButton() {
        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainAdminActivity.class);
            intent.putExtra("user", user); // Truyền lại user khi quay lại
            startActivity(intent);
            finish();
        });
    }

    private void setupRecyclerView() {
        deliveryRecView = findViewById(R.id.deliveryRecyclerView);
        deliveryRecView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeliveryRecViewAdapter(this);
        deliveryRecView.setAdapter(adapter);
    }

    private List<Order> loadSampleOrders() {
        List<Order> orders = new ArrayList<>();

        // Tạo dữ liệu mẫu cho các món ăn
        Item item1 = new Item("item1", "Burger", 5.99, "https://example.com/burger.jpg", "Delicious beef burger", new ArrayList<>(List.of("Beef", "Bread", "Lettuce")));
        Item item2 = new Item("item2", "Pizza", 8.99, "https://example.com/pizza.jpg", "Cheesy pizza", new ArrayList<>(List.of("Cheese", "Tomato", "Bread")));
        Item item3 = new Item("item3", "Soda", 1.99, "https://example.com/soda.jpg", "Refreshing soda", new ArrayList<>(List.of("Water", "Sugar", "Flavoring")));

        // Tạo danh sách OrderedItem
        List<OrderedItem> orderedItems1 = List.of(
                new OrderedItem("orderedItem1", item1, 2),
                new OrderedItem("orderedItem2", item2, 1)
        );

        List<OrderedItem> orderedItems2 = List.of(
                new OrderedItem("orderedItem3", item3, 3)
        );

        // Tạo các ngày đặt hàng cụ thể
        Date orderDate1 = new Date(); // Ngày hiện tại
        Date orderDate2 = new Date(orderDate1.getTime() - 24 * 60 * 60 * 1000); // Hôm qua
        Date orderDate3 = new Date(orderDate1.getTime() - 48 * 60 * 60 * 1000); // Hai ngày trước

        // Tạo danh sách đơn hàng
        orders.add(new Order("order1", new User("1", "John Doe", "123 Main St", "johndoe", "123456789", "password", "client"),
                user, "Not Delivered", "Cash", orderDate1, orderedItems1));
        orders.add(new Order("order2", new User("2", "Jane Smith", "456 Elm St", "janesmith", "987654321", "password", "client"),
                user, "Delivered", "Cash", orderDate2, orderedItems2));
        orders.add(new Order("order3", new User("3", "Michael Brown", "789 Pine St", "michaelbrown", "987651234", "password", "client"),
                user, "Ready", "Card", orderDate3, orderedItems1));

        return orders;
    }


    private List<Order> filterOrdersForDelivery(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getOrderStatus().equals("Not Delivered") || order.getOrderStatus().equals("Delivered"))
                .collect(Collectors.toList());
    }
}
