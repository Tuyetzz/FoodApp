package com.example.foodapp.admin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.foodapp.Login;
import com.example.foodapp.R;
import com.example.foodapp.admin.model.User;

public class MainAdminActivity extends AppCompatActivity {

    // FirebaseAuth auth;
    TextView textView;
    // FirebaseUser user;
    Button logoutBtn, addMenuBtn, adminProfileBtn, viewAllMenuBtn, deliveryBtn, createAdminBtn, viewStatisticBtn, discountBtn;
    LinearLayout pendingOrder, completeOrder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        textView = findViewById(R.id.user_details);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            textView.setText(user.getUsername());
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }


        logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        addMenuBtn = findViewById(R.id.addMenu);
        addMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdminActivity.this, "addMenuBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddItem.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        adminProfileBtn = findViewById(R.id.profile);
        adminProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdminActivity.this, "adminProfileBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Admin_profile.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        viewAllMenuBtn = findViewById(R.id.viewAllMenu);
        viewAllMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdminActivity.this, "viewAllMenuBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), All_item.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        deliveryBtn = findViewById(R.id.deliveryButton);
        deliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdminActivity.this, "deliveryBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), OutForDelivery.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        createAdminBtn = findViewById(R.id.createAdmin);
        createAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdminActivity.this, "createAdminBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CreateAdmin.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        pendingOrder = findViewById(R.id.PendingOrder);
        pendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdminActivity.this, "pendingOrder clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PendingOrder.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        completeOrder = findViewById(R.id.CompleteOrder);
        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdminActivity.this, "completeOrder clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CompleteOrder.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        viewStatisticBtn = findViewById(R.id.viewStatistic);
        viewStatisticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdminActivity.this, "viewStatistic clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ViewStatistic.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        discountBtn = findViewById(R.id.discount);
        discountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdminActivity.this, "discount clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DiscountActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });
    }
}
