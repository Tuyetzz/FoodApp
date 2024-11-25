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
import com.example.foodapp.admin.model.Order;
import com.example.foodapp.admin.model.OrderedItem;
import com.example.foodapp.admin.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OutForDelivery extends AppCompatActivity {

    private RecyclerView deliveryRecView;
    private FloatingActionButton gobackBtn;
    private DeliveryRecViewAdapter adapter;
    private User user; // Biến toàn cục
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();

        setupGoBackButton();
        setupRecyclerView();

        // Lấy dữ liệu từ Firestore
        loadOrdersFromFirestore();
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

    private void loadOrdersFromFirestore() {
        db.collection("orders")
                .whereIn("orderStatus", List.of("Not Delivered", "Delivered")) // Chỉ lấy các trạng thái liên quan
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Order> orders = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = parseOrder(document);
                        if (order != null) {
                            orders.add(order);
                        }
                    }
                    adapter.setOrders(new ArrayList<>(orders));
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private Order parseOrder(QueryDocumentSnapshot document) {
        try {
            // Parse client data
            Map<String, Object> clientMap = (Map<String, Object>) document.get("client");
            User client = clientMap != null ? new User(clientMap) : null;

            // Parse ordered items
            List<Map<String, Object>> orderedItemsList = (List<Map<String, Object>>) document.get("listOrderedItem");
            List<OrderedItem> orderedItems = new ArrayList<>();
            if (orderedItemsList != null) {
                for (Map<String, Object> itemMap : orderedItemsList) {
                    orderedItems.add(new OrderedItem(itemMap));
                }
            }

            return new Order(
                    document.getId(),                 // Order ID
                    client,                           // Client
                    user,                             // Manager từ Intent
                    document.getString("orderStatus"), // Order status
                    document.getString("paymentType"), // Payment type
                    document.getDate("orderDate") != null ? document.getDate("orderDate") : new Date(), // Order date
                    orderedItems                      // List of ordered items
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
