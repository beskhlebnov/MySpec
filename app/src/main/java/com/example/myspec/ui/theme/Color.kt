package com.example.myspec.ui.theme

import androidx.compose.ui.graphics.Color


val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val Orange = Color(0xFFFF9800)
val Navy = Color(0xFF172157)
val Blue = Color(0xFF40A4FF)
val DarkBackground = Color(0xFF121212)
val CardDark = Color(0xFF1E1E1E)
val DarkGreen = Color(0xFF2E7D32)
val DarkBlue = Color(0xFF1565C0)




fun colorChance(chance: Int): Color {
    val color = when (chance) {
        in 0..15 -> Color(0xFFFF1744)
        in 16..30 -> Color(0xFFFF5252)
        in 31..45 -> Color(0xFFFF7043)
        in 46..60 -> Color(0xFFFFAB40)
        in 61..75 -> Color(0xFFAED581)
        else -> Color(0xFF4CAF50)
    }
    return color
}