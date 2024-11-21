package com.example.foodapp.user.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.FragmentCartBinding
import com.example.foodapp.user.adapter.CartAdapter
import com.example.foodapp.user.model.MenuItem
import com.example.foodapp.user.model.OrderedItem
import com.example.foodapp.user.view.PayOutActivity

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var orderedItems: MutableList<OrderedItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        // Khởi tạo danh sách fake OrderedItems
        createFakeCartItems()

        // Xử lý khi nhấn nút Proceed
        binding.proceedBtn.setOnClickListener {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun createFakeCartItems() {
        // Tạo danh sách OrderedItem giả lập
        val fakeMenuItem1 = MenuItem(
            itemId = "1",
            itemName = "Burger",
            itemPrice = 5.99,
            shortDescription = "Delicious beef burger",
            itemImage = "https://bizweb.dktcdn.net/thumb/grande/100/458/718/articles/pho-bo.jpg?v=1697421530047",
            ingredients = listOf("Beef", "Lettuce", "Cheese")
        )

        val fakeMenuItem2 = MenuItem(
            itemId = "2",
            itemName = "Pizza",
            itemPrice = 8.99,
            shortDescription = "Cheesy pepperoni pizza",
            itemImage = "https://bizweb.dktcdn.net/thumb/grande/100/458/718/articles/pho-bo.jpg?v=1697421530047",
            ingredients = listOf("Tomato", "Cheese", "Pepperoni")
        )

        val fakeMenuItem3 = MenuItem(
            itemId = "3",
            itemName = "Soda",
            itemPrice = 1.99,
            shortDescription = "Refreshing soda drink",
            itemImage = "https://bizweb.dktcdn.net/thumb/grande/100/458/718/articles/pho-bo.jpg?v=1697421530047",
            ingredients = listOf("Water", "Sugar", "Flavoring")
        )

        orderedItems = mutableListOf(
            OrderedItem(id = "item1", item = fakeMenuItem1, quantity = 2),
            OrderedItem(id = "item2", item = fakeMenuItem2, quantity = 1),
            OrderedItem(id = "item3", item = fakeMenuItem3, quantity = 3)
        )

        // Cập nhật giao diện sau khi tạo dữ liệu
        updateCartUI()
    }

    private fun updateCartUI() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.layoutManager = layoutManager

        // Sử dụng CartAdapter với dữ liệu giả lập
        binding.cartRecyclerView.adapter = CartAdapter(orderedItems, requireContext()) { updatedItem ->
            // Callback khi giỏ hàng được cập nhật
            handleCartUpdate(updatedItem)
        }
    }

    private fun handleCartUpdate(updatedItem: OrderedItem) {
        // Xử lý logic khi giỏ hàng được cập nhật (nếu cần)
    }

    companion object {
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }
}
