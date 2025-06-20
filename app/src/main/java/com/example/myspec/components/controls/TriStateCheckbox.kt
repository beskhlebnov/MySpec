package com.example.myspec.components.controls

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.myspec.R

@Composable
fun TriStateCheckbox(
    state: Boolean?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val iconResId = when (state) {
        true -> R.drawable.ic_check_box
        false -> R.drawable.ic_check_box_outline_blank
        null -> R.drawable.ic_indeterminate_check_box
    }

    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = Color.White
        )
    }
}