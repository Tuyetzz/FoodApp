package com.example.foodapp.user.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.CartItemBinding
import com.example.foodapp.user.model.OrderedItem
import com.example.foodapp.user.view.SharedViewModel

class CartAdapter(
    private val cartItems: MutableList<OrderedItem>,
    private val context: Context,
    private val sharedViewModel: SharedViewModel,
    private val onCartUpdated: (OrderedItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: OrderedItem) {
            binding.apply {
                cartFoodName.text = cartItem.item?.itemName ?: "Unknown"
                cartItemPrice.text = "$${String.format("%.2f", cartItem.item?.itemPrice ?: 0.0)}"
                cartItemQuantity.text = cartItem.quantity.toString()
                Glide.with(context)
                    .load(cartItem.item?.itemImage)
                    .placeholder(com.example.foodapp.R.drawable.ic_logo) //neu ko co anh
                    .into(cartImage)

                //tang so luong
                plusbtn.setOnClickListener {
                    cartItem.quantity = (cartItem.quantity ?: 0) + 1
                    cartItemQuantity.text = cartItem.quantity.toString()
                    sharedViewModel.updateOrderedItem(cartItem)
                    onCartUpdated(cartItem)
                    logOrderedItems()
                }

                //giam so luong
                minusbtn.setOnClickListener {
                    if (cartItem.quantity ?: 1 > 1) {
                        cartItem.quantity = (cartItem.quantity ?: 0) - 1
                        cartItemQuantity.text = cartItem.quantity.toString()
                        sharedViewModel.updateOrderedItem(cartItem)
                        onCartUpdated(cartItem)
                        logOrderedItems()
                    }
                }

                // Xử lý khi nhấn nút xóa
                deletebtn.setOnClickListener {
                    removeItem(adapterPosition) // Gọi hàm xóa khi nhấn nút xóa
                    logOrderedItems() // Log trạng thái giỏ hàng sau khi thay đổi
                }
            }
        }
    }

    private fun removeItem(position: Int) {
        if (position >= 0 && position < cartItems.size) {
            val removedItem = cartItems[position]
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
            Log.d("CartAdapter", "Removed item: $removedItem")

            sharedViewModel.removeOrderedItem(removedItem)
        }
    }

    // Log tất cả các OrderedItem trong giỏ hàng
    private fun logOrderedItems() {
        Log.d("CartAdapter", "Current cart items:")
        for (item in cartItems) {
            Log.d("CartAdapter", "Item: ${item.item?.itemName}, Quantity: ${item.quantity}")
        }
    }
}
