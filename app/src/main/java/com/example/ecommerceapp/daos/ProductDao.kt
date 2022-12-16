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
import javax.inject.Inject

class ProductDao @Inject constructor() {
    //get the instance of database
    private val db = FirebaseFirestore.getInstance()


    //get/set the collection
    val productsCollection = db.collection("products")
    val reviewsCollection = db.collection("reviews")

    fun getProductById(productId: String): Task<DocumentSnapshot>{
        return productsCollection.document(productId).get()
    }
    fun getReviewsById(reviewId: String): Task<DocumentSnapshot>{
        return reviewsCollection.document(reviewId).get()
    }
    fun addReview(productId: String,review: Review){
        GlobalScope.launch {
            val result = productsCollection.whereEqualTo("productId", productId).get().await()
            val documents = result.documents
            for (document in documents){
                val product = document.toObject(Product::class.java)!!
                product.reviews.add(review)
                reviewsCollection.document(productId).set(review)
                productsCollection.document(productId).set(product)

            }

        }
    }
    fun getReview(productId: String): List<Review>{

        var mReviews: MutableList<Review> = mutableListOf()
        GlobalScope.launch {
            val result = productsCollection.whereEqualTo("productId", productId).get().await()

            try {
                val documents = result.documents
                for (document in documents) {
                    val review = document.toObject(Review::class.java)!!
                    mReviews.add(review)
                }
                //val review = getReviewsById(productId).await().toObject(Review::class.java)!!


            } catch (e: Exception) {
                Log.d("CHECKING", e.message.toString())
            }
        }

        return mReviews
    }


    fun updateProduct(productId:String, product: Product) {
        GlobalScope.launch {
            //update the user in the database
            productsCollection.document(productId).set(product).await()
        }
    }

    suspend fun retrieveAll(collection: Query): List<Product>{
        //val productsCollection = FirebaseFirestore.getInstance().collection("products")
        var mProducts: MutableList<Product> = mutableListOf()
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