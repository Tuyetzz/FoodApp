package com.example.foodapp.user.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.FragmentCartBinding
import com.example.foodapp.user.adapter.CartAdapter
import com.example.foodapp.user.model.OrderedItem
import com.example.foodapp.user.view.PayOutActivity
import com.example.foodapp.user.view.SharedViewModel

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var orderedItems: MutableList<OrderedItem>

    // SharedViewModel để lấy và lưu trạng thái giỏ hàng
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Quan sát sự thay đổi của orderedItems trong ViewModel
        sharedViewModel.orderedItems.observe(this) { items ->
            orderedItems = items.toMutableList()
            updateCartUI() // Cập nhật giao diện khi giỏ hàng thay đổi
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        // Lấy danh sách OrderedItems từ SharedViewModel (giỏ hàng)
        fetchCartItems()

        // Xử lý khi nhấn nút Proceed
        binding.proceedBtn.setOnClickListener {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    // Lấy danh sách OrderedItems từ SharedViewModel
    private fun fetchCartItems() {
        orderedItems = sharedViewModel.getOrderedItems().toMutableList()
        updateCartUI()
    }

    // Cập nhật giao diện giỏ hàng
    private fun updateCartUI() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.layoutManager = layoutManager

        // Sử dụng CartAdapter với danh sách OrderedItems
        binding.cartRecyclerView.adapter = CartAdapter(orderedItems, requireContext(), sharedViewModel) { updatedItem ->
            // Callback khi giỏ hàng được cập nhật
            handleCartUpdate(updatedItem)
        }
    }

    // Xử lý khi giỏ hàng được cập nhật
    private fun handleCartUpdate(updatedItem: OrderedItem) {
        val existingItem = orderedItems.find { it.id == updatedItem.id }
        if (existingItem != null) {
            // Cập nhật số lượng hoặc các thuộc tính khác của món ăn trong giỏ hàng
            existingItem.quantity = updatedItem.quantity
            sharedViewModel.updateOrderedItem(existingItem) // Cập nhật lại trong ViewModel
        } else {
            // Nếu món không tồn tại trong giỏ hàng, thêm mới
            sharedViewModel.addOrderedItem(updatedItem)
        }
    }

    companion object {
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }
}
