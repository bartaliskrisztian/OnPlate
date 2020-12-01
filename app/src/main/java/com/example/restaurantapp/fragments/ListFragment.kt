package com.example.restaurantapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        /*
        restaurantViewModel.restaurants.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
        */

        return binding.root
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}