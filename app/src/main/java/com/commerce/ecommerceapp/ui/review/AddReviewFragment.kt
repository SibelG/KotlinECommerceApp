package com.commerce.ecommerceapp.ui.review

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Utils
import com.commerce.ecommerceapp.Utils.toTime
import com.commerce.ecommerceapp.closeFragment
import com.commerce.ecommerceapp.daos.ProductDao
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.FragmentAddReviewBinding
import com.commerce.ecommerceapp.models.Review
import com.commerce.ecommerceapp.models.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class AddReviewFragment : Fragment() {

    private lateinit var viewModel: ReviewViewModel
    private lateinit var binding: FragmentAddReviewBinding
    private var userDao = UserDao()
    private var productDao = ProductDao()
    private val args by navArgs<AddReviewFragmentArgs>()
    private val productModel by lazy { args.productReviews }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddReviewBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        binding.sendButton.setOnClickListener {
            addTheReview()
        }
        return binding.root
    }

    private fun addTheReview() {
        if (binding.RVText.text.isNotEmpty() && binding.RVratingBar.rating.toString().isNotEmpty() ) {
            val rvText = binding.RVText.text.toString()
            val rvRating = binding.RVratingBar.rating.toString().toFloat()
            val time = toTime()
            GlobalScope.launch {
                val currentUser =
                    userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
                val review = Review(Utils.currentUserId,currentUser.userName,time,rvText,rvRating)
                productDao.addReview(productModel.productId,review)
                requireActivity().runOnUiThread({
                    Toast.makeText(requireContext(),productModel.productId,Toast.LENGTH_LONG).show()
                })

                withContext(Dispatchers.Main) {
                    closeFragment()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please enter all the fields", Toast.LENGTH_LONG)
                .show()
        }
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_cart).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}