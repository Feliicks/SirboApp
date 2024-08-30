package com.felicksdev.onlymap.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectionTopAppBar(modifier: Modifier = Modifier) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                "Medium Top App Bar",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            Row {
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = "",
                    onValueChange = {}, // Implementar lógica para actualizar valor
                    label = { Text("Origen") },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = "",
                    onValueChange = {}, // Implementar lógica para actualizar valor
                    label = { Text("Destino") },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        },
    )
}
@Preview
@Composable
fun TopBarPreview(modifier: Modifier = Modifier) {
    LocationSelectionTopAppBar(modifier = modifier)
}