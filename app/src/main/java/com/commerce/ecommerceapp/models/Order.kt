package com.commerce.ecommerceapp.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Order(
    var orderId: String ="",
    val createdAt: Long = 0L,
    val cart: Cart = Cart(),
    val userId: String="",
    var isDelivered: Boolean=false,
    var orderStatus: OrderStatus = OrderStatus.PLACED,
    val trackingId: String="",
    val courierPartner: String="",
    val orderDate: String="",
    val paymentMethod: String = "",
    val paymentId: String="",
    val isPaid: Boolean = false,
    val address: Address = Address(),
    val deliveredDate: String = ""):Parcelable
