package com.example.ecommerceapp.ui.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.daos.ProductDao
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.DetailFragmentBinding
import com.example.ecommerceapp.models.Cart
import com.example.ecommerceapp.models.CartItem
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.cart.CartFragment
import com.example.ecommerceapp.ui.choose_address.ChooseAddressFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DetailFragment(val productId: String,val fromWhere: String) : Fragment() {

    private lateinit var productDao: ProductDao
    private lateinit var product: Product
    private lateinit var binding: DetailFragmentBinding
    private lateinit var currentUser: User
    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater)
        productDao = ProductDao()
        userDao = UserDao()
        showProductDetails(productId)
        (activity as MainActivity).supportActionBar?.title = "Product Details"

        //add click listener to add product to the cart
        binding.addToCartButton.setOnClickListener {
            addProductToCart(productId)
        }

        binding.buyNowButton.setOnClickListener {
            startCheckoutProcess()
        }

        //change the icon of the up button
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        return binding.root
    }

    private fun startCheckoutProcess() {
        //we need to paas a cart to the choose address fragment, and we don't have any cart here because we want to buy a single item here. so we will create instance of cart using that single element
        val cartItem = CartItem(productId,product.productName,1)
        val cartItems = ArrayList<CartItem>()
        cartItems.add(cartItem)
        val cart = Cart(cartItems,product.productPrice)
        val chooseAddressFragment = ChooseAddressFragment(this,cart)
        val currentFragment = this
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment,chooseAddressFragment,getString(R.string.title_choose_address_fragment)).hide(currentFragment).commit()
    }

    private fun addProductToCart(productId: String) {
        //get the user and then add the product to cart
        GlobalScope.launch {
            currentUser = userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
            val cart = currentUser.cart
            val items = cart.items
            var quantity = 1
            for (item in items){
                if (item.productId==productId){
                    quantity += item.quantity
                    items.remove(item)
                    break
                }
            }
            val newItem = CartItem(productId,product.productName,quantity)
            cart.items.add(newItem)
            cart.price = cart.price + product.productPrice
            currentUser.cart = cart
            userDao.updateProfile(currentUser)
            requireActivity().runOnUiThread{
                Toast.makeText(requireActivity(),"Item added to cart",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                //either we came here from home fragment or from cart fragment
                if (fromWhere=="HomeFragment"){
                    val homeFragment = (activity as MainActivity).activeFragment
                    val currentFragment = this@DetailFragment
                    requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).show(homeFragment).commit()
                    (activity as MainActivity).supportActionBar?.title = "FashionShop"
                    (activity as MainActivity).setDrawerLocked(false)
                    (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
                }else{
                    val cartFragment = CartFragment()
                    val currentFragment = this@DetailFragment
                    requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).add(R.id.nav_host_fragment,cartFragment,getString(R.string.title_cart_fragment)).commit()
                    (activity as MainActivity).supportActionBar?.title = "Cart"
                }
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

    private fun showProductDetails(productId: String) {
        productDao.getProductById(productId).addOnSuccessListener {
            product = it.toObject(Product::class.java)!!
            binding.productImageInDetail.load(product.productImage){
                transformations(RoundedCornersTransformation())
            }
            binding.productNameInDetail.text = product.productName
            binding.productDescriptionInDetail.text = product.description
            binding.productPriceInDetail.text = "₹" + product.productPrice.toString()
            if (!product.availability){
                binding.buyNowButton.isEnabled = false
                binding.addToCartButton.isEnabled = false
                binding.productAvailabilityInDetail.visibility = View.GONE
            }
        }
    }
}