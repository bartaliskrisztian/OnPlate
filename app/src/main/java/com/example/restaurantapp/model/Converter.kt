package com.example.restaurantapp.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun fromRestaurant(value: Restaurant?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toRestaurant(value: String?): Restaurant? {
        val restaurantType: Type = object : TypeToken<Restaurant?>() {}.type
        return Gson().fromJson(value, restaurantType)
    }
}
