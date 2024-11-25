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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PendingOrder extends AppCompatActivity {

    private FloatingActionButton gobackBtn;
    private RecyclerView orderRecyclerView;
    private OrderRecViewAdapter adapter;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pending_order);

        // Adjust layout for insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        User manager = (User) getIntent().getSerializableExtra("user");

        db = FirebaseFirestore.getInstance();

        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(view -> navigateBackToMainAdmin(manager));

        setupRecyclerView();
        loadOrdersFromFirestore(manager);
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

    private void loadOrdersFromFirestore(User manager) {
        db.collection("orders")
                .whereEqualTo("orderStatus", "Pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Order> orders = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = parseOrder(document, manager);
                        if (order != null) {
                            orders.add(order);
                        }
                    }
                    adapter.setOrders(orders);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private Order parseOrder(QueryDocumentSnapshot document, User manager) {
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

            // Check and set orderDate
            Date orderDate = document.getDate("orderDate");
            if (orderDate == null) {
                orderDate = new Date();
            }

            return new Order(
                    document.getId(),                 // Order ID
                    client,                           // Client
                    manager,                          // Manager from Intent
                    document.getString("orderStatus"), // Order status
                    document.getString("paymentType"), // Payment type
                    orderDate,                         // Order date (gán ngày hôm nay nếu null)
                    orderedItems                      // List of ordered items
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
