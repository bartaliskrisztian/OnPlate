package com.example.restaurantapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.db.RestaurantDatabase
import com.example.restaurantapp.model.Restaurant
import com.example.restaurantapp.network.ApiCountryResponse
import com.example.restaurantapp.network.ApiRestaurantResponse
import com.example.restaurantapp.network.BEApi
import com.example.restaurantapp.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantViewModel(application: Application): AndroidViewModel(application) {
    var restaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()
    var countries: MutableLiveData<List<String>> = MutableLiveData()
    var currentCountry: MutableLiveData<String> = MutableLiveData()
    var dataIsLoaded: MutableLiveData<Boolean> = MutableLiveData()
    val currentLoadingState: MutableLiveData<String> = MutableLiveData()
    var restaurantCount: MutableLiveData<Int> = MutableLiveData()

    private val repository: RestaurantRepository

    init {
        val restaurantDao = RestaurantDatabase.getDatabase(application).restaurantDao()
        repository = RestaurantRepository(restaurantDao)
    }

     fun loadCountries() {
        viewModelScope.launch {
            BEApi.retrofitService.getCountries().enqueue(
                    object: Callback<ApiCountryResponse> {
                        override fun onResponse(call: Call<ApiCountryResponse>, response: Response<ApiCountryResponse>) {
                            val res: ApiCountryResponse? = response.body()
                            if(res != null) {
                                countries.value = res.countries
                            }
                        }

                        override fun onFailure(call: Call<ApiCountryResponse>, t: Throwable) {
                            //Toast.makeText(getApplication(), "Failure" + t.message, Toast.LENGTH_SHORT).show()
                            Log.d("aaaaa", "Failure" + t.message)
                        }
                    })
        }
    }

    fun loadRestaurants() {
        val resultLoaded = MutableLiveData<Boolean>()
        val restaurantsLoaded = MutableLiveData<List<Restaurant>>()

        viewModelScope.launch(Dispatchers.IO) {
            val count = repository.getRestaurantCount()
            Thread.sleep(2000)
            if(count == 0) {
                loadRestaurantsFromApi()
            }

            //Log.d("aaaaa", "${repository.getRestaurants()}")
            restaurantsLoaded.postValue(repository.getRestaurants().value)
            resultLoaded.postValue(true)
        }
        Thread.sleep(5000)
        restaurants.value = restaurantsLoaded.value
        dataIsLoaded = resultLoaded
    }

    private fun loadRestaurantsFromApi() {
        if(countries.value != null) {
            countries.value!!.forEach { country ->
                currentLoadingState.postValue(country)
                val params: MutableMap<String, String> = mutableMapOf()
                params["country"] = country
                params["per_page"] = "100"
                var ok = true
                var page = 1
                while (ok && country != "US") {
                    val _page = page
                    params["page"] = page.toString()
                    BEApi.retrofitService.getRestaurants(params).enqueue(
                            object: Callback<ApiRestaurantResponse> {
                                override fun onResponse(call: Call<ApiRestaurantResponse>, response: Response<ApiRestaurantResponse>) {
                                    val res: ApiRestaurantResponse? = response.body()
                                    if(res != null) {
                                        if(res.restaurants.isEmpty()) {
                                            ok = false
                                        }
                                        else {
                                            addMultipleRestaurants(res.restaurants)
                                        }
                                        /*
                                        Log.d("aaaaa", "${res.restaurants}")
                                        Log.d("aaaaa", "${res.restaurants.size}")
                                        Log.d("aaaaa", "${response}")
                                        Log.d("aaaaa", "----------------------")
                                         */
                                        ++page
                                    }
                                }

                                override fun onFailure(call: Call<ApiRestaurantResponse>, t: Throwable) {
                                    Log.d("aaaaa", "Failure" + t.message)
                                }
                            })
                    while (page != _page+1){}
                }
            }
        }
    }

    fun addMultipleRestaurants(restaurants: List<Restaurant>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMultipleRestaurants(restaurants)
        }
    }

    fun getRestaurantCount() {
        val result = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO)
        {
            result.postValue(repository.getRestaurantCount())
        }
        restaurantCount = result
    }

}