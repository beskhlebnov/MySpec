package com.example.myspec.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White

private val DarkColorScheme = darkColorScheme(
    primary = White,
    secondary = Navy,
    tertiary = Blue,
    background = Black
)

@Composable
fun MySpecTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}