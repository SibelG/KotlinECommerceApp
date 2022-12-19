package com.commerce.ecommerceapp.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.databinding.ItemFavBinding
import com.commerce.ecommerceapp.loadImage
import com.commerce.ecommerceapp.models.Product
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
        //return ViewHolder(ItemFavBinding.inflate(LayoutInflater.from(parent.context).))
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fav,parent,false))

    }



    override fun getItemCount(): Int = productList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val productImage: ImageView = itemView.findViewById(R.id.favProductImage)
        val productName: TextView = itemView.findViewById(R.id.favProductName)
        val productDescription: TextView = itemView.findViewById(R.id.favProductDescription)
        val productPrice: TextView = itemView.findViewById(R.id.favProductPrice)

        @SuppressLint("ResourceAsColor")
        fun bind(product: Product){
            productImage.loadImage(product.productImage)
            productName.text = product.productName
            productDescription.text = product.description
            val price = product.productPrice.toString()
            productPrice.text = "Price - ₹$price"

        }
    }



    interface FavoriteProductListener {
        fun onFavProductClick(productModel: Product)
    }

}