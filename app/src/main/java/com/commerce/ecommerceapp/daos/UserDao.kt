package com.commerce.ecommerceapp.daos





import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Resource
import com.commerce.ecommerceapp.Utils
import com.commerce.ecommerceapp.adapters.NotificationAdapter
import com.commerce.ecommerceapp.models.NotificationData
import com.commerce.ecommerceapp.models.Product
import com.commerce.ecommerceapp.models.Review
import com.commerce.ecommerceapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDao @Inject constructor() {

    //create instance of the database
    private val db  by lazy { FirebaseFirestore.getInstance()}
    private val firebaseStorage by lazy { FirebaseStorage.getInstance()}
    private val userUid by lazy { FirebaseAuth.getInstance().uid!! }
    val productsCollection = db.collection("products")
    //choose the collection
    val usersCollection = db.collection("users")
    val notifyCollection = db.collection("notify")

    //this method will add user to the database
    fun addUser(user: User?) {
        user?.let {
            //do this heavy database work in the background thread
            GlobalScope.launch {
                //change it's id to userId and set it
                usersCollection.document(user.userId).set(it).addOnSuccessListener {
                    Log.d("UserRecover","user recovered successfully")
                }.addOnFailureListener{
                    Log.d("UserRecover","user recovered error")
                }
            }

        }
    }


     suspend fun uploadUserInformation(
        userName: String,
        imageUri: Uri?,
        userPhone: String,
        context: Context
    ){
         GlobalScope.launch {
             try {
                 var accountStatusMessage = context.getString(R.string.accountCreatedSuccessfully)
                 if (imageUri != null) {
                     val uploadedImagePath = uploadUserImage(imageUri)
                     val userInfoModel =
                         User(userId = userUid, userName=userName, userImage = uploadedImagePath, mobileNumber = userPhone)
                     updateProfile(userInfoModel)
                 } else {
                     val userInfoModel =
                         User(userId=userUid, userName=userName, userImage = "", mobileNumber = userPhone)
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
    fun addNotify(notifyData: NotificationData) {
        GlobalScope.launch {
            val currentUser =
                getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
            currentUser.notifyList.add(notifyData)
            updateProfile(currentUser)
            notifyCollection.document().set(notifyData)
        }
    }

    fun getNotify(userUid: String): List<NotificationData>{

        var mNotify:  MutableList<NotificationData> = mutableListOf()
        GlobalScope.launch {
            val result = usersCollection.whereEqualTo("userUID", userUid).get().await()

            try {
                val documents = result.documents
                for (document in documents) {
                    val notify = document.toObject(NotificationData::class.java)!!
                    mNotify.add(notify)
                }
                //val review = getReviewsById(productId).await().toObject(Review::class.java)!!


            } catch (e: java.lang.Exception) {
                Log.d("CHECKING", e.message.toString())
            }
        }
        return mNotify

    }


    fun updateProfile(user: User) {
        GlobalScope.launch {
            //update the user in the database
            usersCollection.document(Utils.currentUserId).set(user).await()
        }
    }



}