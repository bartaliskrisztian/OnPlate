package com.example.restaurantapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey
    @SerializedName("id")
    var id: Int,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "address")
    @SerializedName("address")
    val address: String,

    @ColumnInfo(name = "city")
    @SerializedName("city")
    val city: String,

    @ColumnInfo(name = "state")
    @SerializedName("state")
    val state: String,

    @ColumnInfo(name = "area")
    @SerializedName("area")
    val area: String,

    @ColumnInfo(name = "postal_code")
    @SerializedName("postal_code")
    val postal_code: String,

    @ColumnInfo(name = "country")
    @SerializedName("country")
    val country: String,

    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    val phone: String,

    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    val lat: Double,

    @ColumnInfo(name = "lng")
    @SerializedName("lng")
    val lng: Double,

    @ColumnInfo(name = "price")
    @SerializedName("price")
    val price: Int,

    @ColumnInfo(name = "reserve_url")
    @SerializedName("reserve_url")
    val reserve_url: String,

    @ColumnInfo(name = "mobile_reserve_url")
    @SerializedName("mobile_reserve_url")
    val mobile_reserve_url: String,

    @ColumnInfo(name = "image_url")
    @SerializedName("image_url")
    val image_url: String
)