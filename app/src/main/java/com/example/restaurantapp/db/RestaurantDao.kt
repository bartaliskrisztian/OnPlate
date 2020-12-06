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

    @Query("SELECT DISTINCT city FROM restaurants WHERE country=:country")
    suspend fun getCitiesFromCountry(country: String): List<String>

    @Query("SELECT DISTINCT city FROM restaurants")
    suspend fun getAllCities(): List<String>

    @Query("SELECT COUNT(*) FROM restaurants")
    fun getRestaurantCount(): Int

    @Query("DELETE FROM restaurants")
    suspend fun deleteRestaurants()

}