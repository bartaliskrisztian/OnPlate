package com.example.restaurantapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.R
import com.example.restaurantapp.model.FavoriteRestaurants
import com.example.restaurantapp.model.Restaurant
import com.example.restaurantapp.viewmodel.FavoritesViewModel
import com.example.restaurantapp.viewmodel.RestaurantViewModel
import com.example.restaurantapp.viewmodel.UserViewModel

class FavoritesListAdapter(
        private var restaurants: List<FavoriteRestaurants>,
        private val listener: OnItemClickListener,
        private val context: Context,
        private val restaurantViewModel: RestaurantViewModel,
        private val userViewModel: UserViewModel):
RecyclerView.Adapter<FavoritesListAdapter.RestaurantListHolder>() {

    inner class RestaurantListHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                restaurantViewModel.currentRestaurant.value = restaurants[position].restaurant
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesListAdapter.RestaurantListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item_layout, parent, false)
        return RestaurantListHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoritesListAdapter.RestaurantListHolder, position: Int) {
        val currentItem = restaurants[position].restaurant
        holder.itemView.findViewById<ImageButton>(R.id.favoriteButton).visibility = View.GONE
        val restaurantImage = holder.itemView.findViewById<ImageView>(R.id.restaurantImage)
        holder.itemView.findViewById<TextView>(R.id.restaurantTitle).text = currentItem.name
        holder.itemView.findViewById<TextView>(R.id.restaurantAddress).text = currentItem.address
        holder.itemView.findViewById<TextView>(R.id.restaurantPrice).text = currentItem.price.toString()
        Glide.with(context).load(currentItem.image_url).into(restaurantImage)


    }

    override fun getItemCount(): Int = restaurants.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setData(pRestaurants: List<FavoriteRestaurants>) {
        restaurants = pRestaurants
        notifyDataSetChanged()
    }
}