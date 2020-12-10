package com.example.restaurantapp.repository

import androidx.lifecycle.LiveData
import com.example.restaurantapp.db.RestaurantDao
import com.example.restaurantapp.model.Restaurant

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    suspend fun addRestaurant(restaurant: Restaurant) = restaurantDao.addRestaurant(restaurant)
    suspend fun addMultipleRestaurants(restaurants: List<Restaurant>) {
        restaurants.forEach{
            restaurantDao.addRestaurant(it)
        }
    }

    fun getRestaurantCount(): Int = restaurantDao.getRestaurantCount()
    suspend fun getRestaurants(): List<Restaurant> = restaurantDao.getRestaurants()
    suspend fun getCitiesFromCountry(country: String) = restaurantDao.getCitiesFromCountry(country)
    suspend fun getAllCities() = restaurantDao.getAllCities()
}
