package com.example.restaurantapp.repository

import androidx.lifecycle.LiveData
import com.example.restaurantapp.db.ImagesDao
import com.example.restaurantapp.model.RestaurantImages

class ImageRepository(private val imagesDao: ImagesDao) {

    val images = imagesDao.getAllImages()

    suspend fun addImage(restaurantImages: RestaurantImages) = imagesDao.addImage(restaurantImages)
    suspend fun deleteImage(imageId: Int) = imagesDao.deleteImage(imageId)
}