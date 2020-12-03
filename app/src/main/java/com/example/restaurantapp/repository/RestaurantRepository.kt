package com.example.restaurantapp.repository

import androidx.lifecycle.LiveData
import com.example.restaurantapp.db.RestaurantDao
import com.example.restaurantapp.model.Restaurant

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    suspend fun addMultipleRestaurants(restaurants: List<Restaurant>) {
        restaurants.forEach{
            restaurantDao.addRestaurant(it)
        }
    }

    fun getRestaurantCount(): Int = restaurantDao.getRestaurantCount()

    fun getRestaurants(): LiveData<List<Restaurant>> = restaurantDao.getRestaurants()
}
