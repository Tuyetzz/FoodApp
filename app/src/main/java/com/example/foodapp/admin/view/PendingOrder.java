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

import com.example.foodapp.admin.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PendingOrder extends AppCompatActivity {

    FloatingActionButton gobackBtn;
    RecyclerView orderRecyclerView;
    OrderRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pending_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        User user = (User) getIntent().getSerializableExtra("user");

        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainAdminActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderRecViewAdapter(this);

        // Add test data
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order("John Doe", 2, "https://pbs.twimg.com/media/F1PnEIxXwAA8mxF.jpg:large", "Accept", "Not Paid"));
        orders.add(new Order("Jane Smith", 3, "https://pbs.twimg.com/media/F1PnEIxXwAA8mxF.jpg:large", "Not Delivered", "Cash"));
        orders.add(new Order("Michael Brown", 1, "https://pbs.twimg.com/media/F1PnEIxXwAA8mxF.jpg:large", "Delivered", "Card"));


        adapter.setOrders(orders);


        orderRecyclerView.setAdapter(adapter);
    }
}
