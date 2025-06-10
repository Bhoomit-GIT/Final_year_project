package com.example.slrs.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.slrs.navigation.Screen

@Composable
fun MenuScreen(
    onNavigateToQuiz: () -> Unit,
    navController: NavController
) {
    LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF7F00FF), Color(0xFFE100FF))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            MenuItem("👤 Profile") {
                navController.navigate(Screen.Profile.route)
            }

            MenuItem("💡 Best Tech Stack Recommendation") {
                onNavigateToQuiz()
            }

            MenuItem("🗺️ Roadmaps") {
                navController.navigate(Screen.Roadmap.route)
            }

            MenuItem("ℹ️ About App") {
                navController.navigate(Screen.About.route)
            }

            MenuItem("💡 Projects") {
                navController.navigate("projects_webview")
            }
        }
    }
}

@Composable
fun MenuItem(title: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.White.copy(alpha = 0.9f),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(vertical = 18.dp, horizontal = 20.dp),
            color = Color(0xFF4A148C)
        )
    }
}
