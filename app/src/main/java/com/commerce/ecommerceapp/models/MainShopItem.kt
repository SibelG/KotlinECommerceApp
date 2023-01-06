package com.commerce.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainShopItem(
    val id: String,
    val name: String,
    val sort: Int,
    var showInSimpleStyle: Boolean,
    val list: MutableList<Product> = mutableListOf()
): Parcelable
