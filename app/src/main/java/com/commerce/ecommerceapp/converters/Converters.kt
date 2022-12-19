package com.commerce.ecommerceapp.converters

import androidx.room.TypeConverter
import com.commerce.ecommerceapp.models.Review
import com.google.common.reflect.TypeToken
import com.google.gson.Gson


class Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<Review> {
        val listType = object : TypeToken<ArrayList<Review?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Review?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}