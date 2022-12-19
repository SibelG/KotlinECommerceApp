package com.commerce.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Brand(
    var brandId: String="",
    val brandTitle: String="",
    val brandImage: String="",
    val brandProductsNumber: Int=0
):Parcelable


