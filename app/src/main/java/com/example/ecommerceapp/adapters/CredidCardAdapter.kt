package com.example.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.models.CredidCard

class CredidCardAdapter(val card: List<CredidCard>, private val clickListener: ICredidCardAdapter): RecyclerView.Adapter<CredidCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_credit_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val credidCard = card[position]
        holder.bind(credidCard)

        holder.deleteButton.setOnClickListener {
            clickListener.onDeleteClicked(credidCard)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardNumber: TextView = itemView.findViewById(R.id.cardNumber)
        val cardCvc: TextView = itemView.findViewById(R.id.cardCvc)
        val cardExpiryDate: TextView = itemView.findViewById(R.id.cardExpiryDate)
        val deleteButton: Button =itemView.findViewById(R.id.deleteButtton)


        fun bind(card: CredidCard){
            cardNumber.text = card.cardNumber.toString()
            cardCvc.text = card.cardCvc
            cardExpiryDate.text = card.cardExpiryDate
        }
    }

    override fun getItemCount(): Int {
        return card.size
    }
}

interface ICredidCardAdapter{
    fun onDeleteClicked(card: CredidCard)
}