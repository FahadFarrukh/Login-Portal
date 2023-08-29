package com.example.portal

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): User?

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    @Query("UPDATE User SET login = :loginValue WHERE email = :email")
    fun updateLoginByEmail(email: String, loginValue: Boolean)

    @Query("UPDATE User SET login = 0 WHERE login = 1")
    fun logout()

    @Query("SELECT firstname FROM user where login = 1")
    fun firstname(): String

    @Query("SELECT lastname FROM user where login = 1")
    fun lastname(): String

    @Query("SELECT email FROM user where login = 1")
    fun email(): String
}
