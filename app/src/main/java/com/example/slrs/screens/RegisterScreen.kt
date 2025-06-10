package com.example.slrs.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.slrs.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var name by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var studyYear by remember { mutableStateOf("") }
    var stream by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF7F00FF), Color(0xFFE100FF))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Register", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Date of Birth", style = MaterialTheme.typography.labelMedium)
                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = day,
                        onValueChange = { day = it },
                        label = { Text("DD") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = month,
                        onValueChange = { month = it },
                        label = { Text("MM") },
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it },
                        label = { Text("YYYY") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp),
                        singleLine = true
                    )
                }

                Text("Gender", style = MaterialTheme.typography.labelMedium)

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == "Male",
                            onClick = { gender = "Male" }
                        )
                        Text("Male", modifier = Modifier.padding(end = 16.dp))

                        RadioButton(
                            selected = gender == "Female",
                            onClick = { gender = "Female" }
                        )
                        Text("Female")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == "Prefer not to say",
                            onClick = { gender = "Prefer not to say" }
                        )
                        Text("Prefer not to say")
                    }
                }


                OutlinedTextField(
                    value = branch,
                    onValueChange = { branch = it },
                    label = { Text("Branch") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = studyYear,
                    onValueChange = { studyYear = it },
                    label = { Text("Year of Study") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stream,
                    onValueChange = { stream = it },
                    label = { Text("Stream") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )


                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true
                        val dob = "$day/$month/$year"

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                                    val calendar = Calendar.getInstance()
                                    val datePart = String.format(Locale.getDefault(), "%d%d%d",
                                        calendar.get(Calendar.DAY_OF_MONTH),
                                        calendar.get(Calendar.MONTH) + 1,
                                        calendar.get(Calendar.YEAR)
                                    )
                                    val randomFourDigits = (1000..9999).random()
                                    val customId = "$datePart$randomFourDigits"

                                    val userMap = hashMapOf(
                                        "uid" to uid,
                                        "customId" to customId,
                                        "name" to name,
                                        "dob" to dob,
                                        "gender" to gender,
                                        "branch" to branch,
                                        "year" to studyYear,
                                        "stream" to stream,
                                        "email" to email,
                                        "phone" to phone
                                    )

                                    FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(uid)
                                        .set(userMap)
                                        .addOnSuccessListener {
                                            isLoading = false
                                            Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
                                            navController.navigate(Screen.Login.route) {
                                                popUpTo(Screen.Register.route) { inclusive = true }
                                            }
                                        }
                                        .addOnFailureListener {
                                            isLoading = false
                                            Toast.makeText(context, "Firestore Error: ${it.message}", Toast.LENGTH_LONG).show()
                                        }

                                } else {
                                    isLoading = false
                                    Toast.makeText(context, "Auth Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Register")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                    Text("Already have an account? Login")
                }
            }
        }
    }
}
