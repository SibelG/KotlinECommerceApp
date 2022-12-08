package com.example.ecommerceapp.ui.choose_address

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
import com.example.ecommerceapp.adapters.ChooseAddressAdapter
import com.example.ecommerceapp.adapters.IChooseAddressAdapter
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.ChooseAddressFragmentBinding
import com.example.ecommerceapp.models.Cart
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.account.AccountFragmentDirections
import com.example.ecommerceapp.ui.address.AddressFragment
import com.example.ecommerceapp.ui.detail.DetailFragment
import com.example.ecommerceapp.ui.detail.DetailFragmentArgs
import com.example.ecommerceapp.ui.detail.DetailFragmentDirections
import com.example.ecommerceapp.ui.summary.SummaryFragment
import com.example.ecommerceapp.ui.tracking.LocateUserLocationFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ChooseAddressFragment() : Fragment(),
    IChooseAddressAdapter {

    private lateinit var binding: ChooseAddressFragmentBinding
    private lateinit var adapter: ChooseAddressAdapter
    private lateinit var userDao: UserDao
    private lateinit var currentUser: User
    private val args by navArgs<ChooseAddressFragmentArgs>()
    private val cartModel by lazy { args.cartDetails }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChooseAddressFragmentBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.title = "Choose Address"
        userDao = UserDao()
        setupRecyclerView()

        //add new address button click listener
        binding.addNewAddressChooseAddress.setOnClickListener {
            val action = ChooseAddressFragmentDirections.actionChooseAddressFragmentToAddressFragment()
            findNavController().navigate(action)

        }
        //continue button action
        binding.deliverHereButtonChooseAddress.setOnClickListener {
            val position = adapter.lastSelectedPosition
            if (position < 0) {
                Toast.makeText(requireContext(), "Please choose an address", Toast.LENGTH_SHORT)
                    .show()
            } else {
                goToSummaryFragment(position)
            }
        }
        binding.selectAddress.setOnClickListener {
            selectUserLocation()
        }

        return binding.root
    }

    private fun goToSummaryFragment(position: Int) {
        val address = currentUser.addresses[position]
       /* val currentFragment = this
        val summaryFragment = SummaryFragment(currentFragment, currentUser, address, cartModel)*/
        val action = ChooseAddressFragmentDirections.actionChooseAddressFragmentToSummaryFragment(address,cartModel)
        findNavController().navigate(action)
        /*requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.nav_host_fragment,
            summaryFragment,
            getString(R.string.title_summary_fragment)
        ).commit()*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        /*requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentFragment = this@ChooseAddressFragment
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(currentFragment).show(previousFragment).commit()
                    if (previousFragment is DetailFragment) {
                        (activity as MainActivity).supportActionBar?.title = "Product Details"
                    } else {
                        (activity as MainActivity).supportActionBar?.title = "Cart"
                    }
                }
            })*/
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_cart).setVisible(false)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    fun selectUserLocation() {

        val currentFragment = this
        val locationFragment= LocateUserLocationFragment(currentFragment)
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment,
            locationFragment,
            getString(R.string.title_locate)
        ).hide(currentFragment).commit()
    }

    fun setupRecyclerView() {
        GlobalScope.launch {
            currentUser =
                userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
            val addresses = currentUser.addresses
            withContext(Dispatchers.Main) {
                adapter = ChooseAddressAdapter(addresses, this@ChooseAddressFragment)
                binding.chooseAddressRecyclerView.adapter = adapter
            }
        }
    }
}