package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.ViewCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.expensetracker.model.User
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation

// Import for Navigation
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Import your Room database components
import com.example.expensetracker.data.AppDatabase
import com.example.expensetracker.data.UserRepository
import kotlinx.coroutines.launch // Import for coroutines

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") } // Combined for error/success messages
    val context = LocalContext.current // Get the context
    val view = LocalView.current // Get the current view
    val coroutineScope = rememberCoroutineScope() // Coroutine scope for launching suspend functions
    var passwordVisible by remember { mutableStateOf(false) }
    // Initialize UserRepository
    // This should ideally be injected via Hilt/Koin or provided by a ViewModel for better architecture
    // For this example, we'll initialize it directly here.
    val userRepository = remember {
        val database = AppDatabase.getDatabase(context.applicationContext)
        UserRepository(database.userDao())
    }

    SideEffect {
        val window = (view.context as ComponentActivity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(view) { view, windowInsets ->
            windowInsets
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Login", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            })
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = ""
                    loginMessage = "" // Clear message on input change
                },
                label = { Text("Username") },
                isError = usernameError.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp),
                supportingText = {
                    if (usernameError.isNotEmpty()) {
                        Text(usernameError, color = Color.Red)
                    }
                }
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                    loginMessage = "" // Clear message on input change
                },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp),
                supportingText = {
                    if (passwordError.isNotEmpty()) {
                        Text(passwordError, color = Color.Red)
                    }
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )
            Button(
                onClick = {
                    usernameError = when {
                        username.isBlank() -> "Username required"
                        else -> ""
                    }
                    passwordError = when {
                        password.isBlank() -> "Password required"
                        password.length < 3 -> "Password must be at least 3 characters"
                        else -> ""
                    }

                    if (usernameError.isEmpty() && passwordError.isEmpty()) {
                        // Launch a coroutine for the database operation
                        coroutineScope.launch {
                            val user = userRepository.loginUser(username, password)
                            if (user != null) {
                                loginMessage = "Login Successful! Welcome, ${user.name}"
                                println("Login successful! Username: $username")
                                // Navigate to the home screen
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                loginMessage = "Invalid username or password"
                                println("Login failed for username: $username")
                            }
                        }
                    }
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Login")
            }

            // You might want a separate button for registration or a link
            Button(
                onClick = {
                    val name = username // Use the state variable
                    val pwd = password  // Use the state variable
                    if (name.isBlank() || pwd.isBlank()) {
                        loginMessage = "Please enter username and password to register."
                        return@Button
                    }

                    coroutineScope.launch {
                        val newUser = User(name = name, password = pwd)
                        val isRegistered = userRepository.registerUser(newUser)
                        loginMessage = if (isRegistered) {
                            "Registration successful! You can now log in."
                        } else {
                            "Registration failed: Username already exists."
                        }
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue) // Different color for register
            ) {
                Text("Register")
            }

            // Display login/registration messages
            if (loginMessage.isNotEmpty()) {
                val messageColor = if (loginMessage.contains("Successful")) Color.Green else Color.Red
                Text(loginMessage, color = messageColor)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    ExpenseTrackerTheme {
        LoginPage(navController = rememberNavController())
    }
}
