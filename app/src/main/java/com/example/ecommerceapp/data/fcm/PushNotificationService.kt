package com.example.ecommerceapp.data.database.fcm

import android.util.Log
import com.example.ecommerceapp.FCMNotificationBuilder
import com.example.ecommerceapp.models.NotificationData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class PushNotificationService : FirebaseMessagingService() {

    private val TAG = "PushNotificationService"
    private val fcmNotificationBuilder by lazy { FCMNotificationBuilder(this) }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    // received message that sent from firebase cloud messaging with user id topic.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        try {
            // convert data message to notification class.
            NotificationData(
                remoteMessage.data["userUID"]!!,
                remoteMessage.data["title"]!!,
                remoteMessage.data["message"]!!
            ).let {
                // create notification with title and message that sent from cloud.
                fcmNotificationBuilder.buildNotification(it.title, it.message)
            }
        } catch (e: Exception) {
            Log.d(TAG, "onMessageReceived: ${e.message}")
        }
    }

}