package com.commerce.ecommerceapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.commerce.ecommerceapp.ui.account.AccountFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


fun Fragment.showToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

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

fun EditText.searchListener(func: ()-> Unit){
    setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            func()
            return@OnEditorActionListener true
        }
        false
    })
}

fun Context.startHomeActivity() =
    Intent(this, MainActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, AuthenticationActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
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