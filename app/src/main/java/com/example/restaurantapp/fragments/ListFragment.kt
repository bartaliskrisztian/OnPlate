package com.example.restaurantapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.adapters.RestaurantListAdapter
import com.example.restaurantapp.databinding.FragmentListBinding
import com.example.restaurantapp.viewmodel.RestaurantViewModel


class ListFragment : Fragment(), RestaurantListAdapter.OnItemClickListener {

    private lateinit var binding: FragmentListBinding
    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        recyclerView = binding.restaurantList
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        val adapter = RestaurantListAdapter(listOf(), this)
        recyclerView.adapter = adapter

        restaurantViewModel = activity?.run {
           ViewModelProvider(this).get(RestaurantViewModel::class.java)
        }!!

        restaurantViewModel.countries.observe(viewLifecycleOwner) {
            setupSpinner(it)
        }   

        restaurantViewModel.restaurants.observe(viewLifecycleOwner, {
            //adapter.setData(it)

        })

        restaurantViewModel.restaurantCount.observe(viewLifecycleOwner) {
            Log.d("aaaaa", "$it")
        }
        restaurantViewModel.getRestaurantCount()

        restaurantViewModel.currentCountry.observe(viewLifecycleOwner) {

        }


        return binding.root
    }

    private fun setupSpinner(it: List<String>?) {
        val spinner = binding.countrySpinner
        spinner.prompt = "Country"
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
        TODO("Not yet implemented")
    }
}