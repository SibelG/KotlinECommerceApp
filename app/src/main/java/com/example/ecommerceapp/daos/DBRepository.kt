package com.example.ecommerceapp.daos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ecommerceapp.data.database.FavouriteDatabase
import com.example.ecommerceapp.models.Product

class DBRepository constructor(val appDatabase: FavouriteDatabase) {

    /*suspend fun insertFavourite(favourite: Product) {
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
    }*/
    // change product in database (save or remove) by check if it saved before or not.
    suspend fun saveOrRemoveProductFromFavorite(productModel: Product) {
        val isSavedBefore = getProductFromFavorite(productModel.productId)
        return if (isSavedBefore) {
            appDatabase.getFavouriteDao().removeProductFromFavorites(productModel)
        } else {
            appDatabase.getFavouriteDao().saveProduct(productModel)
        }
    }

    // check if product is saved into favorite database or not .
    suspend fun getProductFromFavorite(id: String): Boolean {
        val productModel = appDatabase.getFavouriteDao().getSpecificFavoriteProduct(id)
        return productModel != null
    }

    // observe to specific product when save or not to change favorite icon .
    fun getProductFromFavoriteLiveData(id: String): LiveData<Product?> =
        appDatabase.getFavouriteDao().getSpecificFavoriteProductLiveData(id)

    fun getFavoriteProductsLiveData(): LiveData<List<Product>> =
        appDatabase.getFavouriteDao().getAllFavoriteProducts()
}