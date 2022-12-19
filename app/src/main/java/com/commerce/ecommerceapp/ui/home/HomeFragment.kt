package com.commerce.ecommerceapp.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.commerce.ecommerceapp.MainActivity
import com.commerce.ecommerceapp.adapters.*
import com.commerce.ecommerceapp.databinding.FragmentHomeBinding
import com.commerce.ecommerceapp.models.Order
import com.commerce.ecommerceapp.models.Product
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), IProductAdapter {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var brandAdapter: BrandAdapter
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater)

        binding.viewModel = homeViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initRecycler()

    }

    private fun initRecycler(){
        adapter = ProductAdapter(this)
        binding.productsRecyclerView.adapter = adapter

        categoryAdapter = CategoryAdapter()
        binding.categoriesRecycler.adapter = categoryAdapter

        brandAdapter = BrandAdapter()
        binding.brandsRecycler.adapter = brandAdapter


        homeViewModel.products.observe(viewLifecycleOwner, {
            adapter.notifyDataSetChanged()
            binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //binding.productsRecyclerView.smoothScrollToPosition(it.size)
        })

        homeViewModel.categories.observe(viewLifecycleOwner, {
            categoryAdapter.notifyDataSetChanged()
            binding.categoriesRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //binding.categoriesRecycler.smoothScrollToPosition(it.size)
        })

        homeViewModel.brands.observe(viewLifecycleOwner, {
            brandAdapter.notifyDataSetChanged()
            binding.brandsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //binding.brandsRecycler.smoothScrollToPosition(it.size)

        })

        homeViewModel.status.observe(viewLifecycleOwner,{
            if (it==ProductStatus.DONE){
                binding.homeFragmentProgressBar.visibility = View.GONE
            }
        })

        binding.seeMoreHeadLineText.setOnClickListener {
            seeAllProduct()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun seeAllProduct(){

        val action = HomeFragmentDirections.actionNavHomeToAllProductFragment()
        findNavController().navigate(action)
        (activity as MainActivity).setDrawerLocked(true)
    }

    override fun onProductClicked(product: Product) {
        //navigate to the product detail fragment

        val action = HomeFragmentDirections.actionNavHomeToDetailFragment(product)
        findNavController().navigate(action)


    }

    override fun addCommend(order: Order) {
        TODO("Not yet implemented")
    }

}
