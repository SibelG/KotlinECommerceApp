package com.example.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.ItemFavBinding
import com.example.ecommerceapp.models.Product
import javax.inject.Inject

class FavoriteAdapter
constructor() : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val productList = mutableListOf<Product>()
    private lateinit var favListener: FavoriteProductListener
    fun addProducts(list: List<Product>, listener: FavoriteProductListener) {
        favListener = listener
        productList.apply {
            clear()
            addAll(list)
            if (list.isNotEmpty())
                add(list[0])
        }
        notifyDataSetChanged()
    }

    fun getAllFavoriteProducts() = productList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder =
        FavoriteViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_fav,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    inner class FavoriteViewHolder(private val binding: ItemFavBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val productImage: ImageView = binding.fabProductImage
        val productName: TextView = binding.favProductName
        val productDescription: TextView = binding.favProductDescription
        val productPrice: TextView = binding.favProductPrice
        val availabilityLabel: TextView = binding.availabilityLabelFav
        val productItem: LinearLayout = binding.productItemFav

        @SuppressLint("ResourceAsColor")
        fun bind(product: Product){
            productImage.load(product.productImage){
                transformations(RoundedCornersTransformation())
            }
            productName.text = product.productName
            productDescription.text = product.description
            val price = product.productPrice.toString()
            if (product.availability){
                productPrice.text = "Price - ₹$price"
            }else{
                availabilityLabel.visibility = View.VISIBLE
                productPrice.visibility = View.GONE
                availabilityLabel.text = "Currently Not Available"
            }
            binding.executePendingBindings()
        }
    }

    interface FavoriteProductListener {
        fun onFavProductClick(productModel: Product, favProductImage: ImageView)
    }

}