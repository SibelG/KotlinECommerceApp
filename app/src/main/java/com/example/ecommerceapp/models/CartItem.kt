package com.example.ecommerceapp.models



data class CartItem(
    val productId: String = "",
    val productName: String = "",
    var quantity: Int = 0,
)

data class CartItemOffline(
    val productId: String = "",
    var quantity: Int = 0,
    var product: Product = Product()
)
