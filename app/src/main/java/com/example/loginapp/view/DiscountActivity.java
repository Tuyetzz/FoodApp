package com.example.loginapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loginapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DiscountActivity extends AppCompatActivity {

    FloatingActionButton gobackBtn;
    private TableLayout tableLayout;
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

        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tableLayout = findViewById(R.id.tableLayout);

        // Example data for the table
        String[][] discountData = {
                {"Discount Code", "Percentage", "Used"},
                {"SAVE10", "10%", "No"},
                {"SUMMER20", "20%", "Yes"},
                {"WINTER15", "15%", "No"}
        };

        // Add each row to the table
        for (int i = 0; i < discountData.length; i++) {
            TableRow tableRow = new TableRow(this);

            // For each column in the row, create a TextView
            for (int j = 0; j < discountData[i].length; j++) {
                TextView textView = new TextView(this);
                textView.setText(discountData[i][j]);
                textView.setPadding(16, 16, 16, 16);  // Add padding for better readability
                textView.setTextSize(20);
                tableRow.addView(textView);
            }

            // Add the row to the TableLayout
            tableLayout.addView(tableRow);
        }

    }
}