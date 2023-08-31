package com.example.myapplicationdrawer

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.myapplicationdrawer.AppDatabase
import com.example.myapplicationdrawer.User

class SessionManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("session_pref", Context.MODE_PRIVATE)
    private lateinit var db: AppDatabase


    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean("isLoggedIn", false)
        set(value) = sharedPreferences.edit().putBoolean("isLoggedIn", value).apply()


    var loggedInUserId: Long
        get() = sharedPreferences.getLong("loggedInUserId", -1L)
        set(value) = sharedPreferences.edit().putLong("loggedInUserId", value).apply()

    fun logout() {
        sharedPreferences.edit()
            .remove("isLoggedIn")
            .remove("loggedInUserId")
            .apply()

    }
    fun logout2(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "user-database"
        )
            .fallbackToDestructiveMigration()
            .build()
        Thread {
            db.UserDao().logout()
        }.start()
    }

    fun login(context: Context, email: String) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "user-database"
        )
            .fallbackToDestructiveMigration()
            .build()
        Thread {
            db.UserDao().updateLoginByEmail(email, true)

        }.start()
    }



}
