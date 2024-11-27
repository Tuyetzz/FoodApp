package com.example.foodapp.user.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.user.adapter.MenuAdapter
import com.example.foodapp.user.model.MenuItem
import com.example.foodapp.user.viewmodel.SharedViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter

    // SharedViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val originalMenuItems = arrayListOf<MenuItem>()
    private val filteredMenuItems = arrayListOf<MenuItem>()

    // Firebase Firestore instance
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupSearchView()
        fetchMenuItemsFromFirestore()

        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
            // Có thể thêm lời chào
//            binding.textView22.text = "Welcome, ${user.fullName}!"
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = MenuAdapter(filteredMenuItems, requireContext(), sharedViewModel)
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }

    // Lấy dữ liệu từ Firestore
    private fun fetchMenuItemsFromFirestore() {
        firestore.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                originalMenuItems.clear()
                for (document in documents) {
                    val menuItem = document.toObject<MenuItem>() // Chuyển đổi document thành MenuItem
                    originalMenuItems.add(menuItem)
                }
                showAllMenuItems() // Hiển thị danh sách
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showAllMenuItems() {
        filteredMenuItems.clear()
        filteredMenuItems.addAll(originalMenuItems)
        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        filteredMenuItems.clear()
        if (query.isEmpty()) {
            showAllMenuItems()
        } else {
            filteredMenuItems.addAll(originalMenuItems.filter { menuItem ->
                menuItem.itemName.contains(query, ignoreCase = true)
            })
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
