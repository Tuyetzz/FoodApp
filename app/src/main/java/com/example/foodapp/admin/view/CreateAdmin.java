package com.example.foodapp.admin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.R;
import com.example.foodapp.admin.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateAdmin extends AppCompatActivity {

    FloatingActionButton gobackBtn;
    EditText fullnameedt, usernameedt, passwordedt;
    Button createAdmin;
    FirebaseFirestore db; // Firestore instance

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        User user = (User) getIntent().getSerializableExtra("user");

        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainAdminActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        fullnameedt = findViewById(R.id.editTextFullName);
        usernameedt = findViewById(R.id.editTextUserName);
        passwordedt = findViewById(R.id.editTextPassword);

        createAdmin = findViewById(R.id.createAdminButton);
        createAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = fullnameedt.getText().toString().trim();
                String username = usernameedt.getText().toString().trim();
                String password = passwordedt.getText().toString().trim();

                if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(CreateAdmin.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo user mới với vai trò manager
                User newUser = new User();
                newUser.setFullName(fullName);
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setRole("manager");

                // Thêm user vào Firestore
                db.collection("users")
                        .add(newUser)
                        .addOnSuccessListener(documentReference -> {
                            String generatedId = documentReference.getId();
                            documentReference.update("id", generatedId) // cập nhật id cho tài liệu
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(CreateAdmin.this, "Admin created successfully!", Toast.LENGTH_SHORT).show()
                                    )
                                    .addOnFailureListener(e ->
                                            Toast.makeText(CreateAdmin.this, "Failed to set user ID: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                    );

                            fullnameedt.setText("");
                            usernameedt.setText("");
                            passwordedt.setText("");
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(CreateAdmin.this, "Error creating admin: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
        });
    }
}
