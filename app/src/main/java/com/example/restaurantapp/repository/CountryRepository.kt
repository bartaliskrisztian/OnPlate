package com.example.restaurantapp.repository

import com.example.restaurantapp.db.CountryDao
import com.example.restaurantapp.model.Countries

class CountryRepository(private val countryDao: CountryDao) {

    suspend fun addCountry(countries: Countries) = countryDao.addCountry(countries)
    suspend fun addCountries(countries: List<String>) {
        countries.forEach{
            val newCountry = Countries(0, it)
            countryDao.addCountry(newCountry)
        }
    }
    suspend fun getAllCountries(): List<String> = countryDao.getCountries()
    suspend fun getCountryCount(): Int = countryDao.getCountryCount()
}