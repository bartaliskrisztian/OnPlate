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
        private val userViewModel: UserViewModel,
        private val favoritesViewModel: FavoritesViewModel):
RecyclerView.Adapter<FavoritesListAdapter.RestaurantListHolder>() {

    inner class RestaurantListHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                restaurantViewModel.currentRestaurant.value = restaurants[position].restaurant
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                favoritesViewModel.currentFavorite.value = restaurants[position]
                listener.onItemLongClick(position)
                notifyDataSetChanged()
            }
            return true
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

        val tempByteArray = restaurantImage(currentItem.id)
        val image = if(tempByteArray.isEmpty()) {
            currentItem.image_url
        }
        else {
            tempByteArray
        }
        Glide.with(context).load(image).into(restaurantImage)



    }

    override fun getItemCount(): Int = restaurants.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

    fun setData(pRestaurants: List<FavoriteRestaurants>) {
        restaurants = pRestaurants
        notifyDataSetChanged()
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