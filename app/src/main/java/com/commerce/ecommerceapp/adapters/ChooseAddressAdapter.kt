package com.commerce.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.models.Address


class ChooseAddressAdapter(
    val addresses: List<Address>,
    private val clickListener: IChooseAddressAdapter
) : RecyclerView.Adapter<ChooseAddressAdapter.ViewHolder>() {

    var lastSelectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_choose_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = addresses[position]
        holder.bind(address)
        holder.selectionState.isChecked = (lastSelectedPosition == position)
        holder.selectionState.setOnClickListener {
            lastSelectedPosition = position
            notifyDataSetChanged()
        }
        holder.itemAddress.setOnClickListener{
            lastSelectedPosition = position
            notifyDataSetChanged()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name_choose_address)
        val houseNumber: TextView = itemView.findViewById(R.id.house_number_choose_address)
        val streetName: TextView = itemView.findViewById(R.id.street_name_choose_address)
        val cityPincode: TextView = itemView.findViewById(R.id.city_pincode_choose_address)
        val phoneNumber: TextView = itemView.findViewById(R.id.phone_number_choose_address)
        val selectionState: RadioButton = itemView.findViewById(R.id.radio_button_choose_address)
        val itemAddress: ConstraintLayout = itemView.findViewById(R.id.item_address_choose_address)

        fun bind(address: Address) {
            userName.text = address.userName
            houseNumber.text = address.houseNumber
            streetName.text = address.streetName
            cityPincode.text = address.city + " ," + address.pincodeOfAddress
            phoneNumber.text = "Phone Number: " + address.mobileNumber
        }
    }

    override fun getItemCount(): Int {
        return addresses.size
    }
}

interface IChooseAddressAdapter {
}