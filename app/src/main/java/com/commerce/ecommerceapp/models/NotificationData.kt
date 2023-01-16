package com.commerce.ecommerceapp.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize


@Parcelize
data class NotificationData(
    var userUID: String = "",
    val title: String = "",
    val message: String= ""
):Parcelable


fun convertDocumentsToNotifyList(document: List<DocumentSnapshot>): List<NotificationData>{
    val list = mutableListOf<NotificationData>()
    document.forEach {
        list.add(
            NotificationData(it["userUID"].toString(), it["title"].toString(), it["message"].toString())
        )
    }
    return list
}
fun NotificationData.toMap(): Map<String, Any>{
    val map = mutableMapOf<String, Any>()
    map.let {
        it["userUID"] = userUID
        it["title"] = title
        it["message"] = message

    }
    return map
}
fun convertMapToNotifyItem(map: Map<String, Any>): NotificationData =
    NotificationData(map["userUID"].toString(), map["title"].toString(), map["message"].toString())