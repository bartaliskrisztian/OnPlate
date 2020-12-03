package com.example.restaurantapp.network

import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName

data class ApiCountryResponse(
        @SerializedName("count") val count: Int,
        @SerializedName("countries") val countries: List<String>
)