package com.example.ecommerceapp.ui.credid_card

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.MainActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.Utils
import com.example.ecommerceapp.adapters.CredidCardAdapter
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.FragmentAddCredidCardBinding
import com.example.ecommerceapp.databinding.FragmentCredidCardBinding
import com.example.ecommerceapp.models.Address
import com.example.ecommerceapp.models.CardTypeEnum
import com.example.ecommerceapp.models.CredidCard
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.choose_address.ChooseAddressFragment
import com.example.ecommerceapp.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AddCredidCardFragment(val previousFragment: Fragment) : Fragment() {


    private lateinit var binding: FragmentAddCredidCardBinding
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
        binding = FragmentAddCredidCardBinding.inflate(inflater)

        (activity as MainActivity).supportActionBar?.title = "Add new address"
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        userDao = UserDao()

        /*binding.n.setOnClickListener {
            addTheAddress()
        }*/
        return binding.root
    }
    private fun addTheAddress() {
        if (binding.cardCvc.text.isNotEmpty() && binding.cardExpiry.text.isNotEmpty() && binding.cardNumber.text.isNotEmpty()) {
            val cardCvc = binding.cardCvc.text.toString()
            val cardExpiry = binding.cardExpiry.text.toString()
            val cardNumber = binding.cardNumber.text.toString().toLong()
            val card = CredidCard(cardNumber,cardCvc, cardExpiry,CardTypeEnum.VISA_CARD)
            GlobalScope.launch {
                val currentUser =
                    userDao.getUserById(Utils.currentUserId).await().toObject(User::class.java)!!
                currentUser.credidCards.add(card)
                userDao.updateProfile(currentUser)
                withContext(Dispatchers.Main) {
                    goToPreviousFragment()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please enter all the fields", Toast.LENGTH_LONG)
                .show()
        }
    }
    private fun goToPreviousFragment() {
        val cardFragment = (activity as MainActivity).activeFragment
        val currentFragment = this@AddCredidCardFragment
        if (previousFragment==cardFragment){
            requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment)
                .show(cardFragment).commit()
            (previousFragment as ProfileFragment).setupRecyclerView()
            (activity as MainActivity).supportActionBar?.title = "Credid Card"
            (activity as MainActivity).setDrawerLocked(false)
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            Toast.makeText(requireContext(), "Credid Card added successfully", Toast.LENGTH_LONG).show()
        }else{
            requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment)
                .show(previousFragment).commit()
            (previousFragment as ChooseAddressFragment).setupRecyclerView()
            (activity as MainActivity).supportActionBar?.title = "Account"
            Toast.makeText(requireContext(), "Credid Card added successfully", Toast.LENGTH_LONG).show()
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