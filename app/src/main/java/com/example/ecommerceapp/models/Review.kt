package com.example.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val username: String="",
    val text: String="",
    val rating: Float=0F):Parcelable