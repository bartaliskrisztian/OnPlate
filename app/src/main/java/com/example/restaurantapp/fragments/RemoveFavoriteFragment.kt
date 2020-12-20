package com.example.restaurantapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentRemoveFavoriteBinding
import com.example.restaurantapp.viewmodel.FavoritesViewModel
import com.example.restaurantapp.viewmodel.UserViewModel


class RemoveFavoriteFragment : DialogFragment() {

    private lateinit var binding: FragmentRemoveFavoriteBinding

    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_remove_favorite, container, false)

        // if the user presses the remove button, we remove the favorite from the db
        binding.removeFavoriteButton.setOnClickListener {
            val favoriteId = favoritesViewModel.currentFavorite.value?.restaurantId
            val userId = userViewModel.currentUser.value?.uid
            favoritesViewModel.removeFavorite(userId!!, favoriteId!!)
            dismiss()
        }

        binding.dismissButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}