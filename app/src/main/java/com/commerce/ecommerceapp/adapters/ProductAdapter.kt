package com.commerce.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.daos.DBRepository
import com.commerce.ecommerceapp.data.database.FavouriteDao
import com.commerce.ecommerceapp.databinding.ItemProductBinding
import com.commerce.ecommerceapp.loadImage
import com.commerce.ecommerceapp.loadTimerGif
import com.commerce.ecommerceapp.models.Order
import com.commerce.ecommerceapp.models.Product
import com.commerce.ecommerceapp.ui.home.HomeViewModel
import org.w3c.dom.Text

class ProductAdapter(private val clickListener: IProductAdapter): ListAdapter<Product, ProductAdapter.ViewHolder?>(DiffCallback) {

    private lateinit var  favoriteDao : FavouriteDao
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        holder.productItem.setOnClickListener{
            clickListener.onProductClicked(product)
        }
        /*var data = favoriteDao.getSpecificFavoriteProductLiveData(product.productId)
        if(data!=null){
            holder.favItem.loadTimerGif(R.drawable.favorite_gif_start)
        }
        else{
            holder.favItem.setImageResource(R.drawable.ic_favorite_border)
        }*/

    }


    class ViewHolder(private var binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root){
        val productImage: ImageView = binding.productImageInProductFragment
        val productName: TextView = binding.productNameInProductFragment
        val productDescription: TextView = binding.productDescriptionInProductFragment
        val productPrice: TextView = binding.productPriceInProductFragment
        val availabilityLabel: TextView = binding.availabilityLabel
        val productItem: LinearLayout = binding.productItem
        val favItem : ImageView = binding.favProductView
        val reviewSize : TextView = binding.reviewSize
        val rating: RatingBar = binding.ratingBar

        @SuppressLint("ResourceAsColor")
        fun bind(product: Product){
            productImage.loadImage(product.productImage)
            productName.text = product.productName
            reviewSize.text = product.reviews.size.toString()
            productDescription.text = product.description
            rating.rating= ((product.reviews.size/5).toFloat())
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

}

interface IProductAdapter{
    fun onProductClicked(product: Product)
    fun addCommend(order: Order)
}