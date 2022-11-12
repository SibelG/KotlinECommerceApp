package com.example.ecommerceapp.models

data class User(
    val userId: String="",
    val userName: String="",
    val mobileNumber: String="",
    var cart: Cart=Cart(),
    val orders: ArrayList<String> = ArrayList(),
    val addresses: ArrayList<Address> = ArrayList())