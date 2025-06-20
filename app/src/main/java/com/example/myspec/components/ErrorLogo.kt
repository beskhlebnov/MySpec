package com.example.myspec.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspec.R
import com.example.myspec.components.controls.buttons.GradientButton
import com.example.myspec.ui.theme.Orange

@Composable
fun ErrorLogo(
    text: String = "Похоже что-то сломалось!", reTryButton: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "(╥﹏╥)",
                color = Orange,
                modifier = Modifier.padding(bottom = 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 48.sp
            )
            Text(
                text = text,
                color = Orange,
                modifier = Modifier.padding(horizontal = 15.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
            if (reTryButton) {
                Spacer(modifier = Modifier.height(16.dp))
                GradientButton(stringId = R.string.retry) { onClick() }
            }
        }
    }
}