package com.commerce.ecommerceapp.ui.choose_country

import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.commerce.ecommerceapp.BaseActivity
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.SharedPreferencesUtils
import com.commerce.ecommerceapp.SharedPreferencesUtils.getLanguagePosition
import com.commerce.ecommerceapp.SharedPreferencesUtils.setLanguageCode
import com.commerce.ecommerceapp.SharedPreferencesUtils.setLanguagePosition
import com.commerce.ecommerceapp.databinding.ActivityChooseCountryBinding
import com.commerce.ecommerceapp.helper.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class ChooseCountryActivity : AppCompatActivity() {


    private lateinit var binding: ActivityChooseCountryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_country)
        setContentView(binding.root)

        changeRadioButtonViews(getLanguagePosition(this))

        binding.languageRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            onRadioButtonClicked(radio)
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase!!))
    }

    private fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_english ->
                    if (checked) {
                        // English language selected
                        setLanguageCode(this, "en")
                        setLanguagePosition(this, 0)
                        recreate()
                        //updateLocale(Locale.ENGLISH)
                    }
                R.id.radio_turkish ->
                    if (checked) {
                        // Turkish language selected
                        setLanguageCode(this, "tr")
                        setLanguagePosition(this, 1)
                        recreate()
                       // updateLocale(Locales.Turkish)
                    }

            }
        }
    }

    private fun changeRadioButtonViews(position:Int){
        when(position){
            0 -> binding.radioEnglish.isChecked = true
            1 -> binding.radioTurkish.isChecked = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_dark -> AppCompatDelegate.setDefaultNightMode(UiModeManager.MODE_NIGHT_YES)
            R.id.action_light -> AppCompatDelegate.setDefaultNightMode(UiModeManager.MODE_NIGHT_NO)
        }

        return super.onOptionsItemSelected(item)
    }

}