package com.felicks.sirbo.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felicks.sirbo.ui.theme.FlatBackground
import com.felicks.sirbo.ui.theme.FlatBorder
import com.felicks.sirbo.ui.theme.FlatOnPrimary
import com.felicks.sirbo.ui.theme.FlatPrimary
import com.felicks.sirbo.ui.theme.FlatSurface
import com.felicks.sirbo.ui.theme.FlatTextPrimary


// 🧱 Botón estilo Flat Design
@Composable
fun SirboButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = FlatPrimary,
    contentColor: Color = FlatOnPrimary
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = TextStyle(fontSize = 16.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSirboButton() {
    Column(modifier = Modifier.background(FlatBackground).padding(16.dp)) {
        SirboButton(text = "Presionar", onClick = {})
    }
}

// 🧱 Tarjeta estilo Flat Design
@Composable
fun SirboCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(FlatSurface)
            .padding(16.dp),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSirboCard() {
    Column(modifier = Modifier.background(FlatBackground).padding(16.dp)) {
        SirboCard {
            Text("Esto es una tarjeta flat", color = FlatTextPrimary)
        }
    }
}

// 🧱 Input estilo Flat Design (placeholder)
@Composable
fun SirboInputPlaceholder(
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(label, fontSize = 14.sp, color = FlatTextPrimary)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(FlatSurface)
                .border(width = 1.dp, color = FlatBorder, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text("Texto de ejemplo", color = FlatTextPrimary.copy(alpha = 0.5f))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewSirboInputPlaceholder() {
    Column(modifier = Modifier.background(FlatBackground).padding(16.dp)) {
        SirboInputPlaceholder(label = "Nombre")
    }
}

// 🧱 Top App Bar estilo Flat Design
@Composable
fun SirboTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = FlatSurface,
    contentColor: Color = FlatTextPrimary
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(backgroundColor)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            color = contentColor,
            style = TextStyle(fontSize = 20.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSirboTopAppBar() {
    Column(modifier = Modifier.background(FlatBackground)) {
        SirboTopAppBar(title = "Sirbo")
    }
}
