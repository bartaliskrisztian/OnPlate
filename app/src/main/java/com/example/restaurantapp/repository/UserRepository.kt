package com.example.restaurantapp.repository

import androidx.lifecycle.LiveData
import com.example.restaurantapp.db.UserDao
import com.example.restaurantapp.model.User

class UserRepository(private val userDao: UserDao) {
    val users = userDao.getUsers()

    suspend fun addUser(user: User) = userDao.addUser(user)
    suspend fun deleteUserByID(uid: Int) = userDao.deleteUserByID(uid)
    fun getUserByUsernameOrEmail(username: String, email: String): User = userDao.getUserByUsernameOrEmail(username, email)
    fun getUserByUsernameAndPassword(username: String, password: String): User = userDao.getUserByUsernameAndPassword(username, password)
}