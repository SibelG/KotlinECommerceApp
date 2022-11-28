package com.example.ecommerceapp.ui.cart

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.adapters.CartAdapter
import com.example.ecommerceapp.adapters.ICartAdapter
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.CartFragmentBinding
import com.example.ecommerceapp.models.CartItem
import com.example.ecommerceapp.models.CartItemOffline
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.choose_address.ChooseAddressFragment
import com.example.ecommerceapp.ui.detail.DetailFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CartFragment : Fragment(), ICartAdapter {

    private lateinit var viewModel: CartViewModel
    private lateinit var binding: CartFragmentBinding
    private lateinit var adapter: CartAdapter
    private lateinit var userDao: UserDao
    private lateinit var currentUser: User
    var cartQuantity=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CartFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        (activity as MainActivity).supportActionBar?.title = "Cart"
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        userDao = UserDao()
        binding.viewModel = viewModel
        adapter = CartAdapter(this)
        binding.cartRecyclerView.adapter = adapter

        initializeCurrentUser()

        viewModel.items.observe(viewLifecycleOwner, {
            adapter.notifyDataSetChanged()
        })

        viewModel.subtotal.observe(viewLifecycleOwner, {
            binding.totalAmountCart.text = it
        })

        viewModel.subQuantity.observe(viewLifecycleOwner, {
            cartQuantity = it
        })


        viewModel.listIsEmpty.observe(viewLifecycleOwner, {
            if (it) {
                binding.proceedToBuyCart.visibility = View.GONE
                binding.totalAmountCart.visibility = View.GONE
                binding.emptyCartTextview.visibility = View.VISIBLE
                binding.totalAmountLabelCart.visibility = View.GONE
            }
        })

        binding.proceedToBuyCart.setOnClickListener {
            proceedToCheckout()
        }

        return binding.root
    }

    private fun initializeCurrentUser() {
        GlobalScope.launch {
            currentUser =
                userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
        }
    }

    private fun proceedToCheckout() {
        val currentFragment = this
        //get the cart from the current user
        val cart = currentUser.cart
        //create instance of chooseAddressFragment
        val chooseAddressFragment = ChooseAddressFragment(currentFragment, cart)
        //navigate using fragment manager
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment,
            chooseAddressFragment,
            getString(R.string.title_choose_address_fragment)
        ).hide(currentFragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val homeFragment = (activity as MainActivity).activeFragment
                    val currentFragment = this@CartFragment
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(currentFragment).show(homeFragment).commit()
                    (activity as MainActivity).supportActionBar?.title = "Cart"
                    (activity as MainActivity).setDrawerLocked(false)
                    (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
                }
            })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
       // menu.findItem(R.id.action_cart).setVisible(false)
        super.onPrepareOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onProductClicked(productId: String) {
        val currentFragment = this
        val productDetailFragment = DetailFragment(productId,"CartFragment")
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment,
            productDetailFragment,
            getString(R.string.title_detail_fragment)
        ).remove(currentFragment).commit()
    }

    override fun onAddClicked(cartItemOffline: CartItemOffline) {
        GlobalScope.launch {
            currentUser =
                userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
            for (item in currentUser.cart.items) {
                if (item.productId == cartItemOffline.productId) {
                    currentUser.cart.items.remove(item)
                    break
                }
            }
            cartItemOffline.quantity += 1
            cartQuantity= cartItemOffline.quantity.toString()
            val price = cartItemOffline.product.productPrice
            currentUser.cart.price += price
            val cartItem = CartItem(cartItemOffline.productId,cartItemOffline.product.productName,cartItemOffline.quantity)
            currentUser.cart.items.add(cartItem)
            userDao.updateProfile(currentUser)
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
                binding.totalAmountCart.text = "₹" + currentUser.cart.price.toString()
            }
        }
    }

    override fun onDeleteClicked(cartItemOffline: CartItemOffline) {
        GlobalScope.launch {
            currentUser =
                userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
            for (item in currentUser.cart.items) {
                if (item.productId == cartItemOffline.productId) {
                    currentUser.cart.items.remove(item)
                    break
                }
            }
            if (cartItemOffline.quantity > 1) {
                cartItemOffline.quantity -= 1
                cartQuantity= cartItemOffline.quantity.toString()
                val price = cartItemOffline.product.productPrice
                currentUser.cart.price -= price
                val cartItem = CartItem(cartItemOffline.productId,cartItemOffline.product.productName,cartItemOffline.quantity)
                currentUser.cart.items.add(cartItem)
                userDao.updateProfile(currentUser)
            } else if (cartItemOffline.quantity == 1) {
                cartItemOffline.quantity = 0
                val price = cartItemOffline.product.productPrice
                currentUser.cart.price -= price
                userDao.updateProfile(currentUser)
            }
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
                binding.totalAmountCart.text = "₹" + currentUser.cart.price.toString()
            }
        }
    }
}