package com.example.restaurantapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.restaurantapp.db.RestaurantDatabase
import com.example.restaurantapp.repository.FavoritesRepository

class FavoritesViewModel(application: Application): AndroidViewModel(application) {

    private val favoritesRepository: FavoritesRepository

    init {
        val favoritesDao = RestaurantDatabase.getDatabase(application).favoritesDao()
        favoritesRepository = FavoritesRepository(favoritesDao)
    }
}