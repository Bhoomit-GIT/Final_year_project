package com.example.slrs.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slrs.R


@Composable
fun AboutScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.slrs_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(110.dp)
            )

            // Welcome Message
            Text(
                text = "Welcome to SLRS!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF250144),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // About App
            HighlightedSectionTitle("📱 About the App")
            CenteredText("SLRS (Smart Learning Recommendation System) helps you discover the best technology stack to learn in 2025 based on your interests and skills — powered by AI.")

            // Developed By
            HighlightedSectionTitle("👨‍💻 Developed By")
            CenteredText("SLRS Team || GHRCE")

            // Features
            HighlightedSectionTitle("✨ Features")
            LeftAlignedText(
                "• AI-based personalized tech stack recommendations\n" +
                        "• Dynamic questions using Netmind API\n" +
                        "• Clickable roadmaps\n" +
                        "• Firebase login/register system\n" +
                        "• Beautiful and modern UI experience"
            )

            // Technology Used
            HighlightedSectionTitle("🛠️ Technologies Used")
            LeftAlignedText(
                "• Android (Jetpack Compose)\n" +
                        "• Kotlin\n" +
                        "• Firebase Authentication\n" +
                        "• REST API (Netmind Inference API)\n" +
                        "• Navigation Compose"
            )

            // Android Specs
            HighlightedSectionTitle("📱 Android Specifications")
            CenteredText(
                "• Min SDK: 24\n" +
                        "• Target SDK: 34\n" +
                        "• Jetpack Compose 1.6+"
            )

            // Contact Us
            HighlightedSectionTitle("📬 Contact Us")
            CenteredText("Email: support@slrs.ai\nGitHub: github.com/slrs")

            // Special Message
            HighlightedSectionTitle("💬 Special Message")
            CenteredText(
                "\"Learning is not about catching up, it’s about staying curious. \nSLRS is your compass in the world of endless tech possibilities. 🌟\""
            )

            // App Version
            HighlightedSectionTitle("🔖 Version")
            CenteredText("v1.0.0")
        }
    }
}



@Composable
fun HighlightedSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF250144)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

@Composable
fun LeftAlignedText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
        modifier = Modifier.fillMaxWidth(),
        textAlign = androidx.compose.ui.text.style.TextAlign.Start
    )
}

@Composable
fun CenteredText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

