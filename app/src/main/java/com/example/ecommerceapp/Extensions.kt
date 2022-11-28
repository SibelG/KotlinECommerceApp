package com.example.ecommerceapp

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


fun Fragment.showToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun MainActivity.showBottomNav(){
    val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
    if(!navigation.isShown)
        navigation.visibility= View.VISIBLE
}
fun Fragment.closeFragment() {
    findNavController().popBackStack()
}
fun MainActivity.hideBottomNav(){
    val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
    navigation.visibility=View.GONE
}

fun Activity.hideKeyboard(editText: EditText){
    editText.clearFocus()
    val inputMethodManager: InputMethodManager? =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
}