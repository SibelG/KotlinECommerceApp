package com.example.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Cart(
    val items: ArrayList<CartItem> = ArrayList(),
    var price: Float = 0F
): Parcelable