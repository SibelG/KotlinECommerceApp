package com.example.ecommerceapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.models.Address
import com.example.ecommerceapp.models.Brand
import com.example.ecommerceapp.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*class FavoritesViewModel(private val dbRepository: DBRepository): ViewModel() {


    private lateinit var _favoriteProducts: LiveData<List<Product>>
    val favoriteProductsLiveData get() = _favoriteProducts

    private var mFav: ArrayList<Product> = ArrayList()
    init {
        getAllFavourites()
    }

    fun insertFavourite(favourite: Product) {
        viewModelScope.launch {
           dbRepository.insertFavourite(favourite)

        }
    }

    fun deleteFavourite(favourite: Product) {
        viewModelScope.launch {

            dbRepository.delete(favourite)
        }
    }
    fun getAllFavourites()=

        viewModelScope.launch {
           _favoriteProducts=dbRepository.getAllFavourites()

    }

    fun getIsFavourites(id:Int) {
        viewModelScope.launch {
            //dbRepository.isFavourite(id)
        }
    }
}*/