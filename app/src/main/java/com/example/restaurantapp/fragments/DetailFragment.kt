package com.example.restaurantapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentDetailBinding
import com.example.restaurantapp.model.Restaurant
import com.example.restaurantapp.viewmodel.RestaurantViewModel

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        restaurantViewModel.currentRestaurant.observe(viewLifecycleOwner) {
            if(it != null) {
                changeUI(it)
            }
        }

        return binding.root
    }

    private fun changeUI(restaurant: Restaurant) {
        binding.restaurantTitle.text = restaurant.name
        binding.restaurantAddress.text = restaurant.address
        binding.restaurantCountry.text = restaurant.country
        binding.restaurantPostalCode.text = restaurant.postal_code
        binding.restaurantPhone.text = restaurant.phone
        binding.restaurantPrice.text = restaurant.price.toString()
        binding.restaurantReserve.text = restaurant.reserve_url
    }

}