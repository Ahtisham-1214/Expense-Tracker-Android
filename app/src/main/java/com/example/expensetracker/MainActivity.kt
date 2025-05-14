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
import com.example.expensetracker.model.User
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                LoginPage()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") } // 🔸 New state
    var loginSuccess by remember { mutableStateOf(false) }
    val view = LocalView.current
    val context = LocalContext.current // Get the context

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
                    loginError = "" // 🔸 Clear login error
                },
                label = { Text("Username") },
                isError = usernameError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(0.8f).padding(8.dp),
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
                    loginError = "" // 🔸 Clear login error
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(0.8f).padding(8.dp),
                supportingText = {
                    if (passwordError.isNotEmpty()) {
                        Text(passwordError, color = Color.Red)
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
                        val user = User(username, password)
                        user.setContext(context) // Set the context here
                        if (user.validateUser()) {
                            loginSuccess = true
                            loginError = "" // Clear login error
                            println("Login successful! Username: $username, Password: $password")
                        } else {
                            loginSuccess = false
                            loginError = "Invalid credentials" // 🔸 Show login error
                            println("Login failed")
                        }
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Login", color = Color.Blue)
            }

            // 🔸 Show login error
            if (loginError.isNotEmpty()) {
                Text(loginError, color = Color.Red)
            }

            if (loginSuccess) {
                Text("Login Successful", color = Color.Green)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    ExpenseTrackerTheme {
        LoginPage()
    }
}
