package com.example.myspec.components.controls.buttons


import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myspec.models.Program

@Composable
fun FavoriteButton(program: Program, onClick:() -> Unit) {
    var isFavorite by remember { mutableStateOf(program.isFavorite) }

    IconButton(
        onClick = {
            isFavorite = !isFavorite
            program.toggleFavorite()
            onClick.invoke()
    }) {
        Icon(
            modifier = Modifier.size(28.dp),
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
            tint = Color.Red
        )
    }
}