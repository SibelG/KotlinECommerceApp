package com.example.ecommerceapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.daos.DBRepository
import com.example.ecommerceapp.daos.ProductDao
import com.example.ecommerceapp.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel constructor(
private val dbRepository: DBRepository
) : ViewModel() {

    private lateinit var _favoriteProductsLiveData: LiveData<List<Product>>
    val favoriteProductsLiveData get() = _favoriteProductsLiveData

    fun getFavoriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteProductsLiveData = dbRepository.getFavoriteProductsLiveData()
        }
    }



}