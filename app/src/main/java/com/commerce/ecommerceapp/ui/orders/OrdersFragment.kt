package com.commerce.ecommerceapp.ui.orders


import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.commerce.ecommerceapp.MainActivity
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.adapters.IOrderAdapter
import com.commerce.ecommerceapp.adapters.OrderAdapter
import com.commerce.ecommerceapp.daos.OrderDao
import com.commerce.ecommerceapp.daos.ProductDao
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.FragmentOrdersBinding
import com.commerce.ecommerceapp.models.CartItemOffline
import com.commerce.ecommerceapp.models.Order
import com.commerce.ecommerceapp.models.Product
import com.commerce.ecommerceapp.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class OrdersFragment : Fragment(), IOrderAdapter {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var currentUser: User
    private lateinit var userDao: UserDao
    private lateinit var orderDao: OrderDao
    private lateinit var adapter: OrderAdapter
    private lateinit var productDao: ProductDao
    private lateinit var currentUserId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater)
        userDao = UserDao()
        orderDao = OrderDao()
        productDao = ProductDao()
        currentUserId=Firebase.auth.currentUser!!.uid

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            (activity as MainActivity).drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        GlobalScope.launch {
            currentUser =
                userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            val ordersToBeSent = ArrayList<Order>()
            val orders = currentUser.orders
            for (item in orders) {
                val order = orderDao.getOrderById(item).await().toObject(Order::class.java)!!
                val cart = order.cart
                ordersToBeSent.add(order)
            }
            withContext(Dispatchers.Main) {
                adapter = OrderAdapter(this@OrdersFragment, ordersToBeSent)
                binding.ordersRecyclerView.adapter = adapter
            }
        }
    }

    override fun onOrderClicked(order: Order) {
        val items = order.cart.items
        GlobalScope.launch {
            val cartItemsOffline = ArrayList<CartItemOffline>()
            for (i in 0..(items.size-1)){
                val item = items[i]
                val product = productDao.getProductById(item.productId).await().toObject(Product::class.java)!!
                val cartItemOffline = CartItemOffline(item.productId,item.quantity,product)
                cartItemsOffline.add(cartItemOffline)
            }
            withContext(Dispatchers.Main){

                val bundle = Bundle()
                bundle.putParcelableArrayList(
                    "itemAddress",
                    cartItemsOffline as ArrayList<out Parcelable?>?
                )
                bundle.putParcelable("order",order)
                findNavController().navigate(R.id.action_nav_orders_to_orderDetailFragment, bundle)

            }
        }
    }


}