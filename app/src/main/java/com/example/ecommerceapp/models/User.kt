package com.example.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String="",
    val userName: String="",
    val userImage: String="",
    val mobileNumber: String="",
    var cart: Cart=Cart(),
    var credidCards: ArrayList<CredidCard> = ArrayList(),
    val orders: ArrayList<String> = ArrayList(),
    val addresses: ArrayList<Address> = ArrayList()):Parcelable