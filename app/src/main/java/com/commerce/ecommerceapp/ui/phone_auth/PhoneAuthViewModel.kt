package com.commerce.ecommerceapp.ui.phone_auth

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commerce.ecommerceapp.Resource
import com.commerce.ecommerceapp.daos.AuthenticationRepository
import com.commerce.ecommerceapp.ui.MainAuthState
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel
@Inject
constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _phoneAuthLiveData = MutableLiveData<MainAuthState>()
    val phoneMainAuthLiveData: LiveData<MainAuthState>
        get() = _phoneAuthLiveData
    fun setPhoneAuthLiveData(mainAuthState: MainAuthState) {
        _phoneAuthLiveData.value = mainAuthState
    }

    private val _signInStatusLiveData = MutableLiveData<Resource<Unit?>>()
    val signInStatusLiveData: LiveData<Resource<Unit?>> get() = _signInStatusLiveData

    private val _userInfoLiveData = MutableLiveData<Resource<String>>()
    val userInfoLiveData: LiveData<Resource<String>> get() = _userInfoLiveData
    fun setUserInformationValue() {
        _userInfoLiveData.value = Resource.Idle()
    }

    fun checkIfFirstAppOpened(): Boolean = authenticationRepository.checkIfFirstAppOpened()

    fun checkIfUserLoggedIn(): Boolean = authenticationRepository.checkIfUserLoggedIn()

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        _signInStatusLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _signInStatusLiveData.postValue(
                authenticationRepository.signInWithCredential(
                    credential
                )
            )
        }
    }

    fun phoneAuthCallBack() = authenticationRepository.phoneAuthCallBack(_phoneAuthLiveData)




}