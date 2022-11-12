package com.example.ecommerceapp.daos





import android.content.Context
import android.widget.Toast
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserDao() {

    //create instance of the database
    private val db = FirebaseFirestore.getInstance()

    //choose the collection
    val usersCollection = db.collection("users")

    //this method will add user to the database
    fun addUser(context:Context, user: User?) {
        user?.let {
            //do this heavy database work in the background thread
            GlobalScope.launch {
                //change it's id to userId and set it

                usersCollection.document(user.userId).set(it).addOnSuccessListener {
                    Toast.makeText(context,"Record user successfuly",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(context,"Record user error",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    //this method will return an task instance of the user with the help of the user id
    fun getUserById(uid: String): Task<DocumentSnapshot> {
        return usersCollection.document(uid).get()
    }

    //this method will update the user profile by taking address as parameter and set it to user profile
    fun updateProfile(user: User) {
        GlobalScope.launch {
            //update the user in the database
            usersCollection.document(Utils.currentUserId).set(user).await()
        }
    }

//    fun addProductToCart(productId: String) {
//        //do this work in background
//        GlobalScope.launch {
//            //create instance of product dao
//            val productDao = ProductDao()
//            //get the product using product id
//            val product =
//                productDao.getProductById(productId).await().toObject(Product::class.java)!!
//            //get the user
//            val user = getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
//            //add the product id to the user cart
//            user.cart.products.add(productId)
//            //update the price of the user cart
//            user.cart.price = product.productPrice
//            //update the user in database
//            usersCollection.document(Utils.currentUserId).set(user)
//        }
//    }
}