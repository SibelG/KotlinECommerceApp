package com.commerce.ecommerceapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.commerce.ecommerceapp.converters.Converters
import com.commerce.ecommerceapp.models.Product


@Database(entities = [Product::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavouriteDatabase: RoomDatabase() {

    abstract fun getFavouriteDao(): FavouriteDao

}