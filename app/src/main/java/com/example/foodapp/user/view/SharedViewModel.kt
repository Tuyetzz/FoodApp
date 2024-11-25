package com.example.foodapp.user.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.user.model.Order
import com.example.foodapp.user.model.OrderedItem
import com.example.foodapp.user.model.UserModel

class SharedViewModel : ViewModel() {

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> get() = _user

    private val _orderedItems = MutableLiveData<MutableList<OrderedItem>>(mutableListOf())
    val orderedItems: LiveData<MutableList<OrderedItem>> get() = _orderedItems

    private val _currentOrder = MutableLiveData<Order?>()
    val currentOrder: LiveData<Order?> get() = _currentOrder

    // Đặt thông tin người dùng
    fun setUser(user: UserModel?) {
        _user.value = user
        Log.d("SharedViewModel", "User set: $user")
    }

    // Thêm món ăn vào giỏ hàng
    fun addOrderedItem(item: OrderedItem) {
        val updatedList = _orderedItems.value ?: mutableListOf()
        updatedList.add(item)
        _orderedItems.value = updatedList
        Log.d("SharedViewModel", "Item added: $item")
        Log.d("SharedViewModel", "Current cart: $updatedList")
    }

    // Cập nhật món ăn trong giỏ hàng
    fun updateOrderedItem(updatedItem: OrderedItem) {
        val updatedList = _orderedItems.value?.map { item ->
            if (item.id == updatedItem.id) updatedItem else item
        }?.toMutableList() ?: mutableListOf()
        _orderedItems.value = updatedList
        Log.d("SharedViewModel", "Item updated: $updatedItem")
        Log.d("SharedViewModel", "Updated cart: $updatedList")
    }

    // Xóa món ăn khỏi giỏ hàng
    fun removeOrderedItem(item: OrderedItem) {
        val updatedList = _orderedItems.value?.filterNot { it.id == item.id }?.toMutableList()
        _orderedItems.value = updatedList ?: mutableListOf()
        Log.d("SharedViewModel", "Item removed: $item")
        Log.d("SharedViewModel", "Updated cart: ${_orderedItems.value}")
    }

    // Xóa toàn bộ giỏ hàng
    fun clearOrderedItems() {
        _orderedItems.value = mutableListOf()
        Log.d("SharedViewModel", "Cart cleared.")
    }

    // Đặt đơn hàng hiện tại
    fun setOrder(order: Order?) {
        _currentOrder.value = order
        Log.d("SharedViewModel", "Order set: $order")
    }

    // Lấy đơn hàng hiện tại
    fun getOrder(): Order? {
        return _currentOrder.value.also {
            Log.d("SharedViewModel", "Getting current order: $it")
        }
    }

    // Cập nhật thông tin đơn hàng hiện tại
    fun updateOrder(updatedOrder: Order) {
        _currentOrder.value = updatedOrder
        Log.d("SharedViewModel", "Order updated: $updatedOrder")
    }

    // Xóa đơn hàng hiện tại
    fun clearOrder() {
        _currentOrder.value = null
        Log.d("SharedViewModel", "Order cleared.")
    }

    // Truy xuất danh sách món ăn đã đặt
    fun getOrderedItems(): List<OrderedItem> {
        val items = _orderedItems.value ?: emptyList()
        Log.d("SharedViewModel", "Getting ordered items: $items")
        return items
    }
}
