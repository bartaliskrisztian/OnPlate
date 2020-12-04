package com.example.restaurantapp.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    suspend fun getRestaurants(): List<Restaurant>

    @Query("SELECT * FROM restaurants WHERE country=:country")
    suspend fun getRestaurantsByCountry(country: String): List<Restaurant>

    @Query("SELECT COUNT(*) FROM restaurants")
    fun getRestaurantCount(): Int

    @Query("DELETE FROM restaurants")
    suspend fun deleteRestaurants()

}