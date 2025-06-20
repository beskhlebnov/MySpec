package com.example.myspec.components.controls.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myspec.ui.theme.Orange
import com.example.myspec.ui.theme.White


@Composable
fun GradientButton(enabled: Boolean = true, stringId: Int,
                   paddingTop: Int = 10, paddingBottom: Int = 10,
                   paddingStart: Int = 10, paddingEnd: Int = 10,
                   onClick:() -> Unit){
    val gradientColors = listOf(Orange, Color(0xFFFF7043))
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier.padding(
            bottom = paddingBottom.dp,
            start = paddingStart.dp,
            end= paddingEnd.dp,
            top = paddingTop.dp)
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(gradientColors),
                shape = RoundedCornerShape(15.dp)

            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Column (
            verticalArrangement = Arrangement.Center,) {
            Text(
                text = stringResource(id = stringId),
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.height(20.dp)
            )
        }
    }
}