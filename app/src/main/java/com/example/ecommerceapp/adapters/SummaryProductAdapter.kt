package com.example.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.loadImage
import com.example.ecommerceapp.models.CartItemOffline
import com.example.ecommerceapp.models.Product

class SummaryProductAdapter(val items: ArrayList<CartItemOffline>): RecyclerView.Adapter<SummaryProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_summary_detail,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = items[position]
        holder.bind(cartItem)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val productImage: ImageView = itemView.findViewById(R.id.product_image_item_summary)
        val productName: TextView = itemView.findViewById(R.id.product_name_item_summary)
        val productPrice: TextView = itemView.findViewById(R.id.product_price_item_summary)
        val productQty: TextView = itemView.findViewById(R.id.product_qty_item_summary)


        fun bind(cartItem: CartItemOffline){
            productImage.loadImage(cartItem.product.productImage)
            productName.text = cartItem.product.productName
            productPrice.text = cartItem.product.productPrice.toString()
            productQty.text = "Qty - " + cartItem.quantity.toString()
        }
    }
}
