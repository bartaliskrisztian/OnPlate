package com.example.restaurantapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.restaurantapp.R
import com.example.restaurantapp.viewmodel.RestaurantViewModel
import com.example.restaurantapp.viewmodel.UserViewModel


class SplashFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()

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
        loadCountries()
        loadRestaurants()

        restaurantViewModel.currentLoadingState.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.loadingState).text = "...loading restaurants from state: $it"
        }

        restaurantViewModel.dataIsLoaded.observe(viewLifecycleOwner) {
            if (it) {
                tryToLogin()
            }
        }

    }



    private fun loadCountries() = restaurantViewModel.loadCountries()
    private fun loadRestaurants() = restaurantViewModel.loadRestaurants()
    private fun loadUsers() = userViewModel.loadUsers()

    private fun tryToLogin() {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        if(sharedPref.contains("username") && sharedPref.contains("email") && sharedPref.contains("password")) {
            findNavController().navigate(R.id.action_splashFragment_to_listFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }

}