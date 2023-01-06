package com.commerce.ecommerceapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commerce.ecommerceapp.*
import com.commerce.ecommerceapp.adapters.*
import com.commerce.ecommerceapp.databinding.FragmentAllProductBinding
import com.commerce.ecommerceapp.models.MainShopItem
import com.commerce.ecommerceapp.models.Product
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import java.util.stream.Collectors.toList

@AndroidEntryPoint
class AllProductFragment() : Fragment(), IFilterProductAdapter {

    private lateinit var binding: FragmentAllProductBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: ProductFilterAdapter
    private lateinit var spinner: Spinner
    private lateinit var linearLayout: LinearLayout
    private lateinit var dialog:BottomSheetDialog
    private lateinit var ItemAdapter: ItemAdapter
    private val list = ArrayList<String>()
    private lateinit var rvRecycler: RecyclerView
    var drawerLayout: DrawerLayout? = null
    var toolbar: Toolbar? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var searchedText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sorting = resources.getStringArray(R.array.sorting)
        for(i in sorting){
            list.add(i)
        }
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
        binding.fragment = this

        setupRecyclerView(FirebaseFirestore.getInstance().collection("products"))


        homeViewModel.status.observe(viewLifecycleOwner,{
            if (it==ProductStatus.DONE){
                binding.homeFragmentProgressBar.visibility = View.GONE
            }
        })
        return binding.root
    }
    fun showBottomSheet(){
        val dialogView= layoutInflater.inflate(R.layout.bottom_sheet,null)
        dialog= BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        rvRecycler = dialogView.findViewById(R.id.rvItem)

        ItemAdapter = ItemAdapter(list,object:ItemAdapter.ItemClickListener{
            override fun onItemClicked(item: String) {
                var category = item
                when(category) {
                    "Recommended" ->
                        setupRecyclerView(FirebaseFirestore.getInstance().collection("products"))
                    "Lowest Price" ->
                        setupRecyclerView(FirebaseFirestore.getInstance().collection("products").orderBy("productPrice",Query.Direction.ASCENDING))
                    "Highest Price" ->
                        setupRecyclerView(FirebaseFirestore.getInstance().collection("products").orderBy("productPrice",Query.Direction.DESCENDING))
                    "Newest" ->
                        setupRecyclerView(FirebaseFirestore.getInstance().collection("products").orderBy("productId", Query.Direction.DESCENDING))
                    "Most Evaluated" ->
                        setupRecyclerView(FirebaseFirestore.getInstance().collection("products").orderBy("reviews", Query.Direction.DESCENDING))
                }


                Toast.makeText(requireContext(),item,Toast.LENGTH_LONG).show()
                dialog.cancel()
            }

        })
        rvRecycler.adapter = ItemAdapter
        dialog.show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }
    private fun initViews() {

        binding.apply {
            // submit search when click on search button on keyboard.
            shopSearchEditText.searchListener {
                val search = shopSearchEditText.text.toString().trim()
                if (search.isNotEmpty()) {
                    searchedText = search
                    homeViewModel.getProductsHasContainName(searchedText)
                    shopSearchEditText.text?.clear()
                }
            }
        }
    }

    private fun observeListener(){
        homeViewModel.searchedProductsLiveData.observe(viewLifecycleOwner, { products ->
            when (products) {
                is Resource.Success -> {
                    products.data?.let { newsResponse ->
                        adapter = ProductFilterAdapter(newsResponse,this@AllProductFragment)
                        binding.productsRecyclerView.adapter = adapter
                        binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)

                    }
                }
                is Resource.Error -> {
                    showToast(products.msg!!)
                }
                else -> {}
            }
        })

    }



    private fun setupRecyclerView(collection: Query) {


            homeViewModel.retrieveAll(collection)

            homeViewModel.productsFilter.observe(viewLifecycleOwner, { shopList ->
                adapter = ProductFilterAdapter(shopList,this@AllProductFragment)
                binding.productsRecyclerView.adapter = adapter
                binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)

            })
    }

    override fun onProductClicked(product: Product) {
        //navigate to the product detail fragment
        val action = AllProductFragmentDirections.actionAllProductFragmentToDetailFragment(product)
        findNavController().navigate(action)

    }



}