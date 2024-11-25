package com.example.foodapp.user.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodapp.R
import com.example.foodapp.admin.model.User
import com.example.foodapp.databinding.ActivityMainUserBinding
import com.example.foodapp.user.Fragment.Notifaction_Bottom_Fragment
import com.example.foodapp.user.model.UserModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainUserBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the user object from the Intent
        user = intent.getParcelableExtra("user") ?: run {
            (intent.getSerializableExtra("user") as? User)?.let {
                UserModel(
                    id = it.id,
                    fullName = it.fullName,
                    address = it.address,
                    username = it.username,
                    phone = it.phone,
                    password = it.password,
                    role = it.role
                )
            }
        }

        // Set user data in ViewModel
        user?.let {
            sharedViewModel.setUser(it)
            logUserInfo(it)
        } ?: Log.d("MainUserActivity", "User data not found in intent")

        // Setup layout and navigation
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)

        // Check for the notification flag
        val showNotification = intent.getBooleanExtra("showOrderPlacedNotification", false)
        if (showNotification) {
            showOrderPlacedNotification()
        }

        // Handle notification button
        binding.notificationBtn.setOnClickListener {
            val bottomSheetDialog = Notifaction_Bottom_Fragment()
            bottomSheetDialog.show(supportFragmentManager, "NotificationFragment")
        }
    }

    private fun showOrderPlacedNotification() {
        // Show a toast notification
        Toast.makeText(this, "Congrats! Your order has been placed successfully.", Toast.LENGTH_SHORT).show()

        // Optionally display a bottom sheet for detailed notification
        val notificationFragment = Notifaction_Bottom_Fragment.newInstance(
            "Congrats! Your order has been placed successfully.", R.drawable.illustration
        )
        notificationFragment.show(supportFragmentManager, "OrderPlacedNotification")
    }

    private fun logUserInfo(user: UserModel) {
        val userInfo = """
            User ID: ${user.id}
            Full Name: ${user.fullName}
            Address: ${user.address}
            Username: ${user.username}
            Phone: ${user.phone}
            Password: ${user.password}
            Role: ${user.role}
        """.trimIndent()
        Log.d("MainUserActivity", userInfo)
    }
}
