package com.example.myspec.components.controls.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun IntegerInputField(
    onValueChange: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    value: Int?
) {
    var text by remember { mutableStateOf(value?.toString() ?: "") }

    Column(modifier = modifier.width(60.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                if (newText.isEmpty()) {
                    text = newText
                    onValueChange(null)
                } else if (newText.matches(Regex("^-?\\d*\$"))) {
                    val numericValue = newText.toIntOrNull()
                    if (numericValue != null && numericValue <= 100) {
                        text = newText
                        onValueChange(numericValue)
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}