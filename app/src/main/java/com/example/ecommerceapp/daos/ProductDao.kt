package com.example.ecommerceapp.daos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.data.database.FavouriteDao
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.Review
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.home.ProductStatus
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProductDao {
    //get the instance of database
    private val db = FirebaseFirestore.getInstance()


    //get/set the collection
    val productsCollection = db.collection("products")
    private var mProducts: MutableList<Product> = mutableListOf()


    fun getProductById(productId: String): Task<DocumentSnapshot>{
        return productsCollection.document(productId).get()
    }

    fun addReview(productId: String,review: Review){
        GlobalScope.launch {
            val product = getProductById(productId).await().toObject(Product::class.java)!!
            product.reviews.add(review)
            productsCollection.document(productId).set(product)
        }
    }
    fun updateProduct(productId:String, product: Product) {
        GlobalScope.launch {
            //update the user in the database
            productsCollection.document(productId).set(product).await()
        }
    }

    suspend fun retrieveAll(collection: Query): List<Product>{
        //val productsCollection = FirebaseFirestore.getInstance().collection("products")

            try {
                val query = collection.get().await()
                val documents = query.documents
                for (document in documents){
                    val product = document.toObject(Product::class.java)!!
                    product.productId = document.id
                    mProducts.add(product)
                }
            }catch (e: Exception){
                Log.d("CHECKING",e.message.toString())
            }
        return mProducts
    }

}