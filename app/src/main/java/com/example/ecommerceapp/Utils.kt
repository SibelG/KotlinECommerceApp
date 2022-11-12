package com.example.ecommerceapp

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Utils {

    val currentUserId = Firebase.auth.currentUser!!.uid

}