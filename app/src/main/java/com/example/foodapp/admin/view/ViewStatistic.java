package com.example.foodapp.admin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.R;
import com.example.foodapp.admin.model.Order;
import com.example.foodapp.admin.model.OrderedItem;
import com.example.foodapp.admin.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ViewStatistic extends AppCompatActivity {

    private BarChart barChart;
    private FloatingActionButton gobackBtn;
    private FirebaseFirestore db; // Firestore instance

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
        gobackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainAdminActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });

        barChart = findViewById(R.id.barChart);
        db = FirebaseFirestore.getInstance();

        loadStatistics();
    }

    private void loadStatistics() {
        db.collection("orders")
                .whereEqualTo("orderStatus", "Completed") // Lọc các đơn hàng có trạng thái "Completed"
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Sử dụng LinkedHashMap để giữ thứ tự ngày
                    LinkedHashMap<String, Float> revenueByDate = new LinkedHashMap<>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            // Lấy ngày đặt hàng
                            Date orderDate = document.getDate("orderDate");
                            if (orderDate == null) continue; // Bỏ qua nếu không có ngày

                            // Chuyển ngày sang định dạng chuỗi
                            String dateKey = dateFormat.format(orderDate);

                            // Lấy danh sách các OrderedItem từ Firestore
                            List<HashMap<String, Object>> orderedItemsList =
                                    (List<HashMap<String, Object>>) document.get("listOrderedItem");

                            // Tính doanh thu hàng ngày
                            float dailyRevenue = 0f;
                            if (orderedItemsList != null) {
                                for (HashMap<String, Object> itemMap : orderedItemsList) {
                                    // Chuyển đổi HashMap sang OrderedItem
                                    OrderedItem orderedItem = new OrderedItem(itemMap);
                                    dailyRevenue += orderedItem.getQuantity() * orderedItem.getItem().getItemPrice();
                                }
                            }

                            // Cộng dồn doanh thu vào ngày tương ứng
                            revenueByDate.put(dateKey, revenueByDate.getOrDefault(dateKey, 0f) + dailyRevenue);
                        } catch (Exception e) {
                            e.printStackTrace(); // Log lỗi nếu có lỗi dữ liệu
                        }
                    }

                    // Tạo dữ liệu cho BarChart
                    populateBarChart(revenueByDate);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    // Hiển thị lỗi
                    Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Hàm chuyển dữ liệu sang BarChart và hiển thị.
     *
     * @param revenueByDate Doanh thu theo ngày (LinkedHashMap)
     */
    private void populateBarChart(LinkedHashMap<String, Float> revenueByDate) {
        // Tạo danh sách BarEntry và nhãn cho trục X
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;

        for (String date : revenueByDate.keySet()) {
            barEntries.add(new BarEntry(index, revenueByDate.get(date))); // Dữ liệu cột
            labels.add(date); // Nhãn ngày
            index++;
        }

        // Tạo BarDataSet và gán dữ liệu
        BarDataSet barDataSet = new BarDataSet(barEntries, "Doanh thu theo ngày");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // Cấu hình BarData
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.3f); // Làm thon các cột

        // Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Sử dụng IndexAxisValueFormatter
        xAxis.setGranularity(1f); // Hiển thị từng cột
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-45); // Xoay nhãn ngày
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Hiển thị nhãn dưới cùng

        // Cấu hình trục Y
        barChart.getAxisLeft().setGranularity(1f);
        barChart.getAxisRight().setEnabled(false); // Ẩn trục Y bên phải

        // Gán dữ liệu cho BarChart
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false); // Ẩn mô tả biểu đồ
        barChart.setFitBars(true); // Đảm bảo cột vừa với trục
        barChart.invalidate(); // Làm mới BarChart để hiển thị
    }



}
