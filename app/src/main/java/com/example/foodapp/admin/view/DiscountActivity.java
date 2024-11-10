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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.admin.model.Discount;
import com.example.foodapp.admin.model.User;
import com.example.foodapp.admin.adapter.DiscountRecViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DiscountActivity extends AppCompatActivity {

    private FloatingActionButton gobackBtn;
    private Button generateDiscountBtn, generateRandomBtn, saveBtn;
    private EditText editTextDiscountCode, editTextDiscountPercentage, editTextExpiryDate;
    private RecyclerView discountRecyclerView;
    private DiscountRecViewAdapter discountAdapter;
    private List<Discount> discountList = new ArrayList<>();
    private FirebaseFirestore db; // Firestore instance

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_discount);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        User user = (User) getIntent().getSerializableExtra("user");

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        editTextDiscountCode = findViewById(R.id.editTextDiscountCode);
        editTextDiscountPercentage = findViewById(R.id.editTextDiscountPercent);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);

        generateRandomBtn = findViewById(R.id.generateRandomButton);
        generateRandomBtn.setOnClickListener(view -> {
            String discount = generateRandomString(8);
            editTextDiscountCode.setText(discount);
        });

        // Set up RecyclerView
        discountRecyclerView = findViewById(R.id.discountRecyclerView);
        discountAdapter = new DiscountRecViewAdapter(this, discountList);
        discountRecyclerView.setAdapter(discountAdapter);
        discountRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        generateDiscountBtn = findViewById(R.id.generateDiscountButton);
        generateDiscountBtn.setOnClickListener(view -> addDiscountToList());

        saveBtn = findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(view -> saveDiscounts());

        // Fetch data from Firestore on start
        fetchDiscountsFromFirestore();
    }

    private void addDiscountToList() {
        String discountCode = editTextDiscountCode.getText().toString();
        String discountPercentStr = editTextDiscountPercentage.getText().toString();
        String expiryDateStr = editTextExpiryDate.getText().toString();

        // Validate input
        if (discountCode.isEmpty()) {
            editTextDiscountCode.setError("Please enter a discount code");
            return;
        }

        // Lấy ngày hiện tại
        String currentDateStr = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        // Nếu expiryDate để trống, gán giá trị mặc định là ngày hiện tại
        if (expiryDateStr.isEmpty()) {
            expiryDateStr = currentDateStr;
            editTextExpiryDate.setText(expiryDateStr);
        }

        // Kiểm tra định dạng của ngày hết hạn và so sánh với ngày hiện tại
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            // Chuyển đổi expiryDate và currentDate từ chuỗi sang đối tượng Date
            Date expiryDate = dateFormat.parse(expiryDateStr);
            Date currentDate = dateFormat.parse(currentDateStr);

            if (expiryDate == null || expiryDate.compareTo(currentDate) <= 0) {
                editTextExpiryDate.setError("Expiry date must be later than today");
                return;
            }
        } catch (ParseException e) {
            editTextExpiryDate.setError("Please enter a valid date (dd-MM-yyyy)");
            return;
        }

        float discountPercent;
        try {
            discountPercent = Float.parseFloat(discountPercentStr);
        } catch (NumberFormatException e) {
            editTextDiscountPercentage.setError("Please enter a valid percentage");
            return;
        }

        // Create a new Discount object and add it to the list
        Discount discount = new Discount(null, discountCode, discountPercent, expiryDateStr);
        discountList.add(discount);
        discountAdapter.notifyItemInserted(discountList.size() - 1);

        // Clear input fields for next entry
        editTextDiscountCode.setText("");
        editTextDiscountPercentage.setText("");
        editTextExpiryDate.setText("");

        Toast.makeText(this, "Discount added!", Toast.LENGTH_SHORT).show();
    }


    private void saveDiscounts() {
        for (Discount discount : discountList) {
            db.collection("discounts")
                    .add(discount)
                    .addOnSuccessListener(documentReference -> {
                        discount.setDiscountId(documentReference.getId());
                        db.collection("discounts").document(discount.getDiscountId())
                                .set(discount);
                        Toast.makeText(this, "Discount saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to save discount: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void fetchDiscountsFromFirestore() {
        db.collection("discounts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        discountList.clear(); // Clear the list to avoid duplication
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Discount discount = document.toObject(Discount.class);
                            discount.setDiscountId(document.getId()); // Set document ID to discountId
                            discountList.add(discount);
                        }
                        discountAdapter.notifyDataSetChanged(); // Refresh the adapter with the new data
                    } else {
                        Toast.makeText(this, "Failed to load discounts: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
