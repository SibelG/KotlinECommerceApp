package com.example.ecommerceapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.AuthenticationActivity
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.Utils.currentUserId
import com.example.ecommerceapp.adapters.AddressAdapter
import com.example.ecommerceapp.adapters.IAddressAdapter
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.FragmentProfileBinding
import com.example.ecommerceapp.models.Address
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.account.AccountFragment
import com.example.ecommerceapp.ui.account.AccountFragmentDirections
import com.example.ecommerceapp.ui.address.AddressFragment
import com.example.ecommerceapp.ui.orders.OrdersFragment
import com.example.rshlnapp.ui.edit_address.EditAddressFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class ProfileFragment(val fromWhere: String) : Fragment(), IAddressAdapter {

    private lateinit var binding: FragmentProfileBinding
    lateinit var adapter: AddressAdapter
    private lateinit var userDao: UserDao
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater)
        currentUserId = Firebase.auth.currentUser!!.uid

        binding.addNewAddressButtonProfile.setOnClickListener {
            openAddressFragment()
        }

        auth = Firebase.auth
        userDao = UserDao()

        setupRecyclerView()
        binding.logOutButton.setOnClickListener {
            logout()
        }

        return binding.root
    }

    private fun logout() {
        auth.signOut()
        requireActivity().finish()
        val intent = Intent(requireActivity(), AuthenticationActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        /*requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentFragment = this@ProfileFragment
                    val accountFragment= AccountFragment()
                    if (fromWhere=="AccountFragment") {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .remove(currentFragment).show(accountFragment).commit()
                        (activity as MainActivity).supportActionBar?.title = "AccountFragment"
                        (activity as MainActivity).setDrawerLocked(false)
                        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
                    }
                }
            })*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            (activity as MainActivity).drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun setupRecyclerView() {
        GlobalScope.launch {
            val currentUser =
                userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            val addresses = currentUser.addresses
            withContext(Dispatchers.Main) {
                adapter = AddressAdapter(addresses, this@ProfileFragment)
                binding.addressRecyclerView.adapter = adapter
                binding.usernameInProfile.text = currentUser.userName
                binding.mobileNumberInProfile.text = currentUser.mobileNumber
            }
        }
    }

    private fun openAddressFragment() {
        val addressFragment = AddressFragment(this)
        val currentFragment = this
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment_content_main,
            addressFragment,
            getString(R.string.title_address_fragment)
        ).hide(currentFragment).commit()
        (activity as MainActivity).setDrawerLocked(true)

    }

    override fun onEditClicked(address: Address) {
        val currentFragment = this
        val editAddressFragment = EditAddressFragment(this,address)
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_content_main,editAddressFragment,getString(R.string.title_edit_address_fragment)).hide(currentFragment).commit()
        (activity as MainActivity).setDrawerLocked(true)
    }

    override fun onDeleteClicked(address: Address) {
        GlobalScope.launch {
            val currentUser = userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            currentUser.addresses.remove(address)
            userDao.updateProfile(currentUser)
            withContext(Dispatchers.Main){
                setupRecyclerView()
                Toast.makeText(requireContext(),"Address deleted",Toast.LENGTH_SHORT).show()
            }
        }
    }
}