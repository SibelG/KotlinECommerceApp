package com.example.ecommerceapp.ui.favorites


import android.app.Dialog
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils.currentUserId
import com.example.ecommerceapp.adapters.FavoriteAdapter
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.FragmentFavoritesBinding
import com.example.ecommerceapp.models.Order
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.showToast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class FavoritesFragment : Fragment(R.layout.fragment_favorites),
    FavoriteAdapter.FavoriteProductListener {

    lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var currentUser: User
    private var userDao = UserDao()

    lateinit var loadingDialog: Dialog

    private lateinit var binding: FragmentFavoritesBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewModel.getFavoriteProducts()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        binding.adapter = favoriteAdapter
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener() {
        favoriteViewModel.favoriteProductsLiveData.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.emptyProducts.visibility = View.VISIBLE
                binding.favoriteContainer.visibility = View.GONE
            } else {
                favoriteAdapter.addProducts(it, this)
                binding.emptyProducts.visibility = View.GONE
                binding.favoriteContainer.visibility = View.VISIBLE
            }
        })
    }

    fun addAllToCart() {
        /*val favList = favoriteAdapter.getAllFavoriteProducts()
        addProductsToCart(favList)*/
    }

    fun addProductsToCart(favList: List<Product>){

        GlobalScope.launch {
            currentUser =
                userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            val favs = currentUser.favProduct


        }
    }

    override fun onFavProductClick(productModel: Product, favProductImage: ImageView) {
        navigateToSpecificProductFragment(productModel, favProductImage)
    }

    private fun navigateToSpecificProductFragment(
        productModel: Product,
        transitionImageView: ImageView
    ) {
        // add transition to image view wh en open specific product fragment.
        val extras = FragmentNavigatorExtras(
            transitionImageView to productModel.productImage
        )
        /*val action =
            FavoriteFragmentDirections.actionFavoriteFragmentToSpecificProductFragment(productModel)
        findNavController().navigate(action, extras)*/
    }

}
