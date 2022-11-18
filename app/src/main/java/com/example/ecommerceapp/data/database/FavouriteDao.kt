package com.example.ecommerceapp.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.ecommerceapp.models.Product


/*@Dao
interface FavouriteDao {

    @Insert(onConflict = REPLACE)
    fun saveFavourite(ProductModel: Product)

    @Query("SELECT * FROM Product")
    fun getAllFavoriteFavourites(): MutableLiveData<List<Product>>

    @Query("SELECT * FROM Product WHERE productId =:id")
    suspend fun getSpecificFavoriteFavourite(id: String): Product?

    @Query("SELECT * FROM Product WHERE productId =:id")
    fun getSpecificFavoriteFavouriteLiveData(id: String): LiveData<Product?>

    @Delete
    suspend fun removeFavouriteFromFavorites(Product: Product)

    @Query("DELETE FROM Product")
    suspend fun deleteAllFavourites()

    @Query("SELECT EXISTS (SELECT 1 FROM Product WHERE productId=:id)")
    fun isFavorite(id: String): Int
}*/