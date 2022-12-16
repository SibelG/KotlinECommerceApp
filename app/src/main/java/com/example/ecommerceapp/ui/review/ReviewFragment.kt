package com.example.ecommerceapp.ui.review

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.Utils.currentUserId
import com.example.ecommerceapp.adapters.*
import com.example.ecommerceapp.daos.ProductDao
import com.example.ecommerceapp.databinding.FragmentReviewBinding
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.Review
import com.example.ecommerceapp.models.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private lateinit var binding: com.example.ecommerceapp.databinding.FragmentReviewBinding
    lateinit var adapter: ReviewAdapter
    private var productDao= ProductDao()
    private val args by navArgs<ReviewFragmentArgs>()
    private val productModel by lazy { args.productReviews }
    private lateinit var viewModel: ReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        binding.viewModel = viewModel

        setupRecyclerView(productModel.productId)
        Toast.makeText(requireContext(),productModel.productId,Toast.LENGTH_LONG).show()

        viewModel.listIsEmpty.observe(viewLifecycleOwner, {
            if (it) {
                binding.emptyReview.visibility=View.VISIBLE
            }
        })
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    fun setupRecyclerView(productId: String) {

        GlobalScope.launch {
            val currentReview =
                productDao.getProductById(productId).await().toObject(Product::class.java)!!
            val reviews = currentReview.reviews
            if(reviews.size==0)
                binding.emptyReview.visibility=View.VISIBLE

            withContext(Dispatchers.Main) {
                adapter = ReviewAdapter(reviews)
                binding.reviewRV.adapter = adapter
                //viewModel.reviewAll(productId)
            }
        }
    }

}