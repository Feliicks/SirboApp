package com.felicksdev.onlymap.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SecondScreen(
    text : String
) {
    Column {
        Text(text = "Soy la second Screen")
        Text(text = "El texto escrito es: $text")
    }


}