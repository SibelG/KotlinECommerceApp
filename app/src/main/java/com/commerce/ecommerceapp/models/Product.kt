package com.commerce.ecommerceapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Product(
    @PrimaryKey
    var productId: String = "",
    val productName: String="",
    var productImage: String="",
    var productPrice: Float = 0F,
    var availability: Boolean = true,
    var productBrand: String = "",
    var productCategory: String = "",
    val reviews: ArrayList<Review> = ArrayList(),
    var description: String = "",
    var quantity: Int = 0
): Parcelable
