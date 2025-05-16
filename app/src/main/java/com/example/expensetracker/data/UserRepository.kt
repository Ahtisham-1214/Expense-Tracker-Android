package com.example.expensetracker.data
import com.example.expensetracker.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    // Function to attempt user login
    suspend fun loginUser(name: String, password: String): User? {
        // Run database operation on a background thread (IO dispatcher)
        return withContext(Dispatchers.IO) {
            userDao.getUserByCredentials(name, password)
        }
    }

    // Function to register a new user
    suspend fun registerUser(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            // Check if user already exists
            val existingUser = userDao.getUserByName(user.name)
            if (existingUser == null) {
                userDao.insertUser(user)
                true // User registered successfully
            } else {
                false // User with that name already exists
            }
        }
    }

    // Example: get all users (useful for administration, but careful with sensitive data)
    fun getAllUsers() = userDao.getAllUsers()
}
