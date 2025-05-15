// HomeScreen.kt
package com.example.expensetracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController // Import NavController


// Renamed the Composable to HomeScreenContent to differentiate from the Activity
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) { // Pass NavController to enable navigation
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Welcome to Expense Tracker!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            })
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                // Use navController to navigate to the "add_expense_route"
                onClick = { navController.navigate("AddExpense") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Add Expense")
            }

            Button(
                // TODO: Implement navigation to the "view_expense_route"
                onClick = { /* navController.navigate("view_expense_route") */ },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("View Expense")
            }
        }
    }
}