package com.example.ecommerceapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapters.BrandAdapter
import com.example.ecommerceapp.adapters.CategoryAdapter
import com.example.ecommerceapp.adapters.IProductAdapter
import com.example.ecommerceapp.adapters.ProductAdapter
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.ui.detail.DetailFragment



class HomeFragment : Fragment(), IProductAdapter {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var brandAdapter: BrandAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater)

        binding.viewModel = homeViewModel
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
        return binding.root
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun seeAllProduct(){

       /* val action = HomeFragmentDirections.actionNavHomeToAllProductFragment()
        findNavController().navigate(action)*/
        val currentFragment = this
        val productAllFragment = AllProductFragment()
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment_content_main,
            productAllFragment,
            getString(R.string.title_detail_fragment)
        ).hide(currentFragment).commit()
        (activity as MainActivity).setDrawerLocked(true)
    }

    override fun onProductClicked(productId: String) {
        //navigate to the product detail fragment
        val currentFragment = this
        val productDetailFragment = DetailFragment(productId, "HomeFragment")
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment_content_main,
            productDetailFragment,
            getString(R.string.title_detail_fragment)
        ).hide(currentFragment).commit()
        (activity as MainActivity).setDrawerLocked(true)
    }
}
