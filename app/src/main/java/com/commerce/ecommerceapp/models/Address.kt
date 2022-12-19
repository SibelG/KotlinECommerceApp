package com.commerce.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val userName: String="",
    val mobileNumber: String = "",
    val pincodeOfAddress: Int=0,
    val houseNumber: String="",
    val streetName: String="",
    val city: String=""):Parcelable