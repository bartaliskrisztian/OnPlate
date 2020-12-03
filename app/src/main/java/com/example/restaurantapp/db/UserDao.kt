package com.example.restaurantapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantapp.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM users")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE username=:username OR email=:email")
    fun getUserByUsernameOrEmail(username: String, email: String): User

    @Query("SELECT * FROM users WHERE username=:username AND password=:password")
    fun getUserByUsernameAndPassword(username: String, password: String): User

    @Query("DELETE FROM users WHERE uid=:uid")
    suspend fun deleteUserByID(uid: Int)

    @Query("DELETE FROM users")
    suspend fun deleteUsers()
}