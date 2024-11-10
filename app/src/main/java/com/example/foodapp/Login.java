package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.R;
import com.example.foodapp.admin.model.User;
import com.example.foodapp.admin.view.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    TextInputEditText editTextUsername, editTextPassword;
    Button loginBtn;
    ProgressBar progressBar;
    TextView textView;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
            finish();
        });

        loginBtn.setOnClickListener(view -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();
            progressBar.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(Login.this, "Enter Username", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            db.collection("users")
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            Log.d("Login", "Document data: " + document.getData());
                            try {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    if ("manager".equals(user.getRole())) {
                                        Toast.makeText(Login.this, "Login successful as Manager", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        finish();
                                    } else if ("user".equals(user.getRole())) {
                                        Toast.makeText(Login.this, "Login successful as User", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Login.this, "User data is invalid.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(Login.this, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                            // Authentication failed
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
