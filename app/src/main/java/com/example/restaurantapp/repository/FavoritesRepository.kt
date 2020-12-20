package com.example.restaurantapp.repository

import androidx.lifecycle.LiveData
import com.example.restaurantapp.db.FavoritesDao
import com.example.restaurantapp.model.FavoriteRestaurants

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    val favorites = favoritesDao.getAllFavorites()

    suspend fun addFavorite(favoriteRestaurants: FavoriteRestaurants) = favoritesDao.addFavorite(favoriteRestaurants)
    fun getAllFavorites(): LiveData<List<FavoriteRestaurants>> = favoritesDao.getAllFavorites()
    suspend fun removeFavorite(userId: Int, restaurantId: Int) = favoritesDao.removeFavorite(userId, restaurantId)
}