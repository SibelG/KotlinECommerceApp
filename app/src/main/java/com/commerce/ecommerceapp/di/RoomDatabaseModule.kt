package com.commerce.ecommerceapp.di

import android.content.Context
import androidx.room.Room
import com.commerce.ecommerceapp.data.database.FavouriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            FavouriteDatabase::class.java,
            "fav_database"
        ).build()


    @Singleton
    @Provides
    fun provideYourDao(db: FavouriteDatabase) = db.getFavouriteDao()
}