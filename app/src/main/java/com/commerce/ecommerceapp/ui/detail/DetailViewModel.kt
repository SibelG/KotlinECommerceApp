package com.commerce.ecommerceapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commerce.ecommerceapp.daos.DBRepository
import com.commerce.ecommerceapp.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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