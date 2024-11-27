package com.example.foodapp.user.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.foodapp.databinding.FragmentProfileBinding
import com.example.foodapp.user.viewmodel.SharedViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Access the SharedViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // Initialize Firestore
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe user data and populate EditTexts
        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.fullNameEditText.setText(it.fullName ?: "")
                binding.addressEditText.setText(it.address ?: "")
                binding.usernameEditText.setText(it.username ?: "")
                binding.phoneEditText.setText(it.phone ?: "")
                binding.passwordEditText.setText(it.password ?: "")
            } ?: run {
                Toast.makeText(requireContext(), "No user data available", Toast.LENGTH_SHORT).show()
            }
        }

        // Save data when the save button is clicked
        binding.saveButton.setOnClickListener {
            val currentUser = sharedViewModel.user.value
            if (currentUser != null && !currentUser.id.isNullOrEmpty()) {
                val updatedUser = currentUser.copy(
                    fullName = binding.fullNameEditText.text.toString(),
                    address = binding.addressEditText.text.toString(),
                    username = binding.usernameEditText.text.toString(),
                    phone = binding.phoneEditText.text.toString(),
                    password = binding.passwordEditText.text.toString()
                )

                sharedViewModel.setUser(updatedUser)

                // Save updated user data to Firestore
                firestore.collection("users").document(updatedUser.id!!)
                    .set(updatedUser)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "User ID is missing or invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
