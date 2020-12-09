package com.example.restaurantapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.db.RestaurantDatabase
import com.example.restaurantapp.model.FavoriteRestaurants
import com.example.restaurantapp.model.Restaurant
import com.example.restaurantapp.repository.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application): AndroidViewModel(application) {

    private val repository: FavoritesRepository
    val favorites: MutableLiveData<List<Restaurant>> = MutableLiveData()

    init {
        val favoritesDao = RestaurantDatabase.getDatabase(application).favoritesDao()
        repository = FavoritesRepository(favoritesDao)
    }

     fun loadFavorites(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            favorites.postValue(repository.getFavoritesFromUser(userId))
        }
    }

    fun addFavorite(favoriteRestaurants: FavoriteRestaurants){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavorite(favoriteRestaurants)
        }
    }
}