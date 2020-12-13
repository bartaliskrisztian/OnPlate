package com.example.restaurantapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.db.RestaurantDatabase
import com.example.restaurantapp.model.FavoriteRestaurants
import com.example.restaurantapp.model.Restaurant
import com.example.restaurantapp.repository.FavoritesRepository
import com.example.restaurantapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application): AndroidViewModel(application) {

    private val repository: FavoritesRepository // repo for communicating with the db
    lateinit var favorites: LiveData<List<FavoriteRestaurants>> // list for storing all favorites from the users
    var currentFavorite: MutableLiveData<FavoriteRestaurants> = MutableLiveData()

    init {
        val favoritesDao = RestaurantDatabase.getDatabase(application).favoritesDao()
        repository = FavoritesRepository(favoritesDao)
        loadAllFavorites()
    }

    // loading all favorites from all users
    private fun loadAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            favorites = repository.getAllFavorites()
        }
    }

     fun loadFavoritesFromUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            //favorites.postValue(repository.getFavoritesFromUser(userId))
            //favorites = repository.getFavoritesFromUser(userId)
        }
     }

    // add a restaurant to favorites of user
    fun addFavorite(favoriteRestaurants: FavoriteRestaurants){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavorite(favoriteRestaurants)
        }
    }

    // remove a favorite restaurant from a user
    fun removeFavorite(userId: Int, restaurantId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavorite(userId, restaurantId)
        }
    }

}