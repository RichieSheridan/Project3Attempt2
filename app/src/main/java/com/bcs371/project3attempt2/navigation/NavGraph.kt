package com.bcs371.project3attempt2.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login/{isChild}") {
        fun createRoute(isChild: Boolean) = "login/$isChild"
    }
    object Register : Screen("register/{isChild}") {
        fun createRoute(isChild: Boolean) = "register/$isChild"
    }
    object LevelSelect : Screen("level_select")
    object Game : Screen("game/{difficulty}") {
        fun createRoute(difficulty: String) = "game/$difficulty"
    }
    object ParentDashboard : Screen("parent_dashboard")
} 