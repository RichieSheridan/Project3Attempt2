package com.bcs371.project3attempt2

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bcs371.project3attempt2.data.AppDatabase
import com.bcs371.project3attempt2.game.GameScreen
import com.bcs371.project3attempt2.game.GameStateManager
import com.bcs371.project3attempt2.navigation.Screen
import com.bcs371.project3attempt2.screens.*
import com.bcs371.project3attempt2.ui.theme.Project3Attempt2Theme
import com.bcs371.project3attempt2.viewmodel.ProgressViewModelFactory
import com.bcs371.project3attempt2.viewmodel.UserViewModelFactory
import com.bcs371.project3attempt2.viewmodel.ProgressViewModel
import com.bcs371.project3attempt2.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        //Set up fullscreen
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
        
        //Enable edge to edge
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        
        val database = AppDatabase.getDatabase(applicationContext)
        val userDao = database.userDao()
        val progressDao = database.progressDao()
        GameStateManager.initialize(userDao)
        
        setContent {
            Project3Attempt2Theme {
                val navController = rememberNavController()
                val userViewModel: UserViewModel = viewModel(
                    factory = UserViewModelFactory(LocalContext.current)
                )
                val progressViewModel: ProgressViewModel = viewModel(
                    factory = ProgressViewModelFactory(progressDao)
                )
                
                NavHost(navController = navController, startDestination = Screen.Welcome.route) {
                    composable(Screen.Welcome.route) {
                        WelcomeScreen(
                            navController = navController,
                            viewModel = userViewModel
                        )
                    }
                    composable(
                        route = Screen.Login.route,
                        arguments = listOf(navArgument("isChild") { type = NavType.BoolType })
                    ) { backStackEntry ->
                        val isChild = backStackEntry.arguments?.getBoolean("isChild") ?: false
                        LaunchedEffect(Unit) {
                            userViewModel.clearLoginError()
                            userViewModel.clearCurrentUser()
                        }
                        LoginScreen(
                            navController = navController,
                            isChild = isChild,
                            viewModel = userViewModel
                        )
                    }
                    composable(
                        route = Screen.Register.route,
                        arguments = listOf(navArgument("isChild") { type = NavType.BoolType })
                    ) { backStackEntry ->
                        val isChild = backStackEntry.arguments?.getBoolean("isChild") ?: false
                        RegisterScreen(
                            navController = navController,
                            isChild = isChild,
                            viewModel = userViewModel
                        )
                    }
                    composable(Screen.LevelSelect.route) {
                        LevelSelectScreen(
                            navController = navController,
                            viewModel = userViewModel
                        )
                    }
                    composable(Screen.Game.route) {
                        val currentUserId by userViewModel.currentUser.collectAsState()
                        GameScreen(
                            onExit = { navController.navigate(Screen.LevelSelect.route) },
                            progressViewModel = progressViewModel,
                            userId = currentUserId?.id ?: -1
                        )
                    }
                    composable(Screen.ParentDashboard.route) {
                        val currentUserId by userViewModel.currentUser.collectAsState()
                        ParentDashboardScreen(
                            onClose = {
                                navController.navigate(Screen.Welcome.route) {
                                    popUpTo(Screen.Welcome.route) { inclusive = true }
                                }
                            },
                            progressViewModel = progressViewModel,
                            userId = currentUserId?.id ?: -1,
                            viewModel = userViewModel
                        )
                    }
                }
            }
        }
    }
}