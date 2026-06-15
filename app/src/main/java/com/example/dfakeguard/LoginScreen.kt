package com.example.dfakeguard

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var isLoggedIn by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    if (isLoggedIn) {
        HomeScreen()
    } else {


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation =
                    if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (passwordVisible)
                                    Icons.Filled.Visibility
                                else
                                    Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    when {
                        email.isBlank() -> {
                            message = "Email is required"
                        }

                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            message = "Enter a valid email"
                        }

                        password.length < 6 -> {
                            message = "Password must be at least 6 characters"
                        }

                        else -> {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        message = "Login Successful"
                                        isLoggedIn = true
                                    } else {
                                        message = "Invalid Email or Password"
                                    }
                                }
                        }
                    }
                }
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))


            Button(
                onClick = {
                    when {
                        email.isBlank() -> {
                            message = "Email is required"
                        }

                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            message = "Enter a valid email"
                        }

                        password.length < 6 -> {
                            message = "Password must be at least 6 characters"
                        }

                        else -> {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        message = "Account Created Successfully"
                                    } else {
                                        message = task.exception?.message ?: "Registration Failed"
                                    }
                                }
                        }
                    }
                }
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )

        }
    }
}
