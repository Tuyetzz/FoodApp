package com.example.foodapp.admin.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.R;
import com.example.foodapp.admin.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Admin_profile extends AppCompatActivity {

    FloatingActionButton gobackBtn;
    Button saveBtn, editBtn;
    EditText nameEdt, addressEdt, usernameEdt, phoneEdt, passwordEdt;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        User user = (User) getIntent().getSerializableExtra("user");

        // Khởi tạo FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        nameEdt = findViewById(R.id.nameEditText);
        addressEdt = findViewById(R.id.addressEditText);
        usernameEdt = findViewById(R.id.usernameEditText);
        phoneEdt = findViewById(R.id.phoneEditText);
        passwordEdt = findViewById(R.id.passwordEditText);

        // Thiết lập giá trị ban đầu cho các trường
        nameEdt.setText(user.getFullName());
        addressEdt.setText(user.getAddress());
        usernameEdt.setText(user.getUsername());
        phoneEdt.setText(user.getPhone());

        // Khóa các trường nhập liệu
        nameEdt.setEnabled(false);
        addressEdt.setEnabled(false);
        usernameEdt.setEnabled(false);
        phoneEdt.setEnabled(false);
        passwordEdt.setEnabled(false);

        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        saveBtn = findViewById(R.id.save_button);
        editBtn = findViewById(R.id.editButton);

        editBtn.setOnClickListener(view -> {
            // Mã xử lý nút editBtn như đã trình bày trước đó
            AlertDialog.Builder builder = new AlertDialog.Builder(Admin_profile.this);
            builder.setTitle("Do you want to change user info?");
            LinearLayout layout = new LinearLayout(Admin_profile.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10);

            final EditText passwordInput = new EditText(Admin_profile.this);
            passwordInput.setHint("Enter password");
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(passwordInput);

            final EditText confirmPasswordInput = new EditText(Admin_profile.this);
            confirmPasswordInput.setHint("Re-enter password");
            confirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(confirmPasswordInput);

            builder.setView(layout);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String password = passwordInput.getText().toString();
                    String confirmPassword = confirmPasswordInput.getText().toString();

                    if (password.equals(user.getPassword()) && confirmPassword.equals(user.getPassword())) {
                        Toast.makeText(Admin_profile.this, "Password valid", Toast.LENGTH_SHORT).show();

                        // Mở khóa các trường nhập liệu để người dùng có thể chỉnh sửa
                        nameEdt.setEnabled(true);
                        addressEdt.setEnabled(true);
                        usernameEdt.setEnabled(true);
                        phoneEdt.setEnabled(true);
                        passwordEdt.setText(user.getPassword());
                        passwordEdt.setEnabled(true);

                    } else {
                        Toast.makeText(Admin_profile.this, "Password invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        saveBtn.setOnClickListener(view -> {
            // Lấy giá trị từ các trường nhập liệu
            String fullName = nameEdt.getText().toString();
            String address = addressEdt.getText().toString();
            String username = usernameEdt.getText().toString();
            String phone = phoneEdt.getText().toString();
            String password = passwordEdt.getText().toString();

            // Tạo một Map để lưu trữ thông tin người dùng
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("fullName", fullName);
            userMap.put("address", address);
            userMap.put("username", username);
            userMap.put("phone", phone);
            userMap.put("password", password);

            // Cập nhật vào Firestore
            db.collection("users").document(user.getId())
                    .update(userMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Admin_profile.this, "Information updated successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Admin_profile.this, "Failed to update information", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
