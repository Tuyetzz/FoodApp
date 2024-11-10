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
import com.example.foodapp.admin.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class OutForDelivery extends AppCompatActivity {

    private RecyclerView deliveryRecView;
    private FloatingActionButton gobackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        User user = (User) getIntent().getSerializableExtra("user");

        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        deliveryRecView = findViewById(R.id.deliveryRecyclerView);

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order("John Doe", 2, "https://example.com/image1.jpg", "Accept", "Not Paid"));
        orders.add(new Order("Jane Smith", 3, "https://example.com/image2.jpg", "Not Delivered", "Cash"));
        orders.add(new Order("Michael Brown", 1, "https://example.com/image3.jpg", "Delivered", "Card"));

        // Lọc bỏ các Order có trạng thái "Accept"
        ArrayList<Order> filteredOrders = (ArrayList<Order>) orders.stream()
                .filter(order -> !order.getOrderStatus().equals("Accept"))
                .collect(Collectors.toList());

        DeliveryRecViewAdapter adapter = new DeliveryRecViewAdapter(this);
        adapter.setOrders(filteredOrders);
        deliveryRecView.setAdapter(adapter);

        deliveryRecView.setLayoutManager(new LinearLayoutManager(this));
    }
}
