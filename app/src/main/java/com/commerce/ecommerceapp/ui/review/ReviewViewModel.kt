package com.commerce.ecommerceapp.ui.review

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commerce.ecommerceapp.daos.ProductDao
import com.commerce.ecommerceapp.models.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val productDao: ProductDao): ViewModel() {

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>>
        get() = _reviews

    private val _listIsEmpty = MutableLiveData<Boolean>()
    val listIsEmpty: LiveData<Boolean>
        get() = _listIsEmpty

    private var mItems: ArrayList<Review> = ArrayList()

    fun reviewAll(reviewId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _reviews.value = productDao.getReview(reviewId)
                //_reviews.postValue(productDao.getReview(reviewId))

            }catch (e: Exception){
                Log.d("CHECKING",e.message.toString())
            }
            withContext(Dispatchers.Main) {

                if (_reviews.value?.size == 0) {
                    _listIsEmpty.value = true
                }
            }
        }

    }
}