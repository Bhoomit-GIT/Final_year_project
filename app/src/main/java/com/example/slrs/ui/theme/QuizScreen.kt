package com.example.slrs.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.slrs.viewmodel.QuizViewModel
import com.example.slrs.viewmodel.SmartLearningViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    smartLearningViewModel: SmartLearningViewModel = viewModel()
) {
    val question by viewModel.currentQuestion.collectAsState()
    val options by viewModel.options.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val isTextInputRequired by viewModel.isTextInputRequired.collectAsState()
    val textInput by viewModel.textInput.collectAsState()
    val recommendations = viewModel.recommendations.collectAsState().value

    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    if (recommendations.isNotEmpty()) {
        RecommendationScreen(
            recommendations = recommendations,
            isLoading = isLoading,
            onRestart = { viewModel.restart() },
            viewModel = smartLearningViewModel,
            userEmail = userEmail
        )
        return
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFEAD9FF), Color(0xFFF2E7FE))
                    )
                )
                .padding(16.dp)
        ) {
            if (isLoading && question.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = question,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 60.dp),
                        color = Color(0xFF250144)
                    )

                    if (options.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(options.size) { index ->
                                Button(
                                    onClick = { viewModel.answerSelected(options[index]) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F00FF))
                                ) {
                                    Text(
                                        options[index],
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    if (isTextInputRequired) {
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = textInput,
                            onValueChange = viewModel::onTextInputChanged,
                            label = { Text("Enter your known language or framework") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (textInput.isNotBlank()) {
                                    viewModel.submitTextInput(textInput)
                                }
                            },
                            modifier = Modifier.align(Alignment.End),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F00FF))
                        ) {
                            Text("Submit", color = Color.White)
                        }
                    }

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF7F00FF)
                        )
                    }
                }
            }
        }
    }
}
