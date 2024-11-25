package com.example.foodapp.admin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.R;
import com.example.foodapp.admin.model.Item;
import com.example.foodapp.admin.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity {

    private FloatingActionButton gobackBtn;
    private Button submitButton;
    private EditText itemNameInput, itemPriceInput, imageUrlInput, shortDescriptionInput, ingredientsInput;
    private ArrayList<Item> itemsList = new ArrayList<>();
    private FirebaseFirestore db; // Firebase Firestore instance

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

        // Configure Firebase Firestore
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Enable offline persistence
                .build();
        db.setFirestoreSettings(settings);

        // Configure view insets for Edge to Edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        User user = (User) getIntent().getSerializableExtra("user");

        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();

        // Initialize UI elements
        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainAdminActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        // Connect UI elements with code
        itemNameInput = findViewById(R.id.editTextText);
        itemPriceInput = findViewById(R.id.editTextText2);
        imageUrlInput = findViewById(R.id.imageUrlInput);
        shortDescriptionInput = findViewById(R.id.editTextText3);
        ingredientsInput = findViewById(R.id.editTextText4);
        submitButton = findViewById(R.id.button);

        // Set submit button action
        submitButton.setOnClickListener(view -> addItemToList());
    }

    private void addItemToList() {
        String itemName = itemNameInput.getText().toString();
        String itemPriceStr = itemPriceInput.getText().toString();
        String imageUrl = imageUrlInput.getText().toString();
        String shortDescription = shortDescriptionInput.getText().toString();
        String ingredientsStr = ingredientsInput.getText().toString();
        Toast.makeText(this, "Chay den day roi", Toast.LENGTH_SHORT).show();
        //check
        if (itemName.isEmpty() || itemPriceStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double itemPrice = Double.parseDouble(itemPriceStr);

        ArrayList<String> ingredients = new ArrayList<>();
        for (String ingredient : ingredientsStr.split("\n")) {
            ingredients.add(ingredient.trim());
        }

        Item newItem = new Item();
        newItem.setItemName(itemName);
        newItem.setItemPrice(itemPrice);
        newItem.setItemImage(imageUrl);
        newItem.setShortDescription(shortDescription);
        newItem.setIngredients(ingredients);
        itemsList.add(newItem);

        // save
        db.collection("items")
                .add(newItem)
                .addOnSuccessListener(documentReference -> {
                    // Lấy ID của tài liệu vừa tạo
                    String generatedId = documentReference.getId();
                    // Cập nhật trường itemID trong tài liệu với ID đã tạo
                    documentReference.update("itemId", generatedId)
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(this, "Item added successfully with ID", Toast.LENGTH_SHORT).show()
                            )
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Error updating itemID: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Clear input fields after submission
        itemNameInput.setText("");
        itemPriceInput.setText("");
        imageUrlInput.setText("");
        shortDescriptionInput.setText("");
        ingredientsInput.setText("");
    }
}
