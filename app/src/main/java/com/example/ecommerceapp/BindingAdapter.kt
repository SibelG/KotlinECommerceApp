package com.example.ecommerceapp

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.format.DateFormat
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.adapters.ProductAdapter
import com.example.ecommerceapp.models.Product
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("indicatorProgressColor")
fun indicatorColor(circularProgressIndicator: CircularProgressIndicator, color: String) {
    circularProgressIndicator.setIndicatorColor(Color.parseColor(color))
}

@BindingAdapter("loadGif")
fun loadGifIntoImageView(imageView: ImageView, imageGif: Drawable) {
    imageView.loadGif(R.drawable.out_of_stock)
}

@BindingAdapter("quantityEditText", "increasePrice")
fun changeProductQuantityValue(
    imageView: ImageView,
    quantityEditText: EditText,
    increasePrice: Boolean
) {
    imageView.setOnClickListener {
        var quantity = quantityEditText.text.toString().trim().toInt()
        if (increasePrice) {
            quantity++
        } else if (!increasePrice && quantity > 1) {
            quantity--
        }
        quantityEditText.setText(quantity.toString())
    }
}

@BindingAdapter("formatDate")
fun formatMilliSecondsDate(textView: TextView, milliseconds: Long){
    val df = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
    df.format(Date(milliseconds)).let {
        textView.text = it
    }
}