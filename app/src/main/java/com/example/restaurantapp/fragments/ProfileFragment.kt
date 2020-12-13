package com.example.restaurantapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentProfileBinding
import com.example.restaurantapp.model.User
import com.example.restaurantapp.viewmodel.UserViewModel


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        userViewModel = activity?.run {
            ViewModelProvider(this).get(UserViewModel::class.java)
        }!!

        // if the current user is not null, then we set the UI
        if(userViewModel.currentUser.value != null) {
            user = userViewModel.currentUser.value!!
            setupUI()
        }

        // if the user logs out, we clear the shared preferences and navigate to LoginFragment
        binding.logoutButton.setOnClickListener {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            val sharedPrefEdit = sharedPref.edit()
            sharedPrefEdit.clear()
            sharedPrefEdit.apply()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun setupUI() {
        Glide.with(requireActivity()).load(user.image).into(binding.profilePic)
        binding.userNameText.text = user.username
        binding.userEmailText.text = user.email
        binding.userPhoneText.text = user.phone_number
        binding.userAddressText.text = user.address
    }
}