package com.example.restaurantapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUsers()
        loadCountries()
        getRestaurantCount()

        // when the countries are loaded from the db, we load the restaurants from BE or the db
        restaurantViewModel.countries.observe(viewLifecycleOwner) {
            restaurantViewModel.searchQuery.value = ""
            restaurantViewModel.showFavorites.value = false
            restaurantViewModel.restaurantCount.observe(viewLifecycleOwner) {
                if(it == 0) {
                    loadRestaurantsFromApi()
                }
                else {
                    loadRestaurantsFromDatabase()
                }
            }
        }

        restaurantViewModel.dataLoadedFromApi.observe(viewLifecycleOwner) {
            loadRestaurantsFromDatabase()
        }

        // showing the user the loading state
        restaurantViewModel.currentLoadingState.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.loadingState).text = "...loading restaurants from state: $it"
        }

        // when the restaurants are loaded, we try to login the user if there is a shared preferences
        restaurantViewModel.restaurantsLoaded.observe(viewLifecycleOwner) {
            tryToLogin()
        }

        userViewModel.currentUser.observe(viewLifecycleOwner) {
            if(it != null) {
                findNavController().navigate(R.id.action_splashFragment_to_listFragment)
            }
            else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }



    private fun loadCountries() = restaurantViewModel.loadCountries()
    private fun loadRestaurantsFromApi() = restaurantViewModel.loadRestaurantsFromApi()
    private fun loadRestaurantsFromDatabase() = restaurantViewModel.loadRestaurantsFromDatabase()
    private fun loadUsers() = userViewModel.loadUsers()
    private fun getRestaurantCount() = restaurantViewModel.getRestaurantCount()

    private fun tryToLogin() {
        // load user by the data from the shared preferences
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        if(sharedPref.contains("username") && sharedPref.contains("email") && sharedPref.contains("password")) {
            val username = sharedPref.getString("username", "")
            val email = sharedPref.getString("email", "")
            userViewModel.loadCurrentUser(username!!, email!!)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }

}