package com.example.ecommerceapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.ecommerceapp.daos.ProductDao
import com.example.ecommerceapp.models.Brand
import com.example.ecommerceapp.models.Category
import com.example.ecommerceapp.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

enum class ProductStatus{LOADING,ERROR,DONE}

class HomeViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>>
        get() = _products


    private val _category = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _category

    private val _brand = MutableLiveData<List<Brand>>()
    val brands: LiveData<List<Brand>>
        get() = _brand

    private val _status = MutableLiveData<ProductStatus>()
    val status: LiveData<ProductStatus>
        get() = _status


    private var mProducts: ArrayList<Product> = ArrayList()
    private var mCategories: ArrayList<Category> = ArrayList()
    private var mBrands: ArrayList<Brand> = ArrayList()


    init {
        _category.value = mCategories
        _products.value = mProducts
        _brand.value = mBrands
        _status.value = ProductStatus.LOADING
        retrieveAllProducts()
        retrieveAllCategories()
        retrieveAllBrands()
    }


    private fun retrieveAllProducts(){
        val productsCollection = FirebaseFirestore.getInstance().collection("products")
        viewModelScope.launch {
            try {
                val query = productsCollection.get().await()
                val documents = query.documents
                for (document in documents){
                    val product = document.toObject(Product::class.java)!!
                    product.productId = document.id
                    mProducts.add(product)
                }
            }catch (e: Exception){
                Log.d("CHECKING",e.message.toString())
            }
            withContext(Dispatchers.Main){
                _products.value = mProducts
                _status.value = ProductStatus.DONE
            }
        }
    }


    private fun retrieveAllCategories(){
        val categoryCollection = FirebaseFirestore.getInstance().collection("categories")
        viewModelScope.launch {
            try {
                val query = categoryCollection.get().await()
                val documents = query.documents
                for (document in documents){
                    val category = document.toObject(Category::class.java)!!
                    mCategories.add(category)
                }
            }catch (e: Exception){
                Log.d("CHECKING",e.message.toString())
            }
            withContext(Dispatchers.Main){
                _category.value = mCategories
            }
        }
    }

    private fun retrieveAllBrands(){
        val brandCollection = FirebaseFirestore.getInstance().collection("brands")
        viewModelScope.launch {
            try {
                val query = brandCollection.get().await()
                val documents = query.documents
                for (document in documents){
                    val brand = document.toObject(Brand::class.java)!!
                    mBrands.add(brand)
                }
                Log.d("CHECK", mBrands.toString())
            }catch (e: Exception){
                Log.d("CHECKING",e.message.toString())
            }
            withContext(Dispatchers.Main){
                _brand.value = mBrands
            }
        }
    }
}