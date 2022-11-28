package com.example.ecommerceapp.ui.payment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.daos.OrderDao
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.PaymentFragmentBinding
import com.example.ecommerceapp.models.*
import com.example.ecommerceapp.ui.payment.PaymentViewModel
import com.example.rshlnapp.ui.orderDetails.OrderDetailFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PaymentFragment(
    val previousFragment: Fragment,
    val currentUser: User,
    val address: Address,
    val cart: Cart,
    private val cartItemsOffline: ArrayList<CartItemOffline>
) : Fragment() {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var binding: PaymentFragmentBinding
    private lateinit var orderDao: OrderDao
    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PaymentFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)

        orderDao = OrderDao()
        userDao = UserDao()

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

        (activity as MainActivity).supportActionBar?.title = "Choose Payment Option"

        return binding.root
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
                val currentFragment = this@PaymentFragment
                val orderDetailsFragment = OrderDetailFragment(currentFragment,order,cartItemsOffline)
                requireActivity().supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment,orderDetailsFragment,getString(R.string.title_order_details)).hide(currentFragment).commit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentFragment = this@PaymentFragment
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(currentFragment).show(previousFragment).commit()
                    (activity as MainActivity).supportActionBar?.title = "Order Summary"
                }
            })
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