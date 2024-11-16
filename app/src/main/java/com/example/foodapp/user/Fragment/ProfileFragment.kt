package com.example.foodapp.user.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.foodapp.databinding.FragmentProfileBinding
import com.example.foodapp.user.view.SharedViewModel
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

        //cho vao may cai edit text
        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.fullNameEditText.setText(user.fullName)
            binding.addressEditText.setText(user.address)
            binding.usernameEditText.setText(user.username)
            binding.phoneEditText.setText(user.phone)
            binding.passwordEditText.setText(user.password)
        }

        //save data
        binding.saveButton.setOnClickListener {
            val updatedUser = sharedViewModel.user.value?.copy(
                fullName = binding.fullNameEditText.text.toString(),
                address = binding.addressEditText.text.toString(),
                username = binding.usernameEditText.text.toString(),
                phone = binding.phoneEditText.text.toString(),
                password = binding.passwordEditText.text.toString()
            )

            updatedUser?.let { user ->
                sharedViewModel.setUser(user)

                // Save user data to Firestore
                firestore.collection("users").document(user.id ?: "")
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
