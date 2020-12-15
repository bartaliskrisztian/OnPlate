package com.example.restaurantapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restaurantapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val lat = intent.getDoubleExtra("lat", 46.5446244)
        val lng = intent.getDoubleExtra("lng", 24.5611947)
        val restaurantName = intent.getStringExtra("restaurantName")
        val restaurantPlace = LatLng(lat, lng)

        mMap.addMarker(MarkerOptions().position(restaurantPlace).title(restaurantName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantPlace, 15F))
    }
}