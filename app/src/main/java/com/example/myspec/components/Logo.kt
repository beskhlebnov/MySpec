package com.example.myspec.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myspec.R
import com.example.myspec.components.text.BoldText
import com.example.myspec.components.text.ThinText
import com.example.myspec.ui.theme.Orange

@Composable
fun Logo(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Логотип",
            modifier = Modifier
                .size(120.dp)
        )
        BoldText(R.string.logo_label, 32, Orange)
        ThinText(R.string.logo_title, 25, Orange)
    }
}