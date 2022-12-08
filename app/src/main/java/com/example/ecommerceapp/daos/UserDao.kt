package com.example.ecommerceapp.daos





import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserDao() {

    //create instance of the database
    private val db  by lazy { FirebaseFirestore.getInstance()}
    private val firebaseStorage by lazy { FirebaseStorage.getInstance()}
    private val userUid by lazy { FirebaseAuth.getInstance().uid!! }
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

     suspend fun uploadUserInformation(
        userName: String,
        imageUri: Uri?,
        userEmail: String,
        context: Context
    ){
         GlobalScope.launch {
             try {
                 var accountStatusMessage = context.getString(R.string.accountCreatedSuccessfully)
                 if (imageUri != null) {
                     val uploadedImagePath = uploadUserImage(imageUri)
                     val userInfoModel =
                         User(userUid, userName, uploadedImagePath, userEmail)
                     usersCollection.document(userUid).set(userInfoModel)
                 } else {
                     val userInfoModel =
                         User(userUid, userName, "", userEmail)
                     updateProfile(userInfoModel)
                     accountStatusMessage = context.getString(R.string.accountUpdatedSuccessfully)
                 }
             } catch (e: Exception) {

             }
         }
    }

    private suspend fun uploadUserImage(imageUri: Uri): String {
        val uploadingResult =
            firebaseStorage.reference.child("${"users"}/${System.currentTimeMillis()}.jpg")
                .putFile(imageUri).await()
        return uploadingResult.metadata?.reference?.downloadUrl?.await().toString()
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