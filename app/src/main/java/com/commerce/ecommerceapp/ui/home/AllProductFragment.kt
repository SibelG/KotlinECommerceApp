package com.commerce.ecommerceapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.commerce.ecommerceapp.MainActivity
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.adapters.*
import com.commerce.ecommerceapp.databinding.FragmentAllProductBinding
import com.commerce.ecommerceapp.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllProductFragment() : Fragment(), IFilterProductAdapter {

    private lateinit var binding: FragmentAllProductBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: ProductFilterAdapter
    private lateinit var spinner: Spinner
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val homeFragment = (activity as MainActivity).activeFragment
                    val currentFragment = this@AllProductFragment
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(currentFragment).show(homeFragment).commit()
                    (activity as MainActivity).supportActionBar?.title = "Home"
                    (activity as MainActivity).setDrawerLocked(false)
                    (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_product, container, false)

        binding.viewModel = homeViewModel

        setupRecyclerView(FirebaseFirestore.getInstance().collection("products"))

        sortProduct()
        filterProduct()

        homeViewModel.status.observe(viewLifecycleOwner,{
            if (it==ProductStatus.DONE){
                binding.homeFragmentProgressBar.visibility = View.GONE
            }
        })
        return binding.root
    }


    private fun sortProduct(){
        val sorting = resources.getStringArray(R.array.sorting)
        spinner = binding.sortSpinner
        spinner.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        linearLayout = binding.linearLayoutSort
        //add spinner in linear layout
        if (spinner.getParent() != null) {
            (spinner.getParent() as ViewGroup).removeView(spinner) // <- fix
        }
        linearLayout?.addView(spinner)

        if (spinner != null) {
            val adapterSort = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, sorting
            )
            spinner.adapter = adapterSort
        }
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                var str = parent.getItemAtPosition(position);
                when(str) {
                    "Recommended" -> setupRecyclerView(FirebaseFirestore.getInstance().collection("products"))
                    "Lowest Price" -> setupRecyclerView(FirebaseFirestore.getInstance().collection("products").orderBy("productPrice"))
                    "Highest Price" -> setupRecyclerView(FirebaseFirestore.getInstance().collection("products").orderBy("productPrice",Query.Direction.DESCENDING))
                    "Newest" -> setupRecyclerView(FirebaseFirestore.getInstance().collection("products").orderBy("productId", Query.Direction.DESCENDING))



                    else -> print("I don't know anything about it")
                }
                Toast.makeText(requireContext(),
                    getString(R.string.selected_item) + " " +
                            "" + sorting[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun setupRecyclerView(collection: Query) {


            homeViewModel.retrieveAll(collection)

            homeViewModel.productsFilter.observe(viewLifecycleOwner, { shopList ->
                adapter = ProductFilterAdapter(shopList,this@AllProductFragment)
                binding.productsRecyclerView.adapter = adapter
                binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)

            })
    }
    private fun filterProduct(){
        val filtering = resources.getStringArray(R.array.filtering)
        spinner = binding.filterSpinner
        spinner.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        linearLayout = binding.linearLayoutFilter
        //add spinner in linear layout
        if (spinner.getParent() != null) {
            (spinner.getParent() as ViewGroup).removeView(spinner) // <- fix
        }
        linearLayout?.addView(spinner)

        if (spinner != null) {
            val adapterFilter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, filtering
            )
            spinner.adapter = adapterFilter
        }
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                /*Toast.makeText(requireContext(),
                    getString(R.string.selected_item) + " " +
                            "" + languages[position], Toast.LENGTH_SHORT).show()*/
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

    override fun onProductClicked(product: Product) {
        //navigate to the product detail fragment
        val action = AllProductFragmentDirections.actionAllProductFragmentToDetailFragment(product)
        findNavController().navigate(action)

    }



}