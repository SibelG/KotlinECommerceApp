package com.example.ecommerceapp.daos

import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.Review
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductDao {
    //get the instance of database
    private val db = FirebaseFirestore.getInstance()
    //get/set the collection
    val productsCollection = db.collection("products")



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
}