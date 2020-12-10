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

        }

        return binding.root
    }


}