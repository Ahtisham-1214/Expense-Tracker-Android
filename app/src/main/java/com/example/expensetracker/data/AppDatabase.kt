package com.example.expensetracker.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.expensetracker.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
// Defines the database, lists its entities, and sets the version
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao // Abstract method to get the UserDao instance

    companion object {
        @Volatile // Ensures that changes to INSTANCE are immediately visible to all threads
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If the INSTANCE is null, create the database
            return INSTANCE ?: synchronized(this) { // Synchronized block to prevent multiple threads from creating the database concurrently
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Use applicationContext to prevent memory leaks
                    AppDatabase::class.java,
                    "expense_tracker_db" // The name of your database file
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}