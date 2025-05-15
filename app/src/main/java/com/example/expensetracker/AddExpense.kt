// AddExpenseScreen.kt
package com.example.expensetracker // Make sure this package matches

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth

@OptIn(ExperimentalMaterial3Api::class) // Needed for TopAppBar in AddExpenseScreen
@Composable
fun AddExpenseScreen() { // Renamed to avoid conflict if you had a class named AddExpense
    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Add New Expense",
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
                .fillMaxSize()
                .padding(16.dp), // Add some overall padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text("Expense Name") },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )

            TextField(
                value = expenseDescription,
                onValueChange = { expenseDescription = it },
                label = { Text("Expense Description") },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )

            TextField(
                value = expenseAmount,
                onValueChange = { expenseAmount = it },
                label = { Text("Amount") },
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )

            Button(
                onClick = {
                    // TODO: Implement logic to save the expense (e.g., to a ViewModel, database)
                    println("Saving Expense: Name: $expenseName, Amount: $expenseAmount")
                    // You might want to navigate back after saving:
                    // navController.popBackStack()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Save Expense")
            }
        }
    }
}