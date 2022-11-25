package com.example.ecommerceapp.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.daos.ProductDao
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.models.CartItem
import com.example.ecommerceapp.models.CartItemOffline
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class CartViewModel : ViewModel() {
    private val _items = MutableLiveData<List<CartItemOffline>>()
    val items: LiveData<List<CartItemOffline>>
        get() = _items

    private val _subtotal = MutableLiveData<String>()
    val subtotal: LiveData<String>
        get() = _subtotal

    private val _subQuantity = MutableLiveData<String>()
    val subQuantity: LiveData<String>
        get() = _subQuantity

    private val _listIsEmpty = MutableLiveData<Boolean>()
    val listIsEmpty: LiveData<Boolean>
        get() = _listIsEmpty

    private var mProducts: ArrayList<Product> = ArrayList()
    private var mItems: ArrayList<CartItemOffline> = ArrayList()
    private val productDao = ProductDao()
    private lateinit var currentUser: User
    var productQuantity: Int=0

    init {
        _subtotal.value = ""
        _subQuantity.value = ""
        _items.value = mItems
        retreiveAllProducts()
    }


    private fun retreiveAllProducts() {
        viewModelScope.launch {
            currentUser =
                UserDao().getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
            val cartItems = currentUser.cart.items
            _subtotal.value = "₹" + currentUser.cart.price.toString()

            for (document in cartItems) {
                val productId = document.productId
                productQuantity+= document.quantity
                _subQuantity.value = productQuantity.toString()
                val product =
                    productDao.getProductById(productId).await().toObject(Product::class.java)!!
                val cartItemOffline = CartItemOffline(productId, document.quantity, product)
                mItems.add(cartItemOffline)

            }
            withContext(Dispatchers.Main) {
                _items.value = mItems
                if (items.value?.size == 0) {
                    _listIsEmpty.value = true
                }
            }
        }
    }
}