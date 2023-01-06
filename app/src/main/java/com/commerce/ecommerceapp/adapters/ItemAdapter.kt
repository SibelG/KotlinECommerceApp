package com.commerce.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.models.Product

class ItemAdapter(private var mList:ArrayList<String>,private var onClickEvent:ItemClickListener): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
         return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.itemText.text = item
        holder.itemText.setOnClickListener{
            onClickEvent.onItemClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val itemText: TextView = itemView.findViewById(R.id.tvItem)
    }
    interface ItemClickListener{
        fun onItemClicked(item: String)
    }


}