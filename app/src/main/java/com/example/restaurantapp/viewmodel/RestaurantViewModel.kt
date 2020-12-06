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
    var restaurantsFromCountry: MutableLiveData<List<Restaurant>> = MutableLiveData()
    var filteredRestaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()

    // lists for spinner items
    var countries: MutableLiveData<List<String>> = MutableLiveData()
    var currentCities: MutableLiveData<List<String>> = MutableLiveData()

    // filters
    var currentCountry: MutableLiveData<String> = MutableLiveData()
    var currentCity: MutableLiveData<String> = MutableLiveData()
    var currentPrice: MutableLiveData<Int> = MutableLiveData()
    var showFavorites: MutableLiveData<Boolean> = MutableLiveData()


    val currentLoadingState: MutableLiveData<String> = MutableLiveData() // for splash feedback
    var restaurantCount: MutableLiveData<Int> = MutableLiveData() // number of restaurants in db

    var dataLoadedFromApi: MutableLiveData<Boolean> = MutableLiveData() // true if restaurants are downloaded from BE
    var restaurantsLoaded: MutableLiveData<Boolean> = MutableLiveData() // true if 'restaurants' attribute is initialized from db

    private val repository: RestaurantRepository

    init {
        val restaurantDao = RestaurantDatabase.getDatabase(application).restaurantDao()
        repository = RestaurantRepository(restaurantDao)
    }

    fun applyFilters() {
        val result = MutableLiveData<List<Restaurant>>()

        // filter by city
        result.value = restaurantsFromCountry.value?.filter {
            it.city == currentCity.value
        }

        // filter by price level
        /*
        result.value = result.value?.filter {
            it.price == currentPrice.value
        }
         */
        filteredRestaurants.value = result.value
    }

     fun loadCurrentCities() {
         viewModelScope.launch(Dispatchers.IO) {
             if (currentCountry.value != null) {
                 currentCities.postValue(repository.getCitiesFromCountry(currentCountry.value!!))
             }
         }
     }

     fun loadRestaurantsFromCountry() {
         restaurantsFromCountry.value = restaurants.value?.filter {
             it.country == currentCountry.value
         }
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

     // downloads all restaurants from BE
     fun loadRestaurantsFromApi() {
         val result = MutableLiveData<Boolean>()

         viewModelScope.launch(Dispatchers.IO) {
             countries.value!!.forEach { country ->
                 currentLoadingState.postValue(country)
                 // set the query parameters of the url
                 val params: MutableMap<String, String> = mutableMapOf()
                 params["country"] = country
                 params["per_page"] = "100"
                 var ok = true
                 var page = 1
                 while (ok && country != "US") {
                     val _page = page
                     params["page"] = page.toString()
                     BEApi.retrofitService.getRestaurants(params).enqueue(
                             object : Callback<ApiRestaurantResponse> {
                                 override fun onResponse(call: Call<ApiRestaurantResponse>, response: Response<ApiRestaurantResponse>) {
                                     val res: ApiRestaurantResponse? = response.body()
                                     if (res != null) {
                                         if (res.restaurants.isEmpty()) {
                                             ok = false
                                         } else {
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
                     // wait for answer
                     while (page != _page + 1) {}
                 }
             }
             result.postValue(true)
         }
         dataLoadedFromApi = result
    }

    fun loadRestaurantsFromDatabase() {

        viewModelScope.launch(Dispatchers.IO) {
            restaurants.postValue(repository.getRestaurants())
            restaurantsLoaded.postValue(true)
        }
    }

    // adds multiple restaurants to the db
    fun addMultipleRestaurants(restaurants: List<Restaurant>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMultipleRestaurants(restaurants)
        }
    }

    // gets the number of restaurants stored in db
    fun getRestaurantCount() {
        val result = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO)
        {
            result.postValue(repository.getRestaurantCount())
        }
        restaurantCount = result
    }

}