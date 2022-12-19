package com.commerce.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    var reviewUid:String = "",
    val username: String= "",
    val date: String= "",
    val text: String= "",
    val rating: Float=0F):Parcelable