package com.commerce.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.models.Address


class AddressAdapter(val addresses: List<Address>, private val clickListener: IAddressAdapter): RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_address,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = addresses[position]
        holder.bind(address)
        holder.editButton.setOnClickListener {
            clickListener.onEditClicked(address)
        }
        holder.deleteButton.setOnClickListener {
            clickListener.onDeleteClicked(address)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName: TextView = itemView.findViewById(R.id.user_name_address)
        val houseNumber: TextView = itemView.findViewById(R.id.house_number_address)
        val streetName: TextView = itemView.findViewById(R.id.street_name_address)
        val cityPincode: TextView = itemView.findViewById(R.id.city_pincode_address)
        val phoneNumber: TextView = itemView.findViewById(R.id.phone_number_address)
        val editButton: ImageView = itemView.findViewById(R.id.edit_button)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_address_button)


        fun bind(address: Address){
            userName.text = address.userName
            houseNumber.text = address.houseNumber
            streetName.text = address.streetName
            cityPincode.text = address.city + " ," + address.pincodeOfAddress
            phoneNumber.text = "Phone Number: "+ address.mobileNumber
        }
    }

    override fun getItemCount(): Int {
        return addresses.size
    }
}

interface IAddressAdapter{
    fun onEditClicked(address: Address)
    fun onDeleteClicked(address: Address)
}