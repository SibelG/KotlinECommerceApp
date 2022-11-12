package com.example.ecommerceapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.models.Address
import com.example.ecommerceapp.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() {

    private val _addresses = MutableLiveData<List<Address>>()
    val addresses: LiveData<List<Address>>
        get() = _addresses

    private val _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    private val _mobileNumber = MutableLiveData<String>()
    val mobileNumber: LiveData<String>
        get() = _mobileNumber

    private var mAddresses: ArrayList<Address> = ArrayList()
    private val userDao = UserDao()
    private lateinit var currentUser: User

    init {
        _addresses.value = mAddresses
        _mobileNumber.value = ""
        _username.value = ""
        retrieveAllAddresses()
    }

    private fun retrieveAllAddresses() {
        viewModelScope.launch {
            currentUser =
                userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
            mAddresses = currentUser.addresses
            withContext(Dispatchers.Main) {
                _addresses.value = mAddresses
                _username.value = currentUser.userName
                _mobileNumber.value = currentUser.mobileNumber
            }
        }
    }
}