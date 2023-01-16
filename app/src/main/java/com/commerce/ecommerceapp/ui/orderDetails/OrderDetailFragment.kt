package com.commerce.ecommerceapp.ui.orderDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.commerce.ecommerceapp.FCMNotificationBuilder
import com.commerce.ecommerceapp.MainActivity
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Utils
import com.commerce.ecommerceapp.adapters.ClickListener
import com.commerce.ecommerceapp.adapters.OrderItemAdapter
import com.commerce.ecommerceapp.adapters.SummaryProductAdapter
import com.commerce.ecommerceapp.daos.OrderDao
import com.commerce.ecommerceapp.daos.ProductDao
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.OrderDetailFragmentBinding
import com.commerce.ecommerceapp.models.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class OrderDetailFragment(
) : Fragment(),ClickListener {

    private lateinit var viewModel: OrderDetailViewModel
    private lateinit var binding: OrderDetailFragmentBinding
    lateinit var cartItemsOffline: ArrayList<CartItemOffline>
    lateinit var order:Order
    private lateinit var orderDao: OrderDao
    private var  userDao = UserDao()
    private lateinit var fcmNotificationBuilder: FCMNotificationBuilder

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(OrderDetailViewModel::class.java)
        binding = OrderDetailFragmentBinding.inflate(inflater)
        orderDao= OrderDao()
        arguments?.let {
            cartItemsOffline = it.getParcelableArrayList<CartItemOffline>("itemAddress") as ArrayList<CartItemOffline>
            order = it.getParcelable<Order>("order") as Order
        }

        fcmNotificationBuilder = FCMNotificationBuilder(requireContext())

        setupRecyclerView()

        if (order.orderStatus== OrderStatus.PLACED || order.orderStatus== OrderStatus.APPROVED || order.orderStatus== OrderStatus.PACKED || order.orderStatus== OrderStatus.SHIPPED) {
            binding.cancelOrderButton.isEnabled = true
        }

        binding.cancelOrderButton.setOnClickListener {
            cancelOrder()
        }


        return binding.root
    }

    private fun setupRecyclerView() {
        val cart = order.cart
        val adapter = OrderItemAdapter(cartItemsOffline,this@OrderDetailFragment)
        binding.productsRecyclerViewOrder.adapter = adapter
        binding.orderIdOrderDetails.text = "ORDER ID - " + order.orderId
        binding.orderedOnOrder.text = "Ordered " + order.orderDate
        binding.orderStatusOrder.text = "Order status - " + order.orderStatus.toString()
        binding.userNameAddressOrder.text = order.address.userName
        binding.phoneNumberAddressOrder.text = order.address.mobileNumber
        binding.houseAndStreetAddressOrder.text =
            order.address.houseNumber + ", " + order.address.streetName
        binding.cityPincodeAddressOrder.text =
            order.address.pincodeOfAddress.toString() + ", " + order.address.city
        binding.totalAmountOrder.text = "₹" + cart.price
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = "Order Details"
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

    }

    private fun cancelOrder() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure you want to cancel order?")
        //setup the input
        val input = EditText(requireContext())
        //specify some of the things
        input.setHint("Enter the reason")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        //setup the buttons
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            val reason = input.text.toString()
            order.orderStatus = OrderStatus.CANCEL_REQUESTED
            orderDao.updateStatus(order)
            binding.orderStatusOrder.text = "Order status - " + order.orderStatus.toString()

            fcmNotificationBuilder.buildNotification("Cancel","Cancel your order ${order.orderId}")
            var notify = NotificationData(Utils.currentUserId,"Cancel","Cancel your order ${order.orderId}")
            userDao.addNotify(notify)

        })

        builder.setNegativeButton("No",DialogInterface.OnClickListener{ dialog, which ->
            //do nothing for now
        })

        builder.show()


    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_cart).setVisible(false)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun addReview(product: Product) {
        val action = OrderDetailFragmentDirections.actionOrderDetailFragmentToAddReviewFragment(product)
        findNavController().navigate(action)
    }

}