package com.example.ecommerceapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ecommerceapp.converters.Converters
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

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
    @TypeConverters(Converters::class)
    val reviews: ArrayList<Review> = ArrayList(),
    var description: String = "",
    var quantity: Int = 0
): Parcelable
