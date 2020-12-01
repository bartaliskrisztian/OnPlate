package com.example.restaurantapp.network

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://opentable.herokuapp.com/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BEApiService {
    @GET("/api/restaurants?country=AW")
    fun getRestaurants(): Call<ApiResponse>
}

object BEApi {
    val retrofitService: BEApiService by lazy {
        retrofit.create(BEApiService::class.java)
    }
}