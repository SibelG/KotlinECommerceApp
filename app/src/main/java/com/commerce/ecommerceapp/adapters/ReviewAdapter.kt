package com.commerce.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.databinding.ItemReviewBinding
import com.commerce.ecommerceapp.models.Review
import javax.inject.Inject


class ReviewAdapter @Inject constructor(val reviews: List<Review>): RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_review,parent,false))
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
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val reviewName: TextView = itemView.findViewById(R.id.RVname)
        val reviewDate: TextView = itemView.findViewById(R.id.RVDate)
        val reviewDesc: TextView = itemView.findViewById(R.id.reviewDesc)
        val rating: RatingBar = itemView.findViewById(R.id.ratingBar)

        @SuppressLint("ResourceAsColor")
        fun bind(review: Review){
            reviewName.text = review.username
            reviewDate.text = review.date
            reviewDesc.text = review.text
            rating.rating = review.rating

        }
    }
}

