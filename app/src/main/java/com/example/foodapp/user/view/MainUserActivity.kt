package com.example.foodapp.user.view

import android.os.Bundle
import android.util.Log
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

        // Retrieve the user object from the Intent, handling both Parcelable (Kotlin) and Serializable (Java)
        user = intent.getParcelableExtra("user") ?: run {
            // If "user" was passed as Serializable, convert it to UserModel
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

        // Set user data in the ViewModel
        user?.let {
            sharedViewModel.setUser(it)
            logUserInfo(it)
        } ?: Log.d("MainUserActivity", "User data not found in intent")

        // Rest of your setup code
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

        binding.notificationBtn.setOnClickListener {
            val bottomSheetDialog = Notifaction_Bottom_Fragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }
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
