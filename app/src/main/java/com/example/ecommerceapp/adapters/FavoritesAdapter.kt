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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.ItemFavBinding
import com.example.ecommerceapp.databinding.ItemProductBinding
import com.example.ecommerceapp.loadImage
import com.example.ecommerceapp.models.Product
import javax.inject.Inject

class FavoriteAdapter @Inject constructor (): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(){


    private lateinit var favListener: FavoriteProductListener
    private val productList = mutableListOf<Product>()
    fun addProducts(list: List<Product>, listener: FavoriteProductListener) {
        favListener = listener
        productList.apply {
            clear()
            addAll(list)
            /*if (list.isNotEmpty())
                add(list[0])*/
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    fun getAllFavoriteProducts() = productList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        return ViewHolder(ItemFavBinding.inflate(LayoutInflater.from(parent.context)))
    }



    override fun getItemCount(): Int = productList.size

    class ViewHolder(private val binding: ItemFavBinding) : RecyclerView.ViewHolder(binding.root) {

        val productImage: ImageView = binding.favProductImage
        val productName: TextView = binding.favProductName
        val productDescription: TextView = binding.favProductDescription
        val productPrice: TextView = binding.favProductPrice

        @SuppressLint("ResourceAsColor")
        fun bind(product: Product){
            productImage.loadImage(product.productImage)
            productName.text = product.productName
            productDescription.text = product.description
            val price = product.productPrice.toString()
            productPrice.text = "Price - ₹$price"

            binding.executePendingBindings()
        }
    }



    interface FavoriteProductListener {
        fun onFavProductClick(productModel: Product)
    }

}