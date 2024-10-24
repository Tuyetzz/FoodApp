package com.example.loginapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.loginapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button loginBtn;
    ProgressBar progressBar;
    TextView textView;

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
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                // Commented out Firebase Authentication
                /*
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                */

                // Replaced with if-else logic for hardcoded email and password
                if(email.equals("abc") && password.equals("123")) {
                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
