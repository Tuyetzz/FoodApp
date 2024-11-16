package com.example.foodapp.admin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.admin.adapter.FoodItemRecViewAdapter;
import com.example.foodapp.admin.model.Item;
import com.example.foodapp.admin.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class All_item extends AppCompatActivity {

    private RecyclerView itemsRecView;
    private FirebaseFirestore db;
    private FoodItemRecViewAdapter adapter;
    private ArrayList<Item> itemsList = new ArrayList<>();
    private FloatingActionButton gobackBtn;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_item);

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

        itemsRecView = findViewById(R.id.itemRecyclerView);
        adapter = new FoodItemRecViewAdapter(this);
        itemsRecView.setAdapter(adapter);
        itemsRecView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Fetch items from Firestore
        fetchItemsFromFirestore();
    }

    private void fetchItemsFromFirestore() {
        db.collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Item item = document.toObject(Item.class);
                        item.setItemId(document.getId()); // Set ID manually
                        itemsList.add(item);
                    }
                    adapter.setItems(itemsList);
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error fetching items", e);
                    Toast.makeText(this, "Failed to load items", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });
    }
}
