package com.example.foodapp.user.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.user.adapter.BuyAgainAdapter
import com.example.foodapp.databinding.FragmentHistoryBinding
import com.example.foodapp.user.model.Order
import com.example.foodapp.user.model.OrderedItem
import com.example.foodapp.user.viewmodel.SharedViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private val firestore = FirebaseFirestore.getInstance()

    // Lấy SharedViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        setupRecyclerView()

        // Lấy user từ SharedViewModel và gọi fetchPurchasedItems nếu user không null
        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                fetchPurchasedItems(user.id ?: "")
            } else {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                Log.e("HistoryFragment", "User is null in SharedViewModel.")
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.BuyAgainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fetchPurchasedItems(userId: String) {
        firestore.collection("orders")
            .whereEqualTo("client.id", userId) // Truy vấn theo user ID
            .get()
            .addOnSuccessListener { snapshot ->
                val purchasedItems = mutableListOf<OrderedItem>()

                if (snapshot.isEmpty) {
                    Log.d("HistoryFragment", "No orders found for user: $userId")
                    Toast.makeText(requireContext(), "No purchase history available", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                for (document in snapshot.documents) {
                    val order = document.toObject<Order>()
                    order?.listOrderedItem?.let { orderedItems ->
                        purchasedItems.addAll(orderedItems)
                    }
                }

                if (purchasedItems.isNotEmpty()) {
                    val uniqueItems = removeDuplicates(purchasedItems)
                    updateAdapter(uniqueItems)
                } else {
                    Log.d("HistoryFragment", "No purchased items found.")
                    Toast.makeText(requireContext(), "No purchased items found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("HistoryFragment", "Error fetching purchased items: ${exception.message}")
                Toast.makeText(requireContext(), "Failed to fetch purchase history", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeDuplicates(items: List<OrderedItem>): List<OrderedItem> {
        // Dùng một set để loại bỏ các mục trùng lặp dựa trên itemId
        val seenItemIds = mutableSetOf<String>()
        return items.filter { orderedItem ->
            val itemId = orderedItem.item?.itemId
            if (itemId != null && seenItemIds.add(itemId)) {
                true // Nếu itemId chưa tồn tại, giữ lại mục này
            } else {
                false // Nếu itemId đã tồn tại, bỏ qua
            }
        }
    }

    private fun updateAdapter(uniqueItems: List<OrderedItem>) {
        buyAgainAdapter = BuyAgainAdapter(uniqueItems, requireContext())
        binding.BuyAgainRecyclerView.adapter = buyAgainAdapter
    }

    companion object {
        // Companion object nếu cần
    }
}
