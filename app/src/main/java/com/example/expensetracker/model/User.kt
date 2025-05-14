package com.example.expensetracker.model

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

data class User(val name: String, val password: String) {
    private lateinit var context: Context

    // You'll need to provide the application context to the User object
    fun setContext(context: Context) {
        this.context = context
    }

    fun validateUser(): Boolean {
        if (!::context.isInitialized) {
            println("Error: Context not set. Call setContext(applicationContext).")
            return false
        }
        try {
            val inputStream = context.assets.open("users.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String? = reader.readLine()
            while (line != null) {
                val parts = line.split(",")
                if (parts.size == 2) {
                    val storedName = parts[0].trim()
                    val storedPassword = parts[1].trim()
                    if (name == storedName && password == storedPassword) {
                        reader.close()
                        return true
                    }
                } else {
                    println("Warning: Invalid format in users.csv - skipping line: $line")
                }
                line = reader.readLine()
            }
            reader.close()
            return false
        } catch (e: Exception) {
            println("Error reading users.csv from assets: ${e.message}")
            return false
        }
    }
}