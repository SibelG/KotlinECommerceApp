package com.example.ecommerceapp.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapters.AddressAdapter
import com.example.ecommerceapp.adapters.FavoritesAdapter
import com.example.ecommerceapp.adapters.ProductAdapter
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.FragmentFavoritesBinding
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.databinding.FragmentProfileBinding
import com.example.ecommerceapp.ui.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth


class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    //private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var binding: FragmentFavoritesBinding
    lateinit var adapter: FavoritesAdapter
    private lateinit var userDao: UserDao
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /*favoritesViewModel =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)*/
        binding = FragmentFavoritesBinding.inflate(inflater)

        /*binding.viewModel = favoritesViewModel
        adapter = FavoritesAdapter(this)
        binding.productsRecyclerView.adapter = adapter*/
        return binding.root
    }

}
