package com.example.restaurantapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.db.RestaurantDatabase
import com.example.restaurantapp.model.User
import com.example.restaurantapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    lateinit var users: LiveData<List<User>>
    val repository: UserRepository
    var usedUser =  MutableLiveData<User>()
    var currentUser =  MutableLiveData<User>()

    init {
        val userDao = RestaurantDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun loadCurrentUser(username: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser.postValue(repository.getUserByUsernameOrEmail(username, email))
        }
    }

    fun loadUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            users = repository.users
        }
    }

    fun getUsersForRegistration(username: String, email: String) {
        val result = MutableLiveData<User>()
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserByUsernameOrEmail(username, email)
            result.postValue(user)
        }
        usedUser = result
    }

    fun getUsersForLogin(username: String, password: String) {
        val result = MutableLiveData<User>()
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserByUsernameAndPassword(username, password)
            result.postValue(user)
        }
        usedUser = result
    }

    fun addUser(user: User) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUserByID(user.uid)
        }
    }

}