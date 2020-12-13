package com.example.restaurantapp.model

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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

    @TypeConverter
    fun fromUri(value: Uri?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toUri(value: String?): Uri? {
        val uriType: Type = object : TypeToken<Uri?>() {}.type
        return Gson().fromJson(value, uriType)
    }
}
