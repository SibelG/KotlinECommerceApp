package com.example.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.ItemReviewBinding
import com.example.ecommerceapp.loadImage
import com.example.ecommerceapp.models.CartItemOffline
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.Review
import javax.inject.Inject


class OrderItemAdapter(val items: ArrayList<CartItemOffline>, val clickListener:ClickListener): RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product_summary,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = items[position]
        holder.bind(cartItem)
        holder.sendButton.setOnClickListener {
            clickListener.addReview(cartItem.product)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val productImage: ImageView = itemView.findViewById(R.id.product_image_item_summary)
        val productName: TextView = itemView.findViewById(R.id.product_name_item_summary)
        val productPrice: TextView = itemView.findViewById(R.id.product_price_item_summary)
        val productQty: TextView = itemView.findViewById(R.id.product_qty_item_summary)
        val sendButton: Button = itemView.findViewById(R.id.reviewButton)

        fun bind(cartItem: CartItemOffline){
            productImage.loadImage(cartItem.product.productImage)
            productName.text = cartItem.product.productName
            productPrice.text = cartItem.product.productPrice.toString()
            productQty.text = "Qty - " + cartItem.quantity.toString()
        }
    }
}
interface ClickListener{
    fun addReview(product: Product)
}