package com.example.restaurantapp.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.adapters.RestaurantListAdapter
import com.example.restaurantapp.databinding.FragmentListBinding
import com.example.restaurantapp.model.FavoriteRestaurants
import com.example.restaurantapp.viewmodel.FavoritesViewModel
import com.example.restaurantapp.viewmodel.RestaurantViewModel
import com.example.restaurantapp.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class ListFragment : Fragment(), RestaurantListAdapter.OnItemClickListener {

    private lateinit var binding: FragmentListBinding
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        //loading(true)

        recyclerView = binding.restaurantList
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        val adapter = RestaurantListAdapter(listOf(), this, binding.root.context, favoritesViewModel, userViewModel, restaurantViewModel)
        recyclerView.adapter = adapter

        restaurantViewModel.restaurantImages.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        // when countries are loaded from the API, spinner with countries is set up
        restaurantViewModel.countries.observe(viewLifecycleOwner) {
            setupCountrySpinner(it)
            setupPriceSpinner()
            restaurantViewModel.currentPrice.value = 0
        }

        // when restaurants are loaded from the database, we set the country filter
        restaurantViewModel.restaurants.observe(viewLifecycleOwner, {
            val countries = restaurantViewModel.countries.value
            if (countries != null) {
                if(countries.isNotEmpty()) {
                    restaurantViewModel.currentCountry.value = countries[0]
                }
            }
        })

        // when the country filter is set, we load
        restaurantViewModel.currentCountry.observe(viewLifecycleOwner) {
            restaurantViewModel.loadRestaurantsFromCountry()
        }

        restaurantViewModel.restaurantsFromCountry.observe(viewLifecycleOwner) {
            restaurantViewModel.loadCurrentCities()
        }

        restaurantViewModel.currentCities.observe(viewLifecycleOwner) {
            setupCitySpinner(it)
            if(restaurantViewModel.currentCities.value.isNullOrEmpty()) {
                restaurantViewModel.currentCity.value = "-"
            }
            else {
                restaurantViewModel.currentCity.value = restaurantViewModel.currentCities.value?.get(0)
            }
            //restaurantViewModel.currentCity.value = binding.citySpinner.selectedItem.toString()
        }

        restaurantViewModel.currentCity.observe(viewLifecycleOwner) {
            loading(true)
            restaurantViewModel.applyFilters(listOf())
        }

        restaurantViewModel.currentPrice.observe(viewLifecycleOwner) {
            loading(true)
            restaurantViewModel.applyFilters(listOf())
        }

        binding.searchInput.addTextChangedListener{
            restaurantViewModel.searchQuery.value = it.toString()
        }

        restaurantViewModel.searchQuery.observe(viewLifecycleOwner) {
            loading(true)
            restaurantViewModel.applyFilters(listOf())
        }

        binding.favoriteFilterButton.setOnClickListener {
            val showFavorites = restaurantViewModel.showFavorites.value
            restaurantViewModel.showFavorites.value = !showFavorites!!
        }

        restaurantViewModel.showFavorites.observe(viewLifecycleOwner) {
            loading(true)
            val favorites: List<FavoriteRestaurants> = if(favoritesViewModel.favorites.value == null) {
                listOf()
            } else {
                favoritesViewModel.favorites.value!!
            }
            restaurantViewModel.applyFilters(favorites)
        }

        restaurantViewModel.filteredRestaurants.observe(viewLifecycleOwner) {
            if(it != null) {
                binding.noResult.visibility = View.GONE
                adapter.setData(it)
            }
            else {
                binding.noResult.visibility = View.VISIBLE
            }
            loading(false)
        }

        restaurantViewModel.showFavorites.observe(viewLifecycleOwner) {
            if(it) {
                binding.favoriteFilterButton.imageTintList =  ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.appBackgroundColor))
            }
            else {
                binding.favoriteFilterButton.imageTintList =  ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }

        return binding.root
    }

    private fun loading(b: Boolean) {
        if(b){
            binding.progressCircular.visibility = View.VISIBLE
            binding.restaurantList.visibility = View.INVISIBLE
        }
        else{
            binding.progressCircular.visibility = View.GONE
            binding.restaurantList.visibility = View.VISIBLE
        }
    }

    private fun setupPriceSpinner() {
        val prices = arrayListOf("-","1", "2", "3", "4", "5")
        val spinner = binding.priceSpinner
        val arrayAdapter = ArrayAdapter(binding.root.context, R.layout.spinner_item_layout, prices)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {
                    restaurantViewModel.currentPrice.value = 0
                }
                else {
                    restaurantViewModel.currentPrice.value = prices[position].toInt()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupCitySpinner(it: List<String>?) {
        val spinner = binding.citySpinner
        val list = arrayListOf("-")
        if (it != null) {
            list.addAll(it)
        }
        val arrayAdapter = ArrayAdapter(binding.root.context, R.layout.spinner_item_layout, list)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                restaurantViewModel.currentCity.value = restaurantViewModel.currentCities.value?.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupCountrySpinner(it: List<String>?) {
        val spinner = binding.countrySpinner
        val arrayAdapter = ArrayAdapter(binding.root.context, R.layout.spinner_item_layout, it!!)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                restaurantViewModel.currentCountry.value = restaurantViewModel.countries.value?.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onItemClick(position: Int) {
        val restaurant = restaurantViewModel.filteredRestaurants.value?.get(position)
        restaurantViewModel.currentRestaurant.value = restaurant
        findNavController().navigate(R.id.action_listFragment_to_detailFragment)
    }
}