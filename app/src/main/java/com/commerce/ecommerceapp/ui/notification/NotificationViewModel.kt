package com.commerce.ecommerceapp.ui.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commerce.ecommerceapp.Utils.currentUserId
import com.commerce.ecommerceapp.daos.OrderDao
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.models.NotificationData
import com.commerce.ecommerceapp.models.Product
import com.commerce.ecommerceapp.ui.home.ProductStatus
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private var userDao: UserDao):ViewModel() {

    private val _notifyFilter = MutableLiveData<List<NotificationData>>()
    val notifyFilter: LiveData<List<NotificationData>>
        get() = _notifyFilter

    private val _listIsEmpty = MutableLiveData<Boolean>()
    val listIsEmpty: LiveData<Boolean>
        get() = _listIsEmpty

    init {
        getByNotify()
    }

    fun getByNotify(){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _notifyFilter.postValue(userDao.getNotify(currentUserId))
            }catch (e: Exception){
                Log.d("CHECKING",e.message.toString())
            }

            withContext(Dispatchers.Main) {
                if (_notifyFilter.value?.size == 0) {
                    _listIsEmpty.value = true
                    Log.d("Empty", "Empty Filter")
                }
            }

        }
    }
}