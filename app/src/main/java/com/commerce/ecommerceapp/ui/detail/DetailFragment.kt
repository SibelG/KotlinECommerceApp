package com.commerce.ecommerceapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.commerce.ecommerceapp.*
import com.commerce.ecommerceapp.daos.ProductDao
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.DetailFragmentBinding
import com.commerce.ecommerceapp.models.Cart
import com.commerce.ecommerceapp.models.CartItem
import com.commerce.ecommerceapp.models.Product
import com.commerce.ecommerceapp.models.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class DetailFragment() : Fragment() {

    private var productDao = ProductDao()
    private lateinit var product: Product
    private lateinit var binding: DetailFragmentBinding
    private lateinit var currentUser: User
    private var userDao= UserDao()
    private lateinit var viewModel: DetailViewModel
    private val args by navArgs<DetailFragmentArgs>()
    private val productModel by lazy { args.productDetails }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        binding = DetailFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        showProductDetails(productModel.productId)

        return binding.run {
            fragment = this@DetailFragment
            binding.root
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // showProductDetails(productModel.productId)
        observeListener()
        binding.addToCartButton.setOnClickListener {
            addProductToCart(productModel.productId)
        }

        binding.ratingBar.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                reviewProducts()
            }
            return@OnTouchListener true
        })
    }
    private fun observeListener() {
        // check if product saved in favorite database and change icon image as following.
        viewModel.favoriteLiveData(productModel.productId).observe(viewLifecycleOwner, { favProduct ->
            if (favProduct != null) {
                binding.favImage.loadTimerGif(R.drawable.favorite_gif_start)

            } else {
                binding.favImage.setImageResource(R.drawable.ic_favorite_border)

            }
        })
    }
    fun saveProductInFavorite() {
        viewModel.saveProductInFavorites(productModel)
    }

   fun startCheckoutProcess() {
            val cartItem = CartItem(product.productId,product.productName,1,product.productImage)
            val cartItems = ArrayList<CartItem>()
            cartItems.add(cartItem)
            val cart = Cart(cartItems,product.productPrice)
            val action = DetailFragmentDirections.actionDetailFragmentToChooseAddressFragment(cart)
            findNavController().navigate(action)
       // requireActivity().supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment,chooseAddressFragment,getString(R.string.title_choose_address_fragment)).hide(currentFragment).commit()
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
            val newItem = CartItem(productId,product.productName,quantity,product.productImage)
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

        (activity as MainActivity).supportActionBar?.title = "Product Details"
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

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

    // share product with messaging app.
    fun shareProduct() {
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody =
            getString(R.string.shareProduct)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(intent)
    }

    fun backPressFragment() {
        closeFragment()
    }
    fun reviewProducts(){
        val action = DetailFragmentDirections.actionDetailFragmentToReviewFragment(productModel)
        findNavController().navigate(action)
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
            binding.ratingBar.rating = (product.reviews.size/5).toFloat()
            binding.reviewDetailSize.text = product.reviews.size.toString()
            if (!product.availability){
                binding.buyNowButton.isEnabled = false
                binding.addToCartButton.isEnabled = false
                binding.productAvailabilityInDetail.visibility = View.GONE
            }
        }
    }
}