package com.example.loginapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.model.Delivery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class OutForDelivery extends AppCompatActivity {

    private RecyclerView deliveryRecView;
    FloatingActionButton gobackBtn;
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

        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deliveryRecView = findViewById(R.id.deliveryRecyclerView);

        ArrayList<Delivery> deliveries = new ArrayList<>();
        deliveries.add(new Delivery("John Doe", "Credit Card", "Not Received", "Delivered"));
        deliveries.add(new Delivery("Jane Smith", "Cash", "Received", "Delivered"));
        deliveries.add(new Delivery("Michael Brown", "PayPal", "Not Received", "In Transit"));
        deliveries.add(new Delivery("Emily Clark", "Credit Card", "Received", "Delivered"));
        deliveries.add(new Delivery("David Johnson", "Cash", "Not Received", "Delivered"));

        DeliveryRecViewAdapter adapter = new DeliveryRecViewAdapter(this);
        adapter.setDeliveries(deliveries);
        deliveryRecView.setAdapter(adapter);

        deliveryRecView.setLayoutManager(new LinearLayoutManager(this));

    }
}