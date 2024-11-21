package com.example.foodapp.user.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var id: String? = null,
    var client: UserModel? = null,
    var manager: UserModel? = null,
    var listOrderedItem: List<OrderedItem>? = null,
    var orderStatus: String? = null,
    var paymentType: String? = null
) : Parcelable
