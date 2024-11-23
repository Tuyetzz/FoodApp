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
    private val sharedViewModel: SharedViewModel, // Pass SharedViewModel to update items
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
                // Set tên món ăn
                cartFoodName.text = cartItem.item?.itemName ?: "Unknown"

                // Set giá món ăn
                cartItemPrice.text = "$${String.format("%.2f", cartItem.item?.itemPrice ?: 0.0)}"

                // Set số lượng món ăn
                cartItemQuantity.text = cartItem.quantity.toString()

                // Set hình ảnh món ăn
                Glide.with(context)
                    .load(cartItem.item?.itemImage)
                    .placeholder(com.example.foodapp.R.drawable.ic_logo) // Placeholder nếu không tải được ảnh
                    .into(cartImage)

                // Xử lý khi nhấn nút tăng số lượng
                plusbtn.setOnClickListener {
                    cartItem.quantity = (cartItem.quantity ?: 0) + 1
                    cartItemQuantity.text = cartItem.quantity.toString()
                    sharedViewModel.updateOrderedItem(cartItem) // Cập nhật giỏ hàng trong SharedViewModel
                    onCartUpdated(cartItem) // Callback để cập nhật tổng giỏ hàng
                }

                // Xử lý khi nhấn nút giảm số lượng
                minusbtn.setOnClickListener {
                    // Giảm số lượng tối đa 1
                    if (cartItem.quantity ?: 1 > 1) {
                        cartItem.quantity = (cartItem.quantity ?: 0) - 1
                        cartItemQuantity.text = cartItem.quantity.toString()
                        sharedViewModel.updateOrderedItem(cartItem) // Cập nhật giỏ hàng trong SharedViewModel
                        onCartUpdated(cartItem) // Callback để cập nhật tổng giỏ hàng
                    }
                }

                // Xử lý khi nhấn nút xóa
                deletebtn.setOnClickListener {
                    removeItem(adapterPosition) // Gọi hàm xóa khi nhấn nút xóa
                }
            }
        }
    }

    private fun removeItem(position: Int) {
        if (position >= 0 && position < cartItems.size) {
            val removedItem = cartItems[position]
            cartItems.removeAt(position) // Xóa item khỏi danh sách giỏ hàng
            notifyItemRemoved(position) // Cập nhật RecyclerView
            notifyItemRangeChanged(position, cartItems.size) // Thông báo thay đổi sau khi xóa
            Log.d("CartAdapter", "Removed item: $removedItem")

            // Xóa món ăn khỏi giỏ hàng trong SharedViewModel
            sharedViewModel.removeOrderedItem(removedItem)
        }
    }
}
