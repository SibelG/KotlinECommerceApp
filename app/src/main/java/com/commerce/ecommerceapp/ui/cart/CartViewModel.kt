package com.commerce.ecommerceapp.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commerce.ecommerceapp.Utils
import com.commerce.ecommerceapp.daos.ProductDao
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.models.CartItemOffline
import com.commerce.ecommerceapp.models.Product
import com.commerce.ecommerceapp.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(): ViewModel() {
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