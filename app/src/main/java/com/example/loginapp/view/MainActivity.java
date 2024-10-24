package com.example.loginapp.view;

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

import com.example.loginapp.R;

public class MainActivity extends AppCompatActivity {

    // FirebaseAuth auth;
    TextView textView;
    // FirebaseUser user;
    Button logoutBtn, addMenuBtn, adminProfileBtn, viewAllMenuBtn, deliveryBtn, createAdminBtn, viewStatisticBtn, discountBtn;
    LinearLayout pendingOrder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // auth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.user_details);
        // user = auth.getCurrentUser();
        // if(user == null) {
        //    Intent intent = new Intent(getApplicationContext(), Login.class);
        //    startActivity(intent);
        //    finish();
        // } else {
        //    textView.setText(user.getEmail());
        // }

        // Hardcoded email instead of Firebase user
        textView.setText("Email here");

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
                Toast.makeText(MainActivity.this, "addMenuBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddItem.class);
                startActivity(intent);
                finish();
            }
        });

        adminProfileBtn = findViewById(R.id.profile);
        adminProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "adminProfileBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Admin_profile.class);
                startActivity(intent);
                finish();
            }
        });

        viewAllMenuBtn = findViewById(R.id.viewAllMenu);
        viewAllMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "viewAllMenuBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), All_item.class);
                startActivity(intent);
                finish();
            }
        });

        deliveryBtn = findViewById(R.id.deliveryButton);
        deliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "deliveryBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), OutForDelivery.class);
                startActivity(intent);
                finish();
            }
        });

        createAdminBtn = findViewById(R.id.createAdmin);
        createAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "createAdminBtn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CreateAdmin.class);
                startActivity(intent);
                finish();
            }
        });

        pendingOrder = findViewById(R.id.PendingOrder);
        pendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "pendingOrder clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PendingOrder.class);
                startActivity(intent);
                finish();
            }
        });

        viewStatisticBtn = findViewById(R.id.viewStatistic);
        viewStatisticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "viewStatistic clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ViewStatistic.class);
                startActivity(intent);
                finish();
            }
        });

        discountBtn = findViewById(R.id.discount);
        discountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "discount clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DiscountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
