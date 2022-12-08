package com.example.ecommerceapp.ui.account

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.daos.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(): ViewModel() {

    /*private val _userInfoLiveData = MutableLiveData<String>()
    val userInfoLiveData: LiveData<String> get() = _userInfoLiveData

    fun uploadUserInformation(userName: String, imageUri: Uri?, userEmail: String,context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            _userInfoLiveData.postValue(userDao.uploadUserInformation(userName, imageUri, userEmail,context))
        }

    }*/
}