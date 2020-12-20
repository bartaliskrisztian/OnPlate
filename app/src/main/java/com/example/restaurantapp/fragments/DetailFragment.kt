package com.example.restaurantapp.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.activities.MapsActivity
import com.example.restaurantapp.adapters.RestaurantImageAdapter
import com.example.restaurantapp.databinding.FragmentDetailBinding
import com.example.restaurantapp.model.Restaurant
import com.example.restaurantapp.model.RestaurantImages
import com.example.restaurantapp.viewmodel.RestaurantViewModel
import com.example.restaurantapp.viewmodel.UserViewModel
import java.util.jar.Manifest

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    private val restaurantImageUri: MutableLiveData<Uri> = MutableLiveData()
    private lateinit var restaurantImageByteArray: ByteArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        binding.addImageToRestaurant.setOnClickListener{
            pickImageFromGallery()
        }

        restaurantImageUri.observe(viewLifecycleOwner) {
            val inputStream = activity?.contentResolver?.openInputStream(it)
            restaurantImageByteArray = inputStream!!.readBytes()
            val restaurantId = restaurantViewModel.currentRestaurant.value?.id
            val userId = userViewModel.currentUser.value?.uid
            val newRestaurantImage = RestaurantImages(0, restaurantId!!, userId!!, restaurantImageByteArray)
            restaurantViewModel.addImageToRestaurant(newRestaurantImage)
        }

        recyclerView = binding.restaurantImages
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RestaurantImageAdapter(listOf(), binding.root.context, userViewModel, restaurantViewModel)
        recyclerView.adapter = adapter

        restaurantViewModel.currentRestaurant.observe(viewLifecycleOwner) {
            if(it != null) {
                changeUI(it)
            }
        }

        restaurantViewModel.restaurantImages.observe(viewLifecycleOwner) {

            val restaurantImages = it?.filter { res ->
                res.restaurantId == restaurantViewModel.currentRestaurant.value?.id
            }
            if(restaurantImages!!.isEmpty()) {
                val placeholder = RestaurantImages(-1, -1, -1, byteArrayOf())
                adapter.setData(listOf(placeholder))
            }
            else {
                adapter.setData(restaurantImages)
            }
        }

        binding.mapsButton.setOnClickListener {
            val currentRestaurant = restaurantViewModel.currentRestaurant.value!!
            val intent = Intent(context, MapsActivity::class.java).apply {
                putExtra("lat", currentRestaurant.lat)
                putExtra("lng", currentRestaurant.lng)
                putExtra("restaurantName", currentRestaurant.name)
            }
            startActivity(intent)
        }

        binding.callButton.setOnClickListener {
            val currentRestaurant = restaurantViewModel.currentRestaurant.value!!
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${currentRestaurant.phone}")

            startActivity(intent)
        }

        return binding.root
    }

    private fun changeUI(restaurant: Restaurant) {


        binding.restaurantTitle.text = restaurant.name
        binding.restaurantAddress.text = restaurant.address
        binding.restaurantCountry.text = restaurant.country
        binding.restaurantPostalCode.text = restaurant.postal_code
        binding.restaurantPhone.text = restaurant.phone
        binding.restaurantPrice.text = restaurant.price.toString()
        binding.restaurantReserve.text = restaurant.reserve_url
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            restaurantImageUri.value = data.data!!
        }
    }

}