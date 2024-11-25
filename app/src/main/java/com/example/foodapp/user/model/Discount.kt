package com.example.foodapp.user.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Discount(
    var discountId: String? = null,
    var code: String? = null,
    var percentage: Float? = null,
    var expiryDate: String? = null
) : Parcelable
