package com.yusuf.myapplication.ui.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Entity(tableName = "loggedin_user_table")
data class LoggedInUser(
    @PrimaryKey val _id: Int = 0,
    val email: String,
    val password: String,
    val token: String?
)