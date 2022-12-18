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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Resource
import com.example.ecommerceapp.Utils.currentUserId
import com.example.ecommerceapp.adapters.BrandAdapter
import com.example.ecommerceapp.adapters.FavoriteAdapter
import com.example.ecommerceapp.adapters.ProductAdapter
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.FragmentFavoritesBinding
import com.example.ecommerceapp.models.Order
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.showToast
import com.example.ecommerceapp.ui.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites),
    FavoriteAdapter.FavoriteProductListener {

    private lateinit var adapter:FavoriteAdapter

    lateinit var loadingDialog: Dialog

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        adapter= FavoriteAdapter()
        binding.adapter = adapter
        binding.fragment = this
        viewModel.getFavoriteProducts()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()

    }

    private fun observeListener() {
        viewModel.favoriteProductsLiveData.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.emptyProducts.visibility = View.VISIBLE
                binding.favoriteContainer.visibility = View.GONE
            } else {
                adapter.addProducts(it, this)
                binding.emptyProducts.visibility = View.GONE
                binding.favoriteContainer.visibility = View.VISIBLE

                /*adapter.notifyDataSetChanged()
                binding.favoriteRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)*/
            }
        })
        viewModel.cartProductsLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    showToast(getString(R.string.savedToCart))
                }
                is Resource.Error -> {
                    showToast(it.msg!!)
                }
                else -> {}
            }
        })
    }

    fun addAllToCart() {
        val favList = adapter.getAllFavoriteProducts()
        viewModel.addProductsToCart(favList)
    }

    override fun onFavProductClick(productModel: Product) {
        navigateToSpecificProductFragment(productModel)
    }

    
    private fun navigateToSpecificProductFragment(
        productModel: Product
    ) {
        // add transition to image view wh en open specific product fragment.

        val action =
            FavoritesFragmentDirections.actionNavFavoriteToDetailFragment(productModel)
        findNavController().navigate(action)
    }

}
