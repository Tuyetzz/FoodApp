package com.example.foodapp.user.view

import android.os.Bundle
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

        // Xử lý sự kiện cho nút PlaceMyOrderBtn
        binding.PlaceMyOrderBtn.setOnClickListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }
    }
}
