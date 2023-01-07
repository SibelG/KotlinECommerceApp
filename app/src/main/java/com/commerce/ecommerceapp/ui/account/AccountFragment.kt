package com.commerce.ecommerceapp.ui.account

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
import com.commerce.ecommerceapp.*
import com.commerce.ecommerceapp.Utils.currentUserId
import com.commerce.ecommerceapp.daos.UserDao
import com.commerce.ecommerceapp.databinding.FragmentAccountBinding
import com.commerce.ecommerceapp.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: FragmentAccountBinding

    val firebaseAuth= FirebaseAuth.getInstance()
    private var mImageUri: Uri? = null
    private  var userDao = UserDao()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // (activity as MainActivity).hideBottomNav()
        setupRecyclerView()
        (activity as MainActivity).supportActionBar?.title = "Account Settings"
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        binding.fragment = this

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
            showSignOutDialog()
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
                    binding.accountImageProfile.load(currentUser.userImage)
                }

                binding.accountProfileName.text = currentUser.userName.toEditable()
                binding.accountProfileEmail.text = FirebaseAuth.getInstance().currentUser?.email
                var phone= currentUser.mobileNumber.substring(3)
                binding.accountProfilePhone.text = phone.toEditable()
            }
        }
    }
    fun openOrdersFragment() {

        val action = AccountFragmentDirections.actionNavAccountToNavOrders()
        findNavController().navigate(action)
    }

    fun openProfileFragment() {

        val action = AccountFragmentDirections.actionNavAccountToNavProfile()
        findNavController().navigate(action)
    }

    fun openUserCredidCardFragment() {

        val action = AccountFragmentDirections.actionNavAccountToCredidCardFragment()
        findNavController().navigate(action)
    }


    fun openProductRequestFragment() {

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

    fun submitUserInfo() = with(binding) {
        val userName = accountProfileName.text.toString().trim()
        val userPhone = accountProfilePhone.text.toString().trim()
        val countryCode = binding.countryCodePicker.selectedCountryCode
        if (userName.isEmpty()) {
            showToast(getString(R.string.addUserName))
            return@with
        }
        if (mImageUri == null) {
            showToast(getString(R.string.addUserImage))
            return@with
        }
        if (userPhone.isEmpty()) {
            showToast(getString(R.string.addUserEmail))
            return@with
        }
        var validPhoneNumber = "+$countryCode$userPhone"
        GlobalScope.launch {
            userDao.uploadUserInformation(
                userName,
                mImageUri,
                validPhoneNumber,
                requireContext())
            withContext(Dispatchers.Main) {
                showToast("Account updated successfully")
            }
        }



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
    private fun showSignOutDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.sign_out_dialog_title_text))
                .setMessage(getString(R.string.sign_out_dialog_message_text))
                .setNegativeButton(getString(R.string.pro_cat_dialog_cancel_btn)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(getString(R.string.dialog_sign_out_btn_text)) { dialog, _ ->
                    logout()
                    dialog.cancel()
                }
                .show()
        }
    }


}