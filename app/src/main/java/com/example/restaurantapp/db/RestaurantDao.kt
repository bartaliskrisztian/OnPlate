package com.example.restaurantapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantapp.model.Restaurant

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRestaurant(restaurant: Restaurant)

    @Query("SELECT * FROM restaurants")
    fun getRestaurants(): LiveData<List<Restaurant>>

}