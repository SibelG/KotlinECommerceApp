package com.example.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.media.Image
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
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.ItemFavBinding
import com.example.ecommerceapp.databinding.ItemProductBinding
import com.example.ecommerceapp.models.Product

class FavoritesAdapter(private val clickListener: IFavoritesAdapter): ListAdapter<Product, FavoritesAdapter.ViewHolder?>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFavBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        holder.productItem.setOnClickListener{
            clickListener.onProductClicked(product.productId)
        }
        holder.productFav.setOnClickListener{
            holder.productFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
    }

    class ViewHolder(private var binding: ItemFavBinding): RecyclerView.ViewHolder(binding.root){
        val productImage: ImageView = binding.productImageInProductFragment
        val productName: TextView = binding.productNameInProductFragment
        val productDescription: TextView = binding.productDescriptionInProductFragment
        val productPrice: TextView = binding.productPriceInProductFragment
        val availabilityLabel: TextView = binding.availabilityLabel
        val productItem: LinearLayout = binding.productItem
        val productFav: ImageView = binding.favProductView

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

    companion object DiffCallback: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private var onItemClickListener: ((Product) -> Unit)? = null
    fun onItemClicked(listner: (Product) -> Unit) {
        onItemClickListener = listner
    }
}

interface IFavoritesAdapter{
    fun onProductClicked(productId: String)
}