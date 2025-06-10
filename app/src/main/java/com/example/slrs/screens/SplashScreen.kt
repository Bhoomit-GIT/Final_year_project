package com.example.slrs.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slrs.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(true) {
        delay(2000L)
        onTimeout()
    }

    // 🔁 Animation for logo scaling
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // ✨ Fade-in animation for text
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1200)
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.slrs_logo),
                contentDescription = "SLRS Logo",
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale) // 🌀 Apply scaling animation
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Smart Learning Recommendation System",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(alpha) // ✨ Apply fade-in
            )
        }
    }
}
