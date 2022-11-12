package com.example.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.ItemProductCartBinding
import com.example.ecommerceapp.models.CartItemOffline


class CartAdapter(private val clickListener: ICartAdapter) :
    ListAdapter<CartItemOffline, CartAdapter.ViewHolder>(DiffCallback) {

//    val cart = currentUser.cart
//    val products = cart.products

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProductCartBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = getItem(position)
        if (cartItem.quantity>0){
            holder.bind(cartItem)
        }else{
            holder.bind(CartItemOffline())
            holder.addButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
            holder.productPrice.visibility = View.GONE
            holder.quantityView.visibility = View.GONE
        }
        holder.addButton.setOnClickListener {
            clickListener.onAddClicked(cartItem)
        }
        holder.deleteButton.setOnClickListener {
            clickListener.onDeleteClicked(cartItem)
        }
        holder.item_product.setOnClickListener{
            clickListener.onProductClicked(cartItem.productId)
        }
    }

    class ViewHolder(private var binding: ItemProductCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val item_product: LinearLayout = binding.productItemCart
        val productImage: ImageView = binding.productImageInCart
        val productName: TextView = binding.productNameInCart
        val productPrice: TextView = binding.productPriceInCart
        val availabilityLabel: TextView = binding.availabilityLabelCart
        val deleteButton: ImageButton = binding.deleteProductButtonInCart
        val quantityView: TextView = binding.numberOfProductsInCart
        val addButton: ImageButton = binding.addProductButtonInCart

        fun bind(cartItem: CartItemOffline) {
            productImage.load(cartItem.product.productImage) {
                transformations(RoundedCornersTransformation())
            }
            productName.text = cartItem.product.productName
            val price = cartItem.product.productPrice.toString()
            if (cartItem.product.availability) {
                productPrice.text = "Price - ₹$price"
            } else {
                availabilityLabel.visibility = View.VISIBLE
                productPrice.visibility = View.GONE
                availabilityLabel.text = "Currently Not Available"
            }
            val quantity = cartItem.quantity
            if (quantity>1){
                deleteButton.setImageResource(R.drawable.ic_minus)
            }
            quantityView.text = quantity.toString()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CartItemOffline>() {
        override fun areItemsTheSame(oldItem: CartItemOffline, newItem: CartItemOffline): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItemOffline, newItem: CartItemOffline): Boolean {
            return oldItem == newItem
        }
    }
}

interface ICartAdapter {
    fun onProductClicked(productId: String)
    fun onAddClicked(cartItem: CartItemOffline)
    fun onDeleteClicked(cartItem: CartItemOffline)
}