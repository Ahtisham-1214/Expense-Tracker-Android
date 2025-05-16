package com.example.expensetracker.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.expensetracker.model.User
import kotlinx.coroutines.flow.Flow

@Dao // Marks this interface as a Room DAO
interface UserDao {

    @Insert // Annotation for inserting new users
    suspend fun insertUser(user: User): Long // suspend makes it a coroutine, returning the new row ID

    @Query("SELECT * FROM users WHERE name = :name AND password = :password LIMIT 1")
    // Query to find a user by name and password, limiting to 1 result
    suspend fun getUserByCredentials(name: String, password: String): User?

    @Query("SELECT * FROM users WHERE name = :name LIMIT 1")
    // Query to check if a user with a given name already exists
    suspend fun getUserByName(name: String): User?

    @Query("SELECT * FROM users ORDER BY name ASC")
    // Get all users, ordered by name. Use Flow for observing changes.
    fun getAllUsers(): Flow<List<User>>
}