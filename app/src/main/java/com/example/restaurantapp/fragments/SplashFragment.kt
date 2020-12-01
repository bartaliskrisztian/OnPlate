package com.example.restaurantapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.restaurantapp.R
import com.example.restaurantapp.viewmodel.UserViewModel


class SplashFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUsers()
        tryToLogin()
    }

    private fun loadUsers() = userViewModel.loadUsers()

    private fun tryToLogin() {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        if(sharedPref.contains("username") && sharedPref.contains("email") && sharedPref.contains("password")) {
            // TODO: go to main screen
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }

}