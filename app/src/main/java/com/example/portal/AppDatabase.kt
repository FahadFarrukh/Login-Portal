package com.example.portal


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 3) // Increase version number to 2
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao


}
