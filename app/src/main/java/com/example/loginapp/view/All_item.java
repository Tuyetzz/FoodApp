package com.example.loginapp.view;

import android.annotation.SuppressLint;
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
import com.example.loginapp.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class All_item extends AppCompatActivity {

    private RecyclerView itemsRecView;
    FloatingActionButton gobackBtn;

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


        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        itemsRecView = findViewById(R.id.itemRecyclerView);

        ArrayList<Item> items = new ArrayList<>();

        items.add(new Item("1", "Spicy Fresh Crab", 35.00, "https://png.pngtree.com/png-clipart/20221001/ourmid/pngtree-fast-food-big-ham-burger-png-image_6244235.png", "crab with sauce", new ArrayList<>(List.of("Crab", "Spicy Sauce", "Herbs", "Lemon"))));
        items.add(new Item("2", "Vegetable Salad", 12.50, "https://png.pngtree.com/png-clipart/20221001/ourmid/pngtree-fast-food-big-ham-burger-png-image_6244235.png", "vegetable salad", new ArrayList<>(List.of("Lettuce", "Tomatoes", "Cucumbers", "Olive Oil", "Feta Cheese"))));
        items.add(new Item("3", "Mushroom Pasta", 18.00, "https://png.pngtree.com/png-clipart/20221001/ourmid/pngtree-fast-food-big-ham-burger-png-image_6244235.png", "pasta with mushrooms", new ArrayList<>(List.of("Pasta", "Mushrooms", "Garlic", "Cream", "Parmesan"))));
        items.add(new Item("4", "Herbal Pan Cake", 15.00, "https://png.pngtree.com/png-clipart/20221001/ourmid/pngtree-fast-food-big-ham-burger-png-image_6244235.png", "pancakes with sour cream", new ArrayList<>(List.of("Flour", "Herbs", "Eggs", "Sour Cream", "Butter"))));
        items.add(new Item("5", "BBQ Chicken Wings", 22.00, "https://png.pngtree.com/png-clipart/20221001/ourmid/pngtree-fast-food-big-ham-burger-png-image_6244235.png", "chicken wings with BBQ sauce", new ArrayList<>(List.of("Chicken Wings", "BBQ Sauce", "Honey", "Garlic", "Paprika"))));

        FoodItemRecViewAdapter adapter = new FoodItemRecViewAdapter(this);
        adapter.setItems(items);
        itemsRecView.setAdapter(adapter);

        itemsRecView.setLayoutManager(new LinearLayoutManager(this));
    }
}