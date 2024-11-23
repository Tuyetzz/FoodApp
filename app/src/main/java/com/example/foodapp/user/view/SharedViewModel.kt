package com.example.foodapp.user.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.user.model.OrderedItem
import com.example.foodapp.user.model.UserModel

class SharedViewModel : ViewModel() {
    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    private val _orderedItems = MutableLiveData<MutableList<OrderedItem>>(mutableListOf())
    val orderedItems: LiveData<MutableList<OrderedItem>> get() = _orderedItems

    fun setUser(user: UserModel) {
        _user.value = user
    }

    fun addOrderedItem(item: OrderedItem) {
        _orderedItems.value?.let {
            it.add(item)
            _orderedItems.value = it // Trigger LiveData observers
        }
    }

    fun updateOrderedItem(updatedItem: OrderedItem) {
        _orderedItems.value?.let {
            val updatedList = it.map { item ->
                if (item.id == updatedItem.id) updatedItem else item
            }.toMutableList()  // Tạo một danh sách mới với item đã cập nhật
            _orderedItems.value = updatedList  // Cập nhật lại giá trị của LiveData
        }
    }

    // Thêm phương thức xóa món ăn khỏi giỏ hàng
    fun removeOrderedItem(item: OrderedItem) {
        _orderedItems.value?.let {
            it.removeIf { existingItem -> existingItem.id == item.id }
            _orderedItems.value = it  // Cập nhật lại giá trị của LiveData
        }
    }

    fun getOrderedItems(): List<OrderedItem> = _orderedItems.value ?: emptyList()
}
