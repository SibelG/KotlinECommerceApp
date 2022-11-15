package com.example.ecommerceapp

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Utils {

    val currentUserId = Firebase.auth.currentUser!!.uid

}