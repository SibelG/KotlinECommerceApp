package com.example.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ecommerceapp.databinding.ItemCategoryBinding
import com.example.ecommerceapp.databinding.ItemProductBinding
import com.example.ecommerceapp.loadImage
import com.example.ecommerceapp.models.Category
import com.example.ecommerceapp.models.Product

class CategoryAdapter(): ListAdapter<Category, CategoryAdapter.ViewHolder?>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(category) } }
        /*holder.productItem.setOnClickListener{
            clickListener.onProductClicked(product.productId)
        }*/
    }

    class ViewHolder(private var binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        val categoryImage: ImageView = binding.itemImage
        val categoryName: TextView = binding.itemText


        @SuppressLint("ResourceAsColor")
        fun bind(category: Category){
            categoryImage.loadImage(category.categoryIcon)
            categoryName.text = category.categoryTitle

        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    private var onItemClickListener: ((Category) -> Unit)? = null
    fun onItemClicked(listner: (Category) -> Unit) {
        onItemClickListener = listner
    }
}