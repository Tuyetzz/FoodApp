package com.example.foodapp.user.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    val itemId: String = "",
    val itemName: String = "",
    val itemPrice: Double = 0.0,
    val shortDescription: String = "",
    val itemImage: String = "",
    val ingredients: List<String> = emptyList()
) : Parcelable
