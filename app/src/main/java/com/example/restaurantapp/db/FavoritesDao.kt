package com.example.restaurantapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantapp.model.FavoriteRestaurants
import com.example.restaurantapp.model.Restaurant

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favoriteRestaurants: FavoriteRestaurants)

    @Query("SELECT restaurant FROM favorites WHERE userId=:userId")
    suspend fun getFavoritesFromUser(userId: Int): List<Restaurant>

    @Query("DELETE FROM favorites WHERE userId=:userId AND restaurantId=:restaurantId")
    suspend fun removeFavorite(userId: Int, restaurantId: Int)
}