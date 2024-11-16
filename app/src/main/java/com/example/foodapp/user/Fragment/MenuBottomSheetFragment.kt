package com.example.foodapp.user.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.FragmentMenuBottomSheetBinding
import com.example.foodapp.user.adapter.MenuAdapter
import com.example.foodapp.user.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMenuBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        // Nút Back để đóng BottomSheet
        binding.btnBack.setOnClickListener {
            dismiss()
        }

        // Thiết lập hành vi BottomSheet
        setupBottomSheetBehavior()

        // Lấy dữ liệu từ Firestore
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
                behavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT // Đặt chiều cao tối đa
                behavior.skipCollapsed = true // Bỏ qua trạng thái collapsed
                Log.d("MenuBottomSheetFragment", "BottomSheet behavior configured successfully.")
            } else {
                Log.e("MenuBottomSheetFragment", "BottomSheet view not found.")
            }
        }
    }

    private fun retrieveMenuItemsFromFirestore() {
        firestore = FirebaseFirestore.getInstance()
        menuItems = mutableListOf()

        // Hiển thị spinner khi tải dữ liệu
        showLoadingSpinner(true)

        firestore.collection("items") // Thay "items" bằng tên collection trong Firestore
            .get()
            .addOnSuccessListener { documents ->
                menuItems.clear()
                for (document in documents) {
                    val menuItem = document.toObject(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                Log.d("MenuBottomSheetFragment", "Firestore: Received ${menuItems.size} items.")
                setAdapter()
                showLoadingSpinner(false) // Ẩn spinner sau khi tải thành công
            }
            .addOnFailureListener { exception ->
                Log.e("MenuBottomSheetFragment", "Error retrieving data: ${exception.message}", exception)
                showLoadingSpinner(false) // Ẩn spinner nếu có lỗi
            }
    }

    private fun setAdapter() {
        if (menuItems.isNotEmpty()) {
            val adapter = MenuAdapter(menuItems, requireContext())
            binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerView.adapter = adapter
            Log.d("MenuBottomSheetFragment", "Adapter set with ${menuItems.size} items.")
        } else {
            Log.w("MenuBottomSheetFragment", "No data to display in RecyclerView.")
        }
    }

    private fun showLoadingSpinner(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingSpinner.visibility = View.VISIBLE
            binding.menuRecyclerView.visibility = View.GONE
        } else {
            binding.loadingSpinner.visibility = View.GONE
            binding.menuRecyclerView.visibility = View.VISIBLE
        }
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
