package com.commerce.ecommerceapp

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.adapters.*
import com.commerce.ecommerceapp.models.*


@BindingAdapter("productListData")
fun bindProductRecyclerView(recyclerView: RecyclerView, data: List<Product>?){
    val adapter = recyclerView.adapter as? ProductAdapter
    adapter?.submitList(data)
}

@BindingAdapter("categoryListData")
fun bindCategoryRecyclerView(recyclerView: RecyclerView, data: List<Category>?){
    val adapter = recyclerView.adapter as? CategoryAdapter
    adapter?.submitList(data)
}

@BindingAdapter("brandListData")
fun bindBrandRecyclerView(recyclerView: RecyclerView, data: List<Brand>?){
    val adapter = recyclerView.adapter as? BrandAdapter
    adapter?.submitList(data)
}

@BindingAdapter("loadGif")
fun loadGifIntoImageView(imageView: ImageView, imageGif: Drawable) {
    imageView.loadGif(R.drawable.out_of_stock)
}

@BindingAdapter("cartListData")
fun bindCartRecyclerView(recyclerView: RecyclerView, data: List<CartItemOffline>?){
    val adapter = recyclerView.adapter as? CartAdapter
    adapter?.submitList(data)
}

