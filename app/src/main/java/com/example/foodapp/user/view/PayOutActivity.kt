package com.example.foodapp.user.view

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityPayOutBinding
import com.example.foodapp.user.view.CongratsBottomSheet

class PayOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Thiết lập chế độ edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Sử dụng ViewBinding để liên kết với layout
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Áp dụng WindowInsets để xử lý system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tạo danh sách các lựa chọn cho Spinner
        val paymentMethods = arrayOf("Cash", "Card")

        // Tạo adapter và liên kết với Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.paymentMethodSpinner.adapter = adapter

        // Xử lý sự kiện khi người dùng chọn phương thức thanh toán
        binding.paymentMethodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedPaymentMethod = paymentMethods[position]
                // Xử lý sự kiện lựa chọn (ví dụ: lưu lựa chọn)
                Toast.makeText(applicationContext, "Selected payment method: $selectedPaymentMethod", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Nếu không có gì được chọn (tùy chọn xử lý)
            }
        }

        // Xử lý sự kiện cho nút PlaceMyOrderBtn
        binding.PlaceMyOrderBtn.setOnClickListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }
    }
}
