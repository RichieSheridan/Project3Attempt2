package com.bcs371.project3attempt2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bcs371.project3attempt2.navigation.Screen
import com.bcs371.project3attempt2.game.GameStateManager
import com.bcs371.project3attempt2.viewmodel.UserViewModel

@Composable
fun LevelSelectScreen(
    navController: NavController,
    viewModel: UserViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        if (currentUser != null && !currentUser!!.isChild) {
            navController.navigate(Screen.ParentDashboard.route) {
                popUpTo(Screen.LevelSelect.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select Difficulty",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Button(
                onClick = {
                    GameStateManager.clearCurrentUser()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            ) {
                Text("Logout")
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { 
                GameStateManager.setHardMode(false)
                navController.navigate(Screen.Game.createRoute("easy"))
            },
            modifier = Modifier
                .width(200.dp)
                .height(80.dp)
        ) {
            Text("Easy Mode")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                GameStateManager.setHardMode(true)
                navController.navigate(Screen.Game.createRoute("hard"))
            },
            modifier = Modifier
                .width(200.dp)
                .height(80.dp)
        ) {
            Text("Hard Mode")
        }
        
        Spacer(modifier = Modifier.weight(1f))
    }
} 