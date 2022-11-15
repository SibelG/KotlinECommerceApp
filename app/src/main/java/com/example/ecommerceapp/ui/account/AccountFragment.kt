package com.example.ecommerceapp.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.AuthenticationActivity
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.AddressFragmentBinding
import com.example.ecommerceapp.databinding.FragmentAccountBinding
import com.example.ecommerceapp.showToast
import com.example.ecommerceapp.ui.address.AddressFragment
import com.example.ecommerceapp.ui.address.AddressViewModel
import com.example.ecommerceapp.ui.credid_card.CredidCardFragment
import com.example.ecommerceapp.ui.orders.OrdersFragment
import com.example.ecommerceapp.ui.product_request.ProductRequestFragment
import com.example.ecommerceapp.ui.profile.ProfileFragment
import com.example.rshlnapp.ui.orderDetails.OrderDetailFragment
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: FragmentAccountBinding

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        (activity as MainActivity).supportActionBar?.title = "Account Settings"
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        binding.addressSection.setOnClickListener{
            openDeliveryAddressFragment()
        }
        binding.cardSection.setOnClickListener{
            openUserCredidCardFragment()
        }
        binding.profileSection.setOnClickListener{
            openProfileFragment()
        }
        binding.orderSection.setOnClickListener{
            openOrdersFragment()
        }
        binding.logoutSection.setOnClickListener{
            logout()
        }
        binding.requestSection.setOnClickListener{
            openProductRequestFragment()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    fun openOrdersFragment() {
        val currentFragment = this
        val ordersFragment = OrdersFragment()
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment_content_main,
            ordersFragment,
            getString(R.string.menu_orders)
        ).hide(currentFragment).commit()
    }

    fun openProfileFragment() {
        val profileFragment = ProfileFragment()
        val currentFragment = this
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment_content_main,
            profileFragment,
            getString(R.string.menu_profile)
        ).hide(currentFragment).commit()
        (activity as MainActivity).setDrawerLocked(true)
    }

    fun openDeliveryAddressFragment() {
        val currentFragment = this
        val addressFragment = AddressFragment(currentFragment)
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment_content_main,
            addressFragment,
            getString(R.string.menu_credid)
        ).hide(currentFragment).commit()
    }
    fun openUserCredidCardFragment() {
        val currentFragment = this
        val credidFragment = CredidCardFragment()
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment_content_main,
            credidFragment,
            getString(R.string.menu_credid)
        ).hide(currentFragment).commit()
    }

    fun openProductRequestFragment() {
        val currentFragment = this
        val productRequestFragment = ProductRequestFragment()
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment_content_main,
            productRequestFragment,
            getString(R.string.menu_request)
        ).hide(currentFragment).commit()
    }
    fun openHelpFragment() {
        // TODO: Add help information.
    }

    fun openAboutFragment() {
        // TODO: Add about app information.
    }

    fun logout() {
        firebaseAuth.signOut()
        navigateToAuthFragment()
        showToast(getString(R.string.logOutMessage))
    }
    private fun navigateToAuthFragment() {
        val intent = Intent(requireContext(), AuthenticationActivity::class.java)
        startActivity(intent)
    }

}