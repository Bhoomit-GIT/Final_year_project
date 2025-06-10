package com.example.slrs.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.slrs.R
import com.example.slrs.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SLRSAppBar(
    title: String,
    navController: NavController,
    onBackClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.slrs_logo),
                    contentDescription = "SLRS Logo",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .shadow(2.dp)
                )

            }
        },
        navigationIcon = {
            onBackClick?.let {
                IconButton(
                    onClick = it,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF250144)
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(Screen.About.route)
                },
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xFF250144)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF),

                    scrolledContainerColor = Color(0xFFD6BBFF)
        ),
        modifier = Modifier.shadow(4.dp)
    )
}
