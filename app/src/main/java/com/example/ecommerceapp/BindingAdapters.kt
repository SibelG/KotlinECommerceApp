package com.example.ecommerceapp

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.adapters.BrandAdapter
import com.example.ecommerceapp.adapters.CartAdapter
import com.example.ecommerceapp.adapters.CategoryAdapter
import com.example.ecommerceapp.adapters.ProductAdapter
import com.example.ecommerceapp.models.Brand
import com.example.ecommerceapp.models.CartItemOffline
import com.example.ecommerceapp.models.Category
import com.example.ecommerceapp.models.Product


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

@BindingAdapter("cartListData")
fun bindCartRecyclerView(recyclerView: RecyclerView, data: List<CartItemOffline>?){
    val adapter = recyclerView.adapter as CartAdapter
    adapter.submitList(data)
}