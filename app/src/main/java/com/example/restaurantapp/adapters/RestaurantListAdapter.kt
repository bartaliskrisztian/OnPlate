package com.example.restaurantapp.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
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
import com.example.restaurantapp.viewmodel.RestaurantViewModel
import com.example.restaurantapp.viewmodel.UserViewModel

class RestaurantListAdapter(
        private var restaurants: List<Restaurant>,
        private val listener: OnItemClickListener,
        private val context: Context,
        private val favoritesViewModel: FavoritesViewModel,
        private val userViewModel: UserViewModel,
        private val restaurantViewModel: RestaurantViewModel):
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

        val tempByteArray = restaurantImage(currentItem.id)
        val image = if(tempByteArray.isEmpty()) {
            currentItem.image_url
        }
        else {
            tempByteArray
        }
        Glide.with(context).load(image).into(restaurantImage)

        val favoriteButton = holder.itemView.findViewById<ImageButton>(R.id.favoriteButton)
        val userId = userViewModel.currentUser.value?.uid
        val isFavorite = restaurantIsFavorite(currentItem.id, userId!!)

        // if the restaurant is the user's favorite, we change the button's color
        if(isFavorite) {
            favoriteButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.appBackgroundColor))
        }

        favoriteButton.setOnClickListener{
            val userId = userViewModel.currentUser.value!!.uid
            if(isFavorite) { // if the restaurant is the user's favorite, then we remove it from the favorites
                favoriteButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
                favoritesViewModel.removeFavorite(userId, currentItem.id)
                notifyDataSetChanged()
            }
            else { // if the restaurant is not the user's favorite yet, then we add it to the favorites
                favoriteButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.appBackgroundColor))
                val newFavorite = FavoriteRestaurants(0, userId, currentItem.id, currentItem)
                favoritesViewModel.addFavorite(newFavorite)
                notifyDataSetChanged()
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

    private fun restaurantIsFavorite(restaurantId: Int, userId: Int): Boolean {
        favoritesViewModel.favorites.value?.forEach {
            if(it.restaurantId == restaurantId && it.userId == userId) {
                return true
            }
        }
        return false
    }

    private fun restaurantImage(restaurantId: Int): ByteArray {
        restaurantViewModel.restaurantImages.value?.forEach {
            if(it.restaurantId == restaurantId) {
                return it.image
            }
        }
        return byteArrayOf()
    }
}