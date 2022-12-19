package com.commerce.ecommerceapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CredidCard(
    val cardNumber: Long=0L,
    val cardCvc: String="",
    val cardExpiryDate: String="",
    val cardType: CardTypeEnum= CardTypeEnum.VISA_CARD,
): Parcelable
