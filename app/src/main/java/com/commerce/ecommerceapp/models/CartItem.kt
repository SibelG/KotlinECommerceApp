package com.commerce.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CartItem(
    val productId: String = "",
    val productName: String = "",
    var quantity: Int = 0,
    var productImage: String = ""
):Parcelable

@Parcelize
data class CartItemOffline(
    val productId: String = "",
    var quantity: Int = 0,
    var product: Product = Product()
):Parcelable
