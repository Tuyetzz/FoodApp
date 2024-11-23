package com.example.foodapp.user.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.FragmentMenuBottomSheetBinding
import com.example.foodapp.user.adapter.MenuAdapter
import com.example.foodapp.user.model.MenuItem
import com.example.foodapp.user.view.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMenuBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var firestore: FirebaseFirestore
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            dismiss()
        }

        setupBottomSheetBehavior()
        retrieveMenuItemsFromFirestore()

        return binding.root
    }

    private fun setupBottomSheetBehavior() {
        dialog?.setOnShowListener { dialog ->
            val bottomSheet = (dialog as? com.google.android.material.bottomsheet.BottomSheetDialog)
                ?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT
                behavior.skipCollapsed = true
                Log.d("MenuBottomSheetFragment", "BottomSheet behavior configured successfully.")
            } else {
                Log.e("MenuBottomSheetFragment", "BottomSheet view not found.")
            }
        }
    }

    private fun retrieveMenuItemsFromFirestore() {
        firestore = FirebaseFirestore.getInstance()
        menuItems = mutableListOf()

        showLoadingSpinner(true)

        firestore.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                menuItems.clear()
                for (document in documents) {
                    val menuItem = document.toObject(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                if (menuItems.isEmpty()) {
                    Toast.makeText(requireContext(), "No menu items found.", Toast.LENGTH_SHORT).show()
                }
                setAdapter()
                showLoadingSpinner(false)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error loading menu: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("MenuBottomSheetFragment", "Error retrieving data", exception)
                showLoadingSpinner(false)
            }
    }

    private fun setAdapter() {
        if (menuItems.isNotEmpty()) {
            val adapter = MenuAdapter(menuItems, requireContext(), sharedViewModel)
            binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerView.adapter = adapter
            Log.d("MenuBottomSheetFragment", "Adapter set with ${menuItems.size} items.")
        } else {
            Log.w("MenuBottomSheetFragment", "No data to display in RecyclerView.")
        }
    }

    private fun showLoadingSpinner(isLoading: Boolean) {
        binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.menuRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): MenuBottomSheetFragment {
            return MenuBottomSheetFragment()
        }
    }
}
