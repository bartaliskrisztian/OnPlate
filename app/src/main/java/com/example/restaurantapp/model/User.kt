package com.example.restaurantapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name= "address") val address: String,
    @ColumnInfo(name = "phone_number") val phone_number: String,
    @ColumnInfo(name= "image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray
)
