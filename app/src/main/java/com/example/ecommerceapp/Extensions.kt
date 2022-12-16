package com.example.ecommerceapp

import android.app.Activity
import android.app.ProgressDialog.show
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat


fun Fragment.showToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
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

fun ImageView.loadImage(link: String){
    Glide.with(context).load(link).centerCrop().override(200,200).into(this)
}

fun Activity.hideKeyboard(editText: EditText){
    editText.clearFocus()
    val inputMethodManager: InputMethodManager? =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun ImageView.loadGif(gifImage: Int){
    Glide.with(this.context).asGif().load(gifImage).into(this)
}
fun ImageView.loadTimerGif(gifImage: Int){
    Glide.with(this.context).asGif().load(gifImage).listener(object: RequestListener<GifDrawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<GifDrawable>?,
            isFirstResource: Boolean
        ): Boolean = false

        override fun onResourceReady(
            resource: GifDrawable?,
            model: Any?,
            target: Target<GifDrawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            resource!!.setLoopCount(1)
            resource.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable) {
                    if(!resource.isRunning)
                        this@loadTimerGif.setImageResource(gifImage)
                }
            })
            return false
        }
    }).into(this)


}