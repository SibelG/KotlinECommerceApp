package com.commerce.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String="",
    val userName: String="",
    val userImage: String="",
    val mobileNumber: String="",
    val userEmail: String="",
    var cart: Cart=Cart(),
    var credidCards: ArrayList<CredidCard> = ArrayList(),
    val orders: ArrayList<String> = ArrayList(),
    val notifyList: ArrayList<NotificationData> = ArrayList(),
    val addresses: ArrayList<Address> = ArrayList()):Parcelable