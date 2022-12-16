package com.example.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.databinding.ItemProductBinding
import com.example.ecommerceapp.databinding.ItemReviewBinding
import com.example.ecommerceapp.models.Review
import javax.inject.Inject


class ReviewAdapter @Inject constructor(val reviews: List<Review>): RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReviewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)

    }
    /*fun setData(listOfReview : List<Review>) {
        listOfReview = listOfReview as ArrayList<Review>
        notifyDataSetChanged()
    }*/
    override fun getItemCount(): Int {
        return reviews.size
    }
    class ViewHolder(private var binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root){

        val reviewName: TextView = binding.RVname
        val reviewDate: TextView = binding.RVDate
        val reviewDesc: TextView = binding.productsNameItemReview
        val rating: RatingBar = binding.ratingBar

        @SuppressLint("ResourceAsColor")
        fun bind(review: Review){
            reviewName.text = review.username
            reviewDate.text = review.date
            reviewDesc.text = review.text
            rating.rating = review.rating

            binding.executePendingBindings()
        }
    }
}

