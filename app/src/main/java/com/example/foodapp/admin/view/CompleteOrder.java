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
import com.example.foodapp.admin.adapter.CompleteOrderAdapter;
import com.example.foodapp.admin.model.Order;
import com.example.foodapp.admin.model.OrderedItem;
import com.example.foodapp.admin.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompleteOrder extends AppCompatActivity {

    private FloatingActionButton gobackBtn;
    private RecyclerView recyclerView;
    private CompleteOrderAdapter adapter;
    private FirebaseFirestore db; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_complete_order);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the manager user from Intent
        User user = (User) getIntent().getSerializableExtra("user");

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView and Adapter
        recyclerView = findViewById(R.id.completeOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompleteOrderAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Set up the Go Back button
        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(view -> navigateBackToMainAdmin(user));

        // Load completed orders from Firestore
        loadCompletedOrdersFromFirestore(user);
    }

    private void navigateBackToMainAdmin(User user) {
        Intent intent = new Intent(getApplicationContext(), MainAdminActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private void loadCompletedOrdersFromFirestore(User manager) {
        db.collection("orders")
                .whereEqualTo("orderStatus", "Completed")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Order> completedOrders = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = parseOrder(document, manager);
                        if (order != null) {
                            completedOrders.add(order);
                        }
                    }
                    adapter.setOrders(completedOrders);
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

            return new Order(
                    document.getId(),                 // Order ID
                    client,                           // Client
                    manager,                          // Manager from Intent
                    document.getString("orderStatus"), // Order status
                    document.getString("paymentType"), // Payment type
                    document.getDate("orderDate"),     // Order date
                    orderedItems                      // List of ordered items
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
