package com.commerce.ecommerceapp


import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.commerce.ecommerceapp.databinding.ActivityMainBinding
import com.commerce.ecommerceapp.ui.cart.CartViewModel
import com.commerce.ecommerceapp.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DrawerLocker {

    lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val connectionLiveData by lazy { ConnectionLiveData(this) }
    private var firstCheckInternetConnection = true
    private lateinit var shopViewModel: CartViewModel
    var quantity: String = ""
    var userMail : String = ""
    lateinit var drawerToggle:ActionBarDrawerToggle

    //create variables for the fragments
    val homeFragment = HomeFragment()
    val fragmentManager = supportFragmentManager

    var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout
        var  navView: NavigationView = binding.navView
        val bottomNavView : BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        navController = findNavController(R.id.nav_host_fragment)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.emailText).text =
            Firebase.auth.currentUser?.email?:""

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_favorite, R.id.nav_account), drawerLayout)

        navView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)


        /*shopViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        shopViewModel.subQuantity.observe(this, Observer<String> {
            quantity = it
            invalidateOptionsMenu()

        })*/

        observeNetworkConnection()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val menuItem:MenuItem = menu.findItem(R.id.action_cart)
        val actionView: View = menuItem.getActionView()

        val cartBadgeTextView: TextView = actionView.findViewById(R.id.cart_badge_text_view)

        cartBadgeTextView.text = quantity
        cartBadgeTextView.setVisibility(if (quantity == "0") View.GONE else View.VISIBLE)

        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //here create click listener for the cart icon
        return when (item.itemId) {
            android.R.id.home -> {
               /* onBackPressed()
                true*/
                false
            }
            R.id.action_cart -> {
//                Toast.makeText(this,"Ruko zara sabr karo",Toast.LENGTH_LONG).show()
                navController.navigate(R.id.action_cart)
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