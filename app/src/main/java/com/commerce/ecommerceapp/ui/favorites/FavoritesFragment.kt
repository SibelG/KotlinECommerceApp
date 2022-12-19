package com.commerce.ecommerceapp.ui.favorites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Resource
import com.commerce.ecommerceapp.adapters.FavoriteAdapter
import com.commerce.ecommerceapp.databinding.FragmentFavoritesBinding
import com.commerce.ecommerceapp.models.Product
import com.commerce.ecommerceapp.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites),
    FavoriteAdapter.FavoriteProductListener {

    private lateinit var adapter:FavoriteAdapter

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
        viewModel.getFavoriteProducts()
        adapter= FavoriteAdapter()
        binding.adapter = adapter
        binding.fragment = this
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
