package com.example.restaurantapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.R
import com.example.restaurantapp.model.RestaurantImages
import com.example.restaurantapp.viewmodel.RestaurantViewModel
import com.example.restaurantapp.viewmodel.UserViewModel

class RestaurantImageAdapter(private var restaurantImages: List<RestaurantImages>,
                             private val context: Context,
                             private val userViewModel: UserViewModel,
                             private val restaurantViewModel: RestaurantViewModel
): RecyclerView.Adapter<RestaurantImageAdapter.RestaurantImageHolder>() {

    inner class RestaurantImageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantImageHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_image_item, parent, false)
        return RestaurantImageHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantImageHolder, position: Int) {
        val currentItem = restaurantImages[position]
        val image = holder.itemView.findViewById<ImageView>(R.id.itemImage)

        // if there are on images, load the placeholder into the image
        if(currentItem.uid == -1) {
            Glide.with(context).load(R.drawable.restaurant_icon).into(image)
            return
        }

        val imageByteArray = currentItem.image
        Glide.with(context).load(imageByteArray).into(image)

        val userId = userViewModel.currentUser.value?.uid
        val removeImageButton = holder.itemView.findViewById<ImageButton>(R.id.removeImageButton)

        // if the image is not uploaded by the current user, hide the delete button
        if (currentItem.uploaderId == userId) {
            removeImageButton.setOnClickListener {
                restaurantViewModel.removeImageFromRestaurant(currentItem.uid)
                notifyDataSetChanged()
            }
        }
        else {
            removeImageButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = restaurantImages.size

    // on data change we set the new list of restaurants
    fun setData(newRestaurantImages: List<RestaurantImages>) {
        restaurantImages = newRestaurantImages
        notifyDataSetChanged()
    }
}