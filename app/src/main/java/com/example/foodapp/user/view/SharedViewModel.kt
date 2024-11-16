// File path: com.example.foodapp.user.view.SharedViewModel
package com.example.foodapp.user.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.user.model.UserModel

class SharedViewModel : ViewModel() {
    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    fun setUser(user: UserModel) {
        _user.value = user
    }
}
