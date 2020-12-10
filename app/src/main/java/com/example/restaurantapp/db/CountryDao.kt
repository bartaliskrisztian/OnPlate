package com.example.restaurantapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantapp.model.Countries
import com.example.restaurantapp.model.Restaurant

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCountry(countries: Countries)

    @Query("SELECT name FROM countries")
    suspend fun getCountries(): List<String>

    @Query("SELECT COUNT(*) FROM countries")
    suspend fun getCountryCount(): Int
}