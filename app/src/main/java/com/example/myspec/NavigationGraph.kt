package com.example.myspec

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myspec.screens.StartScreen
import com.example.myspec.screens.auth.AuthorizationScreen
import com.example.myspec.screens.auth.FirstSettingScreen
import com.example.myspec.screens.main.ListScreen
import com.example.myspec.screens.main.ProfileScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "start") {
        composable("favorite") { ListScreen(navController = navController, isFavoriteList = true) }
        composable("profile") { ProfileScreen(navController = navController) }
        composable("start") { StartScreen(navController = navController) }
        composable("list") { ListScreen(navController = navController) }
        composable("auth") { AuthorizationScreen(navController = navController) }
        composable("home") { FirstSettingScreen(navController = navController) }
    }
}