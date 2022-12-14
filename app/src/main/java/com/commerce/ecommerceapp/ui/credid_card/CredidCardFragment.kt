package com.commerce.ecommerceapp.ui.credid_card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.commerce.ecommerceapp.MainActivity
import com.commerce.ecommerceapp.R
import com.commerce.ecommerceapp.Utils.currentUserId
import com.commerce.ecommerceapp.adapters.CredidCardAdapter
import com.commerce.ecommerceapp.adapters.ICredidCardAdapter
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.FragmentCredidCardBinding
import com.commerce.ecommerceapp.models.CredidCard
import com.commerce.ecommerceapp.models.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CredidCardFragment : Fragment() ,ICredidCardAdapter{

    private lateinit var viewModel: CredidCardViewModel
    private lateinit var binding: FragmentCredidCardBinding
    private lateinit var userDao: UserDao
    lateinit var adapter: CredidCardAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCredidCardBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(CredidCardViewModel::class.java)

        (activity as MainActivity).supportActionBar?.title = "Add new address"
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        userDao = UserDao()

        binding.newCardButton.setOnClickListener {
            addTheAddress()
        }
        return binding.root
    }

    private fun addTheAddress() {
        val addCredid = AddCredidCardFragment(this)
        val currentFragment = this
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment,
            addCredid,
            getString(R.string.title_add_credid)
        ).hide(currentFragment).commit()
        (activity as MainActivity).setDrawerLocked(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            (activity as MainActivity).drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun setupRecyclerView() {
        GlobalScope.launch {
            val currentUser =
                userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            val credidCards = currentUser.credidCards
            withContext(Dispatchers.Main) {
                adapter = CredidCardAdapter(credidCards, this@CredidCardFragment)
                binding.cardsRecycler.adapter = adapter
            }
        }
    }

    override fun onDeleteClicked(card: CredidCard) {
        GlobalScope.launch {
            val currentUser = userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            currentUser.credidCards.remove(card)
            userDao.updateProfile(currentUser)
            withContext(Dispatchers.Main){
                setupRecyclerView()
                Toast.makeText(requireContext(),"Card deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }


}