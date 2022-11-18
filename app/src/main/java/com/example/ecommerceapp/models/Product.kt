package com.example.ecommerceapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
//@Entity
data class Product(
    //@PrimaryKey
    var productId: String = "",
    val productName: String="",
    var productImage: String="",
    var productPrice: Float = 0F,
    var availability: Boolean = true,
    var productBrand: Brand = Brand(),
    var productCategory: Category = Category(),
    val reviews: ArrayList<Review> = ArrayList(),
    var description: String = "",
): Parcelable
