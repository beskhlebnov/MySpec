package com.example.myspec.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myspec.components.ErrorLogo
import com.example.myspec.components.LoadingAnimation
import com.example.myspec.viewmodels.StartViewModel


@Composable
fun StartScreen(
    navController: NavController,
    viewModel: StartViewModel = viewModel(),
) {
    val loading by viewModel.loading.collectAsState()
    val refreshed by viewModel.refreshed.collectAsState()
    val state by viewModel.state.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    when {
        loading -> {
            LoadingAnimation()
        }

        error != null -> {
            ErrorLogo { viewModel.refresh() }
        }

        else -> {
            val route = if (!refreshed) "auth" else if (state == 0) "home" else "list"
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    }
}