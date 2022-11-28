package com.example.ecommerceapp.ui.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.ecommerceapp.*
import com.example.ecommerceapp.Utils.currentUserId
import com.example.ecommerceapp.daos.UserDao
import com.example.ecommerceapp.databinding.FragmentAccountBinding
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.address.AddressFragment
import com.example.ecommerceapp.ui.credid_card.CredidCardFragment
import com.example.ecommerceapp.ui.orders.OrdersFragment
import com.example.ecommerceapp.ui.product_request.ProductRequestFragment
import com.example.ecommerceapp.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: FragmentAccountBinding

    val firebaseAuth= FirebaseAuth.getInstance()
    private var mImageUri: Uri? = null
    private lateinit var userDao: UserDao
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // (activity as MainActivity).hideBottomNav()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        userDao = UserDao()

        setupRecyclerView()

        (activity as MainActivity).supportActionBar?.title = "Account Settings"
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)


        binding.cardSection.setOnClickListener{
            openUserCredidCardFragment()
        }
        binding.profileSection.setOnClickListener{
            openProfileFragment()
        }
        binding.orderSection.setOnClickListener{
            openOrdersFragment()
        }
        binding.logoutSection.setOnClickListener{
            logout()
        }
        binding.requestSection.setOnClickListener{
            openProductRequestFragment()
        }
        binding.accountImageProfile.setOnClickListener {
            changeUserProfileImage()
        }
        binding.trackSection.setOnClickListener{
            trackingFragment()

        }

        // Inflate the layout for this fragment
        return binding.root
    }

    fun setupRecyclerView() {
        GlobalScope.launch {
            val currentUser =
                userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            withContext(Dispatchers.Main) {
                if(currentUser.userImage!=null){
                    binding.accountImageProfile.load(currentUser.userImage){
                        transformations(RoundedCornersTransformation())
                    }
                }

                binding.accountProfileEmail.text = currentUser.mobileNumber
            }
        }
    }
    fun openOrdersFragment() {
        /*val currentFragment = this
        val ordersFragment = OrdersFragment()
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment,
            ordersFragment,
            getString(R.string.menu_orders)
        ).hide(currentFragment).commit()*/
        val action = AccountFragmentDirections.actionNavAccountToNavOrders()
        findNavController().navigate(action)
    }

    fun openProfileFragment() {
       /* val profileFragment = ProfileFragment("AccountFragment")
        val currentFragment = this
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment_content_main,
            profileFragment,
            getString(R.string.menu_profile)
        ).hide(currentFragment).commit()*/
        val action = AccountFragmentDirections.actionNavAccountToNavProfile()
        findNavController().navigate(action)
    }

    fun openUserCredidCardFragment() {
       /* val currentFragment = this
        val credidFragment = CredidCardFragment()
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment,
            credidFragment,
            getString(R.string.menu_credid)
        ).hide(currentFragment).commit()*/
        val action = AccountFragmentDirections.actionNavAccountToCredidCardFragment()
        findNavController().navigate(action)
    }


    fun openProductRequestFragment() {
        /*val currentFragment = this
        val productRequestFragment = ProductRequestFragment()
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.nav_host_fragment,
            productRequestFragment,
            getString(R.string.menu_request)
        ).hide(currentFragment).commit()*/
        val action = AccountFragmentDirections.actionNavAccountToProductRequestFragment()
        findNavController().navigate(action)
    }
    fun openHelpFragment() {
        // TODO: Add help information.
    }

    fun openAboutFragment() {
        // TODO: Add about app information.
    }

    fun trackingFragment(){
        val action = AccountFragmentDirections.actionNavAccountToTrackingFragment()
        findNavController().navigate(action)
    }

    fun logout() {
        firebaseAuth.signOut()
        navigateToAuthFragment()
        showToast(getString(R.string.logOutMessage))
    }
    private fun navigateToAuthFragment() {
        val intent = Intent(requireContext(), AuthenticationActivity::class.java)
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        showUserImage()
    }


    fun changeUserProfileImage() {
        selectImageFromGallery()
    }

    private fun selectImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            mImageUri = result.data?.data
            showUserImage()
        }

    private fun showUserImage() {
        if (mImageUri != null)
            binding.accountImageProfile.setImageURI(mImageUri)
    }

}