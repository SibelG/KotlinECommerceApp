package com.commerce.ecommerceapp

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.commerce.ecommerceapp.MainActivity
import com.commerce.ecommerceapp.databinding.ActivityOtpSendBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class OtpSendActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "PhoneAuthActivity"
    }

    private lateinit var binding: ActivityOtpSendBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mVerificationId: String


    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpSendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth

        binding.btnSend.setOnClickListener {
            val number = binding.etPhone.text.toString().trim()
            val countryCode = binding.ccp.selectedCountryCodeWithPlus
            val phoneNumber = getPhoneNumber(number);

            otpSend("$countryCode$phoneNumber")
        }
    }

    private fun getPhoneNumber(phone: String): String? {
        if (phone.isEmpty())
            Toast.makeText(this,"Phone number is required!",Toast.LENGTH_LONG).show()
        else if (!Patterns.PHONE.matcher(phone).matches() || phone.length < 11)
           Toast.makeText(this,"Need valid phone number!",Toast.LENGTH_LONG).show()
        else
            return if (phone.substring(0, 1) == "0") phone.substring(1) else phone

        return null;
    }

    private fun otpSend(phoneNumberWithCountryCode: String) {
        isVisible(true)

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: ${credential.smsCode}")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                isVisible(false)
                Toast.makeText(this@OtpSendActivity,e.localizedMessage!!,Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
//                super.onCodeSent(verificationId, token)
                isVisible(false)
                Toast.makeText(this@OtpSendActivity,"OTP is successfully send.",Toast.LENGTH_LONG).show()
                mResendingToken = token
                mVerificationId = verificationId

                startActivity(
                    Intent(
                        this@OtpSendActivity,
                        OtpVerifyActivity::class.java
                    ).putExtra("phone", phoneNumberWithCountryCode)
                        .putExtra("verificationId", mVerificationId)
                        .putExtra("forceResendingToken", mResendingToken)
                )
            }
        }


        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("$phoneNumberWithCountryCode")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build()
        )
    }

    private fun isVisible(visible: Boolean) {
        binding.progressBar.isVisible = visible
        binding.btnSend.isVisible = !visible
    }
}