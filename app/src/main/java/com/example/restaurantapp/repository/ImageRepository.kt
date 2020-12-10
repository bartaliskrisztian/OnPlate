package com.example.restaurantapp.repository

import androidx.lifecycle.LiveData
import com.example.restaurantapp.db.ImagesDao
import com.example.restaurantapp.model.RestaurantImages

class ImageRepository(private val imagesDao: ImagesDao) {

    suspend fun addImage(restaurantImages: RestaurantImages) = imagesDao.addImage(restaurantImages)
    fun getAllImages(): LiveData<List<RestaurantImages>> = imagesDao.getAllImages()
}