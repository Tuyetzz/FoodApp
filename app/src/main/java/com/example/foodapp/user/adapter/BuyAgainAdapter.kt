package com.example.foodapp.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.BuyAgainItemBinding
import com.example.foodapp.user.model.OrderedItem

class BuyAgainAdapter(
    private val purchasedItems: List<OrderedItem>,
    private val context: Context
) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(purchasedItems[position])
    }

    override fun getItemCount(): Int = purchasedItems.size

    inner class BuyAgainViewHolder(private val binding: BuyAgainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderedItem: OrderedItem) {
            binding.apply {
                buyAgainFoodName.text = orderedItem.item?.itemName ?: "Unknown"
                buyAgainFoodPrice.text = "$${orderedItem.item?.itemPrice ?: 0.0}"

                // Sử dụng Glide để tải ảnh từ URL
                Glide.with(context)
                    .load(orderedItem.item?.itemImage)
                    .placeholder(com.example.foodapp.R.drawable.ic_logo) // Placeholder
                    .into(buyAgainFoodImage)
            }
        }
    }
}
