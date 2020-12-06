package com.example.restaurantapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteRestaurants (
        @PrimaryKey(autoGenerate = true) var uid: Int,
        @ColumnInfo(name = "userId") val userId: Int,
        @ColumnInfo(name = "restaurantId") val restaurantId: Int
)
