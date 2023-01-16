package com.commerce.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Utils.toTime
import com.commerce.ecommerceapp.models.Brand
import com.commerce.ecommerceapp.models.CartItemOffline
import com.commerce.ecommerceapp.models.NotificationData
import com.commerce.ecommerceapp.models.Review
import javax.inject.Inject


class NotificationAdapter @Inject constructor(val notify: List<NotificationData>) :RecyclerView.Adapter<NotificationAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notify,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notifyPos = notify[position]
        holder.bind(notifyPos)

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val notifyDate: TextView = itemView.findViewById(R.id.RVDate)
        val title: TextView = itemView.findViewById(R.id.title)
        val context: TextView = itemView.findViewById(R.id.context)

        @SuppressLint("ResourceAsColor")
        fun bind(notifyItem: NotificationData){

            notifyDate.text = toTime()
            title.text = notifyItem.title
            context.text = notifyItem.message

        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<NotificationData>() {
        override fun areItemsTheSame(oldItem: NotificationData, newItem: NotificationData): Boolean {
            return oldItem.userUID== newItem.userUID
        }

        override fun areContentsTheSame(oldItem: NotificationData, newItem: NotificationData): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemCount(): Int {
        return notify.size
    }
}
