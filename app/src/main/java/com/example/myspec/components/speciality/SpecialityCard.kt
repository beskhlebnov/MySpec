package com.example.myspec.components.speciality

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspec.components.controls.buttons.FavoriteButton
import com.example.myspec.models.Program
import com.example.myspec.ui.theme.CardDark
import com.example.myspec.ui.theme.DarkBackground
import com.example.myspec.ui.theme.Orange
import com.example.myspec.ui.theme.White
import com.example.myspec.ui.theme.colorChance

@Composable
fun SpecialityCard(program: Program, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
    val gradientColors = listOf(Orange, Color(0xFFFF7043))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(gradientColors),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 4.dp, horizontal = 4.dp)
                ) {
                    Text(
                        text = program.code,
                        color = DarkBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                FavoriteButton(program = program, onClick = onFavoriteClick)
            }

            Text(
                text = program.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Orange
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (program.admission_chance > -1) {
                Text(
                    text = "Шанс пройти: ${program.admission_chance}%",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorChance(program.admission_chance)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Экзамены: ${program.getExamsStr()}",
                style = MaterialTheme.typography.bodyMedium,
                color = White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "ВУЗ",
                    tint = Orange
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = program.university,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Ср. бал: ${program.predict_ball}",
                    color = Orange,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = color,
            strokeWidth = 4.dp,
            modifier = Modifier.size(48.dp),
        )
    }
}