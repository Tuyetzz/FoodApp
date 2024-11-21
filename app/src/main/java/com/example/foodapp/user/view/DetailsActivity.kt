package com.example.foodapp.user.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescriptions: String? = null
    private var foodIngredients: String? = null
    private var foodPrice: String? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sử dụng WindowCompat để thiết lập chế độ edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Sử dụng ViewBinding cho activity_details.xml
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Áp dụng WindowInsets để xử lý system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Khởi tạo FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Lấy dữ liệu từ intent
        foodName = intent.getStringExtra("MenuItemName")
        foodDescriptions = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

        // Thiết lập các view bằng dữ liệu từ intent
        with(binding) {
            detailFoodName.text = foodName
            detailDescription.text = foodDescriptions
            detailIngredient.text = foodIngredients
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImage)
        }

        // Xử lý sự kiện cho nút quay lại
        binding.imageButton2.setOnClickListener {
            finish()
        }

        // Xử lý sự kiện cho nút thêm vào giỏ hàng
        binding.addItemBtn.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""


    }
}
