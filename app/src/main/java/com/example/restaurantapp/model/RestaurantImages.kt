package com.example.restaurantapp.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_images")
data class RestaurantImages (
        @PrimaryKey(autoGenerate = true) var uid: Int,
        @ColumnInfo(name = "restaurantId") val restaurantId: Int,
        //@ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray
        @ColumnInfo(name = "image") val imageUri: Uri
)
