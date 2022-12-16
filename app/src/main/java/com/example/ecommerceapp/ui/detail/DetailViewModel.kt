package com.example.ecommerceapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.daos.DBRepository
import com.example.ecommerceapp.daos.ProductDao
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject
constructor(private val dbRepository : DBRepository
) : ViewModel() {

    fun favoriteLiveData(id: String) = dbRepository.getProductFromFavoriteLiveData(id)

    fun saveProductInFavorites(productModel: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.saveOrRemoveProductFromFavorite(productModel)
        }
    }


}