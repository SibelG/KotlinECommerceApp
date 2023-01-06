package com.commerce.ecommerceapp.daos

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Resource
import com.commerce.ecommerceapp.helper.SharedPreferenceHelper
import com.commerce.ecommerceapp.ui.MainAuthState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class AuthenticationRepository
@Inject
constructor(
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
    @ApplicationContext private val context: Context,
) {

    private val userUid by lazy { firebaseAuth.uid!! }
    private val firebaseUserCollection by lazy { firebaseFirestore.collection("users") }

    init {
        firebaseAuth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
    }
    fun checkIfFirstAppOpened(): Boolean = sharedPreferenceHelper.checkIfFirstAppOpened()

    fun checkIfUserLoggedIn(): Boolean {
        val user = firebaseAuth.currentUser
        return user != null
    }

    fun phoneAuthCallBack(_phoneMainAuthLiveData: MutableLiveData<MainAuthState>): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _phoneMainAuthLiveData.value =
                    MainAuthState.SuccessWithCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _phoneMainAuthLiveData.value = MainAuthState.Error(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                _phoneMainAuthLiveData.value =
                    MainAuthState.SuccessWithCode(verificationId, token)
            }
        }
    }


    suspend fun signInWithCredential(credential: AuthCredential): Resource<Unit?> {
        return try {
            firebaseAuth.signInWithCredential(credential).await()
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.errorMessage))
        }
    }


    suspend fun changeUserLocation(location: String): Boolean {
        return try {
            firebaseFirestore.collection("users").document(userUid).update(
                mapOf("userLocationName" to location)
            ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

}