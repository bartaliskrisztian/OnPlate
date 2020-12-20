package com.example.restaurantapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantapp.model.RestaurantImages

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addImage(restaurantImages: RestaurantImages)

    @Query("SELECT * FROM restaurant_images")
    fun getAllImages(): LiveData<List<RestaurantImages>>

    @Query("DELETE FROM restaurant_images WHERE uid=:imageId")
    suspend fun deleteImage(imageId: Int)
}