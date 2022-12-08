package com.example.ecommerceapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
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
import com.example.ecommerceapp.ui.account.AccountFragmentDirections
import com.example.ecommerceapp.ui.detail.DetailFragment



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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
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
        /*val currentFragment = this
        val productDetailFragment = DetailFragment(productId)
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment,
            productDetailFragment,
            getString(R.string.title_detail_fragment)
        ).commit()*/
        /*val action= HomeFragmentDirections.actionNavHomeToDetailFragment()
        navController.navigate(action)*/
        //(activity as MainActivity).setDrawerLocked(true)
        /*val bundle = Bundle()
        bundle.putParcelable("productDetails", productId)
        findNavController().navigate(
            R.id.action_nav_home_to_detailFragment,
            bundle
        )*/

    }
}
