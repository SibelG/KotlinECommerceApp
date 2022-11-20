package com.example.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Category(
    var categoryId: String="",
    val categoryTitle: String="",
    val categoryIcon: String=""
):Parcelable

