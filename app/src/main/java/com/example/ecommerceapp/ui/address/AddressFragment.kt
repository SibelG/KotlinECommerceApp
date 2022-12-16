package com.example.ecommerceapp.ui.address

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.closeFragment
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.AddressFragmentBinding
import com.example.ecommerceapp.models.Address
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.choose_address.ChooseAddressFragment
import com.example.ecommerceapp.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddressFragment() : Fragment() {

    private lateinit var viewModel: AddressViewModel
    private lateinit var binding: AddressFragmentBinding
    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddressFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)


        userDao = UserDao()

        binding.addAddressButton.setOnClickListener {
            addTheAddress()
        }
        return binding.root
    }

    private fun addTheAddress() {
        if (binding.usernameInAddress.text.isNotEmpty() && binding.mobileNumberInAddress.text.isNotEmpty() && binding.houseNumberInAddress.text.isNotEmpty() && binding.streetNameInAddress.text.isNotEmpty() && binding.pincodeInAddress.text.isNotEmpty() && binding.cityInAddress.text.isNotEmpty()) {
            val name = binding.usernameInAddress.text.toString()
            val number = binding.mobileNumberInAddress.text.toString()
            val houseNumber = binding.houseNumberInAddress.text.toString()
            val streetName = binding.streetNameInAddress.text.toString()
            val pincode = binding.pincodeInAddress.text.toString().toInt()
            val city = binding.cityInAddress.text.toString()
            val address = Address(name, number, pincode, houseNumber, streetName, city)
            GlobalScope.launch {
                val currentUser =
                    userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
                currentUser.addresses.add(address)
                userDao.updateProfile(currentUser)
                withContext(Dispatchers.Main) {
                    closeFragment()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please enter all the fields", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = "Add new address"
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