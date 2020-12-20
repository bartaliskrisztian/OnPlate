package com.example.restaurantapp.repository

import com.example.restaurantapp.db.RestaurantDao
import com.example.restaurantapp.model.Restaurant

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    suspend fun addMultipleRestaurants(restaurants: List<Restaurant>) {
        restaurants.forEach{
            restaurantDao.addRestaurant(it)
        }
    }

    fun getRestaurantCount(): Int = restaurantDao.getRestaurantCount()
    suspend fun getRestaurants(): List<Restaurant> = restaurantDao.getRestaurants()
    suspend fun getCitiesFromCountry(country: String) = restaurantDao.getCitiesFromCountry(country)
}
