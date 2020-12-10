package com.example.restaurantapp.repository

import com.example.restaurantapp.db.FavoritesDao
import com.example.restaurantapp.model.FavoriteRestaurants
import com.example.restaurantapp.model.Restaurant

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    suspend fun addFavorite(favoriteRestaurants: FavoriteRestaurants) = favoritesDao.addFavorite(favoriteRestaurants)
    suspend fun getFavoritesFromUser(userId: Int): List<Restaurant> = favoritesDao.getFavoritesFromUser(userId)
    suspend fun removeFavorite(userId: Int, restaurantId: Int) = favoritesDao.removeFavorite(userId, restaurantId)
}