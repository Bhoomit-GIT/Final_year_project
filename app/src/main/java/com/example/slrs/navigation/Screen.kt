package com.example.slrs.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Menu : Screen("menu")
    object Quiz : Screen("quiz")       // 🔥 This routes to QuizScreen.kt
    object Roadmap : Screen("roadmap") // ✅ Added this for WebView
    object About : Screen("about")
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")

}
