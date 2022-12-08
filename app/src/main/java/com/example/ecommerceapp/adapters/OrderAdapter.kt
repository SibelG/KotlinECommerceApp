package com.example.ecommerceapp.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.loadImage
import com.example.ecommerceapp.models.Order
import com.example.ecommerceapp.models.OrderStatus


class OrderAdapter(private val clickListener: IOrderAdapter,val orders: List<Order>): RecyclerView.Adapter<OrderAdapter.ViewHolder>(){

    private val n = orders.size -1
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[n - position]
        holder.bind(order,context)
        holder.itemOrder.setOnClickListener {
            clickListener.onOrderClicked(order)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemOrder: CardView = itemView.findViewById(R.id.item_order_view)
        val productsName: TextView = itemView.findViewById(R.id.products_name_item_order)
        val orderStatus: TextView = itemView.findViewById(R.id.order_status_item_order)
        val orderImage: ImageView = itemView.findViewById(R.id.orderImage)

        fun bind(order: Order, context: Context){
            val cartItems = order.cart.items
            var name = cartItems[0].productName
            var image = cartItems[0].productImage
            for(i in 1..(cartItems.size-1)){
                name += ", " + cartItems[i].productName
                image += cartItems[i].productImage
            }
            orderImage.loadImage(image)
            productsName.text = name
            val status = order.orderStatus
            var statusString = ""
            when(status){
                OrderStatus.PLACED ->{
                    statusString = "Order Placed"
                }
                OrderStatus.APPROVED->{
                    statusString = "Order Approved"
                }
                OrderStatus.CANCELLED->{
                    statusString = "Order Cancelled"
                    val color = context.resources.getColor(R.color.red)
                    orderStatus.setTextColor(color)
                }
                OrderStatus.CANCEL_REQUESTED->{
                    statusString = "Cancellation Requested"
                    val color = context.resources.getColor(R.color.red)
                    orderStatus.setTextColor(color)
                }
                OrderStatus.DELIVERED->{
                    statusString = "Order Delivered"
                }
                OrderStatus.PACKED->{
                    statusString = "Order Packed"
                }
                OrderStatus.REJECTED->{
                    statusString = "Order Rejected"
                    val color = context.resources.getColor(R.color.red)
                }
                OrderStatus.RETURNED->{
                    statusString = "Order Returned"
                    val color = context.resources.getColor(R.color.red)
                }
                OrderStatus.SHIPPED->{
                    statusString = "Order Shipped"
                }
            }
            orderStatus.text = statusString
        }
    }
}

interface IOrderAdapter{
    fun onOrderClicked(order: Order)
}