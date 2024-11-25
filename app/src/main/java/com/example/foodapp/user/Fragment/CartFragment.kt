package com.example.foodapp.user.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.FragmentCartBinding
import com.example.foodapp.user.adapter.CartAdapter
import com.example.foodapp.user.model.Order
import com.example.foodapp.user.model.OrderedItem
import com.example.foodapp.user.view.PayOutActivity
import com.example.foodapp.user.view.SharedViewModel

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderedItems: MutableList<OrderedItem>

    //lay tu sharedViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Lấy danh sách OrderedItems từ SharedViewModel
        sharedViewModel.orderedItems.observe(viewLifecycleOwner) { items ->
            orderedItems = items.toMutableList()
            updateRecyclerView()
        }

        binding.proceedBtn.setOnClickListener {
            createOrder()
        }
    }

    private fun setupRecyclerView() {
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateRecyclerView() {
        binding.cartRecyclerView.adapter = CartAdapter(orderedItems, requireContext(), sharedViewModel) { updatedItem ->
            handleCartUpdate(updatedItem)
        }
    }

    private fun handleCartUpdate(updatedItem: OrderedItem) {
        val existingItem = orderedItems.find { it.id == updatedItem.id }
        if (existingItem != null) {
            existingItem.quantity = updatedItem.quantity
            sharedViewModel.updateOrderedItem(existingItem)
        } else {
            sharedViewModel.addOrderedItem(updatedItem)
        }
    }

    private fun createOrder() {
        val client = sharedViewModel.user.value

        if (client == null) {
            Log.e("CartFragment", "Cannot create order: Client is null.")
            return
        }

        val order = Order(
            client = client,
            manager = null,
            listOrderedItem = orderedItems,
            orderStatus = "Ready",
            paymentType = null,
            discount = null
        )

        logOrderDetails(order)


        val intent = Intent(requireContext(), PayOutActivity::class.java)
        intent.putExtra("order", order)
        startActivity(intent)
    }


    private fun logOrderDetails(order: Order) {
        Log.d("CartFragment", "Order Details:")
        Log.d("CartFragment", "Client: ${order.client?.fullName ?: "No client"}")
        order.listOrderedItem?.forEach { item ->
            Log.d(
                "CartFragment",
                "OrderedItem ID: ${item.id}, Name: ${item.item?.itemName}, Quantity: ${item.quantity}"
            )
        }
        Log.d("CartFragment", "Order Status: ${order.orderStatus}")
        Log.d("CartFragment", "Payment Type: ${order.paymentType ?: "No payment type"}")
        Log.d("CartFragment", "Discount: ${order.discount ?: "No discount"}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }
}
