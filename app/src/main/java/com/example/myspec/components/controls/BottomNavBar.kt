package com.example.myspec.components.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myspec.R
import com.example.myspec.ui.theme.Orange
import com.example.myspec.ui.theme.White

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        NavItem("favorite", "Избранное", R.drawable.ic_favorite, R.drawable.ic_favorite_fill),
        NavItem("list", "Рекомендации", R.drawable.ic_list, R.drawable.ic_list_fill),
        NavItem("profile", "Профиль", R.drawable.ic_profile, R.drawable.ic_profile_fill),
    )

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavItem(
                item = item,
                isSelected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}


@Composable
fun BottomNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick, enabled = !isSelected)
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = if (isSelected) item.activeIconRes else item.iconRes),
            contentDescription = item.title,
            tint = Orange
        )
        Text(
            text = item.title,
            fontSize = 12.sp,
            color = Orange
        )
    }
}

data class NavItem(
    val route: String,
    val title: String,
    val iconRes: Int,
    val activeIconRes: Int
)


