package com.example.slrs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.slrs.components.SLRSAppBar
import com.example.slrs.navigation.Screen
import com.example.slrs.screens.*
import com.example.slrs.ui.theme.QuizScreen
import com.example.slrs.ui.theme.RoadmapWebViewScreen
import com.example.slrs.ui.theme.SLRSTheme
import com.example.slrs.viewmodel.QuizViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize Firebase
        FirebaseApp.initializeApp(this)

        setContent {
            SLRSTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route

                Surface(modifier = Modifier.fillMaxSize()) {
                    Column {
                        if (currentRoute != Screen.Splash.route) {
                            SLRSAppBar(
                                title = "SLRS",
                                navController = navController,
                                onBackClick = if (navController.previousBackStackEntry != null) {
                                    { navController.popBackStack() }
                                } else null
                            )
                        }

                        NavHost(
                            navController = navController,
                            startDestination = Screen.Splash.route
                        ) {
                            composable(Screen.Splash.route) {
                                SplashScreen(onTimeout = {
                                    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
                                    navController.navigate(
                                        if (isLoggedIn) Screen.Menu.route else Screen.Welcome.route
                                    ) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                })
                            }

                            composable(Screen.Welcome.route) {
                                WelcomeScreen(onEnterClick = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Welcome.route) { inclusive = true }
                                    }
                                })
                            }

                            composable(Screen.Login.route) {
                                LoginScreen(navController)
                            }

                            composable(Screen.Register.route) {
                                RegisterScreen(navController)
                            }

                            composable(Screen.Menu.route) {
                                MenuScreen(
                                    navController = navController,
                                    onNavigateToQuiz = {
                                        navController.navigate(Screen.Quiz.route)
                                    }
                                )
                            }

                            composable(Screen.Quiz.route) {
                                QuizScreen(viewModel = viewModel)
                            }

                            composable(Screen.Roadmap.route) {
                                WebViewScreen(url = "https://roadmap.sh")
                            }

                            composable(Screen.About.route) {
                                AboutScreen()
                            }

                            composable(Screen.Profile.route) {
                                ProfileScreen(navController)
                            }
                            composable("projects_webview") {
                                RoadmapWebViewScreen(
                                    url = "https://roadmap.sh/projects",
                                    onBack = { navController.popBackStack() }
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}
