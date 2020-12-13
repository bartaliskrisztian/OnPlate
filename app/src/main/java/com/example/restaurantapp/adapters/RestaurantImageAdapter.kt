package com.example.restaurantapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.R
import com.example.restaurantapp.model.RestaurantImages

class RestaurantImageAdapter(private var restaurantImages: List<RestaurantImages>, private val context: Context): RecyclerView.Adapter<RestaurantImageAdapter.RestaurantImageHolder>() {

    inner class RestaurantImageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantImageHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_image_item, parent, false)
        return RestaurantImageHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantImageHolder, position: Int) {
        val imageByteArray = restaurantImages[position].image
        val image = holder.itemView.findViewById<ImageView>(R.id.itemImage)
        Glide.with(context).load(imageByteArray).into(image)
    }

    override fun getItemCount(): Int = restaurantImages.size

    fun setData(newRestaurantImages: List<RestaurantImages>) {
        restaurantImages = newRestaurantImages
        notifyDataSetChanged()
    }
}