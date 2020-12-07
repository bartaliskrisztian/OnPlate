package com.example.restaurantapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
class Countries(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "name") val name: String
)
