package com.example.restaurantapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.db.RestaurantDatabase
import com.example.restaurantapp.model.Restaurant
import com.example.restaurantapp.network.ApiResponse
import com.example.restaurantapp.network.BEApi
import com.example.restaurantapp.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantViewModel(application: Application): AndroidViewModel(application) {
     lateinit var restaurants: LiveData<List<Restaurant>>
     private val repository: RestaurantRepository

    init {
        val restaurantDao = RestaurantDatabase.getDatabase(application).restaurantDao()
        repository = RestaurantRepository(restaurantDao)

        viewModelScope.launch {
            BEApi.retrofitService.getRestaurants().enqueue(
                object: Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        val res: ApiResponse? = response.body()
                        if(res != null) {
                            addMultipleRestaurants(res.restaurants)
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        //_response.value = "Failure" + t.message
                    }

                })
        }
    }


    fun addMultipleRestaurants(restaurants: LiveData<List<Restaurant>>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMultipleRestaurants(restaurants)
        }
    }

    fun setRestaurantList(newRestaurants: LiveData<List<Restaurant>>) {
        restaurants = newRestaurants
    }
}