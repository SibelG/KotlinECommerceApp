package com.example.ecommerceapp

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Utils {

    val currentUserId = Firebase.auth.currentUser!!.uid
    const val BASE_LATITUDE = 26.0
    const val BASE_LONGITUDE = 32.0
    const val LOCATION_ZOOM = 20f
}