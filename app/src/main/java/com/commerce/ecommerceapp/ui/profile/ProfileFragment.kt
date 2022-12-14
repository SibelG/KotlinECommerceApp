package com.commerce.ecommerceapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.commerce.ecommerceapp.AuthenticationActivity
import com.commerce.ecommerceapp.MainActivity
import com.commerce.ecommerceapp.adapters.AddressAdapter
import com.commerce.ecommerceapp.adapters.IAddressAdapter
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.FragmentProfileBinding
import com.commerce.ecommerceapp.models.Address
import com.commerce.ecommerceapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProfileFragment() : Fragment(), IAddressAdapter {

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

        val action = ProfileFragmentDirections.actionNavProfileToAddressFragment()
        findNavController().navigate(action)
        (activity as MainActivity).setDrawerLocked(true)

    }

    override fun onEditClicked(address: Address) {
        val action = ProfileFragmentDirections.actionNavProfileToEditAddressFragment(address)
        findNavController().navigate(action)
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