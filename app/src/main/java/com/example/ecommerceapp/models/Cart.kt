package com.example.ecommerceapp.models




data class Cart(val items: ArrayList<CartItem> = ArrayList(), var price: Float = 0F)