package com.example.restaurantapp.viewmodel

import android.app.Application
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
    private val repository: UserRepository
    var usedUser =  MutableLiveData<User>()
    var currentUser =  MutableLiveData<User>()

    init {
        val userDao = RestaurantDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    // getting the user by username or email address
    fun loadCurrentUser(username: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser.postValue(repository.getUserByUsernameOrEmail(username, email))
        }
    }

    // loading all registered users
    fun loadUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            users = repository.users
        }
    }

    // getting all users with specific username or email (should be 1 or 0)
    // needed for checking if the given username or email is used by another user
    fun getUsersForRegistration(username: String, email: String) {
        val result = MutableLiveData<User>()
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserByUsernameOrEmail(username, email)
            result.postValue(user)
        }
        usedUser = result
    }

    // getting user by username/email and password, needed for log the user in
    fun getUsersForLogin(username: String, password: String) {
        val result = MutableLiveData<User>()
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserByUsernameAndPassword(username, password)
            result.postValue(user)
        }
        usedUser = result
    }

    // adding one user to db
    fun addUser(user: User) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.addUser(user)
        }
    }
}