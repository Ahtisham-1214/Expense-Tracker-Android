package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

// Import for Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                // Set up the NavController and NavHost
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login"){
                        LoginPage(navController = navController)
                    }
                    composable("home"){
                        HomeScreen(navController = navController)
                    }
                    composable("AddExpense"){
                        AddExpenseScreen()
                    }

                }
            }
        }
    }
}