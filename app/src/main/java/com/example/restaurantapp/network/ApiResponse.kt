package com.example.restaurantapp.network

import androidx.lifecycle.LiveData
import com.example.restaurantapp.model.Restaurant
import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("total_entries") val total_entries: Int,
    @SerializedName("per_page") val per_page: Int,
    @SerializedName("current_page") val current_page: Int,
    @SerializedName("restaurants") val restaurants: LiveData<List<Restaurant>>
)
