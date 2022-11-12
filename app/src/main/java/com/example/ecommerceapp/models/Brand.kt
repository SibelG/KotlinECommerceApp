package com.example.ecommerceapp.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Brand(
    val brandId: String="",
    val brandTitle: String="",
    val brandImage: String="",
    val brandProductsNumber: Int=0
):Parcelable


