package com.example.portal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "firstname") val firstname: String?,
    @ColumnInfo(name = "lastname") val lastname: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "login") val login: Boolean?,
)
