package com.example.restaurantapp.fragments.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.model.Restaurant

class RestaurantListAdapter(private var restaurants: List<Restaurant>, private val listener: OnItemClickListener):
    RecyclerView.Adapter<RestaurantListAdapter.RestaurantListHolder>() {

    inner class RestaurantListHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return RestaurantListHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantListHolder, position: Int) {
        val currentItem = restaurants[position]
    }

    override fun getItemCount(): Int = restaurants.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}