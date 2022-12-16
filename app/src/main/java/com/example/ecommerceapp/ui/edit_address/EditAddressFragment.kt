package com.example.ecommerceapp.ui.edit_address

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.FragmentEditAddressBinding
import com.example.ecommerceapp.models.Address
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.detail.DetailFragmentArgs
import com.example.ecommerceapp.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class EditAddressFragment() : Fragment() {

    private lateinit var binding: FragmentEditAddressBinding
    private lateinit var userDao: UserDao
    private lateinit var currentUser: User
    private val args by navArgs<EditAddressFragmentArgs>()
    private val address by lazy { args.editAddress }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditAddressBinding.inflate(inflater)
        userDao = UserDao()

        binding.editAddressButton.setOnClickListener {
            addTheAddress()
        }
        setUpEditText()
        return binding.root
    }

    private fun setUpEditText() {
        binding.usernameInEditAddress.setText(address.userName)
        binding.mobileNumberInEditAddress.setText(address.mobileNumber)
        binding.houseNumberInEditAddress.setText(address.houseNumber)
        binding.streetNameInEditAddress.setText(address.streetName)
        binding.pincodeInEditAddress.setText(address.pincodeOfAddress.toString())
        binding.cityInEditAddress.setText(address.city)
    }

    private fun addTheAddress() {
        //check if all the parameters are filled
        if (binding.usernameInEditAddress.text.isNotEmpty() && binding.mobileNumberInEditAddress.text.isNotEmpty() && binding.houseNumberInEditAddress.text.isNotEmpty() && binding.streetNameInEditAddress.text.isNotEmpty() && binding.pincodeInEditAddress.text.isNotEmpty() && binding.cityInEditAddress.text.isNotEmpty()) {
            //get all the field from user input
            val name = binding.usernameInEditAddress.text.toString()
            val number = binding.mobileNumberInEditAddress.text.toString()
            val houseNumber = binding.houseNumberInEditAddress.text.toString()
            val streetName = binding.streetNameInEditAddress.text.toString()
            val pincode = binding.pincodeInEditAddress.text.toString().toInt()
            val city = binding.cityInEditAddress.text.toString()
            //create the address object
            val newAddress = Address(name, number, pincode, houseNumber, streetName, city)
            GlobalScope.launch {
                //get the user
                currentUser =
                    userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
                //remove the old one
                currentUser.addresses.remove(address)
                //add the new one
                currentUser.addresses.add(newAddress)
                //update the user
                userDao.updateProfile(currentUser)
                withContext(Dispatchers.Main) {
                    goToProfileFragment()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please enter all the fields", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun goToProfileFragment() {
        val action = EditAddressFragmentDirections.actionEditAddressFragmentToNavProfile()
        findNavController().navigate(action)
        (activity as MainActivity).setDrawerLocked(false)
        Toast.makeText(requireContext(), "Address Edited successfully", Toast.LENGTH_LONG).show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = "Edit Address"
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_cart).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}