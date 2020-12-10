package com.example.restaurantapp.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.R
import com.example.restaurantapp.model.FavoriteRestaurants
import com.example.restaurantapp.model.Restaurant
import com.example.restaurantapp.viewmodel.FavoritesViewModel
import com.example.restaurantapp.viewmodel.UserViewModel

class RestaurantListAdapter(
        private var restaurants: List<Restaurant>,
        private val listener: OnItemClickListener,
        private val context: Context,
        private val favoritesViewModel: FavoritesViewModel,
        private val userViewModel: UserViewModel):
    RecyclerView.Adapter<RestaurantListAdapter.RestaurantListHolder>() {

    inner class RestaurantListHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item_layout, parent, false)
        return RestaurantListHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantListHolder, position: Int) {
        val currentItem = restaurants[position]
        val restaurantImage = holder.itemView.findViewById<ImageView>(R.id.restaurantImage)
        holder.itemView.findViewById<TextView>(R.id.restaurantTitle).text = currentItem.name
        holder.itemView.findViewById<TextView>(R.id.restaurantAddress).text = currentItem.address
        holder.itemView.findViewById<TextView>(R.id.restaurantPrice).text = currentItem.price.toString()
        Glide.with(context).load(currentItem.image_url).into(restaurantImage)
        val favoriteButton = holder.itemView.findViewById<ImageButton>(R.id.favoriteButton)
        val isFavorite = restaurantIsFavorite(currentItem.id)

        if(isFavorite) {
            favoriteButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.appBackgroundColor))
        }

        favoriteButton.setOnClickListener{
            val userId = userViewModel.currentUser.value!!.uid
            if(isFavorite) {
                favoriteButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
                favoritesViewModel.removeFavorite(userId, currentItem.id)
            }
            else {
                favoriteButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.appBackgroundColor))
                val newFavorite = FavoriteRestaurants(0, userId, currentItem.id, currentItem)
                favoritesViewModel.addFavorite(newFavorite)
            }
        }
    }

    override fun getItemCount(): Int = restaurants.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setData(pRestaurants: List<Restaurant>) {
        restaurants = pRestaurants
        notifyDataSetChanged()
    }

    private fun restaurantIsFavorite(restaurantId: Int): Boolean {
        favoritesViewModel.favorites.value?.forEach {
            if(it.restaurantId == restaurantId) {
                return true
            }
        }
        return false
    }
}