package com.example.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CartItem(
    val productId: String = "",
    val productName: String = "",
    var quantity: Int = 0,
):Parcelable

data class CartItemOffline(
    val productId: String = "",
    var quantity: Int = 0,
    var product: Product = Product()
)
