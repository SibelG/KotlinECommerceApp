package com.commerce.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.databinding.ItemCategoryBinding
import com.commerce.ecommerceapp.loadImage
import com.commerce.ecommerceapp.models.Category

class CategoryAdapter(): ListAdapter<Category, CategoryAdapter.ViewHolder?>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(category) } }
        /*holder.categoryItem.setOnClickListener{
            clickListener.onCategoryClicked(category)
        }*/
    }

    class ViewHolder(private var binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        val categoryImage: ImageView = binding.itemImage
        val categoryName: TextView = binding.itemText
        val categoryItem: ConstraintLayout = binding.categoryItem



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
interface ICategoryAdapter{
    fun onCategoryClicked(category: Category)
}