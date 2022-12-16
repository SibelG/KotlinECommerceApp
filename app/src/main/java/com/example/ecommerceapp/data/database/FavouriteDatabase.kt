package com.example.ecommerceapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ecommerceapp.converters.Converters
import com.example.ecommerceapp.models.Product


@Database(entities = [Product::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavouriteDatabase: RoomDatabase() {

    abstract fun getFavouriteDao(): FavouriteDao

}