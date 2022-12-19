package com.commerce.ecommerceapp.daos

import android.content.Context
import androidx.lifecycle.LiveData
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Resource
import com.commerce.ecommerceapp.Utils
import com.commerce.ecommerceapp.data.database.FavouriteDatabase
import com.commerce.ecommerceapp.models.Product
import com.commerce.ecommerceapp.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class DBRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    val userDao: UserDao,
    val productDao: ProductDao,
    val appDatabase: FavouriteDatabase) {

    private val errorMessage by lazy { context.getString(R.string.errorMessage) }
    suspend fun saveOrRemoveProductFromFavorite(productModel: Product) {
        val isSavedBefore = getProductFromFavorite(productModel.productId)
        return if (isSavedBefore) {
            appDatabase.getFavouriteDao().removeProductFromFavorites(productModel)
        } else {
            appDatabase.getFavouriteDao().saveProduct(productModel)
        }
    }

    // check if product is saved into favorite database or not .
    suspend fun getProductFromFavorite(id: String): Boolean {
        val productModel = appDatabase.getFavouriteDao().getSpecificFavoriteProduct(id)
        return productModel != null
    }

    // observe to specific product when save or not to change favorite icon .
    fun getProductFromFavoriteLiveData(id: String): LiveData<Product?> =
        appDatabase.getFavouriteDao().getSpecificFavoriteProductLiveData(id)

    fun getFavoriteProductsLiveData(): LiveData<List<Product>> =
        appDatabase.getFavouriteDao().getAllFavoriteProducts()


    suspend fun addProductsToCart(
        list: List<Product>,
        deleteFavoriteProducts: Boolean
    ):Resource<Any> {
        return try {
            /*val collection: Query = FirebaseFirestore.getInstance().collection("products")
            val cartProductsList = productDao.retrieveAll(collection)*/
            val currentUser =
                userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
            var items = currentUser.cart.items

            list.forEach { product ->
                // check if same item saved to cart before and if it saved before here we will get last quantity saved of item and
                // added to new product quantity.
                if (items != null && items.any { it.productId == product.productId }) {
                    val selectedProduct = items.last { it.productId == product.productId }

                    selectedProduct.quantity +=1
                }

                //update the price of the user cart
                currentUser.cart.price = product.productPrice
                //update the user in database
                userDao.usersCollection.document(Utils.currentUserId).set(currentUser)
            }
            if (deleteFavoriteProducts)
                appDatabase.getFavouriteDao().deleteAllProducts()
            Resource.Success(Any())
        } catch (e: Exception) {
            Resource.Error(errorMessage)
        }

    }
}