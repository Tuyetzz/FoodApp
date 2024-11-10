package com.example.foodapp.admin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.foodapp.R;
import com.example.foodapp.admin.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewStatistic extends AppCompatActivity {

    private BarChart barChart;
    FloatingActionButton gobackBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistic);

        // Áp dụng padding cho hệ thống thanh (system bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        User user = (User) getIntent().getSerializableExtra("user");

        gobackBtn = findViewById(R.id.goback);
        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        // Khởi tạo BarChart từ layout
        barChart = findViewById(R.id.barChart);

        // Thêm dữ liệu mẫu cho BarChart
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, 100)); // Dữ liệu cho tháng 1
        barEntries.add(new BarEntry(2f, 200)); // Dữ liệu cho tháng 2
        barEntries.add(new BarEntry(3f, 500)); // Dữ liệu cho tháng 3
        barEntries.add(new BarEntry(4f, 400)); // Dữ liệu cho tháng 4
        barEntries.add(new BarEntry(5f, 300)); // Dữ liệu cho tháng 5

        // Tạo BarDataSet và thiết lập màu sắc
        BarDataSet barDataSet = new BarDataSet(barEntries, "Doanh thu theo tháng");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Đặt màu cho các cột

        // Cung cấp dữ liệu cho BarChart
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Làm mới BarChart để hiển thị dữ liệu mới
        barChart.invalidate();
    }
}
