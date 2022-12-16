package com.example.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayoutStates
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ecommerceapp.databinding.ItemBrandBinding
import com.example.ecommerceapp.generated.callback.OnClickListener
import com.example.ecommerceapp.models.Brand
import com.example.ecommerceapp.models.Product
import javax.inject.Inject


class BrandAdapter() : ListAdapter<Brand, BrandAdapter.ViewHolder?>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBrandBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val brand = getItem(position)
        holder.bind(brand)
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(brand) } }
        /*holder.brandItem.setOnClickListener{
            clickListener.onBrandClicked(brand)
        }*/

    }

    class ViewHolder(private var binding: ItemBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val brandImage: ImageView = binding.brandImageView
        val brandName: TextView = binding.brandTitle
        val brandProducts: TextView = binding.brandProducts
        val brandItem: ConstraintLayout = binding.brandItem


        @SuppressLint("ResourceAsColor")
        fun bind(brand: Brand) {
            brandImage.load(brand.brandImage)
            brandName.text = brand.brandTitle
            brandProducts.text = brand.brandProductsNumber.toString()

        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Brand>() {
        override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem.brandId == newItem.brandId
        }

        override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem == newItem
        }
    }

    private var onItemClickListener: ((Brand) -> Unit)? = null
    fun onItemClicked(listner: (Brand) -> Unit) {
        onItemClickListener = listner
    }
}
interface IBrandAdapter{
    fun onBrandClicked(brand: Brand)
}