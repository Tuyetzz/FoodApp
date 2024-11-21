package com.example.foodapp.user.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderedItem(
    var id: String? = null,
    var item: MenuItem? = null,
    var quantity: Int? = null,
    var orderDate: String? = null
) : Parcelable
