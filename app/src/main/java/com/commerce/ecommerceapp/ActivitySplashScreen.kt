package com.commerce.ecommerceapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.commerce.ecommerceapp.AuthenticationActivity
import com.commerce.ecommerceapp.databinding.ActivitySplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivitySplashScreen : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        binding!!.splashMotionLayout.setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                startActivity(Intent(this@ActivitySplashScreen, AuthenticationActivity::class.java))
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

        })
    }
}