package com.commerce.ecommerceapp

import android.app.UiModeManager.MODE_NIGHT_NO
import android.app.UiModeManager.MODE_NIGHT_YES
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity

open class BaseActivity : LocaleAwareCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_dark -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            R.id.action_light -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }

        return super.onOptionsItemSelected(item)
    }
}