package com.example.restaurantapp.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
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
        user = userViewModel.currentUser.value!!
        if(user != null) {
            setupUI()
        }

        return binding.root
    }

    private fun setupUI() {
        val bitmap = BitmapFactory.decodeByteArray(user.picture, 0, user.picture.size)
        binding.profilePic.setImageBitmap(bitmap)
        binding.userNameText.text = user.username
        binding.userEmailText.text = user.email
        binding.userPhoneText.text = user.phone_number
        binding.userAddressText.text = user.address
    }
}