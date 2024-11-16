package com.example.foodapp.user.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var id: String? = null,
    var fullName: String? = null,
    var address: String? = null,
    var username: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var role: String? = null
) : Parcelable
