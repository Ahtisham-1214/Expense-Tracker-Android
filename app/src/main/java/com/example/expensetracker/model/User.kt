package com.example.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users") // Defines this class as a Room entity with a table name "users"
data class User(
    @PrimaryKey(autoGenerate = true) // Automatically generates unique IDs for each user
    val id: Long = 0, // Primary key for the user, with a default value for new users

    @ColumnInfo(name = "name") // Defines the column name in the table
    val name: String,

    @ColumnInfo(name = "password")
    val password: String
)