package com.bcs371.project3attempt2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bcs371.project3attempt2.navigation.Screen
import com.bcs371.project3attempt2.viewmodel.UserViewModel

@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: UserViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Welcome to my game!",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { navController.navigate(Screen.Login.createRoute(true)) },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Child Login")
        }
        
        Button(
            onClick = { navController.navigate(Screen.Login.createRoute(false)) },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Parent Login")
        }
        
        Spacer(modifier = Modifier.weight(1f))
    }
} 