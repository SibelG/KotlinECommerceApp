package com.commerce.ecommerceapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.commerce.ecommerceapp.models.Product


@Dao
interface FavouriteDao {

    @Insert(onConflict = REPLACE)
    fun saveProduct(productModel: Product)

    @Query("SELECT * FROM Product")
    fun getAllFavoriteProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM Product WHERE productId =:id")
    suspend fun getSpecificFavoriteProduct(id: String): Product?

    @Query("SELECT * FROM Product WHERE productId =:id")
    fun getSpecificFavoriteProductLiveData(id: String): LiveData<Product?>

    @Delete
    suspend fun removeProductFromFavorites(productModel: Product)

    @Query("DELETE FROM Product")
    suspend fun deleteAllProducts()
}