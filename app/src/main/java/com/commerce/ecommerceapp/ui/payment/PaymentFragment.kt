package com.commerce.ecommerceapp.ui.payment

import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.commerce.ecommerceapp.MainActivity
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Utils
import com.commerce.ecommerceapp.daos.OrderDao
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.PaymentFragmentBinding
import com.commerce.ecommerceapp.models.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PaymentFragment(

) : Fragment() {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var binding: PaymentFragmentBinding
    private var orderDao = OrderDao()
    private var userDao = UserDao()
    private lateinit var cartItemsOffline:ArrayList<CartItemOffline>
    private lateinit var cart:Cart
    private lateinit var address:Address
    private lateinit var currentUser:User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PaymentFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)

        initializeCurrentUser()

        binding.placeOrderButton.setOnClickListener {
            if (binding.codRadioButton.isChecked) {
                placeTheOrder()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please choose an valid option",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        arguments?.let {
            cartItemsOffline = it.getParcelableArrayList<CartItemOffline>("itemAddress") as ArrayList<CartItemOffline>
            cart = it.getParcelable<Cart>("cart") as Cart
            address = it.getParcelable<Address>("address") as Address
        }



        return binding.root
    }
    private fun initializeCurrentUser() {
        GlobalScope.launch {
            currentUser =
                userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
        }
    }
    private fun placeTheOrder() {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val simpleTimeFormat = SimpleDateFormat("hh:mm aa")
        val dateString = simpleDateFormat.format(System.currentTimeMillis())
        val timeString = simpleTimeFormat.format(System.currentTimeMillis())
        val time = String.format("at %s on %s", timeString, dateString)
        val order = Order(
            cart = cart,
            userId = Utils.currentUserId,
            orderDate = time,
            paymentMethod = "COD",
            address = address,
            createdAt = System.currentTimeMillis()
        )
        currentUser.cart = Cart()
        userDao.updateProfile(currentUser)
        GlobalScope.launch {
            val orderId = orderDao.placeOrder(order)
            withContext(Dispatchers.Main){
                order.orderId = orderId

                val bundle = Bundle()
                bundle.putParcelableArrayList(
                    "itemAddress",
                    cartItemsOffline as ArrayList<out Parcelable?>?
                )
                bundle.putParcelable("order",order)
                findNavController().navigate(R.id.action_paymentFragment_to_orderDetailFragment, bundle)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = "Choose Payment Option"

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