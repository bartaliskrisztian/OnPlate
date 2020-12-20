package com.example.restaurantapp.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

//private const val BASE_URL = "https://opentable.herokuapp.com/api/"
private const val BASE_URL = "https://ratpark-api.imok.space/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BEApiService {
    @GET("restaurants")
    fun getRestaurants(@QueryMap params: Map<String, String>): Call<ApiRestaurantResponse>

    @GET("countries")
    fun getCountries(): Call<ApiCountryResponse>
}

object BEApi {
    val retrofitService: BEApiService by lazy {
        retrofit.create(BEApiService::class.java)
    }
}