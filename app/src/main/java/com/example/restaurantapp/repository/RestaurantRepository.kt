package com.example.restaurantapp.repository

import androidx.lifecycle.LiveData
import com.example.restaurantapp.db.RestaurantDao
import com.example.restaurantapp.model.Restaurant

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    val restaurants = restaurantDao.getRestaurants()

    suspend fun addMultipleRestaurants(restaurants: LiveData<List<Restaurant>>) {
        restaurants.value?.forEach{
            restaurantDao.addRestaurant(it)
        }
    }
}
