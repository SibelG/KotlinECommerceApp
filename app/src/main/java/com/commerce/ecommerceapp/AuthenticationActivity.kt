package com.commerce.ecommerceapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.commerce.ecommerceapp.Utils.currentUserId
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.ActivityAuthenticationBinding
import com.commerce.ecommerceapp.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {


    private lateinit var googleSignInClient: GoogleSignInClient
    private var userDao = UserDao()

    companion object {
        private const val RC_SIGN_IN = 1
    }


    private lateinit var binding: ActivityAuthenticationBinding
    lateinit var auth: FirebaseAuth
    var user= FirebaseAuth.getInstance().currentUser
    private val TAG = "AuthenticationActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)
        binding.activity = this
        auth = Firebase.auth



        FirebaseApp.initializeApp(/*context=*/ this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance())

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1010115632144-l24qee3v5e5ca06dlqgc99tplo2fn5gh.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleLoginBtn.setOnClickListener {
            signIn()

        }
        binding.phoneLoginBtn.setOnClickListener {
            val intent = Intent(this,OtpSendActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception

            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)

                    /*val email= account.email
                    val googleFirstName= account.givenName
                    val picture= account.photoUrl.toString()
                    val user = User(userId = auth.currentUser!!.uid,userName = googleFirstName.toString(),userEmail= email.toString(), userImage = picture)*/

                    if (user != null) {
                        for (profile in user!!.getProviderData()) {
                            // Id of the provider (ex: google.com)
                            val providerId = profile.providerId
                            val uid = profile.uid
                            val name = profile.displayName
                            val email = profile.email
                            val photoUrl: Uri? = profile.photoUrl

                            val user = User(userId = currentUserId,userName = name.toString(),userEmail= email.toString())
                            userDao.addUser(user)
                        }
                    }
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }
    /*private fun signUp(username: String) {
        val user = User(userId = auth.currentUser!!.uid,userName = username,mobileNumber = number)
        UserDao().addUser(this,user)
        val intent = Intent(this , MainActivity::class.java)
        startActivity(intent)
        finish()
    }*/


}