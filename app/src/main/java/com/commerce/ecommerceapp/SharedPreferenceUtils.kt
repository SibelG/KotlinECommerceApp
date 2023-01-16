package com.commerce.ecommerceapp

import android.content.Context
import android.content.SharedPreferences


object SharedPreferencesUtils {

    fun getLanguageCode(mContext: Context):String{
        val sharedPreferences: SharedPreferences =mContext.getSharedPreferences("languageCodePrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("languageCodeValue", "en")!!
    }

    fun setLanguageCode(mContext: Context, code: String){
        val sharedPreferences: SharedPreferences=mContext.getSharedPreferences("languageCodePrefs", Context.MODE_PRIVATE)
        val sharedPreferencesEditor: SharedPreferences.Editor  = sharedPreferences.edit()
        sharedPreferencesEditor.putString("languageCodeValue", code)
        sharedPreferencesEditor.apply()
    }

    fun getLanguagePosition(mContext: Context):Int{
        val sharedPreferences: SharedPreferences=mContext.getSharedPreferences("languagePositionPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("languagePositionValue", 0)
    }

    fun setLanguagePosition(mContext: Context, number: Int){
        val sharedPreferences: SharedPreferences=mContext.getSharedPreferences("languagePositionPrefs", Context.MODE_PRIVATE)
        val sharedPreferencesEditor: SharedPreferences.Editor  = sharedPreferences.edit()
        sharedPreferencesEditor.putInt("languagePositionValue", number)
        sharedPreferencesEditor.apply()
    }


}