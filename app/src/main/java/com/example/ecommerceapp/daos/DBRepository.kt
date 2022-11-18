package com.example.ecommerceapp.daos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ecommerceapp.models.Product

/*class DBRepository constructor(val appDatabase: FavouriteDatabase) {

    suspend fun insertFavourite(favourite: Product) {
        return appDatabase.getFavouriteDao().saveFavourite(favourite)

    }

    suspend fun delete(favourite: Product) {
        appDatabase.getFavouriteDao().removeFavouriteFromFavorites(favourite)
    }

    fun getAllFavourites(): MutableLiveData<List<Product>> {
        return appDatabase.getFavouriteDao().getAllFavoriteFavourites()
    }
    fun searchDatabase(searchQuery: String): LiveData<Product?> {
        return appDatabase.getFavouriteDao().getSpecificFavoriteFavouriteLiveData(searchQuery)
    }

}*/