package com.example.restaurantapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.db.RestaurantDatabase
import com.example.restaurantapp.model.Countries
import com.example.restaurantapp.model.Restaurant
import com.example.restaurantapp.model.RestaurantImages
import com.example.restaurantapp.network.ApiCountryResponse
import com.example.restaurantapp.network.ApiRestaurantResponse
import com.example.restaurantapp.network.BEApi
import com.example.restaurantapp.repository.CountryRepository
import com.example.restaurantapp.repository.ImageRepository
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
    var currentRestaurant: MutableLiveData<Restaurant> = MutableLiveData()

    lateinit var restaurantImages: LiveData<List<RestaurantImages>>

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
    private val countryRepository: CountryRepository
    private val imageRepository: ImageRepository

    init {
        val db = RestaurantDatabase.getDatabase(application)

        val restaurantDao = db.restaurantDao()
        val countryDao = db.countryDao()
        val imagesDao = db.imageDao()

        repository = RestaurantRepository(restaurantDao)
        countryRepository = CountryRepository(countryDao)
        imageRepository = ImageRepository(imagesDao)

        loadRestaurantImages()
    }

    private fun loadRestaurantImages() {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantImages = imageRepository.getAllImages()
        }
    }

    fun applyFilters() {
        val result = MutableLiveData<List<Restaurant>>()

        // filter by city
        result.value = restaurantsFromCountry.value?.filter {
            it.city == currentCity.value
        }

        // filter by price level

        result.value = result.value?.filter {
            it.price == currentPrice.value
        }

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

        viewModelScope.launch(Dispatchers.IO) {
            if(countryRepository.getCountryCount() == 0) {
                loadCountriesFromApi()
            }
            else {
                countries.postValue(countryRepository.getAllCountries())
            }

        }
    }


    fun loadCountriesFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            BEApi.retrofitService.getCountries().enqueue(
                    object: Callback<ApiCountryResponse> {
                        override fun onResponse(call: Call<ApiCountryResponse>, response: Response<ApiCountryResponse>) {
                            Log.d("aaaaa", "${response.isSuccessful}")
                            if(response.isSuccessful) {
                                val res: ApiCountryResponse? = response.body()
                                if(res != null) {
                                    countries.value = res.countries
                                    addMultipleCountries(res.countries)
                                }
                            }
                            else {
                                countries.value = listOf("AW")
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

         viewModelScope.launch(Dispatchers.IO) {
             countries.value!!.forEach { country ->
                 currentLoadingState.postValue(country)
                 // set the query parameters of the url
                 val params: MutableMap<String, String> = mutableMapOf()
                 params["country"] = country
                 params["per_page"] = "100"
                 var ok = true
                 var page = 1
                 while (ok ) { //  && country != "US"
                     val _page = page
                     params["page"] = page.toString()
                     BEApi.retrofitService.getRestaurants(params).enqueue(
                             object : Callback<ApiRestaurantResponse> {
                                 override fun onResponse(call: Call<ApiRestaurantResponse>, response: Response<ApiRestaurantResponse>) {
                                     if(response.isSuccessful) {
                                         val res: ApiRestaurantResponse? = response.body()
                                         if (res != null) {
                                             if (res.restaurants.isEmpty()) {
                                                 ok = false
                                             } else {
                                                 addMultipleRestaurants(res.restaurants)
                                             }
                                         }
                                     }
                                     else {
                                         ok = false
                                     }
                                     ++page
                                 }

                                 override fun onFailure(call: Call<ApiRestaurantResponse>, t: Throwable) {
                                     Log.d("aaaaa", "Failure" + t.message)
                                 }
                             })
                     // wait for answer
                     while (page != _page + 1) {}
                 }
             }
             /*
             val newRestaurant1 = Restaurant(0,
                     "Pizza 21",
                     "Street 23.",
                     "New York",
                     "AW",
                     "New York",
                     "535600",
                     "AW",
                     "0695994",
                     41.935137,
                     -87.662815,
                     2,
                     "http://www.opentable.com/single.aspx?rid=107257",
                     "http://mobile.opentable.com/opentable/?restId=107257",
                     "https://www.opentable.com/img/restimages/107257.jpg"
             )
             val newRestaurant2 = Restaurant(1,
                     "Factory",
                     "Street 98.",
                     "Las Vegas",
                     "AW",
                     "Las Vegas",
                     "535600",
                     "AW",
                     "0695994",
                     41.933737,
                     -77.662815,
                     4,
                     "http://www.opentable.com/single.aspx?rid=107257",
                     "http://mobile.opentable.com/opentable/?restId=107257",
                     "https://www.opentable.com/img/restimages/107257.jpg"
             )
             repository.addMultipleRestaurants(listOf(newRestaurant1, newRestaurant2))

              */
             dataLoadedFromApi.postValue(true)
         }
    }

    fun loadRestaurantsFromDatabase() {

        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.getRestaurants()
            restaurants.postValue(res)
            restaurantsLoaded.postValue(true)
        }
    }

    // adds multiple restaurants to the db
    fun addMultipleRestaurants(restaurants: List<Restaurant>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMultipleRestaurants(restaurants)
        }
    }

    fun addMultipleCountries(countries: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            countryRepository.addCountries(countries)
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