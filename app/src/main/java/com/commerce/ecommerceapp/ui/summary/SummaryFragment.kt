package com.commerce.ecommerceapp.ui.summary

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.commerce.ecommerceapp.MainActivity
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Utils
import com.commerce.ecommerceapp.adapters.SummaryProductAdapter
import com.commerce.ecommerceapp.daos.ProductDao
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.SummaryFragmentBinding
import com.commerce.ecommerceapp.models.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SummaryFragment(
) : Fragment(){

    private lateinit var viewModel: SummaryViewModel
    private lateinit var binding: SummaryFragmentBinding
    private var productDao = ProductDao()
    private lateinit var adapter: SummaryProductAdapter
    private lateinit var cartItemsOffline: ArrayList<CartItemOffline>
    private val args by navArgs<SummaryFragmentArgs>()
    private val cart by lazy { args.cartSummary}
    private val address by lazy { args.addressDetails}
    private lateinit var currentUser : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SummaryViewModel::class.java)
        binding = SummaryFragmentBinding.inflate(inflater)

        initializeCurrentUser()
        setUpAddress()
        setupRecyclerView()


        binding.continueButtonSummary.setOnClickListener {
            //go to payment screen
            goToPaymentFragment()
        }

        return binding.root
    }

    private fun goToPaymentFragment() {

        val bundle = Bundle()
        bundle.putParcelableArrayList(
            "itemAddress",
            cartItemsOffline as ArrayList<out Parcelable?>?
        )
        bundle.putParcelable("cart",cart)
        bundle.putParcelable("address",address)
        findNavController().navigate(R.id.action_summaryFragment_to_paymentFragment, bundle)
        /*val currentFragment = this
        val paymentFragment = PaymentFragment(currentFragment,currentUser,address,cart,cartItemsOffline)
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment,paymentFragment,getString(R.string.title_payment_fragment)).hide(currentFragment).commit()*/
    }



    private fun setupRecyclerView() {
        GlobalScope.launch {
            val items = cart.items
            cartItemsOffline = ArrayList()
            for (i in 0..(items.size-1)){
                val item = items[i]
                val product = productDao.getProductById(item.productId).await().toObject(Product::class.java)!!
                cartItemsOffline.add(CartItemOffline(item.productId,item.quantity,product))
            }
            withContext(Dispatchers.Main){
                adapter = SummaryProductAdapter(cartItemsOffline)
                binding.productsRecyclerViewSummary.adapter = adapter
                binding.totalAmountSummary.text = "???" + cart.price
                binding.payableAmountSummary.text = "???" + cart.price
            }
        }
    }
    private fun initializeCurrentUser() {
        var userDao= UserDao()
        GlobalScope.launch {
            currentUser =
                userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
        }
    }
    private fun setUpAddress() {
        binding.userNameAddressSummary.text = address.userName
        binding.phoneNumberAddressSummary.text = address.mobileNumber
        binding.houseAndStreetAddressSummary.text = address.houseNumber + ", " + address.streetName
        binding.cityPincodeAddressSummary.text = address.pincodeOfAddress.toString() + ", " + address.city
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = "Order Summary"

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


}