package com.commerce.ecommerceapp.daos

import android.util.Log
import com.commerce.ecommerceapp.Utils
import com.commerce.ecommerceapp.Utils.currentUserId
import com.commerce.ecommerceapp.models.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class OrderDao @Inject constructor() {
    //get the instance of the database
    private val db = FirebaseFirestore.getInstance()
    val userDao = UserDao()

    //get the collection
    val ordersCollection = db.collection("orders")
    val usersCollection = db.collection("users")


    suspend fun placeOrder(order: Order): String {
        val documentId = ordersCollection.add(order).await().id
        val userDao = UserDao()
        val user = userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
        order.orderId = documentId
        user.orders.add(documentId)
        ordersCollection.document(documentId).set(order)
        usersCollection.document(Utils.currentUserId).set(user)
        return documentId
    }

    fun getOrderById(orderId: String): Task<DocumentSnapshot> {
        return ordersCollection.document(orderId).get()
    }

    fun updateStatus(order: Order) {
        GlobalScope.launch {
            ordersCollection.document(order.orderId).set(order)
        }
    }
}