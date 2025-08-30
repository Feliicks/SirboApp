package com.felicks.sirbo.ui.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.felicks.sirbo.ui.theme.ButtonShape
import com.felicks.sirbo.ui.theme.onPrimaryLight
import com.felicks.sirbo.ui.theme.primaryLight


@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = primaryLight,
    contentColor: Color = onPrimaryLight
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Text(text = text)
    }
}