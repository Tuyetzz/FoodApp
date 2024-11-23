package com.example.foodapp.user.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MenuItemBinding
import com.example.foodapp.user.model.MenuItem
import com.example.foodapp.user.model.OrderedItem
import com.example.foodapp.user.view.DetailsActivity
import com.example.foodapp.user.view.SharedViewModel

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val context: Context,
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuItems[position])
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(menuItems[position])
                }
            }

            binding.menuAddToCart.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    addToOrderedItems(menuItems[position])
                }
            }
        }

        private fun openDetailsActivity(menuItem: MenuItem) {
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("MenuItemId", menuItem.itemId)
                putExtra("MenuItemName", menuItem.itemName)
                putExtra("MenuItemImage", menuItem.itemImage)
                putExtra("MenuItemDescription", menuItem.shortDescription)
                putExtra("MenuItemIngredients", menuItem.ingredients.joinToString(", "))
                putExtra("MenuItemPrice", menuItem.itemPrice)
            }
            context.startActivity(intent)
        }

        private fun addToOrderedItems(menuItem: MenuItem) {
            val existingItem = sharedViewModel.getOrderedItems()
                .find { it.item?.itemId == menuItem.itemId }

            if (existingItem == null) {
                val newOrderedItem = OrderedItem(
                    id = menuItem.itemId,
                    item = menuItem,
                    quantity = 1,
                    orderDate = null
                )
                sharedViewModel.addOrderedItem(newOrderedItem)
                Log.d("MenuAdapter", "Added new OrderedItem: $newOrderedItem")
            } else {
                Log.d("MenuAdapter", "Item already exists in OrderedItems: $existingItem")
            }

            Log.d("MenuAdapter", "Updated OrderedItems List: ${sharedViewModel.getOrderedItems()}")
        }

        fun bind(menuItem: MenuItem) {
            binding.apply {
                menuFoodName.text = menuItem.itemName
                menuPrice.text = "$${String.format("%.2f", menuItem.itemPrice)}"

                Glide.with(context)
                    .load(menuItem.itemImage)
                    .placeholder(com.example.foodapp.R.drawable.ic_logo)
                    .into(menuImage)
            }
        }
    }
}
