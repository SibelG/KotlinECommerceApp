package com.commerce.ecommerceapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commerce.ecommerceapp.Resource
import com.commerce.ecommerceapp.daos.DBRepository
import com.commerce.ecommerceapp.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
private val dbRepository: DBRepository
) : ViewModel() {

    private lateinit var  _favoriteProductsLiveData: LiveData<List<Product>>
    val favoriteProductsLiveData get() = _favoriteProductsLiveData

    private val _cartProductsLiveData = MutableLiveData<Resource<Any>>()
    val cartProductsLiveData: LiveData<Resource<Any>> = _cartProductsLiveData

    init {
        getFavoriteProducts()
    }


    fun getFavoriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteProductsLiveData = dbRepository.getFavoriteProductsLiveData()
        }
    }

    fun addProductsToCart(favList: List<Product>) {
        _cartProductsLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _cartProductsLiveData.postValue(
                dbRepository.addProductsToCart(favList, true)
            )
        }
    }

}