package com.example.ecommerceapp

import android.graphics.Color.green
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.ecommerceapp.databinding.ActivityMainBinding
import com.example.ecommerceapp.summary.HelplineFragment
import com.example.ecommerceapp.ui.account.AccountFragment
import com.example.ecommerceapp.ui.cart.CartFragment
import com.example.ecommerceapp.ui.favorites.FavoritesFragment
import com.example.ecommerceapp.ui.home.HomeFragment
import com.example.ecommerceapp.ui.orders.OrdersFragment
import com.example.ecommerceapp.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), DrawerLocker {

    lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val connectionLiveData by lazy { ConnectionLiveData(this) }
    private var firstCheckInternetConnection = true

    //create variables for the fragments
    val homeFragment = HomeFragment()
    val ordersFragment = OrdersFragment()
    val profileFragment = ProfileFragment()
    val helplineFragment = HelplineFragment()
    val accountFragment = AccountFragment()
    val favoritesFragment = FavoritesFragment()
    val fragmentManager = supportFragmentManager

    var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNavView: BottomNavigationView= binding.navigation
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_orders,
                R.id.nav_helpline
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/
        //bottomNavView.setupWithNavController(navController)
        bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        openFragment(homeFragment)

        /*fragmentManager.beginTransaction().apply {
            add(
                R.id.nav_host_fragment_content_main,
                profileFragment,
                getString(R.string.menu_profile)
            ).hide(profileFragment)
            add(
                R.id.nav_host_fragment_content_main,
                ordersFragment,
                getString(R.string.menu_orders)
            ).hide(ordersFragment)
            add(
                R.id.nav_host_fragment_content_main,
                helplineFragment,
                getString(R.string.title_fragment_helpline)
            ).hide(helplineFragment)

            add(R.id.nav_host_fragment_content_main, homeFragment, getString(R.string.menu_home))
        }.commit()*/

        navView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId) {

                R.id.nav_home -> {
                    openFragment(homeFragment)
                    /*fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment)
                        .commit()
                    activeFragment = homeFragment*/

                    true
                }
                R.id.nav_profile -> {
                   openFragment(profileFragment)
                    true
                }
                R.id.nav_orders -> {
                    openFragment(ordersFragment)
                    true
                }
                R.id.nav_helpline -> {
                    openFragment(helplineFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }


        observeNetworkConnection()
    }
    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            item.setChecked(true)
            when (item.itemId) {

                R.id.homeFragment -> {
                    openFragment(homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.favoriteFragment -> {
                    openFragment(favoritesFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.accountFragment -> {
                    openFragment(accountFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_content_main, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun observeNetworkConnection() {
        connectionLiveData.observe(this) { isInternetAvailable ->
            if (isInternetAvailable && !firstCheckInternetConnection) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Snackbar.make(
                        binding.drawerLayout,
                        getString(R.string.backOnline),
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(getColor(R.color.green))
                        .show()
                }
            } else if (!isInternetAvailable) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Snackbar.make(
                        binding.drawerLayout,
                        getString(R.string.connectionLost),
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(getColor(R.color.red))
                        .show()
                }
            }

            firstCheckInternetConnection = false
        }
    }

    override fun onPause() {
        super.onPause()
        firstCheckInternetConnection = true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //here create click listener for the cart icon
        return when (item.itemId) {
            android.R.id.home -> {
                false
            }
            R.id.action_cart -> {
//                Toast.makeText(this,"Ruko zara sabr karo",Toast.LENGTH_LONG).show()
                val cartFragment = CartFragment()
                /*fragmentManager.beginTransaction().add(
                    R.id.nav_host_fragment_content_main,
                    cartFragment,
                    getString(R.string.title_cart_fragment)
                ).hide(activeFragment).commit()*/
                openFragment(cartFragment)
                setDrawerLocked(true)
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun setDrawerLocked(shouldLock: Boolean) {
        if (shouldLock) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }
}

interface DrawerLocker {
    fun setDrawerLocked(shouldLock: Boolean)
}