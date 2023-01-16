package com.commerce.ecommerceapp.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.app.ActivityCompat.recreate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.SharedPreferencesUtils.setLanguageCode
import com.commerce.ecommerceapp.SharedPreferencesUtils.setLanguagePosition
import com.commerce.ecommerceapp.Utils.currentUserId
import com.commerce.ecommerceapp.adapters.FavoriteAdapter
import com.commerce.ecommerceapp.adapters.NotificationAdapter
import com.commerce.ecommerceapp.adapters.ProductFilterAdapter
import com.commerce.ecommerceapp.adapters.ReviewAdapter
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.CartFragmentBinding
import com.commerce.ecommerceapp.databinding.FragmentNotificationBinding
import com.commerce.ecommerceapp.helper.LocaleHelper
import com.commerce.ecommerceapp.models.Product
import com.commerce.ecommerceapp.models.User
import com.commerce.ecommerceapp.ui.favorites.FavoriteViewModel
import com.commerce.ecommerceapp.ui.home.HomeViewModel
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var viewModel: NotificationViewModel
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var adapter: NotificationAdapter
    private var userDao= UserDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        binding.viewModel = viewModel

        setupRecyclerView()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //observeListener()
    }

    private fun observeListener(){

        viewModel.getByNotify()
        viewModel.notifyFilter.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.emptyNotifyTextview.visibility = View.VISIBLE
            }
            else{
                binding.emptyNotifyTextview.visibility = View.GONE
                adapter.notifyDataSetChanged()
                binding.notifyRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }

        })

    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_cart).setVisible(false)
        super.onPrepareOptionsMenu(menu)
    }

    fun setupRecyclerView() {

        GlobalScope.launch {
            val notifyList =
                userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            val notify = notifyList.notifyList
            if(notify.isEmpty())
                binding.emptyNotifyTextview.visibility = View.VISIBLE

            withContext(Dispatchers.Main) {
                adapter = NotificationAdapter(notify)
                binding.notifyRV.adapter = adapter
            }
        }
    }
}